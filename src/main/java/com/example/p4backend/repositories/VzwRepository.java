package com.example.p4backend.repositories;

import com.example.p4backend.models.Vzw;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VzwRepository extends MongoRepository<Vzw, String> {
    List<Vzw> searchByNameContaining(String name);
}
