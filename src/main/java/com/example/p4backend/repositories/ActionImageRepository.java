package com.example.p4backend.repositories;

import com.example.p4backend.models.ActionImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActionImageRepository extends MongoRepository<ActionImage, String> {
    List<ActionImage> findActionImagesByActionId(String actionID);
}
