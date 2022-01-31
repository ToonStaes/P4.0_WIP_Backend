package com.example.p4backend.controllers;

import com.example.p4backend.models.DTOs.DonationDTO;
import com.example.p4backend.models.Donation;
import com.example.p4backend.models.User;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.models.complete.CompleteDonation;
import com.example.p4backend.repositories.DonationRepository;
import com.example.p4backend.repositories.UserRepository;
import com.example.p4backend.repositories.VzwRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.*;

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
            Donation donation5 = new Donation("user1", "vzw2", new Decimal128(7));

            donationRepository.saveAll(Arrays.asList(donation1, donation2, donation3, donation4, donation5));
        }
    }

    @GetMapping("/donations")
    public List<CompleteDonation> getAll() {
        List<CompleteDonation> returnList = new ArrayList<>();
        List<Donation> donations = donationRepository.findAll();

        for (Donation donation : donations) {
            returnList.add(getCompleteDonation(donation));
        }
        return returnList;
    }

    @GetMapping("/donations/{id}")
    public CompleteDonation getDonationById(@PathVariable String id) {
        Optional<Donation> donation = donationRepository.findById(id);

        if (donation.isPresent()) {
            return getCompleteDonation(Objects.requireNonNull(donation.get()));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The Donation with ID " + id + " doesn't exist"
            );
        }
    }

    @PostMapping("/donation")
    public CompleteDonation addDonation(@RequestBody DonationDTO donationDTO){
        Donation donation = new Donation(donationDTO.getVzwId(), donationDTO.getAmount());
        donationRepository.save(donation);
        return getCompleteDonation(donation);
    }

    // Get the filled CompleteDonation for the given donation
    private CompleteDonation getCompleteDonation(Donation donation) {
        Optional<Vzw> vzw = vzwRepository.findById(donation.getVzwId());
        if (donation.getUserId() != null){
            Optional<User> user = userRepository.findById(donation.getUserId());
            return new CompleteDonation(donation, user, vzw);
        }
        else {
            return new CompleteDonation(donation, vzw);
        }


    }
}
