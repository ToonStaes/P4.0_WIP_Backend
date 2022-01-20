package com.example.p4backend.repositories;

import com.example.p4backend.models.Action;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActionRepository  extends MongoRepository<Action, String> {

}
