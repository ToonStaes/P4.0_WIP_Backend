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
@Document(collection = "vzws")
public class Vzw {
    @Id
    private String id;
    private String name;
    private String email;
    private String rekeningNR;
    private String bio;
    @Nullable
    private String youtubeLink;
    @Nullable
    private String profilePicture;
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
}