package com.example.p4backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "addresses")
public class Address {
    @Id
    private String id;
    private String street;
    private String houseNumber;
    private String box;
    private String city;
    private String postalCode;
    private String userID;
    private String vzwID;
}
