package com.example.p4backend.controllers;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.models.dto.VzwDTO;
import com.example.p4backend.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AddressController {
    @Autowired
    private AddressRepository addressRepository;

    @PostConstruct
    public void fillDB(){
        if (addressRepository.count() == 0) {
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

            addressRepository.save(address1);
            addressRepository.save(address2);
            addressRepository.save(address3);
            addressRepository.save(address4);
            addressRepository.save(address5);
            addressRepository.save(address6);
            addressRepository.save(address7);
            addressRepository.save(address8);
            addressRepository.save(address9);
            addressRepository.save(address10);
        }
    }

    @GetMapping("/addresses")
    public List<Address> getAll() { return addressRepository.findAll(); }

    @GetMapping("/addresses/{id}")
    public Optional<Address> getAddressById(@PathVariable String id) { return addressRepository.findById(id); }

    // @PostMapping("/addresses")
    public Address addAddress(@RequestBody AddressDTO addressDTO) {
        Address tempAddress = new Address();
        Address persistentAddress = getAddressFromAddressDTO(tempAddress, addressDTO);
        addressRepository.save(persistentAddress);
        return persistentAddress;
    }

    // Make a real Address from the AddressDTO
    private Address getAddressFromAddressDTO(Address address, AddressDTO addressDTO) {
        address.setStreet(addressDTO.getStreet());
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.setBox(addressDTO.getBox());
        address.setCity(addressDTO.getCity());
        address.setPostalCode(addressDTO.getPostalCode());
        return address;
    }
}
