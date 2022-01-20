package com.example.p4backend.repositories;

import com.example.p4backend.models.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseRepository extends MongoRepository<Purchase, String> {
}
