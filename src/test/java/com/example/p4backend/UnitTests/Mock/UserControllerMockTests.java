package com.example.p4backend.UnitTests.Mock;

import com.example.p4backend.controllers.UserController;
import com.example.p4backend.models.User;
import com.example.p4backend.models.dto.UserDTO;
import com.example.p4backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerMockTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserController userController;

    @Test
    void givenUser_whenPostUser_thenReturnJsonUser() {
        UserDTO userDTO = new UserDTO("User Put", "user.put@student.thomasmore.be", "1");
        User user = new User(userDTO);

        when(userRepository.save(user)).thenReturn(user);

        User result = userController.addUser(userDTO);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getAddressID(), result.getAddressID());
    }

    @Test
    void givenUser_whenPutUser_thenReturnJsonUser() {
        UserDTO userDTO = new UserDTO("User Put", "user.put@student.thomasmore.be", "1");
        User user = new User(userDTO);

        given(userRepository.findById("user1")).willReturn(Optional.of(user));

        User result = userController.updateUser(userDTO, "user1");
        assertEquals(user, result);
    }

    @Test
    void givenUser_whenPutUserIdNotExist_thenReturn404() {
        UserDTO userDTO = new UserDTO("User Put", "user.put@student.thomasmore.be", "1");
        given(userRepository.findById("user999")).willReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            userController.updateUser(userDTO, "user999");
        });

        String expectedMessage = "404 NOT_FOUND \"The User with ID user999 doesn't exist\"";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
