package com.example.p4backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "actionimages")
public class ActionImage {
    @Id
    private String id;
    private String fileLocation;
    private String actionId;

    public ActionImage(String fileLocation, String actionId) {
        this.fileLocation = fileLocation;
        this.actionId = actionId;
    }
}
