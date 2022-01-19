package com.example.p4backend.models;

import com.mongodb.lang.Nullable;
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
    @Nullable
    private String box;
    private String city;
    private String postalCode;

    public Address(String street, String houseNumber, String box, String city, String postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.box = box;
        this.city = city;
        this.postalCode = postalCode;
    }
}
