package com.example.p4backend.models;

import com.example.p4backend.models.dto.DonationDTO;
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
    private String description;

    public Donation(String userId, String vzwId, Decimal128 amount, String description) {
        this.userId = userId;
        this.vzwId = vzwId;
        this.amount = amount;
        this.description = description;
    }

    public Donation(DonationDTO donationDTO) {
        this.vzwId = donationDTO.getVzwId();
        this.amount = donationDTO.getAmount();
        this.description = donationDTO.getDescription();
    }
}
