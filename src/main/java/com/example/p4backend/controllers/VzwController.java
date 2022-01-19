package com.example.p4backend.controllers;

import com.example.p4backend.models.Vzw;
import com.example.p4backend.repositories.VzwRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class VzwController {

    @Autowired
    private VzwRepository vzwRepository;

    @GetMapping("/vzws")
    public List<Vzw> getAll() {
        return vzwRepository.findAll();
    }

    @GetMapping(value="/vzws/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Optional<Vzw> getVzwById(@PathVariable String id){
        return vzwRepository.findById(id);
    }

    @GetMapping(value="/vzws/name")
    public List<Vzw> searchVzwsByNameEmpty(){
        return vzwRepository.findAll();
    }

    @GetMapping(value="/vzws/name/{name}")
    public List<Vzw> searchVzwsByNameContaining(@PathVariable String name){
        return vzwRepository.searchByNameContaining(name);
    }
}
