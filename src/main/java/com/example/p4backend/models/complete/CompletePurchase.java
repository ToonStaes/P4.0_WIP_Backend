package com.example.p4backend.models.complete;

import com.example.p4backend.models.Product;
import com.example.p4backend.models.Purchase;
import com.example.p4backend.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class CompletePurchase {
    private String id;
    private User user;
    private Product product;
    private int amount;

    public CompletePurchase(Purchase purchase, Optional<User> user, Optional<Product> product) {
        this.id = purchase.getId();
        user.ifPresent(value -> this.user = value); // If user is present, the value of the optional is taken and placed into the user property of the object.
        product.ifPresent(value -> this.product = value); // If product is present, the value of the optional is taken and placed into the product property of the object.
        this.amount = purchase.getAmount();
    }
}
