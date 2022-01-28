package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.models.complete.CompleteVzw;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.models.dto.VzwDTO;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.VzwRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class VzwController {

    @Autowired
    private VzwRepository vzwRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressController addressController;

    private static final String PATTERN_REKENINGNR = "^BE[0-9]{2}[- ]{0,1}[0-9]{4}[- ]{0,1}[0-9]{4}[- ]{0,1}[0-9]{4}$";
    private static final String PATTERN_EMAIL = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,6})+$";

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
                    "password",
                    "8");
            vzw1.setId("vzw1");

            Vzw vzw2 = new Vzw(
                    "vzw2",
                    "vzw2.malle@mail.com",
                    "be1234566798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/201.jpg",
                    "password",
                    "7");
            vzw2.setId("vzw2");

            Vzw vzw3 = new Vzw(
                    "vzw3",
                    "vzw3.herselt@mail.com",
                    "be1234599798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/202.jpg",
                    "password",
                    "10");
            vzw3.setId("vzw3");

            Vzw vzw4 = new Vzw(
                    "vzw4",
                    "vzw4.malle@mail.com",
                    "be1234566798",
                    "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://http.cat/203.jpg",
                    "password",
                    "9");
            vzw4.setId("vzw4");

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

    @GetMapping(value="/vzws/{id}")
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

    @PostMapping("/vzws")
    public Vzw addVzw(@RequestBody VzwDTO vzwDTO) {
        // Check to validate if the user input is valid
        if (!vzwDTO.getRekeningNR().matches(PATTERN_REKENINGNR)
        ) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"Input rekeningnummer doesn't match the pattern");}
        if (!vzwDTO.getEmail().matches(PATTERN_EMAIL)
        ) {throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"Input email doesn't seem te be a valid email address");}

        // Address
        AddressDTO tempAddress = new AddressDTO(
                vzwDTO.getStreet(),
                vzwDTO.getHouseNumber(),
                vzwDTO.getBox(),
                vzwDTO.getCity(),
                vzwDTO.getPostalCode());

        Address persistentAddress = addressController.addAddress(tempAddress);

        // Vzw
        Vzw tempVzw = new Vzw();
        Vzw persistentVzw = getVzwFromVzwDTO(tempVzw, vzwDTO, persistentAddress);
        vzwRepository.save(persistentVzw);
        return persistentVzw;
    }

    // Make a real Vzw from the VzwDTO
    private Vzw getVzwFromVzwDTO(Vzw vzw, VzwDTO vzwDTO, Address persistentAddress) {
        vzw.setName(vzwDTO.getName());
        vzw.setEmail(vzwDTO.getEmail());
        vzw.setRekeningNR(vzwDTO.getRekeningNR());
        vzw.setBio(vzwDTO.getBio());
        vzw.setYoutubeLink(vzwDTO.getYoutubeLink());
        vzw.setProfilePicture(vzwDTO.getProfilePicture());
        vzw.setPassword(vzwDTO.getPassword());
        vzw.setAddressID(persistentAddress.getId());
        return vzw;
    }
}
