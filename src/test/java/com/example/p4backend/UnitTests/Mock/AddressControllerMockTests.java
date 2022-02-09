package com.example.p4backend.UnitTests.Mock;

import com.example.p4backend.controllers.AddressController;
import com.example.p4backend.models.Address;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.repositories.AddressRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class AddressControllerMockTests {
    @Mock
    private AddressRepository addressRepository;
    @InjectMocks
    private AddressController addressController;

    @Test
    void givenAddress_whenPutAddress_thenReturnJsonAddress() {
        AddressDTO addressDTO = new AddressDTO("Address Put", "153", "12a", "Kasterlee", "2460");
        Address address = new Address(addressDTO);

        given(addressRepository.findById("address1")).willReturn(Optional.of(address));

        Address result = addressController.updateAddress(addressDTO, "address1");
        assertEquals(address, result);
    }

    @Test
    void givenAddress_whenPutAddressIdNotExist_thenReturn404() {
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
