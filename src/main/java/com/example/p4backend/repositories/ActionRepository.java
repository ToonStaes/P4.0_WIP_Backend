package com.example.p4backend.repositories;

import com.example.p4backend.models.Action;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface ActionRepository extends MongoRepository<Action, String> {
    List<Action> findActionsByIsActiveTrueAndEndDateAfterOrderByStartDateDesc(Date endDate);

    List<Action> findActionsByIsActiveTrueAndEndDateBetweenAndStartDateBeforeOrderByEndDateDesc(Date currentDate, Date endDate, Date startDate);

    List<Action> findActionsByIsActiveTrueAndVzwIDAndEndDateAfter(String VzwId, Date endDate);

    List<Action> findActionsByIsActiveTrueAndNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndEndDateAfter(String termsName, String termsDescription, Date endDate);

    List<Action> findActionsByIsActiveTrueAndEndDateAfter(Date endDate);
}
