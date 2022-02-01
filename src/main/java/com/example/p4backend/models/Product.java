package com.example.p4backend.models;

import com.example.p4backend.models.DTOs.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private Decimal128 cost;
    private String actionId;

    public Product(String name, Decimal128 cost, String actionId) {
        this.name = name;
        this.cost = cost;
        this.actionId = actionId;
    }

    public Product(ProductDTO productDTO){
        this.name = productDTO.getName();
        this.cost = productDTO.getCost();
        this.actionId = productDTO.getActionId();
    }
}
