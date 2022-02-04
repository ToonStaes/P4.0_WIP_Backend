package com.example.p4backend.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VzwDTO {
    // VZW
    private String name;
    private String email;
    private String rekeningNR;
    private String bio;
    private String youtubeLink;
    private String profilePicture;
    private String password;
    // ADRESS
    private String street;
    private String houseNumber;
    private String box;
    private String city;
    private String postalCode;
}