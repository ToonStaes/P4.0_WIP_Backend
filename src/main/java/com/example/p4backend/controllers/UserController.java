package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.User;
import com.example.p4backend.models.complete.CompleteUser;
import com.example.p4backend.models.dto.UserDTO;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @PostConstruct
    public void fillDB() {
        if (userRepository.count() == 0) {
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

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
        }
    }

    @GetMapping("/users")
    public List<CompleteUser> getAll() {
        List<User> users = userRepository.findAll();
        List<CompleteUser> completeUsers = new ArrayList<CompleteUser>();

        for (User user : users) {
            // Get address from DB
            Optional<Address> address = addressRepository.findById(user.getAddressID());
            CompleteUser completeUser = new CompleteUser(user, address);
            completeUsers.add(completeUser);
        }

        return completeUsers;
    }

    @GetMapping("/users/{id}")
    public CompleteUser getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        CompleteUser completeUser = new CompleteUser();

        // Get address from DB
        if (user.isPresent()) {
            Optional<Address> address = addressRepository.findById(user.get().getAddressID());
            // Make completeUser
            completeUser = new CompleteUser(user.get(), address);
        }

        return completeUser;
    }

    @GetMapping("/users/email/{email}")
    public Object getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findFirstByEmail(email);
        if (user.isPresent()) {
            return getCompleteUser(Objects.requireNonNull(user.get()));
        } else {
            return "false";
        }
    }

    // @PostMapping("/users")
    public User addUser(@RequestBody UserDTO userDTO) {
        User persistentUser = new User(userDTO);
        userRepository.save(persistentUser);
        return persistentUser;
    }

    // @PutMapping("/users/{id}")
    public User updateUser(@RequestBody UserDTO updateUser, @PathVariable String id) {
        Optional<User> tempUser = userRepository.findById(id);

        if (tempUser.isPresent()) {
            User user = Objects.requireNonNull(tempUser.get());
            user.setName(updateUser.getName());
            userRepository.save(user);
            return user;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The User with ID " + id + " doesn't exist"
            );
        }
    }

    // Get the filled CompleteUser for the given user
    private CompleteUser getCompleteUser(User user) {
        Optional<Address> address = addressRepository.findById(user.getAddressID());
        return new CompleteUser(user, address);
    }
}
