package com.example.p4backend.controllers;

import com.example.p4backend.models.Product;
import com.example.p4backend.models.Purchase;
import com.example.p4backend.models.User;
import com.example.p4backend.models.complete.CompletePurchase;
import com.example.p4backend.repositories.ProductRepository;
import com.example.p4backend.repositories.PurchaseRepository;
import com.example.p4backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PurchaseController {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void fillDB(){
        if (purchaseRepository.count() == 0) {
            Purchase purchase1 = new Purchase("user1", "product1", 1);
            Purchase purchase2 = new Purchase("user2", "product2", 2);
            Purchase purchase3 = new Purchase("user3", "product4", 5);
            Purchase purchase4 = new Purchase("user4", "product5", 2);
            Purchase purchase5 = new Purchase("user1", "product3", 3);

            purchaseRepository.saveAll(Arrays.asList(purchase1, purchase2, purchase3, purchase4, purchase5));
        }
    }

    @GetMapping("/purchases")
    public List<CompletePurchase> getAll() {
        List<CompletePurchase> returnList = new ArrayList<>();
        List<Purchase> purchases = purchaseRepository.findAll();

        for (Purchase purchase : purchases) {
            returnList.add(getCompletePurchase(purchase));
        }
        return returnList;
    }

    @GetMapping("/purchases/{id}")
    public CompletePurchase getPurchaseById(@PathVariable String id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);

        if (purchase.isPresent()) {
            return getCompletePurchase(Objects.requireNonNull(purchase.get()));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The Purchase with ID " + id + " doesn't exist"
            );
        }
    }

    // Get the filled CompletePurchase for the given purchase
    private CompletePurchase getCompletePurchase(Purchase purchase) {
        Optional<User> user = userRepository.findById(purchase.getUserId());
        Optional<Product> product = productRepository.findById(purchase.getProductId());
        return new CompletePurchase(purchase, user, product);
    }
}
