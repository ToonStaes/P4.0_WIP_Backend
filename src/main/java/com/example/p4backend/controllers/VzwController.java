package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.models.auth.LoginRequest;
import com.example.p4backend.models.complete.CompleteVzw;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.models.dto.VzwDTO;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.VzwRepository;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = {"http://www.wip-shop.be","http://wip-shop.be"}, allowedHeaders = "*")
@Getter
@RestController
public class VzwController {
    private static final String PATTERN_REKENINGNR = "^(?i)BE[0-9]{2}[- ]?[0-9]{4}[- ]?[0-9]{4}[- ]?[0-9]{4}$";
    private static final String PATTERN_EMAIL = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,6})+$";

    private final VzwRepository vzwRepository;
    private final AddressRepository addressRepository;
    private final AddressController addressController;
    private final PasswordEncoder passwordEncoder;

    public VzwController(VzwRepository vzwRepository, AddressRepository addressRepository, AddressController addressController, PasswordEncoder passwordEncoder) {
        this.vzwRepository = vzwRepository;
        this.addressRepository = addressRepository;
        this.addressController = addressController;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void fillDB() {
        if (getVzwRepository().count() == 0) {
            Vzw vzw1 = new Vzw(
                    "Chiro",
                    "chiro.kasterlee@mail.com",
                    "BE12-3456-6798-1234",
                    "Elk jaar organiseren wij, chiro Kasterlee, een leuke verkoop die ons wat geld oplevert zodat\n" +
                            "wij de kinderen een geweldig jaar kunnen bezorgen. Wij zijn een jeugdbeweging die vooral actief zijn in Vlaanderen. \n" +
                            "Elke zondagnamiddag komen wij samen om allerlei activiteiten te doen met de kindjes, dit kan gaan van bekende spelletjes zoals tikkertje en verstoppertje tot het bakken van taarten of het bouwen van kampen in het bos.\n" +
                            "Wilt u ons graag sponsoren kan je zeker kijken naar onze lopende activiteiten of een centje doneren. Als je het mij vraagt, CHIRO!",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://upload.wikimedia.org/wikipedia/commons/6/6f/Logo_chiro.jpg",
                    "$2a$10$GJAYbleVgyB0gXfnXMvVYuAXEL6tyUrmnA0jY65oSPb6.NwmYWu3K", // password
                    "8");
            vzw1.setId("vzw1");

            Vzw vzw2 = new Vzw(
                    "Movements",
                    "movements.malle@mail.com",
                    "BE12-3456-6798-2564",
                    "Wij als Movements zijn een dansvereniging in Malle waar kennis, kunst, sport, vriendschap en teamwork centraal staan.\n" +
                            "Met een gemotiveerd, professioneel team, ieder gespecialiseerd in een eigen dansstijl, kan het dan ook niet anders dan dat de naam “Movements” overal opduikt.\n" +
                            "Elke dinsdag, donderdag en zaterdag kan je ons terugvinden in onze dansstudio waar we ons 100% geven en nieuwe moves aanleren. \n" +
                            "Wilt u ons graag sponsoren kan je zeker kijken naar onze lopende activiteiten of een centje doneren. Het leven is een dansfeest!",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://upload.wikimedia.org/wikipedia/commons/2/2f/Movements_logo_large.png",
                    "$2a$10$sZC3j1tNmN.gM0t8Ic50KudiaCBINjncw3d.nlzo5RwZzlKaPRuzi", // password
                    "7");
            vzw2.setId("vzw2");

            Vzw vzw3 = new Vzw(
                    "Vlaamse Volksbeweging",
                    "vlaamse.volksbeweging.herselt@mail.com",
                    "BE12-3459-9798-6547",
                    "De VVB streeft naar de maatschappelijke, culturele en politieke ontvoogding van Vlaanderen tot een onafhankelijke staat in Europa en speelt daarbij een proactieve voortrekkersrol bij het behartigen van de Vlaamse belangen. \n" +
                            "Als niet-partijpolitieke, pluralistische vereniging wil ze daarom via socio-culturele weg de brede publieke opinie bij dat streven betrekken en aanzetten tot actieve inzet in het democratische besluitvormingsproces.\n" +
                            "Ons doel: een geweldloze wijze wantoestanden aan de kaak stellen, alternatieven naar voren schuiven en als drukkingsgroep de politiek beïnvloeden.\n" +
                            "Steun de VVB en koop onze producten of doneer een centje. ",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://upload.wikimedia.org/wikipedia/commons/3/3e/VVB-logo.png",
                    "$2a$10$GMQj3igq4pNpB1IlaKArSu5LWT90R/32AFgiVw6d7L8j49LAVN/fq", // password
                    "10");
            vzw3.setId("vzw3");

            Vzw vzw4 = new Vzw(
                    "Vriendenkring Kleine Bloemenstoet",
                    "vkb.malle@mail.com",
                    "BE12-3456-6798-6971",
                    "Wij zijn een vriendenkring. Dit wil zeggen dat wij een groep vrienden zijn die vrijwillig samenkomen om (onder andere) onze stoet te organiseren. \n" +
                            "Onze leden zijn mensen van in en rond Wommelgem die zich elke keer inzetten tijdens de bloemenstoet om deze tot een goed einde te brengen en er voor zorgen dat dit een zorgeloos uitstapje wordt voor iedere aanwezige.\n" +
                            "Wilt u ons graag sponsoren kan je zeker kijken naar onze lopende activiteiten of een centje doneren.",
                    "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
                    "https://upload.wikimedia.org/wikipedia/commons/d/d7/Logo_Kleine_Bloemenstoet_Wommelgem.jpg",
                    "$2a$10$NgLOCjJ3.SxLIxfJrgo6zeBQ1XrhWuhD9/wVJ6TSNqV7Sphf7Vh4.", // password
                    "9");
            vzw4.setId("vzw4");

            getVzwRepository().saveAll(List.of(vzw1, vzw2, vzw3, vzw4));
        }
    }

    @GetMapping("/vzws")
    public List<CompleteVzw> getAll() {
        List<Vzw> vzws = getVzwRepository().findAll();
        List<CompleteVzw> completeVzws = new ArrayList<>();

        for (Vzw vzw : vzws) { // for vzw in vzws
            // Get address from DB
            Optional<Address> address = getAddressRepository().findById(vzw.getAddressID());
            CompleteVzw completeVzw = new CompleteVzw(vzw, address);
            completeVzws.add(completeVzw);
        }
        return completeVzws;
    }

    @GetMapping(value = "/vzws/{id}")
    public CompleteVzw getVzwById(@PathVariable String id) {
        Vzw vzw = getVzwRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The vzw with ID " + id + " doesn't exist"));

        Optional<Address> address = getAddressRepository().findById(vzw.getAddressID());
        // Make completeUser
        return new CompleteVzw(vzw, address);
    }

    @GetMapping(value = "/vzws/name")
    public List<CompleteVzw> searchVzwsByNameEmpty() {
        return getAll();
    }

    @GetMapping(value = "/vzws/name/{name}")
    public List<CompleteVzw> searchVzwsByNameContaining(@PathVariable String name) {
        List<Vzw> vzws = getVzwRepository().searchByNameContaining(name);
        List<CompleteVzw> completeVzws = new ArrayList<>();

        for (Vzw vzw : vzws) { // for vzw in vzws
            // Get address from DB
            Optional<Address> address = getAddressRepository().findById(vzw.getAddressID());
            CompleteVzw completeVzw = new CompleteVzw(vzw, address);
            completeVzws.add(completeVzw);
        }
        return completeVzws;
    }

    // register vzw
    @PostMapping("/vzw")
    public CompleteVzw addVzw(@RequestBody VzwDTO vzwDTO) {
        // Check to validate if the user input is valid
        if (!vzwDTO.getRekeningNR().matches(PATTERN_REKENINGNR)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input rekeningnummer doesn't match the pattern");
        }
        if (!vzwDTO.getEmail().matches(PATTERN_EMAIL)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input email doesn't seem te be a valid email address");
        }

        // Check if email not already taken
        if (getVzwRepository().existsByEmail(vzwDTO.getEmail())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vzw with email already exists");
        }

        // Address
        AddressDTO tempAddress = new AddressDTO(
                vzwDTO.getStreet(),
                vzwDTO.getHouseNumber(),
                vzwDTO.getBox(),
                vzwDTO.getCity(),
                vzwDTO.getPostalCode());

        Address persistentAddress = getAddressController().addAddress(tempAddress);

        // Vzw
        Vzw persistentVzw = new Vzw(vzwDTO, persistentAddress, getPasswordEncoder().encode(vzwDTO.getPassword()));
        getVzwRepository().save(persistentVzw);
        return getCompleteVzw(persistentVzw);
    }

    // Login as vzw
    @PostMapping("/vzw/login")
    public CompleteVzw authenticateVzw(@RequestBody LoginRequest loginRequest) {
        Vzw vzw = getVzwRepository().findVzwByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No vzw with email " + loginRequest.getEmail() + " exists"));
        // Check if input passwords matches the hashed password
        if (getPasswordEncoder().matches(loginRequest.getPassword(), vzw.getPassword())) {
            return getCompleteVzw(vzw);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password doesn't match for the vzw linked to the provided email");
        }
    }

    // update vzw
    @PutMapping("/vzw/{id}")
    public CompleteVzw updateVzw(@RequestBody VzwDTO vzwDTO, @PathVariable String id) {
        // Check to validate if the user input is valid
        if (!vzwDTO.getRekeningNR().matches(PATTERN_REKENINGNR)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input rekeningnummer doesn't match the pattern");
        }
        if (!vzwDTO.getEmail().matches(PATTERN_EMAIL)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Input email doesn't seem te be a valid email address");
        }

        Vzw vzw = getVzwRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The vzw with ID " + id + " doesn't exist"));

        // Check if email not already taken and email has been updated
        if (getVzwRepository().existsByEmail(vzwDTO.getEmail()) && !Objects.equals(vzwDTO.getEmail(), vzw.getEmail())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vzw with email " + vzwDTO.getEmail() + " already exists");
        }

        // Update vzw's address
        AddressDTO addressDTO = new AddressDTO(
                vzwDTO.getStreet(),
                vzwDTO.getHouseNumber(),
                vzwDTO.getBox(),
                vzwDTO.getCity(),
                vzwDTO.getPostalCode());
        getAddressController().updateAddress(addressDTO, vzw.getAddressID());

        // Update vzw
        vzw.UpdateVzwNoPassword(vzwDTO);
        getVzwRepository().save(vzw);
        return getCompleteVzw(vzw);
    }

    // Get the filled CompleteVzw for the given vzw
    private CompleteVzw getCompleteVzw(Vzw vzw) {
        Optional<Address> address = getAddressRepository().findById(vzw.getAddressID());
        return new CompleteVzw(vzw, address);
    }
}
