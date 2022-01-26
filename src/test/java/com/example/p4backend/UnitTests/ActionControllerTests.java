package com.example.p4backend.UnitTests;

import com.example.p4backend.models.*;
import com.example.p4backend.repositories.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ActionControllerTests {
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
    // ----- PRODUCT -----
    Product product1 = new Product("product1", new Decimal128(5), "action1");
    Product product2 = new Product("product2", new Decimal128(15), "action1");
    Product product3 = new Product("product3", new Decimal128(12), "action1");
    Product product4 = new Product("product4", new Decimal128(2), "action2");
    Product product5 = new Product("product5", new Decimal128(6), "action3");
    // ----- PURCHASE -----
    Purchase purchase1 = new Purchase("user1", "product1", 1);
    Purchase purchase2 = new Purchase("user2", "product2", 2);
    Purchase purchase3 = new Purchase("user3", "product4", 5);
    Purchase purchase4 = new Purchase("user4", "product5", 2);
    Purchase purchase5 = new Purchase("user1", "product3", 3);
    // ----- ACTION IMAGE -----
    ActionImage image1 = new ActionImage("https://http.cat/400.jpg", "action1");
    ActionImage image2 = new ActionImage("https://http.cat/401.jpg", "action1");
    ActionImage image3 = new ActionImage("https://http.cat/402.jpg", "action2");
    ActionImage image4 = new ActionImage("https://http.cat/403.jpg", "action2");
    ActionImage image5 = new ActionImage("https://http.cat/404.jpg", "action2");
    ActionImage image6 = new ActionImage("https://http.cat/405.jpg", "action3");
    ActionImage image7 = new ActionImage("https://http.cat/406.jpg", "action4");
    // ----- ACTION -----
    Action action1 = new Action(
            "action1",
            new Decimal128(500),
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "vzw1",
            new GregorianCalendar(2022, Calendar.FEBRUARY, 18).getTime());
    Action action2 = new Action(
            "action2",
            new Decimal128(200),
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "vzw2",
            new GregorianCalendar(2022, Calendar.MARCH, 18).getTime());
    Action action3 = new Action(
            "action3",
            new Decimal128(400),
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "vzw3",
            new GregorianCalendar(2022, Calendar.APRIL, 28).getTime());
    Action action4 = new Action(
            "action4",
            new Decimal128(450),
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "vzw4",
            new GregorianCalendar(2022, Calendar.FEBRUARY, 28).getTime());
    Action action5 = new Action(
            "action5",
            new Decimal128(500),
            "Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?",
            "vzw1",
            new GregorianCalendar(2022, Calendar.FEBRUARY, 1).getTime());
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AddressRepository addressRepository;
    @MockBean
    private VzwRepository vzwRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private PurchaseRepository purchaseRepository;
    @MockBean
    private ActionImageRepository actionImageRepository;
    @MockBean
    private ActionRepository actionRepository;

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

    private List<Product> generateProductsList() {
        product1.setId("product1");
        product2.setId("product2");
        product3.setId("product3");
        product4.setId("product4");
        product5.setId("product5");
        return List.of(product1, product2, product3, product4, product5);
    }

    private List<Purchase> generatePurchasesList() {
        return List.of(purchase1, purchase2, purchase3, purchase4, purchase5);
    }

    private List<ActionImage> generateActionImagesList() {
        return List.of(image1, image2, image3, image4, image5, image6, image7);
    }

    private List<Action> generateActionsList() {
        // Generate the current date
        GregorianCalendar currentCallender = new GregorianCalendar();

        // Action 1
        GregorianCalendar action1StartCallender = currentCallender;
        GregorianCalendar action1EndCallender = currentCallender;
        action1StartCallender.add(Calendar.DAY_OF_YEAR, -5);
        action1EndCallender.add(Calendar.YEAR, 1);
        action1.setId("action1");
        action1.setStartDate(action1StartCallender.getTime());
        action1.setEndDate(action1EndCallender.getTime());
        // Action 2
        GregorianCalendar action2StartCallender = currentCallender;
        GregorianCalendar action2EndCallender = currentCallender;
        action2StartCallender.add(Calendar.DAY_OF_YEAR, -4);
        action2EndCallender.add(Calendar.DAY_OF_YEAR, 61);
        action2.setId("action2");
        action2.setStartDate(action2StartCallender.getTime());
        action2.setEndDate(action2EndCallender.getTime());
        // Action 3
        GregorianCalendar action3StartCallender = currentCallender;
        GregorianCalendar action3EndCallender = currentCallender;
        action3StartCallender.add(Calendar.DAY_OF_YEAR, -3);
        action3EndCallender.add(Calendar.DAY_OF_YEAR, 30);
        action3.setId("action3");
        action3.setStartDate(action3StartCallender.getTime());
        action3.setEndDate(action3EndCallender.getTime());
        // Action 4
        GregorianCalendar action4StartCallender = currentCallender;
        GregorianCalendar action4EndCallender = currentCallender;
        action4StartCallender.add(Calendar.DAY_OF_YEAR, -2);
        action4EndCallender.add(Calendar.DAY_OF_YEAR, 10);
        action4.setId("action4");
        action4.setStartDate(action4StartCallender.getTime());
        action4.setEndDate(action4EndCallender.getTime());
        // Action 5
        GregorianCalendar action5StartCallender = currentCallender;
        GregorianCalendar action5EndCallender = currentCallender;
        action5StartCallender.add(Calendar.DAY_OF_YEAR, 5);
        action5EndCallender.add(Calendar.DAY_OF_YEAR, 155);
        action5.setId("action5");
        action5.setStartDate(action5StartCallender.getTime());
        action5.setEndDate(action5EndCallender.getTime());

        return List.of(action1, action2, action3, action4, action5);
    }



}
