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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressController addressController;
    @Autowired
    private UserController userController;

    private static final String PATTERN_EMAIL = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,6})+$";

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

    @PostMapping("/purchases")
    public Purchase addPurchase(@RequestBody PurchaseDTO purchaseDTO) {
        // Check to validate if the user input is valid
        if (!purchaseDTO.getEmail().matches(PATTERN_EMAIL) || purchaseDTO.getAmount() <= 0 || !productRepository.existsById(purchaseDTO.getProductId())
        ) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"Input email, amount or productId aren't valid");}

        User persistentUser;
        AddressDTO addressDTO = new AddressDTO(
                purchaseDTO.getStreet(),
                purchaseDTO.getHouseNumber(),
                purchaseDTO.getBox(),
                purchaseDTO.getCity(),
                purchaseDTO.getPostalCode());

        // Check if user already exists
        Optional<User> user = userRepository.findFirstByEmail(purchaseDTO.getEmail());
        if (user.isPresent()) {
            User oldUser = Objects.requireNonNull(user.get());
            persistentUser = userController.updateUser(new UserDTO(purchaseDTO.getName()), oldUser.getId());
            Optional<Address> oldAddressOptional = addressRepository.findById(persistentUser.getAddressID());
            // Update old address if present
            if (oldAddressOptional.isPresent()) {
                Address oldAddress = Objects.requireNonNull(oldAddressOptional.get());
                addressController.updateAddress(addressDTO, oldAddress.getId());
            }
        } else {
            // Add new Address
            Address persistentAddress;
            persistentAddress = addressController.addAddress(addressDTO);
            // Add new User
            UserDTO userDTO = new UserDTO(
                    purchaseDTO.getName(),
                    purchaseDTO.getEmail(),
                    persistentAddress.getId());
            persistentUser = userController.addUser(userDTO);
        }

        // Create new Purchase
        Purchase persistentPurchase = new Purchase();
        persistentPurchase.setUserId(persistentUser.getId());
        persistentPurchase.setProductId(purchaseDTO.getProductId());
        persistentPurchase.setAmount(purchaseDTO.getAmount());
        purchaseRepository.save(persistentPurchase);
        return persistentPurchase;
    }

    // Get the filled CompletePurchase for the given purchase
    private CompletePurchase getCompletePurchase(Purchase purchase) {
        Optional<User> user = userRepository.findById(purchase.getUserId());
        Optional<Product> product = productRepository.findById(purchase.getProductId());
        return new CompletePurchase(purchase, user, product);
    }
}
