package com.example.p4backend.models.complete;

import com.example.p4backend.models.Donation;
import com.example.p4backend.models.User;
import com.example.p4backend.models.Vzw;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Decimal128;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class CompleteDonation {
    private String id;
    private User user;
    private Vzw vzw;
    private Decimal128 amount;

    public CompleteDonation(Donation donation, Optional<User> user, Optional<Vzw> vzw) {
        this.id = donation.getId();
        user.ifPresent(value -> this.user = value); // If user is present, the value of the optional is taken and placed into the user property of the object.
        vzw.ifPresent(value -> this.vzw = value); // If vzw is present, the value of the optional is taken and placed into the vzw property of the object.
        this.amount = donation.getAmount();
    }

    public CompleteDonation(Donation donation,  Optional<Vzw> vzw) {
        this.id = donation.getId();
        vzw.ifPresent(value -> this.vzw = value); // If vzw is present, the value of the optional is taken and placed into the vzw property of the object.
        this.amount = donation.getAmount();
    }
}
