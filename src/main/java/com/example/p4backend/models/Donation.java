package com.example.p4backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "donations")
public class Donation {
    @Id
    private String id;
    private String userId;
    private String vzwId;
    private Decimal128 amount;

    public Donation(String userId, String vzwId, Decimal128 amount) {
        this.userId = userId;
        this.vzwId = vzwId;
        this.amount = amount;
    }

    public Donation(String vzwId, Decimal128 amount) {
        this.vzwId = vzwId;
        this.amount = amount;
    }
}
