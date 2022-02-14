package com.example.p4backend.models;

import com.example.p4backend.models.dto.VzwDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "vzws")
public class Vzw {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String rekeningNR;
    private String bio;
    @Nullable
    private String youtubeLink;
    @Nullable
    private String profilePicture;
    @JsonIgnore
    private String password;
    private String addressID;

    public Vzw(String name, String email, String rekeningNR, String bio, String youtubeLink, String profilePicture, String password, String addressID) {
        this.name = name;
        this.email = email;
        this.rekeningNR = rekeningNR;
        this.bio = bio;
        this.youtubeLink = youtubeLink;
        this.profilePicture = profilePicture;
        this.password = password;
        this.addressID = addressID;
    }

    public Vzw(VzwDTO vzwDTO, Address address, String password) {
        this.name = vzwDTO.getName();
        this.email = vzwDTO.getEmail();
        this.rekeningNR = vzwDTO.getRekeningNR();
        this.bio = vzwDTO.getBio();
        this.youtubeLink = vzwDTO.getYoutubeLink();
        this.profilePicture = vzwDTO.getProfilePicture();
        this.password = password;
        this.addressID = address.getId();
    }

    public void UpdateVzwNoPassword(VzwDTO vzwDTO) {
        this.setName(vzwDTO.getName());
        this.setEmail(vzwDTO.getEmail());
        this.setRekeningNR(vzwDTO.getRekeningNR());
        this.setRekeningNR(vzwDTO.getRekeningNR());
        this.setBio(vzwDTO.getBio());
        this.setYoutubeLink(vzwDTO.getYoutubeLink());
        this.setProfilePicture(vzwDTO.getProfilePicture());
    }
}