package com.example.p4backend.repositories;

import com.example.p4backend.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findProductsByActionId(String actionID);
}
