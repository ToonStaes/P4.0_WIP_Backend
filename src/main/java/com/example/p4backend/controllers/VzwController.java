package com.example.p4backend.controllers;

import com.example.p4backend.models.Vzw;
import com.example.p4backend.repositories.VzwRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "", allowedHeaders = "")
@RestController
public class VzwController {

    @Autowired
    private VzwRepository vzwRepository;

    @PostConstruct
    public void fillDB() {
        if (vzwRepository.count() == 0) {
            Vzw vzw1 = new Vzw(
                    "vzw1",
                    "vzw1.kasterlee@mail.com",
                    "be1234566798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/200.jpg",
                    "password");
            vzw1.setId("vzw1");

            Vzw vzw2 = new Vzw(
                    "vzw2",
                    "vzw2.malle@mail.com",
                    "be1234566798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/201.jpg",
                    "password");
            vzw2.setId("vzw2");

            Vzw vzw3 = new Vzw(
                    "vzw3",
                    "vzw3.herselt@mail.com",
                    "be1234599798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/202.jpg",
                    "password");
            vzw3.setId("vzw3");

            Vzw vzw4 = new Vzw(
                    "vzw4",
                    "vzw4.malle@mail.com",
                    "be1234566798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/203.jpg",
                    "password");
            vzw4.setId("vzw4");

            vzwRepository.save(vzw1);
            vzwRepository.save(vzw2);
            vzwRepository.save(vzw3);
            vzwRepository.save(vzw4);
        }
    }

    @GetMapping("/vzws")
    public List<Vzw> getAll() {
        return vzwRepository.findAll();
    }

    @GetMapping(value="/vzws/{id}")
    public Optional<Vzw> getVzwById(@PathVariable String id){
        return vzwRepository.findById(id);
    }

    @GetMapping(value="/vzws/name")
    public List<Vzw> searchVzwsByNameEmpty(){
        return vzwRepository.findAll();
    }

    @GetMapping(value="/vzws/name/{name}")
    public List<Vzw> searchVzwsByNameContaining(@PathVariable String name){
        return vzwRepository.searchByNameContaining(name);
    }
}
