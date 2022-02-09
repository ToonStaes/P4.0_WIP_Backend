package com.example.p4backend.UnitTests;

import com.example.p4backend.models.Address;
import com.example.p4backend.repositories.AddressRepository;
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
public class AddressControllerTests {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    final Address address1 = new Address("Polderken", "7", "Kasterlee", "2460");
    final Address address2 = new Address("Parklaan", "35", "Turnhout", "2300");
    final Address address3 = new Address("Kerkeveld", "7", "Herselt", "2230");
    final Address address4 = new Address("Zielestraat", "6", "Poederlee", "2275");
    final Address address5 = new Address("Hoogland", "2", "Kasterlee", "2460");
    final Address address6 = new Address("Slachthuisstraat", "87", "Turnhout", "2300");
    final Address address7 = new Address("Kerstraat", "87", "255", "Malle", "2390");
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AddressRepository addressRepository;

    private Address generateAddress1() {
        address1.setId("1");
        return address1;
    }

    private Address generateAddress7() {
        address7.setId("7");
        return address7;
    }

    private List<Address> generateAddressList() {
        address1.setId("1");
        address2.setId("2");
        address3.setId("3");
        address4.setId("4");
        address5.setId("5");
        address6.setId("6");
        address7.setId("7");

        return List.of(address1, address2, address3, address4, address5, address6, address7);
    }

    // Gives back a list of all Addresses
    @Test
    void givenAddresses_whenGetAllAddresses_thenReturnJsonAddresses() throws Exception {
        List<Address> addressList = generateAddressList();
        given(addressRepository.findAll()).willReturn(addressList);

        mockMvc.perform(get("/addresses"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(7)))
                // address1 is correct
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].street", is("Polderken")))
                .andExpect(jsonPath("$[0].houseNumber", is("7")))
                .andExpect(jsonPath("$[0].box").isEmpty())
                .andExpect(jsonPath("$[0].city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].postalCode", is("2460")))
                // address2 is correct
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].street", is("Parklaan")))
                .andExpect(jsonPath("$[1].houseNumber", is("35")))
                .andExpect(jsonPath("$[1].box").isEmpty())
                .andExpect(jsonPath("$[1].city", is("Turnhout")))
                .andExpect(jsonPath("$[1].postalCode", is("2300")))
                // address3 is correct
                .andExpect(jsonPath("$[2].id", is("3")))
                .andExpect(jsonPath("$[2].street", is("Kerkeveld")))
                .andExpect(jsonPath("$[2].houseNumber", is("7")))
                .andExpect(jsonPath("$[2].box").isEmpty())
                .andExpect(jsonPath("$[2].city", is("Herselt")))
                .andExpect(jsonPath("$[2].postalCode", is("2230")))
                // address4 is correct
                .andExpect(jsonPath("$[3].id", is("4")))
                .andExpect(jsonPath("$[3].street", is("Zielestraat")))
                .andExpect(jsonPath("$[3].houseNumber", is("6")))
                .andExpect(jsonPath("$[3].box").isEmpty())
                .andExpect(jsonPath("$[3].city", is("Poederlee")))
                .andExpect(jsonPath("$[3].postalCode", is("2275")))
                // address5 is correct
                .andExpect(jsonPath("$[4].id", is("5")))
                .andExpect(jsonPath("$[4].street", is("Hoogland")))
                .andExpect(jsonPath("$[4].houseNumber", is("2")))
                .andExpect(jsonPath("$[4].box").isEmpty())
                .andExpect(jsonPath("$[4].city", is("Kasterlee")))
                .andExpect(jsonPath("$[4].postalCode", is("2460")))
                // address6 is correct
                .andExpect(jsonPath("$[5].id", is("6")))
                .andExpect(jsonPath("$[5].street", is("Slachthuisstraat")))
                .andExpect(jsonPath("$[5].houseNumber", is("87")))
                .andExpect(jsonPath("$[5].box").isEmpty())
                .andExpect(jsonPath("$[5].city", is("Turnhout")))
                .andExpect(jsonPath("$[5].postalCode", is("2300")))
                // address7 is correct
                .andExpect(jsonPath("$[6].id", is("7")))
                .andExpect(jsonPath("$[6].street", is("Kerstraat")))
                .andExpect(jsonPath("$[6].houseNumber", is("87")))
                .andExpect(jsonPath("$[6].box", is("255")))
                .andExpect(jsonPath("$[6].city", is("Malle")))
                .andExpect(jsonPath("$[6].postalCode", is("2390")));
    }


    // Gives one address back, searched on addressId (address1)
    @Test
    void givenAddress_whenGetAddressById_thenReturnJsonAddress1() throws Exception {
        Address address1 = generateAddress1();
        given(addressRepository.findById("1")).willReturn(Optional.of(address1));

        mockMvc.perform(get("/addresses/{id}", "1")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // address1 is correct
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.street", is("Polderken")))
                .andExpect(jsonPath("$.houseNumber", is("7")))
                .andExpect(jsonPath("$.box").isEmpty())
                .andExpect(jsonPath("$.city", is("Kasterlee")))
                .andExpect(jsonPath("$.postalCode", is("2460")));
    }


    // Gives one address back, searched on addressId (address7)
    @Test
    void givenAddress_whenGetAddressById_thenReturnJsonAddress7() throws Exception {
        Address address7 = generateAddress7();
        given(addressRepository.findById("7")).willReturn(Optional.of(address7));

        mockMvc.perform(get("/addresses/{id}", "7")) // command
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // address2 is correct
                .andExpect(jsonPath("$.id", is("7")))
                .andExpect(jsonPath("$.street", is("Kerstraat")))
                .andExpect(jsonPath("$.houseNumber", is("87")))
                .andExpect(jsonPath("$.box", is("255")))
                .andExpect(jsonPath("$.city", is("Malle")))
                .andExpect(jsonPath("$.postalCode", is("2390")));
    }
}
