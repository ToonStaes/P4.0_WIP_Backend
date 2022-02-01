package com.example.p4backend.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.Decimal128;

@Getter
@Setter
@AllArgsConstructor
public class DonationDTO {
    private String vzwId;
    private Decimal128 amount;
    private String description;
}
