package com.example.p4backend;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.User;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserUnitTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    User user1 = new User("Toon Staes", "r0784094@student.thomasmore.be", "password", "1");
    User user2 = new User("Rutger Mols", "r0698466@student.thomasmore.be", "password", "4");
    User user3 = new User("Axel Van Gestel", "r0784084@student.thomasmore.be", "password", "2");
    User user4 = new User("Britt Ooms", "r0802207@student.thomasmore.be", "password", "3");
    User user5 = new User("Sebastiaan Hensels", "r0698052@student.thomasmore.be", "password", "5");
    User user6 = new User("Gerd Janssens", "r0370181@student.thomasmore.be", "password", "6");
    User userToBeDeleted = new User("To Delete", "to@delete.be", "password", "6");

    Address address1 = new Address("Polderken", "7", "Kasterlee", "2460");
    Address address2 = new Address("Parklaan", "35", "Turnhout", "2300");
    Address address3 = new Address("Kerkeveld", "7", "Herselt", "2230");
    Address address4 = new Address("Zielestraat", "6", "Poederlee", "2275");
    Address address5 = new Address("Hoogland", "2", "Kasterlee", "2460");
    Address address6 = new Address("Slachthuisstraat", "87", "Turnhout", "2300");

    @BeforeEach
    public void beforeAllTests() {
        // ----- Address -----
        addressRepository.deleteAll();

        address1.setId("1");
        address2.setId("2");
        address3.setId("3");
        address4.setId("4");
        address5.setId("5");
        address6.setId("6");

        addressRepository.saveAll(Arrays.asList(address1, address2, address3, address4, address5, address6));

        userRepository.deleteAll();

        // ----- User -----
        // Set ID's
        user1.setId("user1");
        user2.setId("user2");
        user3.setId("user3");
        user4.setId("user4");
        user5.setId("user5");
        user6.setId("user6");
        userToBeDeleted.setId("userToBeDeleted");

        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5, user6, userToBeDeleted));

    }

    @AfterEach
    public void afterAllTests() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();


    // Gives back a list of all Users
    @Test
    void givenUsers_whenGetAllUsers_thenReturnJsonUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(7)))
                // user1 is correct
                .andExpect(jsonPath("$[0].id",is("user1")))
                .andExpect(jsonPath("$[0].name",is("Toon Staes")))
                .andExpect(jsonPath("$[0].email",is("r0784094@student.thomasmore.be")))
                .andExpect(jsonPath("$[0].address.id",is("1")))
                .andExpect(jsonPath("$[0].address.street",is("Polderken")))
                .andExpect(jsonPath("$[0].address.houseNumber",is("7")))
                .andExpect(jsonPath("$[0].address.box").isEmpty())
                .andExpect(jsonPath("$[0].address.city",is("Kasterlee")))
                .andExpect(jsonPath("$[0].address.postalCode",is("2460")));
    }
}
