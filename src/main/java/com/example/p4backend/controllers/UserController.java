package com.example.p4backend.controllers;

import com.example.p4backend.models.User;
import com.example.p4backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void fillDB(){
        if (userRepository.count() == 0){
            User user1 = new User("Toon Staes", "r0784094@student.thomasmore.be", "password");
            User user2 = new User("Rutger Mols", "r0698466@student.thomasmore.be", "password");
            User user3 = new User("Axel Van Gestel", "r0784084@student.thomasmore.be", "password");
            User user4 = new User("Britt Ooms", "r0802207@student.thomasmore.be", "password");
            User user5 = new User("Sebastiaan Hensels", "r0698052@student.thomasmore.be", "password");
            User user6 = new User("Gerd Janssens", "r0370181@student.thomasmore.be", "password");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
        }
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable String id){
        return userRepository.findById(id);
    }
}
