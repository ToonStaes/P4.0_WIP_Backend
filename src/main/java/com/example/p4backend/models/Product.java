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
}
