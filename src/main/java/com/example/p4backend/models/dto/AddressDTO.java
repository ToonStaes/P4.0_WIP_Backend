package com.example.p4backend.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {
    private String street;
    private String houseNumber;
    private String box;
    private String city;
    private String postalCode;

    public AddressDTO(String street, String houseNumber, String box, String city, String postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.box = box;
        this.city = city;
        this.postalCode = postalCode;
    }
}