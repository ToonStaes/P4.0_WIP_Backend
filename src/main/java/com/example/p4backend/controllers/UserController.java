package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.User;
import com.example.p4backend.models.complete.CompleteUser;
import com.example.p4backend.models.dto.UserDTO;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.UserRepository;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserController(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @PostConstruct
    public void fillDB() {
        if (getUserRepository().count() == 0) {
            User user1 = new User("Toon Staes", "r0784094@student.thomasmore.be", "password", "1");
            user1.setId("user1");
            User user2 = new User("Rutger Mols", "r0698466@student.thomasmore.be", "password", "4");
            user2.setId("user2");
            User user3 = new User("Axel Van Gestel", "r0784084@student.thomasmore.be", "password", "2");
            user3.setId("user3");
            User user4 = new User("Britt Ooms", "r0802207@student.thomasmore.be", "password", "3");
            user4.setId("user4");
            User user5 = new User("Sebastiaan Hensels", "r0698052@student.thomasmore.be", "password", "5");
            user5.setId("user5");
            User user6 = new User("Gerd Janssens", "r0370181@student.thomasmore.be", "password", "6");
            user6.setId("user6");

            getUserRepository().saveAll(List.of(user1, user2, user3, user4, user5, user6));
        }
    }

    @GetMapping("/users")
    public List<CompleteUser> getAll() {
        List<User> users = getUserRepository().findAll();
        List<CompleteUser> completeUsers = new ArrayList<>();

        for (User user : users) {
            // Get address from DB
            Optional<Address> address = getAddressRepository().findById(user.getAddressID());
            CompleteUser completeUser = new CompleteUser(user, address);
            completeUsers.add(completeUser);
        }

        return completeUsers;
    }

    @GetMapping("/users/{id}")
    public CompleteUser getUserById(@PathVariable String id) {
        User user = getUserRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The User with ID " + id + " doesn't exist"));

        // Get address from DB
        Optional<Address> address = getAddressRepository().findById(user.getAddressID());
        // Make completeUser
        return new CompleteUser(user, address);
    }

    @GetMapping("/users/email/{email}")
    public Object getUserByEmail(@PathVariable String email) {
        Optional<User> user = getUserRepository().findFirstByEmail(email);
        if (user.isPresent()) {
            return getCompleteUser(Objects.requireNonNull(user.get()));
        } else {
            return "false";
        }
    }

    // @PostMapping("/users")
    public User addUser(@RequestBody UserDTO userDTO) {
        User persistentUser = new User(userDTO);
        getUserRepository().save(persistentUser);
        return persistentUser;
    }

    // @PutMapping("/users/{id}")
    public User updateUser(@RequestBody UserDTO updateUser, @PathVariable String id) {
        User user = getUserRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The User with ID " + id + " doesn't exist"));

        user.setName(updateUser.getName());
        getUserRepository().save(user);
        return user;
    }

    // Get the filled CompleteUser for the given user
    private CompleteUser getCompleteUser(User user) {
        Optional<Address> address = getAddressRepository().findById(user.getAddressID());
        return new CompleteUser(user, address);
    }
}
