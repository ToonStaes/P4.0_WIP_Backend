package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.Product;
import com.example.p4backend.models.Purchase;
import com.example.p4backend.models.User;
import com.example.p4backend.models.complete.CompletePurchase;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.models.dto.PurchaseDTO;
import com.example.p4backend.models.dto.UserDTO;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.ProductRepository;
import com.example.p4backend.repositories.PurchaseRepository;
import com.example.p4backend.repositories.UserRepository;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@RestController
@CrossOrigin(origins = {"http://www.wip-shop.be","http://wip-shop.be"}, allowedHeaders = "*")
public class PurchaseController {
    private static final String PATTERN_EMAIL = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,6})+$";

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final AddressController addressController;
    private final UserController userController;

    public PurchaseController(PurchaseRepository purchaseRepository, UserRepository userRepository, ProductRepository productRepository, AddressRepository addressRepository, AddressController addressController, UserController userController) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.addressController = addressController;
        this.userController = userController;
    }

    @PostConstruct
    public void fillDB() {
        if (getPurchaseRepository().count() == 0) {
            Purchase purchase1 = new Purchase("user1", "product1", 1);
            Purchase purchase2 = new Purchase("user2", "product2", 2);
            Purchase purchase3 = new Purchase("user3", "product4", 5);
            Purchase purchase4 = new Purchase("user4", "product5", 2);
            Purchase purchase5 = new Purchase("user1", "product3", 3);

            getPurchaseRepository().saveAll(List.of(purchase1, purchase2, purchase3, purchase4, purchase5));
        }
    }

    @GetMapping("/purchases")
    public List<CompletePurchase> getAll() {
        List<CompletePurchase> returnList = new ArrayList<>();
        List<Purchase> purchases = getPurchaseRepository().findAll();

        for (Purchase purchase : purchases) {
            returnList.add(getCompletePurchase(purchase));
        }
        return returnList;
    }

    @GetMapping("/purchases/{id}")
    public CompletePurchase getPurchaseById(@PathVariable String id) {
        Purchase purchase = getPurchaseRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Purchase with ID " + id + " doesn't exist"));
        return getCompletePurchase(purchase);
    }

    @PostMapping("/purchases")
    public CompletePurchase addPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        // Check to validate if the user input is valid
        if (!purchaseDTO.getEmail().matches(PATTERN_EMAIL) || purchaseDTO.getAmount() <= 0 || !getProductRepository().existsById(purchaseDTO.getProductId())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input email, amount or productId aren't valid");
        }

        User persistentUser;
        AddressDTO addressDTO = new AddressDTO(
                purchaseDTO.getStreet(),
                purchaseDTO.getHouseNumber(),
                purchaseDTO.getBox(),
                purchaseDTO.getCity(),
                purchaseDTO.getPostalCode());

        // Check if user already exists
        Optional<User> user = getUserRepository().findFirstByEmail(purchaseDTO.getEmail());
        if (user.isPresent()) {
            User oldUser = Objects.requireNonNull(user.get());
            persistentUser = getUserController().updateUser(new UserDTO(purchaseDTO.getName()), oldUser.getId());
            Optional<Address> oldAddressOptional = getAddressRepository().findById(persistentUser.getAddressID());
            // Update old address if present
            if (oldAddressOptional.isPresent()) {
                Address oldAddress = Objects.requireNonNull(oldAddressOptional.get());
                getAddressController().updateAddress(addressDTO, oldAddress.getId());
            }
        } else {
            // Add new Address
            Address persistentAddress = getAddressController().addAddress(addressDTO);
            // Add new User
            UserDTO userDTO = new UserDTO(
                    purchaseDTO.getName(),
                    purchaseDTO.getEmail(),
                    persistentAddress.getId());
            persistentUser = getUserController().addUser(userDTO);
        }

        // Create new Purchase
        Purchase persistentPurchase = new Purchase(purchaseDTO, persistentUser.getId());
        getPurchaseRepository().save(persistentPurchase);
        return getCompletePurchase(persistentPurchase);
    }

    // Get the filled CompletePurchase for the given purchase
    private CompletePurchase getCompletePurchase(Purchase purchase) {
        Optional<User> user = getUserRepository().findById(purchase.getUserId());
        Optional<Product> product = getProductRepository().findById(purchase.getProductId());
        return new CompletePurchase(purchase, user, product);
    }
}
