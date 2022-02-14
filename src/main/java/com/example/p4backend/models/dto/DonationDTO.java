package com.example.p4backend.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.Decimal128;

@Getter
@AllArgsConstructor
public class DonationDTO {
    private String vzwId;
    private Decimal128 amount;
    private String description;
}
