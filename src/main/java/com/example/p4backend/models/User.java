package com.example.p4backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    @Nullable
    private String name;
    private String email;
    @Nullable
    @JsonIgnore
    private String password;
    private String addressID;

    public User(String name, String email, String password, String addressID) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.addressID = addressID;
    }
}