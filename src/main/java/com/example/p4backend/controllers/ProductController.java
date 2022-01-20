package com.example.p4backend.controllers;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.Product;
import com.example.p4backend.models.complete.CompleteProduct;
import com.example.p4backend.repositories.ActionRepository;
import com.example.p4backend.repositories.ProductRepository;
import org.bson.types.Decimal128;
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
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ActionRepository actionRepository;

    @PostConstruct
    public void fillDB(){
        if (productRepository.count() == 0) {
            Product product1 = new Product("product1", new Decimal128(5), "action1");
            product1.setId("product1");
            Product product2 = new Product("product2", new Decimal128(15), "action1");
            product2.setId("product2");
            Product product3 = new Product("product3", new Decimal128(12), "action1");
            product3.setId("product3");
            Product product4 = new Product("product4", new Decimal128(2), "action2");
            product4.setId("product4");
            Product product5 = new Product("product5", new Decimal128(6), "action3");
            product5.setId("product5");

            productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5));
        }
    }

    @GetMapping("/products")
    public List<CompleteProduct> getAll() {
        List<CompleteProduct> returnList = new ArrayList<>();
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            returnList.add(getCompleteProduct(product));
        }
        return returnList;
    }

    @GetMapping("/products/{id}")
    public CompleteProduct getProductById(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return getCompleteProduct(Objects.requireNonNull(product.get()));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The Product with ID " + id + " doesn't exist"
            );
        }
    }

    // Get the filled CompleteProduct for the given product
    private CompleteProduct getCompleteProduct(Product product) {
        Optional<Action> action = actionRepository.findById(product.getActionId());
        return new CompleteProduct(product, action);
    }
}
