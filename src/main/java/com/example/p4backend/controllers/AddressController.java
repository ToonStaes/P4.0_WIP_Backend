package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.repositories.AddressRepository;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Getter
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AddressController {
    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @PostConstruct
    public void fillDB() {
        if (getAddressRepository().count() == 0) {
            Address address1 = new Address("Polderken", "7", "Kasterlee", "2460");
            address1.setId("1");
            Address address2 = new Address("Parklaan", "35", "Turnhout", "2300");
            address2.setId("2");
            Address address3 = new Address("Kerkeveld", "7", "Herselt", "2230");
            address3.setId("3");
            Address address4 = new Address("Zielestraat", "6", "Poederlee", "2275");
            address4.setId("4");
            Address address5 = new Address("Hoogland", "2", "Kasterlee", "2460");
            address5.setId("5");
            Address address6 = new Address("Slachthuisstraat", "87", "Turnhout", "2300");
            address6.setId("6");

            Address address7 = new Address("Kerstraat", "87", "Malle", "2390");
            address7.setId("7");
            Address address8 = new Address("Markt", "22", "Kasterlee", "2460");
            address8.setId("8");
            Address address9 = new Address("Sparrelaan", "17", "Malle", "2390");
            address9.setId("9");
            Address address10 = new Address("Stationsstraat", "27", "Herselt", "2230");
            address10.setId("10");

            getAddressRepository().saveAll(List.of(address1, address2, address3, address4, address5, address6, address7, address8, address9, address10));
        }
    }

    @GetMapping("/addresses")
    public List<Address> getAll() {
        return getAddressRepository().findAll();
    }

    @GetMapping("/addresses/{id}")
    public Optional<Address> getAddressById(@PathVariable String id) {
        return getAddressRepository().findById(id);
    }

    // @PostMapping("/addresses")
    public Address addAddress(@RequestBody AddressDTO addressDTO) {
        Address persistentAddress = new Address(addressDTO);
        getAddressRepository().save(persistentAddress);
        return persistentAddress;
    }

    // @PutMapping("/addresses/{id}")
    public Address updateAddress(@RequestBody AddressDTO updateAddress, @PathVariable String id) {
        Address address = getAddressRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Address with ID " + id + " doesn't exist"));

        address.setBox(updateAddress.getBox());
        address.setCity(updateAddress.getCity());
        address.setStreet(updateAddress.getStreet());
        address.setHouseNumber(updateAddress.getHouseNumber());
        address.setPostalCode(updateAddress.getPostalCode());
        getAddressRepository().save(address);
        return address;
    }
}
