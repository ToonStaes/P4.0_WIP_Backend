package com.example.p4backend.services;

import com.example.p4backend.models.complete.CompleteAction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WipActionService {

    /**
     *
     */
    List<CompleteAction> getNewestActions(boolean progress);
}
