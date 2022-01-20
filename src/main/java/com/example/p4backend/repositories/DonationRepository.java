package com.example.p4backend.repositories;

import com.example.p4backend.models.Donation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DonationRepository extends MongoRepository<Donation, String> {
}
