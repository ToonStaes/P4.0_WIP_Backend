package com.example.p4backend.controllers;

import com.example.p4backend.models.Donation;
import com.example.p4backend.repositories.DonationRepository;
import com.example.p4backend.repositories.UserRepository;
import com.example.p4backend.repositories.VzwRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DonationController {
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VzwRepository vzwRepository;

    @PostConstruct
    public void fillDB(){
        if (donationRepository.count() == 0) {
            Donation donation1 = new Donation("user1", "vzw1", new Decimal128(5));
            Donation donation2 = new Donation("user2", "vzw2", new Decimal128(15));
            Donation donation3 = new Donation("user3", "vzw3", new Decimal128(10));
            Donation donation4 = new Donation("user4", "vzw4", new Decimal128(3));
            Donation donation5 = new Donation("user1", "vzw2", new Decimal128((long) 7.5));

            donationRepository.saveAll(Arrays.asList(donation1, donation2, donation3, donation4, donation5));
        }
    }

    @GetMapping("/donations")
    public List<Donation> getAll() { return donationRepository.findAll(); }

    @GetMapping("/donations/{id}")
    public Optional<Donation> getDonationById(@PathVariable String id) { return donationRepository.findById(id); }
}
