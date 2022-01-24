package com.example.p4backend.models.complete;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class CompleteUser {
    private String id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private Address address;

    public CompleteUser(User user, Optional<Address> address){
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        address.ifPresent(value -> this.address = value); // If address is present, the value of the optional is taken and placed into the address property of the object.
    }
}
