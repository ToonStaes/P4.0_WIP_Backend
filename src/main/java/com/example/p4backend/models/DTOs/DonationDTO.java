package com.example.p4backend.models.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;

@Getter
@Setter
@NoArgsConstructor
public class DonationDTO {
    private String vzwId;
    private Decimal128 amount;
}
