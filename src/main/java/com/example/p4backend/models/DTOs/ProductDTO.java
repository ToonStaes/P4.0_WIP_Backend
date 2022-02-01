package com.example.p4backend.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.types.Decimal128;

@Getter
@AllArgsConstructor
public class ProductDTO {
    private String name;
    private Decimal128 cost;
    private String actionId;
}
