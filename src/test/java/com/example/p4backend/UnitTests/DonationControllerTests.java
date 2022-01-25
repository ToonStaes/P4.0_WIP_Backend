package com.example.p4backend.UnitTests;

import com.example.p4backend.models.Donation;
import com.example.p4backend.models.User;
import com.example.p4backend.models.Vzw;
import com.example.p4backend.repositories.DonationRepository;
import com.example.p4backend.repositories.UserRepository;
import com.example.p4backend.repositories.VzwRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.bson.types.Decimal128;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DonationControllerTests {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    // ----- USER -----
    User user1 = new User("Toon Staes", "r0784094@student.thomasmore.be", "password", "1");
    User user2 = new User("Rutger Mols", "r0698466@student.thomasmore.be", "password", "4");
    User user3 = new User("Axel Van Gestel", "r0784084@student.thomasmore.be", "password", "2");
    User user4 = new User("Britt Ooms", "r0802207@student.thomasmore.be", "password", "3");
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
    // ----- DONATION -----
    Donation donation1 = new Donation("user1", "vzw1", new Decimal128(5));
    Donation donation2 = new Donation("user2", "vzw2", new Decimal128(15));
    Donation donation3 = new Donation("user3", "vzw3", new Decimal128(10));
    Donation donation4 = new Donation("user4", "vzw4", new Decimal128(3));
    Donation donation5 = new Donation("user1", "vzw2", new Decimal128(7));
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private VzwRepository vzwRepository;
    @MockBean
    private DonationRepository donationRepository;

    private List<User> generateUsersList() {
        user1.setId("user1");
        user2.setId("user2");
        user3.setId("user3");
        user4.setId("user4");
        return List.of(user1, user2, user3, user4);
    }

    private List<Vzw> generateVzwsList() {
        vzw1.setId("vzw1");
        vzw2.setId("vzw2");
        vzw3.setId("vzw3");
        vzw4.setId("vzw4");
        return List.of(vzw1, vzw2, vzw3, vzw4);
    }

    private Donation generateDonation1() {
        donation1.setId("donation1");
        return donation1;
    }

    private List<Donation> generateDonationsList() {
        donation1.setId("donation1");
        donation2.setId("donation2");
        donation3.setId("donation3");
        donation4.setId("donation4");
        donation5.setId("donation5");
        return List.of(donation1, donation2, donation3, donation4, donation5);
    }

    // Gives back a list of all Donations
    @Test
    void givenDonations_whenGetAllDonations_thenReturnJsonDonations() throws Exception {
        List<User> userList = generateUsersList();
        List<Vzw> vzwList = generateVzwsList();
        List<Donation> donationList = generateDonationsList();
        given(donationRepository.findAll()).willReturn(donationList);
        given(userRepository.findById("user1")).willReturn(Optional.of(userList.get(0)));
        given(userRepository.findById("user2")).willReturn(Optional.of(userList.get(1)));
        given(userRepository.findById("user3")).willReturn(Optional.of(userList.get(2)));
        given(userRepository.findById("user4")).willReturn(Optional.of(userList.get(3)));
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzwList.get(0)));
        given(vzwRepository.findById("vzw2")).willReturn(Optional.of(vzwList.get(1)));
        given(vzwRepository.findById("vzw3")).willReturn(Optional.of(vzwList.get(2)));
        given(vzwRepository.findById("vzw4")).willReturn(Optional.of(vzwList.get(3)));

        mockMvc.perform(get("/donations"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(5)))
                // donation1 is correct
                .andExpect(jsonPath("$[0].id", is("donation1")))
                .andExpect(jsonPath("$[0].user.id", is("user1")))
                .andExpect(jsonPath("$[0].user.name", is("Toon Staes")))
                .andExpect(jsonPath("$[0].user.email", is("r0784094@student.thomasmore.be")))
                .andExpect(jsonPath("$[0].user.password").doesNotExist())
                .andExpect(jsonPath("$[0].user.addressID", is("1")))
                .andExpect(jsonPath("$[0].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[0].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[0].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[0].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[0].vzw.addressID", is("8")))
                .andExpect(jsonPath("$[0].amount", is(5)))
                // donation2 is correct
                .andExpect(jsonPath("$[1].id", is("donation2")))
                .andExpect(jsonPath("$[1].user.id", is("user2")))
                .andExpect(jsonPath("$[1].user.name", is("Rutger Mols")))
                .andExpect(jsonPath("$[1].user.email", is("r0698466@student.thomasmore.be")))
                .andExpect(jsonPath("$[1].user.password").doesNotExist())
                .andExpect(jsonPath("$[1].user.addressID", is("4")))
                .andExpect(jsonPath("$[1].vzw.id", is("vzw2")))
                .andExpect(jsonPath("$[1].vzw.name", is("vzw2")))
                .andExpect(jsonPath("$[1].vzw.email", is("vzw2.malle@mail.com")))
                .andExpect(jsonPath("$[1].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[1].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[1].vzw.profilePicture", is("https://http.cat/201.jpg")))
                .andExpect(jsonPath("$[1].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[1].vzw.addressID", is("7")))
                .andExpect(jsonPath("$[1].amount", is(15)))
                // donation3 is correct
                .andExpect(jsonPath("$[2].id", is("donation3")))
                .andExpect(jsonPath("$[2].user.id", is("user3")))
                .andExpect(jsonPath("$[2].user.name", is("Axel Van Gestel")))
                .andExpect(jsonPath("$[2].user.email", is("r0784084@student.thomasmore.be")))
                .andExpect(jsonPath("$[2].user.password").doesNotExist())
                .andExpect(jsonPath("$[2].user.addressID", is("2")))
                .andExpect(jsonPath("$[2].vzw.id", is("vzw3")))
                .andExpect(jsonPath("$[2].vzw.name", is("vzw3")))
                .andExpect(jsonPath("$[2].vzw.email", is("vzw3.herselt@mail.com")))
                .andExpect(jsonPath("$[2].vzw.rekeningNR", is("be1234599798")))
                .andExpect(jsonPath("$[2].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[2].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[2].vzw.profilePicture", is("https://http.cat/202.jpg")))
                .andExpect(jsonPath("$[2].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[2].vzw.addressID", is("10")))
                .andExpect(jsonPath("$[2].amount", is(10)))
                // donation4 is correct
                .andExpect(jsonPath("$[3].id", is("donation4")))
                .andExpect(jsonPath("$[3].user.id", is("user4")))
                .andExpect(jsonPath("$[3].user.name", is("Britt Ooms")))
                .andExpect(jsonPath("$[3].user.email", is("r0802207@student.thomasmore.be")))
                .andExpect(jsonPath("$[3].user.password").doesNotExist())
                .andExpect(jsonPath("$[3].user.addressID", is("3")))
                .andExpect(jsonPath("$[3].vzw.id", is("vzw4")))
                .andExpect(jsonPath("$[3].vzw.name", is("vzw4")))
                .andExpect(jsonPath("$[3].vzw.email", is("vzw4.malle@mail.com")))
                .andExpect(jsonPath("$[3].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[3].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[3].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[3].vzw.profilePicture", is("https://http.cat/203.jpg")))
                .andExpect(jsonPath("$[3].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[3].vzw.addressID", is("9")))
                .andExpect(jsonPath("$[3].amount", is(3)))
                // donation5 is correct
                .andExpect(jsonPath("$[4].id", is("donation5")))
                .andExpect(jsonPath("$[4].user.id", is("user1")))
                .andExpect(jsonPath("$[4].user.name", is("Toon Staes")))
                .andExpect(jsonPath("$[4].user.email", is("r0784094@student.thomasmore.be")))
                .andExpect(jsonPath("$[4].user.password").doesNotExist())
                .andExpect(jsonPath("$[4].user.addressID", is("1")))
                .andExpect(jsonPath("$[4].vzw.id", is("vzw2")))
                .andExpect(jsonPath("$[4].vzw.name", is("vzw2")))
                .andExpect(jsonPath("$[4].vzw.email", is("vzw2.malle@mail.com")))
                .andExpect(jsonPath("$[4].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[4].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[4].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[4].vzw.profilePicture", is("https://http.cat/201.jpg")))
                .andExpect(jsonPath("$[4].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[4].vzw.addressID", is("7")))
                .andExpect(jsonPath("$[4].amount", is(7)));
    }

    // Gives one donation back, searched on donationId (donation1)
    @Test
    void givenDonation_whenGetDonationById_thenReturnJsonDonation1() throws Exception {
        List<User> userList = generateUsersList();
        List<Vzw> vzwList = generateVzwsList();
        Donation donation1 = generateDonation1();
        given(donationRepository.findById("donation1")).willReturn(Optional.of(donation1));
        given(userRepository.findById("user1")).willReturn(Optional.of(userList.get(0)));
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzwList.get(0)));

        mockMvc.perform(get("/donations/{id}", "donation1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // donation1 is correct
                .andExpect(jsonPath("$.id", is("donation1")))
                .andExpect(jsonPath("$.user.id", is("user1")))
                .andExpect(jsonPath("$.user.name", is("Toon Staes")))
                .andExpect(jsonPath("$.user.email", is("r0784094@student.thomasmore.be")))
                .andExpect(jsonPath("$.user.password").doesNotExist())
                .andExpect(jsonPath("$.user.addressID", is("1")))
                .andExpect(jsonPath("$.vzw.id", is("vzw1")))
                .andExpect(jsonPath("$.vzw.name", is("vzw1")))
                .andExpect(jsonPath("$.vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$.vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$.vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$.vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$.vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$.vzw.password").doesNotExist())
                .andExpect(jsonPath("$.vzw.addressID", is("8")))
                .andExpect(jsonPath("$.amount", is(5)));
    }

    // Gives one 404 back, searched on donationId (donation999)
    @Test
    void givenDonation_whenGetDonationByIdNotExist_thenReturn404() throws Exception {
        given(donationRepository.findById("donation999")).willReturn(Optional.empty());

        mockMvc.perform(get("/donations/{id}", "donation999"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"The Donation with ID donation999 doesn't exist\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
