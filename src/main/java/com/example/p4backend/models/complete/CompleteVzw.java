package com.example.p4backend.models.complete;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.Vzw;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class CompleteVzw {
    private String id;
    private String name;
    private String email;
    private String rekeningNR;
    private String bio;
    private String youtubeLink;
    private String profilePicture;
    private String password;
    private Address address;

    public CompleteVzw(Vzw vzw, Optional<Address> address) {
        this.id = vzw.getId();
        this.name = vzw.getName();
        this.email = vzw.getEmail();
        this.rekeningNR = vzw.getRekeningNR();
        this.bio = vzw.getBio();
        this.youtubeLink = vzw.getYoutubeLink();
        this.profilePicture = vzw.getProfilePicture();
        this.password = vzw.getPassword();
        address.ifPresent(value -> this.address = value); // If address is present, the value of the optional is taken and placed into the address property of the object.
    }
}
