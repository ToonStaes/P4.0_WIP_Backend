package com.example.p4backend.models.DTOs;

import lombok.Getter;
import org.bson.types.Decimal128;

@Getter
public class ProductDTO {
    private String name;
    private Decimal128 cost;
    private String actionId;
}
