package com.example.p4backend.UnitTests;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.VzwRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    Address address7 = new Address("Kerstraat", "87", "Malle", "2390");
    Address address8 = new Address("Markt", "22", "Kasterlee", "2460");
    Address address9 = new Address("Sparrelaan", "17", "Malle", "2390");
    Address address10 = new Address("Stationsstraat", "27", "Herselt", "2230");
    // ----- VZW -----
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
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VzwRepository vzwRepository;
    @MockBean
    private AddressRepository addressRepository;


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
                .andExpect(jsonPath("$[0].rekeningNR", is("be1234566798")))
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
                .andExpect(jsonPath("$[1].rekeningNR", is("be1234566798")))
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
                .andExpect(jsonPath("$[2].rekeningNR", is("be1234599798")))
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
                .andExpect(jsonPath("$[3].rekeningNR", is("be1234566798")))
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
                .andExpect(jsonPath("$.rekeningNR", is("be1234566798")))
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
                .andExpect(jsonPath("$.rekeningNR", is("be1234566798")))
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
                .andExpect(jsonPath("$[0].rekeningNR", is("be1234566798")))
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
                .andExpect(jsonPath("$[1].rekeningNR", is("be1234566798")))
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
                .andExpect(jsonPath("$[2].rekeningNR", is("be1234599798")))
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
                .andExpect(jsonPath("$[3].rekeningNR", is("be1234566798")))
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
                .andExpect(jsonPath("$[0].rekeningNR", is("be1234566798")))
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
}
