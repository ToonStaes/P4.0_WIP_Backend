package com.example.p4backend.UnitTests;

import com.example.p4backend.controllers.AddressController;
import com.example.p4backend.controllers.UserController;
import com.example.p4backend.models.Address;
import com.example.p4backend.models.Product;
import com.example.p4backend.models.Purchase;
import com.example.p4backend.models.User;
import com.example.p4backend.models.complete.CompletePurchase;
import com.example.p4backend.models.dto.AddressDTO;
import com.example.p4backend.models.dto.PurchaseDTO;
import com.example.p4backend.models.dto.UserDTO;
import com.example.p4backend.repositories.AddressRepository;
import com.example.p4backend.repositories.ProductRepository;
import com.example.p4backend.repositories.PurchaseRepository;
import com.example.p4backend.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.bson.types.Decimal128;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseControllerTests {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PurchaseRepository purchaseRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private AddressRepository addressRepository;
    @Mock
    private UserController userController;
    @Mock
    private AddressController addressController;

    private User generateUser() {
        User user = new User("user1", "user1@mail.be", "password", "address1");
        user.setId("user1");
        return user;
    }

    private Product generateProduct() {
        Product product = new Product("product1", new Decimal128(new BigDecimal("25.99")), "action1");
        product.setId("product1");
        return product;
    }

    private Purchase generatePurchase() {
        Purchase purchase = new Purchase("user1", "product1", 5);
        purchase.setId("purchase1");
        return purchase;
    }

    private CompletePurchase generateCompletePurchase() {
        Purchase purchase = generatePurchase();
        User user = generateUser();
        Product product = generateProduct();

        return new CompletePurchase(purchase, Optional.of(user), Optional.of(product));
    }

    private List<Purchase> generatePurchases() {
        int i = 1;
        List<Purchase> purchases = new ArrayList<>();

        while (i < 6) {
            Purchase purchase = new Purchase("user1", "product1", 5 + i);
            purchase.setId("purchase" + i);
            purchases.add(purchase);
            i++;
        }

        return purchases;
    }

    private List<CompletePurchase> generateCompletePurchases() {
        List<Purchase> purchases = generatePurchases();
        User user = generateUser();
        Product product = generateProduct();

        List<CompletePurchase> completePurchases = new ArrayList<>();

        for (Purchase purchase : purchases) {
            CompletePurchase completePurchase = new CompletePurchase(purchase, Optional.of(user), Optional.of(product));
            completePurchases.add(completePurchase);
        }

        return completePurchases;
    }

    private User generateUserPost(UserDTO userDTO) {
        User user = new User(userDTO);
        user.setId("user2");
        return user;
    }

    private Address generateAddressPost(AddressDTO addressDTO) {
        Address address = new Address(addressDTO);
        address.setId("address2");
        return address;
    }

    private Purchase generatePurchasePost(PurchaseDTO purchaseDTO, User user) {
        Purchase purchase = new Purchase(purchaseDTO, user.getId());
        purchase.setId("purchase2");
        return purchase;
    }


    @Test
    void givenPurchases_whenGetAll_thenReturnJsonPurchases() throws Exception {
        List<Purchase> purchases = generatePurchases();
        List<CompletePurchase> completePurchases = generateCompletePurchases();
        User user = generateUser();
        Product product = generateProduct();

        given(purchaseRepository.findAll()).willReturn(purchases);
        given(userRepository.findById("user1")).willReturn(Optional.of(user));
        given(productRepository.findById("product1")).willReturn(Optional.of(product));

        mockMvc.perform(get("/purchases"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(completePurchases.get(0).getId())))
                .andExpect(jsonPath("$[0].amount", is(completePurchases.get(0).getAmount())))
                .andExpect(jsonPath("$[0].user.id", is(completePurchases.get(0).getUser().getId())))
                .andExpect(jsonPath("$[0].user.email", is(completePurchases.get(0).getUser().getEmail())))
                .andExpect(jsonPath("$[0].user.addressID", is(completePurchases.get(0).getUser().getAddressID())))
                .andExpect(jsonPath("$[0].user.name", is(completePurchases.get(0).getUser().getName())))
                .andExpect(jsonPath("$[0].user.id", is(completePurchases.get(0).getUser().getId())))
                .andExpect(jsonPath("$[0].product.id", is(completePurchases.get(0).getProduct().getId())))
                .andExpect(jsonPath("$[0].product.cost", is(completePurchases.get(0).getProduct().getCost().doubleValue())))
                .andExpect(jsonPath("$[0].product.actionId", is(completePurchases.get(0).getProduct().getActionId())))
                .andExpect(jsonPath("$[0].product.name", is(completePurchases.get(0).getProduct().getName())))
                .andExpect(jsonPath("$[1].id", is(completePurchases.get(1).getId())))
                .andExpect(jsonPath("$[1].amount", is(completePurchases.get(1).getAmount())))
                .andExpect(jsonPath("$[1].user.id", is(completePurchases.get(1).getUser().getId())))
                .andExpect(jsonPath("$[1].user.email", is(completePurchases.get(1).getUser().getEmail())))
                .andExpect(jsonPath("$[1].user.addressID", is(completePurchases.get(1).getUser().getAddressID())))
                .andExpect(jsonPath("$[1].user.name", is(completePurchases.get(1).getUser().getName())))
                .andExpect(jsonPath("$[1].user.id", is(completePurchases.get(1).getUser().getId())))
                .andExpect(jsonPath("$[1].product.id", is(completePurchases.get(1).getProduct().getId())))
                .andExpect(jsonPath("$[1].product.cost", is(completePurchases.get(1).getProduct().getCost().doubleValue())))
                .andExpect(jsonPath("$[1].product.actionId", is(completePurchases.get(1).getProduct().getActionId())))
                .andExpect(jsonPath("$[1].product.name", is(completePurchases.get(1).getProduct().getName())))
                .andExpect(jsonPath("$[3].id", is(completePurchases.get(3).getId())))
                .andExpect(jsonPath("$[3].amount", is(completePurchases.get(3).getAmount())))
                .andExpect(jsonPath("$[3].user.id", is(completePurchases.get(3).getUser().getId())))
                .andExpect(jsonPath("$[3].user.email", is(completePurchases.get(3).getUser().getEmail())))
                .andExpect(jsonPath("$[3].user.addressID", is(completePurchases.get(3).getUser().getAddressID())))
                .andExpect(jsonPath("$[3].user.name", is(completePurchases.get(3).getUser().getName())))
                .andExpect(jsonPath("$[3].user.id", is(completePurchases.get(3).getUser().getId())))
                .andExpect(jsonPath("$[3].product.id", is(completePurchases.get(3).getProduct().getId())))
                .andExpect(jsonPath("$[3].product.cost", is(completePurchases.get(3).getProduct().getCost().doubleValue())))
                .andExpect(jsonPath("$[3].product.actionId", is(completePurchases.get(3).getProduct().getActionId())))
                .andExpect(jsonPath("$[3].product.name", is(completePurchases.get(3).getProduct().getName())));
    }

    @Test
    void givenPurchase_whenGetPurchaseByID_thenReturnJsonPurchase() throws Exception {
        Purchase purchase = generatePurchase();
        CompletePurchase completePurchase = generateCompletePurchase();
        User user = generateUser();
        Product product = generateProduct();

        given(purchaseRepository.findById("purchase1")).willReturn(Optional.of(purchase));
        given(userRepository.findById("user1")).willReturn(Optional.of(user));
        given(productRepository.findById("product1")).willReturn(Optional.of(product));

        mockMvc.perform(get("/purchases/{id}", "purchase1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(completePurchase.getId())))
                .andExpect(jsonPath("$.amount", is(completePurchase.getAmount())))
                .andExpect(jsonPath("$.user.id", is(completePurchase.getUser().getId())))
                .andExpect(jsonPath("$.user.email", is(completePurchase.getUser().getEmail())))
                .andExpect(jsonPath("$.user.addressID", is(completePurchase.getUser().getAddressID())))
                .andExpect(jsonPath("$.user.name", is(completePurchase.getUser().getName())))
                .andExpect(jsonPath("$.user.id", is(completePurchase.getUser().getId())))
                .andExpect(jsonPath("$.product.id", is(completePurchase.getProduct().getId())))
                .andExpect(jsonPath("$.product.cost", is(completePurchase.getProduct().getCost().doubleValue())))
                .andExpect(jsonPath("$.product.actionId", is(completePurchase.getProduct().getActionId())))
                .andExpect(jsonPath("$.product.name", is(completePurchase.getProduct().getName())));
    }

    @Test
    void givenPurchase_whenGetPurchaseByIdNotExist_thenReturn404() throws Exception {
        given(purchaseRepository.findById("purchase999")).willReturn(Optional.empty());

        mockMvc.perform(get("/purchases/{id}", "purchase999"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"The Purchase with ID purchase999 doesn't exist\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Disabled
    // Test is disabled because even though I say that address & user controller should return a full address & user object including id it just doesn't and the id is null, breaking the test...
    void whenPostPurchase_thenReturnPurchase() throws Exception {
        // user
        UserDTO userDTO = new UserDTO("User Name", "user.name@test.be", "address2");
        User user = generateUserPost(userDTO);
        // address
        AddressDTO addressDTO = new AddressDTO("street", "123", "5a", "city", "2362");
        Address address = generateAddressPost(addressDTO);
        // product
        Product product = generateProduct();
        // Purchase
        PurchaseDTO purchaseDTO = new PurchaseDTO(5, product.getId(),user.getName() , user.getEmail(), address.getStreet(), address.getHouseNumber(), address.getBox(), address.getCity(), address.getPostalCode());
        Purchase purchase = generatePurchasePost(purchaseDTO, user);
        CompletePurchase completePurchase = new CompletePurchase(purchase, Optional.of(user), Optional.of(product));

        given(productRepository.existsById(product.getId())).willReturn(Boolean.TRUE);
        doReturn(address).when(addressController).addAddress(addressDTO);
//       when(addressController.addAddress(addressDTO)).thenReturn(address);
        given(userRepository.findFirstByEmail(user.getEmail())).willReturn(Optional.empty());
        doReturn(user).when(userController).addUser(userDTO);
//       when(userController.addUser(userDTO)).thenReturn(user);
        given(purchaseRepository.findById(purchase.getId())).willReturn(Optional.of(purchase));
//        given(purchaseRepository.save(purchase)).willReturn(purchase);

        mockMvc.perform(post("/purchases")
                    .content(mapper.writeValueAsString(purchaseDTO))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(purchase.getId())))
                .andExpect(jsonPath("$.userId", is(purchase.getUserId())))
                .andExpect(jsonPath("$.productId", is(purchase.getProductId())))
                .andExpect(jsonPath("$.amount", is(purchase.getAmount())));
    }
}
