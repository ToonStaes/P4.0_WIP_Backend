package com.example.p4backend.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String password;
    private String addressID;

    public UserDTO(String name, String email, String addressID) {
        this.name = name;
        this.email = email;
        this.addressID = addressID;
    }
}
