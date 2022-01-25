package com.example.p4backend.UnitTests;

import com.example.p4backend.models.Address;
import com.example.p4backend.models.User;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.UserRepository;
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
public class UserControllerTests {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
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
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AddressRepository addressRepository;

    private User generateUser1() {
        user1.setId("user1");
        return user1;
    }

    private User generateUser2() {
        user2.setId("user2");
        return user2;
    }

    private List<Address> generateAddressList() {
        address1.setId("1");
        address2.setId("2");
        address3.setId("3");
        address4.setId("4");
        address5.setId("5");
        address6.setId("6");

        return List.of(address1, address2, address3, address4, address5, address6);
    }

    private List<User> generateUsersList() {
        user1.setId("user1");
        user2.setId("user2");
        user3.setId("user3");
        user4.setId("user4");
        user5.setId("user5");
        user6.setId("user6");
        userToBeDeleted.setId("userToBeDeleted");

        return List.of(user1, user2, user3, user4, user5, user6, userToBeDeleted);
    }

    // Gives back a list of all Users
    @Test
    void givenUsers_whenGetAllUsers_thenReturnJsonUsers() throws Exception {
        List<User> userList = generateUsersList();
        List<Address> addressList = generateAddressList();
        given(userRepository.findAll()).willReturn(userList);
        given(addressRepository.findById("1")).willReturn(Optional.of(addressList.get(0)));
        given(addressRepository.findById("2")).willReturn(Optional.of(addressList.get(1)));
        given(addressRepository.findById("3")).willReturn(Optional.of(addressList.get(2)));
        given(addressRepository.findById("4")).willReturn(Optional.of(addressList.get(3)));
        given(addressRepository.findById("5")).willReturn(Optional.of(addressList.get(4)));
        given(addressRepository.findById("6")).willReturn(Optional.of(addressList.get(5)));

        mockMvc.perform(get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(7)))
                // user1 is correct
                .andExpect(jsonPath("$[0].id", is("user1")))
                .andExpect(jsonPath("$[0].name", is("Toon Staes")))
                .andExpect(jsonPath("$[0].email", is("r0784094@student.thomasmore.be")))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].address.id", is("1")))
                .andExpect(jsonPath("$[0].address.street", is("Polderken")))
                .andExpect(jsonPath("$[0].address.houseNumber", is("7")))
                .andExpect(jsonPath("$[0].address.box").isEmpty())
                .andExpect(jsonPath("$[0].address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].address.postalCode", is("2460")))
                // user2 is correct
                .andExpect(jsonPath("$[1].id", is("user2")))
                .andExpect(jsonPath("$[1].name", is("Rutger Mols")))
                .andExpect(jsonPath("$[1].email", is("r0698466@student.thomasmore.be")))
                .andExpect(jsonPath("$[1].password").doesNotExist())
                .andExpect(jsonPath("$[1].address.id", is("4")))
                .andExpect(jsonPath("$[1].address.street", is("Zielestraat")))
                .andExpect(jsonPath("$[1].address.houseNumber", is("6")))
                .andExpect(jsonPath("$[1].address.box").isEmpty())
                .andExpect(jsonPath("$[1].address.city", is("Poederlee")))
                .andExpect(jsonPath("$[1].address.postalCode", is("2275")))
                // user3 is correct
                .andExpect(jsonPath("$[2].id", is("user3")))
                .andExpect(jsonPath("$[2].name", is("Axel Van Gestel")))
                .andExpect(jsonPath("$[2].email", is("r0784084@student.thomasmore.be")))
                .andExpect(jsonPath("$[2].password").doesNotExist())
                .andExpect(jsonPath("$[2].address.id", is("2")))
                .andExpect(jsonPath("$[2].address.street", is("Parklaan")))
                .andExpect(jsonPath("$[2].address.houseNumber", is("35")))
                .andExpect(jsonPath("$[2].address.box").isEmpty())
                .andExpect(jsonPath("$[2].address.city", is("Turnhout")))
                .andExpect(jsonPath("$[2].address.postalCode", is("2300")))
                // user4 is correct
                .andExpect(jsonPath("$[3].id", is("user4")))
                .andExpect(jsonPath("$[3].name", is("Britt Ooms")))
                .andExpect(jsonPath("$[3].email", is("r0802207@student.thomasmore.be")))
                .andExpect(jsonPath("$[3].password").doesNotExist())
                .andExpect(jsonPath("$[3].address.id", is("3")))
                .andExpect(jsonPath("$[3].address.street", is("Kerkeveld")))
                .andExpect(jsonPath("$[3].address.houseNumber", is("7")))
                .andExpect(jsonPath("$[3].address.box").isEmpty())
                .andExpect(jsonPath("$[3].address.city", is("Herselt")))
                .andExpect(jsonPath("$[3].address.postalCode", is("2230")))
                // user5 is correct
                .andExpect(jsonPath("$[4].id", is("user5")))
                .andExpect(jsonPath("$[4].name", is("Sebastiaan Hensels")))
                .andExpect(jsonPath("$[4].email", is("r0698052@student.thomasmore.be")))
                .andExpect(jsonPath("$[4].password").doesNotExist())
                .andExpect(jsonPath("$[4].address.id", is("5")))
                .andExpect(jsonPath("$[4].address.street", is("Hoogland")))
                .andExpect(jsonPath("$[4].address.houseNumber", is("2")))
                .andExpect(jsonPath("$[4].address.box").isEmpty())
                .andExpect(jsonPath("$[4].address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[4].address.postalCode", is("2460")))
                // user6 is correct
                .andExpect(jsonPath("$[5].id", is("user6")))
                .andExpect(jsonPath("$[5].name", is("Gerd Janssens")))
                .andExpect(jsonPath("$[5].email", is("r0370181@student.thomasmore.be")))
                .andExpect(jsonPath("$[5].password").doesNotExist())
                .andExpect(jsonPath("$[5].address.id", is("6")))
                .andExpect(jsonPath("$[5].address.street", is("Slachthuisstraat")))
                .andExpect(jsonPath("$[5].address.houseNumber", is("87")))
                .andExpect(jsonPath("$[5].address.box").isEmpty())
                .andExpect(jsonPath("$[5].address.city", is("Turnhout")))
                .andExpect(jsonPath("$[5].address.postalCode", is("2300")))
                // userToBeDeleted is correct
                .andExpect(jsonPath("$[6].id", is("userToBeDeleted")))
                .andExpect(jsonPath("$[6].name", is("To Delete")))
                .andExpect(jsonPath("$[6].email", is("to@delete.be")))
                .andExpect(jsonPath("$[6].password").doesNotExist())
                .andExpect(jsonPath("$[6].address.id", is("6")))
                .andExpect(jsonPath("$[6].address.street", is("Slachthuisstraat")))
                .andExpect(jsonPath("$[6].address.houseNumber", is("87")))
                .andExpect(jsonPath("$[6].address.box").isEmpty())
                .andExpect(jsonPath("$[6].address.city", is("Turnhout")))
                .andExpect(jsonPath("$[6].address.postalCode", is("2300")));
    }


    // Gives one user back, searched on userId (user1)
    @Test
    void givenUser_whenGetUserById_thenReturnJsonUser1() throws Exception {
        User user1 = generateUser1();
        List<Address> addressList = generateAddressList();
        given(userRepository.findById("user1")).willReturn(Optional.of(user1));
        given(addressRepository.findById("1")).willReturn(Optional.of(addressList.get(0)));

        mockMvc.perform(get("/users/{id}", "user1")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // user1 is correct
                .andExpect(jsonPath("$.id", is("user1")))
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


    // Gives one user back, searched on userId (user2)
    @Test
    void givenUser_whenGetUserById_thenReturnJsonUser2() throws Exception {
        User user2 = generateUser2();
        List<Address> addressList = generateAddressList();
        given(userRepository.findById("user2")).willReturn(Optional.of(user2));
        given(addressRepository.findById("4")).willReturn(Optional.of(addressList.get(3)));

        mockMvc.perform(get("/users/{id}", "user2")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // user2 is correct
                .andExpect(jsonPath("$.id", is("user2")))
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
