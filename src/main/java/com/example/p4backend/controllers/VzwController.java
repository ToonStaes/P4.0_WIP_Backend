package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.models.complete.CompleteUser;
import com.example.p4backend.models.complete.CompleteVzw;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.VzwRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class VzwController {

    @Autowired
    private VzwRepository vzwRepository;
    @Autowired
    private AddressRepository addressRepository;

    @PostConstruct
    public void fillDB() {
        if (vzwRepository.count() == 0) {
            Vzw vzw1 = new Vzw(
                    "vzw1",
                    "vzw1.kasterlee@mail.com",
                    "be1234566798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/200",
                    "password",
                    "8");

            Vzw vzw2 = new Vzw(
                    "vzw2",
                    "vzw2.malle@mail.com",
                    "be1234566798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/201",
                    "password",
                    "7");

            Vzw vzw3 = new Vzw(
                    "vzw3",
                    "vzw3.herselt@mail.com",
                    "be1234599798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/202",
                    "password",
                    "10");

            Vzw vzw4 = new Vzw(
                    "vzw4",
                    "vzw4.malle@mail.com",
                    "be1234566798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/203",
                    "password",
                    "9");

            vzwRepository.save(vzw1);
            vzwRepository.save(vzw2);
            vzwRepository.save(vzw3);
            vzwRepository.save(vzw4);
        }
    }

    @GetMapping("/vzws")
    public List<CompleteVzw> getAll() {
        List<Vzw> vzws = vzwRepository.findAll();
        List<CompleteVzw> completeVzws = new ArrayList<>();

        for (Vzw vzw: vzws){ // for vzw in vzws
            // Get address from DB
            Optional<Address> address = addressRepository.findById(vzw.getAddressID());
            CompleteVzw completeVzw = new CompleteVzw(vzw, address);
            completeVzws.add(completeVzw);
        }
        return completeVzws;
    }

    @GetMapping(value="/vzws/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public CompleteVzw getVzwById(@PathVariable String id){
        Optional<Vzw> vzw = vzwRepository.findById(id);
        CompleteVzw completeVzw = new CompleteVzw();

        if (vzw.isPresent()){
            Optional<Address> address = addressRepository.findById(vzw.get().getAddressID());
            // Make completeUser
            completeVzw = new CompleteVzw(vzw.get(), address);
        }

        return completeVzw;
    }

    @GetMapping(value="/vzws/name")
    public List<CompleteVzw> searchVzwsByNameEmpty(){
        return getAll();
    }

    @GetMapping(value="/vzws/name/{name}")
    public List<CompleteVzw> searchVzwsByNameContaining(@PathVariable String name){
        List<Vzw> vzws = vzwRepository.searchByNameContaining(name);
        List<CompleteVzw> completeVzws = new ArrayList<>();

        for (Vzw vzw: vzws){ // for vzw in vzws
            // Get address from DB
            Optional<Address> address = addressRepository.findById(vzw.getAddressID());
            CompleteVzw completeVzw = new CompleteVzw(vzw, address);
            completeVzws.add(completeVzw);
        }
        return completeVzws;
    }
}
