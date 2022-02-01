package com.example.p4backend.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String street;
    private String houseNumber;
    private String box;
    private String city;
    private String postalCode;
}