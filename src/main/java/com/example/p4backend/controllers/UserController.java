package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.User;
import com.example.p4backend.models.complete.CompleteUser;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @PostConstruct
    public void fillDB(){
        userRepository.deleteAll();
        if (userRepository.count() == 0){
            User user1 = new User("Toon Staes", "r0784094@student.thomasmore.be", "password", "1");
            User user2 = new User("Rutger Mols", "r0698466@student.thomasmore.be", "password", "4");
            User user3 = new User("Axel Van Gestel", "r0784084@student.thomasmore.be", "password", "2");
            User user4 = new User("Britt Ooms", "r0802207@student.thomasmore.be", "password", "3");
            User user5 = new User("Sebastiaan Hensels", "r0698052@student.thomasmore.be", "password", "5");
            User user6 = new User("Gerd Janssens", "r0370181@student.thomasmore.be", "password", "6");

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

        for (User user: users ){
            // Get address from DB
            Optional<Address> address = addressRepository.findById(user.getAddressID());
            CompleteUser completeUser = new CompleteUser(user, address);
            completeUsers.add(completeUser);
        }

        return completeUsers;
    }

    @GetMapping("/users/{id}")
    public CompleteUser getUserById(@PathVariable String id){
        Optional<User> user = userRepository.findById(id);
        CompleteUser completeUser = new CompleteUser();

        // Get address from DB
        if (user.isPresent()){
            Optional<Address> address = addressRepository.findById(user.get().getAddressID());
            // Make completeUser
            completeUser = new CompleteUser(user.get(), address);
        }

        return completeUser;
    }
}
