package com.example.p4backend.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PurchaseDTO {
    // PURCHASE
    private int amount;
    private String productId;
    // USER
    private String name;
    private String email;
    // ADRESS
    private String street;
    private String houseNumber;
    private String box;
    private String city;
    private String postalCode;
}