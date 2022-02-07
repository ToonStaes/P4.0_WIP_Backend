package com.example.p4backend.UnitTests;

import com.example.p4backend.controllers.AddressController;
import com.example.p4backend.models.Address;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.repositories.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerPutTests {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private AddressController addressController;

    @Test
    void givenAddress_whenPutAddress_thenReturnJsonAddress() throws Exception {
        AddressDTO addressDTO = new AddressDTO("Address Put", "153", "12a", "Kasterlee", "2460");
        Address address = new Address(addressDTO);

        given(addressRepository.findById("address1")).willReturn(Optional.of(address));

        Address result = addressController.updateAddress(addressDTO, "address1");
        assertEquals(address, result);
    }

    @Test
    void givenAddress_whenPutAddressIdNotExist_thenReturn404() throws Exception {
        AddressDTO addressDTO = new AddressDTO("Address Put", "153", "12a", "Kasterlee", "2460");
        given(addressRepository.findById("address999")).willReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            addressController.updateAddress(addressDTO, "address999");
        });

        String expectedMessage = "404 NOT_FOUND \"The Address with ID address999 doesn't exist\"";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
