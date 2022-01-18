package com.example.p4backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "purchases")
public class Purchase {
    @Id
    private String id;
    private String userId;
    private String productId;
    private int amount;

    public Purchase(String userId, String productId, int amount) {
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }
}
