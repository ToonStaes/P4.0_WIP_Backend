package com.example.p4backend.services.impl;

import com.example.p4backend.models.*;
import com.example.p4backend.models.complete.CompleteAction;
import com.example.p4backend.models.complete.CompleteActionWithProgress;
import com.example.p4backend.models.complete.CompleteVzw;
import com.example.p4backend.repositories.*;
import com.example.p4backend.services.WipActionService;
import org.bson.types.Decimal128;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WipActionServiceImpl implements WipActionService {

    private ActionRepository actionRepository;
    private VzwRepository vzwRepository;
    private ActionImageRepository actionImageRepository;
    private ProductRepository productRepository;
    private PurchaseRepository purchaseRepository;
    private AddressRepository addressRepository;

    public WipActionServiceImpl(ActionRepository actionRepository, VzwRepository vzwRepository, ActionImageRepository actionImageRepository, ProductRepository productRepository, PurchaseRepository purchaseRepository, AddressRepository addressRepository) {
        this.actionRepository = actionRepository;
        this.vzwRepository = vzwRepository;
        this.actionImageRepository = actionImageRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
        this.addressRepository = addressRepository;
    }

    public List<CompleteAction> getNewestActions(boolean progress) {
        List<Action> newestActions = getActionRepository().findByEndDateAfterOrderByStartDateDesc(new Date());
        List<CompleteAction> completeActions = new ArrayList<>();

        for (Action action : newestActions) {
            if (progress) {
                double actionProgress = getProgress(action);
                completeActions.add(getCompleteActionWithProgress(action, actionProgress));
            } else {
                completeActions.add(getCompleteAction(action));
            }
        }

        return completeActions.stream().limit(4).collect(Collectors.toList()); // Take first n (number in limit(n)) items and return them.
    }

    protected double getProgress(Action action) {
        Decimal128 actionGoal = action.getGoal();
        List<Product> products = getProductRepository().findProductsByActionId(action.getId());

        BigDecimal actionPurchased = new BigDecimal(0);
        for (Product product : products) {
            List<Purchase> purchases = purchaseRepository.findPurchasesByProductId(product.getId());
            int sum = purchases.stream().mapToInt(Purchase::getAmount).sum(); // Sum of all the amounts a product has been purchased
            actionPurchased = actionPurchased.add(BigDecimal.valueOf(sum * product.getCost().doubleValue())); // Add the total value of this product purchased to the total value action purchased
        }

        return actionPurchased.doubleValue() / Objects.requireNonNull(actionGoal).doubleValue() * 100; // calculate progress percentage (total value purchased / goal * 100)
    }

    private CompleteActionWithProgress getCompleteActionWithProgress(Action action, double progress) {
        Optional<Vzw> vzw = vzwRepository.findById(action.getVzwID());
        CompleteVzw completeVzw = getCompleteVzw(vzw);
        List<ActionImage> actionImages = actionImageRepository.findActionImagesByActionId(action.getId());
        return new CompleteActionWithProgress(action, completeVzw, actionImages, progress);
    }

    private CompleteVzw getCompleteVzw(Optional<Vzw> vzw) {
        if (vzw.isPresent()) {
            Optional<Address> address = addressRepository.findById(vzw.get().getAddressID());
            return new CompleteVzw(vzw.get(), address);
        }
        return null;
    }

    private CompleteAction getCompleteAction(Action action) {
        Optional<Vzw> vzw = vzwRepository.findById(action.getVzwID());
        CompleteVzw completeVzw = getCompleteVzw(vzw);
        List<ActionImage> actionImages = actionImageRepository.findActionImagesByActionId(action.getId());
        return new CompleteAction(action, completeVzw, actionImages);
    }

    public ActionRepository getActionRepository() {
        return actionRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public VzwRepository getVzwRepository() {
        return vzwRepository;
    }

    public ActionImageRepository getActionImageRepository() {
        return actionImageRepository;
    }

    public PurchaseRepository getPurchaseRepository() {
        return purchaseRepository;
    }

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }
}
