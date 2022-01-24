package com.example.p4backend.UnitTests;

import com.example.p4backend.models.Vzw;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.VzwRepository;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VzwControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VzwRepository vzwRepository;
    @Autowired
    private AddressRepository addressRepository;

    Vzw vzw1 = new Vzw(
            "vzw1",
            "vzw1.kasterlee@mail.com",
            "be1234566798",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
            "https://http.cat/200.jpg",
            "password",
            "8");

    Vzw vzw2 = new Vzw(
            "vzw2",
            "vzw2.malle@mail.com",
            "be1234566798",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
            "https://http.cat/201.jpg",
            "password",
            "7");

    Vzw vzw3 = new Vzw(
            "vzw3",
            "vzw3.herselt@mail.com",
            "be1234599798",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
            "https://http.cat/202.jpg",
            "password",
            "10");

    Vzw vzw4 = new Vzw(
            "vzw4",
            "vzw4.malle@mail.com",
            "be1234566798",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
            "https://http.cat/203.jpg",
            "password",
            "9");

    @BeforeEach
    public void beforeAllTests() {
        vzwRepository.deleteAll();

        // Set ID's
        vzw1.setId("vzw1");
        vzw2.setId("vzw2");
        vzw3.setId("vzw3");
        vzw4.setId("vzw4");

        vzwRepository.saveAll(Arrays.asList(vzw1, vzw2, vzw3, vzw4));

    }

    @AfterEach
    public void afterAllTests() {
        addressRepository.deleteAll();
        vzwRepository.deleteAll();
    }

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();


    // Gives back a list of all Vzws
    @Test
    void givenVzws_whenGetAllVzws_thenReturnJsonVzws() throws Exception {
        mockMvc.perform(get("/vzws"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(7)))
                // vzw1 is correct
                .andExpect(jsonPath("$[0].id", is("vzw1")))
                .andExpect(jsonPath("$[0].name", is("Toon Staes")))
                .andExpect(jsonPath("$[0].email", is("r0784094@student.thomasmore.be")))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].address.id", is("1")))
                .andExpect(jsonPath("$[0].address.street", is("Polderken")))
                .andExpect(jsonPath("$[0].address.houseNumber", is("7")))
                .andExpect(jsonPath("$[0].address.box").isEmpty())
                .andExpect(jsonPath("$[0].address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].address.postalCode", is("2460")))
                // vzw2 is correct
                .andExpect(jsonPath("$[1].id", is("vzw2")))
                .andExpect(jsonPath("$[1].name", is("Rutger Mols")))
                .andExpect(jsonPath("$[1].email", is("r0698466@student.thomasmore.be")))
                .andExpect(jsonPath("$[1].password").doesNotExist())
                .andExpect(jsonPath("$[1].address.id", is("4")))
                .andExpect(jsonPath("$[1].address.street", is("Zielestraat")))
                .andExpect(jsonPath("$[1].address.houseNumber", is("6")))
                .andExpect(jsonPath("$[1].address.box").isEmpty())
                .andExpect(jsonPath("$[1].address.city", is("Poederlee")))
                .andExpect(jsonPath("$[1].address.postalCode", is("2275")))
                // vzw3 is correct
                .andExpect(jsonPath("$[2].id", is("vzw3")))
                .andExpect(jsonPath("$[2].name", is("Axel Van Gestel")))
                .andExpect(jsonPath("$[2].email", is("r0784084@student.thomasmore.be")))
                .andExpect(jsonPath("$[2].password").doesNotExist())
                .andExpect(jsonPath("$[2].address.id", is("2")))
                .andExpect(jsonPath("$[2].address.street", is("Parklaan")))
                .andExpect(jsonPath("$[2].address.houseNumber", is("35")))
                .andExpect(jsonPath("$[2].address.box").isEmpty())
                .andExpect(jsonPath("$[2].address.city", is("Turnhout")))
                .andExpect(jsonPath("$[2].address.postalCode", is("2300")))
                // vzw4 is correct
                .andExpect(jsonPath("$[3].id", is("vzw4")))
                .andExpect(jsonPath("$[3].name", is("Britt Ooms")))
                .andExpect(jsonPath("$[3].email", is("r0802207@student.thomasmore.be")))
                .andExpect(jsonPath("$[3].password").doesNotExist())
                .andExpect(jsonPath("$[3].address.id", is("3")))
                .andExpect(jsonPath("$[3].address.street", is("Kerkeveld")))
                .andExpect(jsonPath("$[3].address.houseNumber", is("7")))
                .andExpect(jsonPath("$[3].address.box").isEmpty())
                .andExpect(jsonPath("$[3].address.city", is("Herselt")))
                .andExpect(jsonPath("$[3].address.postalCode", is("2230")));
    }


    // Gives one vzw back, searched on vzwId (vzw1)
    @Test
    void givenVzw_whenGetVzwById_thenReturnJsonVzw1() throws Exception {
        mockMvc.perform(get("/vzws/{id}", "vzw1")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // vzw1 is correct
                .andExpect(jsonPath("$.id", is("vzw1")))
                .andExpect(jsonPath("$.name", is("Toon Staes")))
                .andExpect(jsonPath("$.email", is("r0784094@student.thomasmore.be")))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.address.id", is("1")))
                .andExpect(jsonPath("$.address.street", is("Polderken")))
                .andExpect(jsonPath("$.address.houseNumber", is("7")))
                .andExpect(jsonPath("$.address.box").isEmpty())
                .andExpect(jsonPath("$.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$.address.postalCode", is("2460")));
    }


    // Gives one vzw back, searched on vzwId (vzw2)
    @Test
    void givenVzw_whenGetVzwById_thenReturnJsonVzw2() throws Exception {
        mockMvc.perform(get("/vzws/{id}", "vzw2")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // vzw2 is correct
                .andExpect(jsonPath("$.id", is("vzw2")))
                .andExpect(jsonPath("$.name", is("Rutger Mols")))
                .andExpect(jsonPath("$.email", is("r0698466@student.thomasmore.be")))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.address.id", is("4")))
                .andExpect(jsonPath("$.address.street", is("Zielestraat")))
                .andExpect(jsonPath("$.address.houseNumber", is("6")))
                .andExpect(jsonPath("$.address.box").isEmpty())
                .andExpect(jsonPath("$.address.city", is("Poederlee")))
                .andExpect(jsonPath("$.address.postalCode", is("2275")));
    }
}
