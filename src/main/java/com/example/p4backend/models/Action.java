package com.example.p4backend.models;

import com.mongodb.lang.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Document(collection = "actions")
public class Action {
    @Id
    private String id;
    private String name;
    @Nullable
    private Decimal128 goal;
    private String description;
    private String vzwID;
    private Date startDate;
    private Date endDate;

    public Action(String name, @Nullable Decimal128 goal, String description, String vzwID, Date endDate) {
        this.name = name;
        this.goal = goal;
        this.description = description;
        this.vzwID = vzwID;
        this.endDate = endDate;
        this.startDate = new Date();
    }
}

