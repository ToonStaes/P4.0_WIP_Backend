package com.example.p4backend.models.complete;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class CompleteProduct {
    private String id;
    private String name;
    private Decimal128 cost;
    private Action action;
    private String image;

    public CompleteProduct(Product product, Optional<Action> action) {
        this.id = product.getId();
        this.name = product.getName();
        this.cost = product.getCost();
        this.image = product.getImage();
        action.ifPresent(value -> this.action = value); // If action is present, the value of the optional is taken and placed into the action property of the object.
    }
}
