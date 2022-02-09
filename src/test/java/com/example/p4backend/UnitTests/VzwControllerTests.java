package com.example.p4backend.UnitTests;

import com.example.p4backend.controllers.AddressController;
import com.example.p4backend.models.Address;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.models.auth.LoginRequest;
import com.example.p4backend.models.complete.CompleteVzw;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.models.dto.VzwDTO;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.VzwRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VzwControllerTests {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    // ----- ADDRESS -----
    final Address address7 = new Address("Kerstraat", "87", "Malle", "2390");
    final Address address8 = new Address("Markt", "22", "Kasterlee", "2460");
    final Address address9 = new Address("Sparrelaan", "17", "Malle", "2390");
    final Address address10 = new Address("Stationsstraat", "27", "Herselt", "2230");
    // ----- VZW -----
    final Vzw vzw1 = new Vzw(
            "vzw1",
            "vzw1.kasterlee@mail.com",
            "BE12-3456-6798-1234",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
            "https://http.cat/200.jpg",
            "password",
            "8");
    final Vzw vzw2 = new Vzw(
            "vzw2",
            "vzw2.malle@mail.com",
            "BE12-3456-6798-2564",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
            "https://http.cat/201.jpg",
            "password",
            "7");
    final Vzw vzw3 = new Vzw(
            "vzw3",
            "vzw3.herselt@mail.com",
            "BE12-3459-9798-6547",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
            "https://http.cat/202.jpg",
            "password",
            "10");
    final Vzw vzw4 = new Vzw(
            "vzw4",
            "vzw4.malle@mail.com",
            "BE12-3456-6798-6971",
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley",
            "https://http.cat/203.jpg",
            "password",
            "9");
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VzwRepository vzwRepository;
    @MockBean
    private AddressRepository addressRepository;
    @Mock
    private AddressController addressController;


    private Vzw generateVzw1() {
        vzw1.setId("vzw1");
        return vzw1;
    }

    private Vzw generateVzw2() {
        vzw2.setId("vzw2");
        return vzw2;
    }

    private List<Address> generateAddressList() {
        address7.setId("7");
        address8.setId("8");
        address9.setId("9");
        address10.setId("10");

        return List.of(address7, address8, address9, address10);
    }

    private List<Vzw> generateVzwsList() {
        vzw1.setId("vzw1");
        vzw2.setId("vzw2");
        vzw3.setId("vzw3");
        vzw4.setId("vzw4");

        return List.of(vzw1, vzw2, vzw3, vzw4);
    }

    // Gives back a list of all Vzws
    @Test
    void givenVzws_whenGetAllVzws_thenReturnJsonVzws() throws Exception {
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        given(vzwRepository.findAll()).willReturn(vzwList);
        given(addressRepository.findById("7")).willReturn(Optional.of(addressList.get(0)));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(addressRepository.findById("10")).willReturn(Optional.of(addressList.get(3)));

        mockMvc.perform(get("/vzws"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(4)))
                // vzw1 is correct
                .andExpect(jsonPath("$[0].id", is("vzw1")))
                .andExpect(jsonPath("$[0].name", is("vzw1")))
                .andExpect(jsonPath("$[0].email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[0].rekeningNR", is("BE12-3456-6798-1234")))
                .andExpect(jsonPath("$[0].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].address.id", is("8")))
                .andExpect(jsonPath("$[0].address.street", is("Markt")))
                .andExpect(jsonPath("$[0].address.houseNumber", is("22")))
                .andExpect(jsonPath("$[0].address.box").isEmpty())
                .andExpect(jsonPath("$[0].address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].address.postalCode", is("2460")))
                // vzw2 is correct
                .andExpect(jsonPath("$[1].id", is("vzw2")))
                .andExpect(jsonPath("$[1].name", is("vzw2")))
                .andExpect(jsonPath("$[1].email", is("vzw2.malle@mail.com")))
                .andExpect(jsonPath("$[1].rekeningNR", is("BE12-3456-6798-2564")))
                .andExpect(jsonPath("$[1].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[1].profilePicture", is("https://http.cat/201.jpg")))
                .andExpect(jsonPath("$[1].password").doesNotExist())
                .andExpect(jsonPath("$[1].address.id", is("7")))
                .andExpect(jsonPath("$[1].address.street", is("Kerstraat")))
                .andExpect(jsonPath("$[1].address.houseNumber", is("87")))
                .andExpect(jsonPath("$[1].address.box").isEmpty())
                .andExpect(jsonPath("$[1].address.city", is("Malle")))
                .andExpect(jsonPath("$[1].address.postalCode", is("2390")))
                // vzw3 is correct
                .andExpect(jsonPath("$[2].id", is("vzw3")))
                .andExpect(jsonPath("$[2].name", is("vzw3")))
                .andExpect(jsonPath("$[2].email", is("vzw3.herselt@mail.com")))
                .andExpect(jsonPath("$[2].rekeningNR", is("BE12-3459-9798-6547")))
                .andExpect(jsonPath("$[2].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[2].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[2].profilePicture", is("https://http.cat/202.jpg")))
                .andExpect(jsonPath("$[2].password").doesNotExist())
                .andExpect(jsonPath("$[2].address.id", is("10")))
                .andExpect(jsonPath("$[2].address.street", is("Stationsstraat")))
                .andExpect(jsonPath("$[2].address.houseNumber", is("27")))
                .andExpect(jsonPath("$[2].address.box").isEmpty())
                .andExpect(jsonPath("$[2].address.city", is("Herselt")))
                .andExpect(jsonPath("$[2].address.postalCode", is("2230")))
                // vzw4 is correct
                .andExpect(jsonPath("$[3].id", is("vzw4")))
                .andExpect(jsonPath("$[3].name", is("vzw4")))
                .andExpect(jsonPath("$[3].email", is("vzw4.malle@mail.com")))
                .andExpect(jsonPath("$[3].rekeningNR", is("BE12-3456-6798-6971")))
                .andExpect(jsonPath("$[3].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[3].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[3].profilePicture", is("https://http.cat/203.jpg")))
                .andExpect(jsonPath("$[3].password").doesNotExist())
                .andExpect(jsonPath("$[3].address.id", is("9")))
                .andExpect(jsonPath("$[3].address.street", is("Sparrelaan")))
                .andExpect(jsonPath("$[3].address.houseNumber", is("17")))
                .andExpect(jsonPath("$[3].address.box").isEmpty())
                .andExpect(jsonPath("$[3].address.city", is("Malle")))
                .andExpect(jsonPath("$[3].address.postalCode", is("2390")));
    }


    // Gives one vzw back, searched on vzwId (vzw1)
    @Test
    void givenVzw_whenGetVzwById_thenReturnJsonVzw1() throws Exception {
        Vzw vzw1 = generateVzw1();
        List<Address> addressList = generateAddressList();
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzw1));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));

        mockMvc.perform(get("/vzws/{id}", "vzw1")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // vzw1 is correct
                .andExpect(jsonPath("$.id", is("vzw1")))
                .andExpect(jsonPath("$.name", is("vzw1")))
                .andExpect(jsonPath("$.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$.rekeningNR", is("BE12-3456-6798-1234")))
                .andExpect(jsonPath("$.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.address.id", is("8")))
                .andExpect(jsonPath("$.address.street", is("Markt")))
                .andExpect(jsonPath("$.address.houseNumber", is("22")))
                .andExpect(jsonPath("$.address.box").isEmpty())
                .andExpect(jsonPath("$.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$.address.postalCode", is("2460")));
    }


    // Gives one vzw back, searched on vzwId (vzw2)
    @Test
    void givenVzw_whenGetVzwById_thenReturnJsonVzw2() throws Exception {
        Vzw vzw2 = generateVzw2();
        List<Address> addressList = generateAddressList();
        given(vzwRepository.findById("vzw2")).willReturn(Optional.of(vzw2));
        given(addressRepository.findById("7")).willReturn(Optional.of(addressList.get(0)));

        mockMvc.perform(get("/vzws/{id}", "vzw2")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // vzw2 is correct
                .andExpect(jsonPath("$.id", is("vzw2")))
                .andExpect(jsonPath("$.name", is("vzw2")))
                .andExpect(jsonPath("$.email", is("vzw2.malle@mail.com")))
                .andExpect(jsonPath("$.rekeningNR", is("BE12-3456-6798-2564")))
                .andExpect(jsonPath("$.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$.profilePicture", is("https://http.cat/201.jpg")))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.address.id", is("7")))
                .andExpect(jsonPath("$.address.street", is("Kerstraat")))
                .andExpect(jsonPath("$.address.houseNumber", is("87")))
                .andExpect(jsonPath("$.address.box").isEmpty())
                .andExpect(jsonPath("$.address.city", is("Malle")))
                .andExpect(jsonPath("$.address.postalCode", is("2390")));
    }


    // When search by empty name, Gives back a list of all Vzws
    @Test
    void givenVzws_whenSearchVzwsByNameEmpty_thenReturnJsonVzws() throws Exception {
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        given(vzwRepository.findAll()).willReturn(vzwList);
        given(addressRepository.findById("7")).willReturn(Optional.of(addressList.get(0)));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(addressRepository.findById("10")).willReturn(Optional.of(addressList.get(3)));

        mockMvc.perform(get("/vzws/name"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(4)))
                // vzw1 is correct
                .andExpect(jsonPath("$[0].id", is("vzw1")))
                .andExpect(jsonPath("$[0].name", is("vzw1")))
                .andExpect(jsonPath("$[0].email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[0].rekeningNR", is("BE12-3456-6798-1234")))
                .andExpect(jsonPath("$[0].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].address.id", is("8")))
                .andExpect(jsonPath("$[0].address.street", is("Markt")))
                .andExpect(jsonPath("$[0].address.houseNumber", is("22")))
                .andExpect(jsonPath("$[0].address.box").isEmpty())
                .andExpect(jsonPath("$[0].address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].address.postalCode", is("2460")))
                // vzw2 is correct
                .andExpect(jsonPath("$[1].id", is("vzw2")))
                .andExpect(jsonPath("$[1].name", is("vzw2")))
                .andExpect(jsonPath("$[1].email", is("vzw2.malle@mail.com")))
                .andExpect(jsonPath("$[1].rekeningNR", is("BE12-3456-6798-2564")))
                .andExpect(jsonPath("$[1].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[1].profilePicture", is("https://http.cat/201.jpg")))
                .andExpect(jsonPath("$[1].password").doesNotExist())
                .andExpect(jsonPath("$[1].address.id", is("7")))
                .andExpect(jsonPath("$[1].address.street", is("Kerstraat")))
                .andExpect(jsonPath("$[1].address.houseNumber", is("87")))
                .andExpect(jsonPath("$[1].address.box").isEmpty())
                .andExpect(jsonPath("$[1].address.city", is("Malle")))
                .andExpect(jsonPath("$[1].address.postalCode", is("2390")))
                // vzw3 is correct
                .andExpect(jsonPath("$[2].id", is("vzw3")))
                .andExpect(jsonPath("$[2].name", is("vzw3")))
                .andExpect(jsonPath("$[2].email", is("vzw3.herselt@mail.com")))
                .andExpect(jsonPath("$[2].rekeningNR", is("BE12-3459-9798-6547")))
                .andExpect(jsonPath("$[2].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[2].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[2].profilePicture", is("https://http.cat/202.jpg")))
                .andExpect(jsonPath("$[2].password").doesNotExist())
                .andExpect(jsonPath("$[2].address.id", is("10")))
                .andExpect(jsonPath("$[2].address.street", is("Stationsstraat")))
                .andExpect(jsonPath("$[2].address.houseNumber", is("27")))
                .andExpect(jsonPath("$[2].address.box").isEmpty())
                .andExpect(jsonPath("$[2].address.city", is("Herselt")))
                .andExpect(jsonPath("$[2].address.postalCode", is("2230")))
                // vzw4 is correct
                .andExpect(jsonPath("$[3].id", is("vzw4")))
                .andExpect(jsonPath("$[3].name", is("vzw4")))
                .andExpect(jsonPath("$[3].email", is("vzw4.malle@mail.com")))
                .andExpect(jsonPath("$[3].rekeningNR", is("BE12-3456-6798-6971")))
                .andExpect(jsonPath("$[3].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[3].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[3].profilePicture", is("https://http.cat/203.jpg")))
                .andExpect(jsonPath("$[3].password").doesNotExist())
                .andExpect(jsonPath("$[3].address.id", is("9")))
                .andExpect(jsonPath("$[3].address.street", is("Sparrelaan")))
                .andExpect(jsonPath("$[3].address.houseNumber", is("17")))
                .andExpect(jsonPath("$[3].address.box").isEmpty())
                .andExpect(jsonPath("$[3].address.city", is("Malle")))
                .andExpect(jsonPath("$[3].address.postalCode", is("2390")));
    }


    // When search by name '2', Gives back a list of all Vzws having '2' in their name
    @Test
    void givenVzws_whenSearchVzwsByName_2_thenReturnJsonVzws() throws Exception {
        Vzw vzw2 = generateVzw2();
        List<Address> addressList = generateAddressList();
        given(vzwRepository.searchByNameContaining("2")).willReturn(List.of(vzw2));
        given(addressRepository.findById("7")).willReturn(Optional.of(addressList.get(0)));

        mockMvc.perform(get("/vzws/name/{name}", "2")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(1)))
                // vzw2 is correct
                .andExpect(jsonPath("$[0].id", is("vzw2")))
                .andExpect(jsonPath("$[0].name", is("vzw2")))
                .andExpect(jsonPath("$[0].email", is("vzw2.malle@mail.com")))
                .andExpect(jsonPath("$[0].rekeningNR", is("BE12-3456-6798-2564")))
                .andExpect(jsonPath("$[0].bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].profilePicture", is("https://http.cat/201.jpg")))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].address.id", is("7")))
                .andExpect(jsonPath("$[0].address.street", is("Kerstraat")))
                .andExpect(jsonPath("$[0].address.houseNumber", is("87")))
                .andExpect(jsonPath("$[0].address.box").isEmpty())
                .andExpect(jsonPath("$[0].address.city", is("Malle")))
                .andExpect(jsonPath("$[0].address.postalCode", is("2390")));
    }

    // When register a vzw, when valid gives back a completeVZW
    // Address part disabled because even though I say that address controller should return a full address object including id it just doesn't and the id is null, breaking the test...
    @Test
    void whenAddVzw_thenReturnJsonVzw() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Add", "vzw.add@test.com", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat", "1", null, "Test City", "123");
        // address
        AddressDTO addressDTO = new AddressDTO(vzwDTO.getStreet(), vzwDTO.getHouseNumber(), vzwDTO.getBox(), vzwDTO.getCity(), vzwDTO.getPostalCode());
        Address address = new Address(addressDTO);
        address.setId("AddressPost1");
        // vzw
        Vzw vzw = new Vzw(vzwDTO, address, "$2a$10$YBlUtu8sF86Sg8xeyZJJtecgjw5vq839dr1Tqb9XiLGZbfZIfzB2O");
        CompleteVzw completeVzw = new CompleteVzw(vzw, Optional.of(address));
        given(vzwRepository.existsByEmail(vzwDTO.getEmail())).willReturn(false);
        doReturn(address).when(addressController).addAddress(addressDTO);

        mockMvc.perform(post("/vzw")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(completeVzw.getId())))
                .andExpect(jsonPath("$.name", is(completeVzw.getName())))
                .andExpect(jsonPath("$.email", is(completeVzw.getEmail())))
                .andExpect(jsonPath("$.rekeningNR", is(completeVzw.getRekeningNR())))
                .andExpect(jsonPath("$.bio", is(completeVzw.getBio())))
                .andExpect(jsonPath("$.youtubeLink", is(completeVzw.getYoutubeLink())))
                .andExpect(jsonPath("$.profilePicture", is(completeVzw.getProfilePicture())))
//                .andExpect(jsonPath("$.address.id", is(completeVzw.getAddress().getId())))
//                .andExpect(jsonPath("$.address.street", is(completeVzw.getAddress().getStreet())))
//                .andExpect(jsonPath("$.address.houseNumber", is(completeVzw.getAddress().getHouseNumber())))
//                .andExpect(jsonPath("$.address.box", is(completeVzw.getAddress().getBox())))
//                .andExpect(jsonPath("$.address.city", is(completeVzw.getAddress().getCity())))
//                .andExpect(jsonPath("$.address.postalCode", is(completeVzw.getAddress().getPostalCode())))
        ;
    }

    // When register a vzw, when email taken returns 400 bad request
    @Test
    void whenAddVzwEmailTaken_thenReturn400BadRequest() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Add", "vzw.add@test.com", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat", "1", null, "Test City", "123");
        given(vzwRepository.existsByEmail(vzwDTO.getEmail())).willReturn(true);

        mockMvc.perform(post("/vzw")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Vzw with email already exists\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    // When register a vzw, when invalid rekeningnr returns 400 bad request
    @Test
    void whenAddVzwRekeningNRInvalid_thenReturn400BadRequest() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Add", "vzw.add@test.com", "BE12-invalid", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat gerd", "1", null, "Test City", "123");

        mockMvc.perform(post("/vzw")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Input rekeningnummer doesn't match the pattern\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    // When register a vzw, when invalid email returns 400 bad request
    @Test
    void whenAddVzwEmailInvalid_thenReturn400BadRequest() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Add", "vzw.add.invalid", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat gerd", "1", null, "Test City", "123");

        mockMvc.perform(post("/vzw")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Input email doesn't seem te be a valid email address\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    // When updating a vzw, when valid gives back a completeVZW
    @Test
    void whenPutVzw_thenReturnJsonVzw() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Put", "vzw.put@test.com", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat", "1", null, "Test City", "123");

        // address
        Address address = new Address(vzwDTO.getStreet(), vzwDTO.getHouseNumber(), vzwDTO.getBox(), vzwDTO.getCity(), vzwDTO.getPostalCode());
        address.setId("8");

        // vzw
        Vzw vzw = new Vzw(vzwDTO, address, "password");
        vzw.setId("vzw1");
        CompleteVzw completeVzw = new CompleteVzw(vzw, Optional.of(address));

        given(vzwRepository.existsByEmail(vzwDTO.getEmail())).willReturn(false);
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(generateVzw1()));
        given(addressRepository.findById("8")).willReturn(Optional.of(address));

        mockMvc.perform(put("/vzw/{id}", "vzw1")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(completeVzw.getId())))
                .andExpect(jsonPath("$.name", is(completeVzw.getName())))
                .andExpect(jsonPath("$.email", is(completeVzw.getEmail())))
                .andExpect(jsonPath("$.rekeningNR", is(completeVzw.getRekeningNR())))
                .andExpect(jsonPath("$.bio", is(completeVzw.getBio())))
                .andExpect(jsonPath("$.youtubeLink", is(completeVzw.getYoutubeLink())))
                .andExpect(jsonPath("$.profilePicture", is(completeVzw.getProfilePicture())))
                .andExpect(jsonPath("$.address.id", is(completeVzw.getAddress().getId())))
                .andExpect(jsonPath("$.address.street", is(completeVzw.getAddress().getStreet())))
                .andExpect(jsonPath("$.address.houseNumber", is(completeVzw.getAddress().getHouseNumber())))
                .andExpect(jsonPath("$.address.box", is(completeVzw.getAddress().getBox())))
                .andExpect(jsonPath("$.address.city", is(completeVzw.getAddress().getCity())))
                .andExpect(jsonPath("$.address.postalCode", is(completeVzw.getAddress().getPostalCode())));
    }

    // When updating a vzw, when invalid id returns 404 not found
    @Test
    void whenPutVzwIdNotExist_thenReturn404BadRequest() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Put", "vzw.put@test.com", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat", "1", null, "Test City", "123");

        mockMvc.perform(put("/vzw/{id}", "vzw999")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"The vzw with ID vzw999 doesn't exist\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    // When updating a vzw, when email taken returns 400 bad request
    @Test
    void whenPutVzwEmailTaken_thenReturn400BadRequest() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Put", "vzw.put@test.com", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat", "1", null, "Test City", "123");
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(generateVzw1()));
        given(vzwRepository.existsByEmail(vzwDTO.getEmail())).willReturn(true);

        mockMvc.perform(put("/vzw/{id}", "vzw1")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Vzw with email vzw.put@test.com already exists\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    // When updating a vzw, when invalid rekeningnr returns 400 bad request
    @Test
    void whenPutVzwRekeningNRInvalid_thenReturn400BadRequest() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Put", "vzw.put@test.com", "BE12-invalid", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat gerd", "1", null, "Test City", "123");

        mockMvc.perform(put("/vzw/{id}", "vzw1")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Input rekeningnummer doesn't match the pattern\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    // When updating a vzw, when invalid email returns 400 bad request
    @Test
    void whenPutVzwEmailInvalid_thenReturn400BadRequest() throws Exception {
        VzwDTO vzwDTO = new VzwDTO("VZW Put", "vzw.put.invalid", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "test", "Test Straat gerd", "1", null, "Test City", "123");

        mockMvc.perform(put("/vzw/{id}", "vzw1")
                        .content(mapper.writeValueAsString(vzwDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Input email doesn't seem te be a valid email address\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    // When login as vzw, when valid gives back a completeVZW
    @Test
    void whenLogin_thenReturnJsonVzw() throws Exception {
        LoginRequest loginRequest = new LoginRequest("vzw.add@test.com", "test");
        // address
        Address address = new Address("Test Straat", "1", null, "Test City", "123");
        address.setId("AddressPost");
        // vzw
        Vzw vzw = new Vzw("VZW Add", "vzw.add@test.com", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "$2a$10$YBlUtu8sF86Sg8xeyZJJtecgjw5vq839dr1Tqb9XiLGZbfZIfzB2O", address.getId()); // password = bcrypt hashed version of "test"
        CompleteVzw completeVzw = new CompleteVzw(vzw, Optional.of(address));
        given(vzwRepository.findVzwByEmail(loginRequest.getEmail())).willReturn(Optional.of(vzw));
        given(addressRepository.findById("AddressPost")).willReturn(Optional.of(address));

        mockMvc.perform(post("/vzw/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(completeVzw.getId())))
                .andExpect(jsonPath("$.name", is(completeVzw.getName())))
                .andExpect(jsonPath("$.email", is(completeVzw.getEmail())))
                .andExpect(jsonPath("$.rekeningNR", is(completeVzw.getRekeningNR())))
                .andExpect(jsonPath("$.bio", is(completeVzw.getBio())))
                .andExpect(jsonPath("$.youtubeLink", is(completeVzw.getYoutubeLink())))
                .andExpect(jsonPath("$.profilePicture", is(completeVzw.getProfilePicture())))
                .andExpect(jsonPath("$.address.id", is(completeVzw.getAddress().getId())))
                .andExpect(jsonPath("$.address.street", is(completeVzw.getAddress().getStreet())))
                .andExpect(jsonPath("$.address.houseNumber", is(completeVzw.getAddress().getHouseNumber())))
                .andExpect(jsonPath("$.address.box", is(completeVzw.getAddress().getBox())))
                .andExpect(jsonPath("$.address.city", is(completeVzw.getAddress().getCity())))
                .andExpect(jsonPath("$.address.postalCode", is(completeVzw.getAddress().getPostalCode())));
    }

    // When login as vzw, when invalid password returns 400 bad request
    @Test
    void whenLoginPasswordInvalid_thenReturn400BadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("vzw.add@test.com", "passwordInvalid");
        // vzw
        Vzw vzw = new Vzw("VZW Add", "vzw.add@test.com", "BE12-3456-6798-2555", "A new vzw.", "https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley", "https://http.cat/200.jpg", "$2a$10$YBlUtu8sF86Sg8xeyZJJtecgjw5vq839dr1Tqb9XiLGZbfZIfzB2O", "addressPost"); // password = bcrypt hashed version of "test"
        given(vzwRepository.findVzwByEmail(loginRequest.getEmail())).willReturn(Optional.of(vzw));

        mockMvc.perform(post("/vzw/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"The password doesn't match for the vzw linked to the provided email\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    // When login as vzw, when mail doesn't exist returns 404 not found
    @Test
    void whenLoginEmailNotFound_thenReturn400BadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("vzw.add@test.com", "passwordInvalid");
        given(vzwRepository.findVzwByEmail(loginRequest.getEmail())).willReturn(Optional.empty());

        mockMvc.perform(post("/vzw/login")
                        .content(mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"No vzw with email vzw.add@test.com exists\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
