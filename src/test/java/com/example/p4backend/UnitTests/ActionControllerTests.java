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
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        action4EndCallender.add(Calendar.DAY_OF_YEAR, 5);
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


    // Gives back a list of all Actions
    @Test
    void givenActions_whenGetAllActions_thenReturnJsonActions() throws Exception {
        List<Action> actionList = generateActionsList();
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        given(actionRepository.findAll()).willReturn(actionList);
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzwList.get(0)));
        given(vzwRepository.findById("vzw2")).willReturn(Optional.of(vzwList.get(1)));
        given(vzwRepository.findById("vzw3")).willReturn(Optional.of(vzwList.get(2)));
        given(vzwRepository.findById("vzw4")).willReturn(Optional.of(vzwList.get(3)));
        given(addressRepository.findById("7")).willReturn(Optional.of(addressList.get(0)));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(addressRepository.findById("10")).willReturn(Optional.of(addressList.get(3)));
        given(actionImageRepository.findActionImagesByActionId("action1")).willReturn(List.of(actionImageList.get(0), actionImageList.get(1)));
        given(actionImageRepository.findActionImagesByActionId("action2")).willReturn(List.of(actionImageList.get(2), actionImageList.get(3), actionImageList.get(4)));
        given(actionImageRepository.findActionImagesByActionId("action3")).willReturn(List.of(actionImageList.get(5)));
        given(actionImageRepository.findActionImagesByActionId("action4")).willReturn(List.of(actionImageList.get(6)));
        given(actionImageRepository.findActionImagesByActionId("action5")).willReturn(new ArrayList<>());

        mockMvc.perform(get("/actions"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(5)))
                // action1 is correct
                .andExpect(jsonPath("$[0].id", is("action1")))
                .andExpect(jsonPath("$[0].name", is("action1")))
                .andExpect(jsonPath("$[0].goal", is(500)))
                .andExpect(jsonPath("$[0].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[0].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[0].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[0].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[0].vzw.address.id", is("8")))
                .andExpect(jsonPath("$[0].vzw.address.street", is("Markt")))
                .andExpect(jsonPath("$[0].vzw.address.houseNumber", is("22")))
                .andExpect(jsonPath("$[0].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[0].vzw.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].vzw.address.postalCode", is("2460")))
                .andExpect(jsonPath("$[0].actionImages[0].fileLocation", is("https://http.cat/400.jpg")))
                .andExpect(jsonPath("$[0].actionImages[0].actionId", is("action1")))
                .andExpect(jsonPath("$[0].actionImages[1].fileLocation", is("https://http.cat/401.jpg")))
                .andExpect(jsonPath("$[0].actionImages[1].actionId", is("action1")))
                // action2 is correct
                .andExpect(jsonPath("$[1].id", is("action2")))
                .andExpect(jsonPath("$[1].name", is("action2")))
                .andExpect(jsonPath("$[1].goal", is(200)))
                .andExpect(jsonPath("$[1].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].startDate").exists())
                .andExpect(jsonPath("$[1].endDate").exists())
                .andExpect(jsonPath("$[1].vzw.id", is("vzw2")))
                .andExpect(jsonPath("$[1].vzw.name", is("vzw2")))
                .andExpect(jsonPath("$[1].vzw.email", is("vzw2.malle@mail.com")))
                .andExpect(jsonPath("$[1].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[1].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[1].vzw.profilePicture", is("https://http.cat/201.jpg")))
                .andExpect(jsonPath("$[1].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[1].vzw.address.id", is("7")))
                .andExpect(jsonPath("$[1].vzw.address.street", is("Kerstraat")))
                .andExpect(jsonPath("$[1].vzw.address.houseNumber", is("87")))
                .andExpect(jsonPath("$[1].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[1].vzw.address.city", is("Malle")))
                .andExpect(jsonPath("$[1].vzw.address.postalCode", is("2390")))
                .andExpect(jsonPath("$[1].actionImages[0].fileLocation", is("https://http.cat/402.jpg")))
                .andExpect(jsonPath("$[1].actionImages[0].actionId", is("action2")))
                .andExpect(jsonPath("$[1].actionImages[1].fileLocation", is("https://http.cat/403.jpg")))
                .andExpect(jsonPath("$[1].actionImages[1].actionId", is("action2")))
                .andExpect(jsonPath("$[1].actionImages[2].fileLocation", is("https://http.cat/404.jpg")))
                .andExpect(jsonPath("$[1].actionImages[2].actionId", is("action2")))
                // action3 is correct
                .andExpect(jsonPath("$[2].id", is("action3")))
                .andExpect(jsonPath("$[2].name", is("action3")))
                .andExpect(jsonPath("$[2].goal", is(400)))
                .andExpect(jsonPath("$[2].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[2].startDate").exists())
                .andExpect(jsonPath("$[2].endDate").exists())
                .andExpect(jsonPath("$[2].vzw.id", is("vzw3")))
                .andExpect(jsonPath("$[2].vzw.name", is("vzw3")))
                .andExpect(jsonPath("$[2].vzw.email", is("vzw3.herselt@mail.com")))
                .andExpect(jsonPath("$[2].vzw.rekeningNR", is("be1234599798")))
                .andExpect(jsonPath("$[2].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[2].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[2].vzw.profilePicture", is("https://http.cat/202.jpg")))
                .andExpect(jsonPath("$[2].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[2].vzw.address.id", is("10")))
                .andExpect(jsonPath("$[2].vzw.address.street", is("Stationsstraat")))
                .andExpect(jsonPath("$[2].vzw.address.houseNumber", is("27")))
                .andExpect(jsonPath("$[2].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[2].vzw.address.city", is("Herselt")))
                .andExpect(jsonPath("$[2].vzw.address.postalCode", is("2230")))
                .andExpect(jsonPath("$[2].actionImages[0].fileLocation", is("https://http.cat/405.jpg")))
                .andExpect(jsonPath("$[2].actionImages[0].actionId", is("action3")))
                // action4 is correct
                .andExpect(jsonPath("$[3].id", is("action4")))
                .andExpect(jsonPath("$[3].name", is("action4")))
                .andExpect(jsonPath("$[3].goal", is(450)))
                .andExpect(jsonPath("$[3].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[3].startDate").exists())
                .andExpect(jsonPath("$[3].endDate").exists())
                .andExpect(jsonPath("$[3].vzw.id", is("vzw4")))
                .andExpect(jsonPath("$[3].vzw.name", is("vzw4")))
                .andExpect(jsonPath("$[3].vzw.email", is("vzw4.malle@mail.com")))
                .andExpect(jsonPath("$[3].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[3].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[3].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[3].vzw.profilePicture", is("https://http.cat/203.jpg")))
                .andExpect(jsonPath("$[3].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[3].vzw.address.id", is("9")))
                .andExpect(jsonPath("$[3].vzw.address.street", is("Sparrelaan")))
                .andExpect(jsonPath("$[3].vzw.address.houseNumber", is("17")))
                .andExpect(jsonPath("$[3].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[3].vzw.address.city", is("Malle")))
                .andExpect(jsonPath("$[3].vzw.address.postalCode", is("2390")))
                .andExpect(jsonPath("$[3].actionImages[0].fileLocation", is("https://http.cat/406.jpg")))
                .andExpect(jsonPath("$[3].actionImages[0].actionId", is("action4")))
                // action5 is correct
                .andExpect(jsonPath("$[4].id", is("action5")))
                .andExpect(jsonPath("$[4].name", is("action5")))
                .andExpect(jsonPath("$[4].goal", is(500)))
                .andExpect(jsonPath("$[4].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[4].startDate").exists())
                .andExpect(jsonPath("$[4].endDate").exists())
                .andExpect(jsonPath("$[4].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[4].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[4].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[4].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[4].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[4].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[4].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[4].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[4].vzw.address.id", is("8")))
                .andExpect(jsonPath("$[4].vzw.address.street", is("Markt")))
                .andExpect(jsonPath("$[4].vzw.address.houseNumber", is("22")))
                .andExpect(jsonPath("$[4].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[4].vzw.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[4].vzw.address.postalCode", is("2460")))
                .andExpect(jsonPath("$[4].actionImages").isArray());
    }


    // Gives back a list of all Actions
    @Test
    void givenActions_whenGetAllActionsWithProgress_thenReturnJsonActions() throws Exception {
        List<Action> actionList = generateActionsList();
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        given(actionRepository.findAll()).willReturn(actionList);
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzwList.get(0)));
        given(vzwRepository.findById("vzw2")).willReturn(Optional.of(vzwList.get(1)));
        given(vzwRepository.findById("vzw3")).willReturn(Optional.of(vzwList.get(2)));
        given(vzwRepository.findById("vzw4")).willReturn(Optional.of(vzwList.get(3)));
        given(addressRepository.findById("7")).willReturn(Optional.of(addressList.get(0)));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(addressRepository.findById("10")).willReturn(Optional.of(addressList.get(3)));
        given(actionImageRepository.findActionImagesByActionId("action1")).willReturn(List.of(actionImageList.get(0), actionImageList.get(1)));
        given(actionImageRepository.findActionImagesByActionId("action2")).willReturn(List.of(actionImageList.get(2), actionImageList.get(3), actionImageList.get(4)));
        given(actionImageRepository.findActionImagesByActionId("action3")).willReturn(List.of(actionImageList.get(5)));
        given(actionImageRepository.findActionImagesByActionId("action4")).willReturn(List.of(actionImageList.get(6)));
        given(actionImageRepository.findActionImagesByActionId("action5")).willReturn(new ArrayList<>());
        List<Product> productList = generateProductsList();
        given(productRepository.findProductsByActionId("action1")).willReturn(List.of(productList.get(0), productList.get(1), productList.get(2)));
        given(productRepository.findProductsByActionId("action2")).willReturn(List.of(productList.get(3)));
        given(productRepository.findProductsByActionId("action3")).willReturn(List.of(productList.get(4)));
        given(productRepository.findProductsByActionId("action4")).willReturn(new ArrayList<>());
        given(productRepository.findProductsByActionId("action5")).willReturn(new ArrayList<>());
        List<Purchase> purchaseList = generatePurchasesList();
        given(purchaseRepository.findPurchasesByProductId("product1")).willReturn(List.of(purchaseList.get(0)));
        given(purchaseRepository.findPurchasesByProductId("product2")).willReturn(List.of(purchaseList.get(1)));
        given(purchaseRepository.findPurchasesByProductId("product3")).willReturn(List.of(purchaseList.get(4)));
        given(purchaseRepository.findPurchasesByProductId("product4")).willReturn(List.of(purchaseList.get(2)));
        given(purchaseRepository.findPurchasesByProductId("product5")).willReturn(List.of(purchaseList.get(3)));

        mockMvc.perform(get("/actions?progress=true"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(5)))
                // action1 is correct
                .andExpect(jsonPath("$[0].id", is("action1")))
                .andExpect(jsonPath("$[0].name", is("action1")))
                .andExpect(jsonPath("$[0].goal", is(500)))
                .andExpect(jsonPath("$[0].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[0].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[0].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[0].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[0].vzw.address.id", is("8")))
                .andExpect(jsonPath("$[0].vzw.address.street", is("Markt")))
                .andExpect(jsonPath("$[0].vzw.address.houseNumber", is("22")))
                .andExpect(jsonPath("$[0].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[0].vzw.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].vzw.address.postalCode", is("2460")))
                .andExpect(jsonPath("$[0].actionImages[0].fileLocation", is("https://http.cat/400.jpg")))
                .andExpect(jsonPath("$[0].actionImages[0].actionId", is("action1")))
                .andExpect(jsonPath("$[0].actionImages[1].fileLocation", is("https://http.cat/401.jpg")))
                .andExpect(jsonPath("$[0].actionImages[1].actionId", is("action1")))
                .andExpect(jsonPath("$[0].progress", is(14.2)))
                // action2 is correct
                .andExpect(jsonPath("$[1].id", is("action2")))
                .andExpect(jsonPath("$[1].name", is("action2")))
                .andExpect(jsonPath("$[1].goal", is(200)))
                .andExpect(jsonPath("$[1].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].startDate").exists())
                .andExpect(jsonPath("$[1].endDate").exists())
                .andExpect(jsonPath("$[1].vzw.id", is("vzw2")))
                .andExpect(jsonPath("$[1].vzw.name", is("vzw2")))
                .andExpect(jsonPath("$[1].vzw.email", is("vzw2.malle@mail.com")))
                .andExpect(jsonPath("$[1].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[1].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[1].vzw.profilePicture", is("https://http.cat/201.jpg")))
                .andExpect(jsonPath("$[1].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[1].vzw.address.id", is("7")))
                .andExpect(jsonPath("$[1].vzw.address.street", is("Kerstraat")))
                .andExpect(jsonPath("$[1].vzw.address.houseNumber", is("87")))
                .andExpect(jsonPath("$[1].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[1].vzw.address.city", is("Malle")))
                .andExpect(jsonPath("$[1].vzw.address.postalCode", is("2390")))
                .andExpect(jsonPath("$[1].actionImages[0].fileLocation", is("https://http.cat/402.jpg")))
                .andExpect(jsonPath("$[1].actionImages[0].actionId", is("action2")))
                .andExpect(jsonPath("$[1].actionImages[1].fileLocation", is("https://http.cat/403.jpg")))
                .andExpect(jsonPath("$[1].actionImages[1].actionId", is("action2")))
                .andExpect(jsonPath("$[1].actionImages[2].fileLocation", is("https://http.cat/404.jpg")))
                .andExpect(jsonPath("$[1].actionImages[2].actionId", is("action2")))
                .andExpect(jsonPath("$[1].progress", is(5.0)))
                // action3 is correct
                .andExpect(jsonPath("$[2].id", is("action3")))
                .andExpect(jsonPath("$[2].name", is("action3")))
                .andExpect(jsonPath("$[2].goal", is(400)))
                .andExpect(jsonPath("$[2].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[2].startDate").exists())
                .andExpect(jsonPath("$[2].endDate").exists())
                .andExpect(jsonPath("$[2].vzw.id", is("vzw3")))
                .andExpect(jsonPath("$[2].vzw.name", is("vzw3")))
                .andExpect(jsonPath("$[2].vzw.email", is("vzw3.herselt@mail.com")))
                .andExpect(jsonPath("$[2].vzw.rekeningNR", is("be1234599798")))
                .andExpect(jsonPath("$[2].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[2].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[2].vzw.profilePicture", is("https://http.cat/202.jpg")))
                .andExpect(jsonPath("$[2].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[2].vzw.address.id", is("10")))
                .andExpect(jsonPath("$[2].vzw.address.street", is("Stationsstraat")))
                .andExpect(jsonPath("$[2].vzw.address.houseNumber", is("27")))
                .andExpect(jsonPath("$[2].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[2].vzw.address.city", is("Herselt")))
                .andExpect(jsonPath("$[2].vzw.address.postalCode", is("2230")))
                .andExpect(jsonPath("$[2].actionImages[0].fileLocation", is("https://http.cat/405.jpg")))
                .andExpect(jsonPath("$[2].actionImages[0].actionId", is("action3")))
                .andExpect(jsonPath("$[2].progress", is(3.0)))
                // action4 is correct
                .andExpect(jsonPath("$[3].id", is("action4")))
                .andExpect(jsonPath("$[3].name", is("action4")))
                .andExpect(jsonPath("$[3].goal", is(450)))
                .andExpect(jsonPath("$[3].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[3].startDate").exists())
                .andExpect(jsonPath("$[3].endDate").exists())
                .andExpect(jsonPath("$[3].vzw.id", is("vzw4")))
                .andExpect(jsonPath("$[3].vzw.name", is("vzw4")))
                .andExpect(jsonPath("$[3].vzw.email", is("vzw4.malle@mail.com")))
                .andExpect(jsonPath("$[3].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[3].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[3].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[3].vzw.profilePicture", is("https://http.cat/203.jpg")))
                .andExpect(jsonPath("$[3].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[3].vzw.address.id", is("9")))
                .andExpect(jsonPath("$[3].vzw.address.street", is("Sparrelaan")))
                .andExpect(jsonPath("$[3].vzw.address.houseNumber", is("17")))
                .andExpect(jsonPath("$[3].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[3].vzw.address.city", is("Malle")))
                .andExpect(jsonPath("$[3].vzw.address.postalCode", is("2390")))
                .andExpect(jsonPath("$[3].actionImages[0].fileLocation", is("https://http.cat/406.jpg")))
                .andExpect(jsonPath("$[3].actionImages[0].actionId", is("action4")))
                .andExpect(jsonPath("$[3].progress", is(0.0)))
                // action5 is correct
                .andExpect(jsonPath("$[4].id", is("action5")))
                .andExpect(jsonPath("$[4].name", is("action5")))
                .andExpect(jsonPath("$[4].goal", is(500)))
                .andExpect(jsonPath("$[4].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[4].startDate").exists())
                .andExpect(jsonPath("$[4].endDate").exists())
                .andExpect(jsonPath("$[4].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[4].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[4].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[4].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[4].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[4].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[4].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[4].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[4].vzw.address.id", is("8")))
                .andExpect(jsonPath("$[4].vzw.address.street", is("Markt")))
                .andExpect(jsonPath("$[4].vzw.address.houseNumber", is("22")))
                .andExpect(jsonPath("$[4].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[4].vzw.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[4].vzw.address.postalCode", is("2460")))
                .andExpect(jsonPath("$[4].actionImages").isArray())
                .andExpect(jsonPath("$[4].progress", is(0.0)));
    }


/*    // Gives back a list of all Actions that are close to their deadline
    @Test
    void givenActions_whenGetDeadlineActions_thenReturnJsonActions() throws Exception {
        // Generate the current date
        GregorianCalendar currentCallender = new GregorianCalendar();
        Date currentDate = currentCallender.getTime();

        // Generate the date a month in the future from the current date
        currentCallender.add(Calendar.WEEK_OF_YEAR, 2);
        Date futureDate = currentCallender.getTime();

        List<Action> actionList = generateActionsList();
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        Mockito.when(currentCallender).thenReturn(currentCallender);
        Mockito.when(futureDate).thenReturn(futureDate);
        given(actionRepository.findActionsByEndDateBetweenAndStartDateBeforeOrderByEndDateDesc(currentDate, futureDate, currentDate)).willReturn(List.of(actionList.get(3)));
        given(vzwRepository.findById("vzw4")).willReturn(Optional.of(vzwList.get(3)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(actionImageRepository.findActionImagesByActionId("action4")).willReturn(List.of(actionImageList.get(6)));

        mockMvc.perform(get("/actions/deadline"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(1)))
                // action4 is correct
                .andExpect(jsonPath("$[0].id", is("action4")))
                .andExpect(jsonPath("$[0].name", is("action4")))
                .andExpect(jsonPath("$[0].goal", is(450)))
                .andExpect(jsonPath("$[0].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].vzw.id", is("vzw4")))
                .andExpect(jsonPath("$[0].vzw.name", is("vzw4")))
                .andExpect(jsonPath("$[0].vzw.email", is("vzw4.malle@mail.com")))
                .andExpect(jsonPath("$[0].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[0].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].vzw.profilePicture", is("https://http.cat/203.jpg")))
                .andExpect(jsonPath("$[0].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[0].vzw.address.id", is("9")))
                .andExpect(jsonPath("$[0].vzw.address.street", is("Sparrelaan")))
                .andExpect(jsonPath("$[0].vzw.address.houseNumber", is("17")))
                .andExpect(jsonPath("$[0].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[0].vzw.address.city", is("Malle")))
                .andExpect(jsonPath("$[0].vzw.address.postalCode", is("2390")))
                .andExpect(jsonPath("$[0].actionImages[0].fileLocation", is("https://http.cat/406.jpg")))
                .andExpect(jsonPath("$[0].actionImages[0].actionId", is("action4")));
    }*/


    // Gives back an action with the id 'action4'
    @Test
    void givenActions_whenGetActionById_thenReturnJsonAction4() throws Exception {
        List<Action> actionList = generateActionsList();
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        given(actionRepository.findById("action4")).willReturn(Optional.of(actionList.get(3)));
        given(vzwRepository.findById("vzw4")).willReturn(Optional.of(vzwList.get(3)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(actionImageRepository.findActionImagesByActionId("action4")).willReturn(List.of(actionImageList.get(6)));

        mockMvc.perform(get("/actions/{id}", "action4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // action4 is correct
                .andExpect(jsonPath("$.id", is("action4")))
                .andExpect(jsonPath("$.name", is("action4")))
                .andExpect(jsonPath("$.goal", is(450)))
                .andExpect(jsonPath("$.description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.vzw.id", is("vzw4")))
                .andExpect(jsonPath("$.vzw.name", is("vzw4")))
                .andExpect(jsonPath("$.vzw.email", is("vzw4.malle@mail.com")))
                .andExpect(jsonPath("$.vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$.vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$.vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$.vzw.profilePicture", is("https://http.cat/203.jpg")))
                .andExpect(jsonPath("$.vzw.password").doesNotExist())
                .andExpect(jsonPath("$.vzw.address.id", is("9")))
                .andExpect(jsonPath("$.vzw.address.street", is("Sparrelaan")))
                .andExpect(jsonPath("$.vzw.address.houseNumber", is("17")))
                .andExpect(jsonPath("$.vzw.address.box").isEmpty())
                .andExpect(jsonPath("$.vzw.address.city", is("Malle")))
                .andExpect(jsonPath("$.vzw.address.postalCode", is("2390")))
                .andExpect(jsonPath("$.actionImages[0].fileLocation", is("https://http.cat/406.jpg")))
                .andExpect(jsonPath("$.actionImages[0].actionId", is("action4")));
    }


    // Gives back an action with the id 'action4' and progress=true
    @Test
    void givenActions_whenGetActionByIdWithProgress_thenReturnJsonAction4() throws Exception {
        List<Action> actionList = generateActionsList();
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        given(actionRepository.findById("action4")).willReturn(Optional.of(actionList.get(3)));
        given(vzwRepository.findById("vzw4")).willReturn(Optional.of(vzwList.get(3)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(actionImageRepository.findActionImagesByActionId("action4")).willReturn(List.of(actionImageList.get(6)));
        given(productRepository.findProductsByActionId("action4")).willReturn(new ArrayList<>());

        mockMvc.perform(get("/actions/{id}?progress=true", "action4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // action4 is correct
                .andExpect(jsonPath("$.id", is("action4")))
                .andExpect(jsonPath("$.name", is("action4")))
                .andExpect(jsonPath("$.goal", is(450)))
                .andExpect(jsonPath("$.description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.vzw.id", is("vzw4")))
                .andExpect(jsonPath("$.vzw.name", is("vzw4")))
                .andExpect(jsonPath("$.vzw.email", is("vzw4.malle@mail.com")))
                .andExpect(jsonPath("$.vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$.vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$.vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$.vzw.profilePicture", is("https://http.cat/203.jpg")))
                .andExpect(jsonPath("$.vzw.password").doesNotExist())
                .andExpect(jsonPath("$.vzw.address.id", is("9")))
                .andExpect(jsonPath("$.vzw.address.street", is("Sparrelaan")))
                .andExpect(jsonPath("$.vzw.address.houseNumber", is("17")))
                .andExpect(jsonPath("$.vzw.address.box").isEmpty())
                .andExpect(jsonPath("$.vzw.address.city", is("Malle")))
                .andExpect(jsonPath("$.vzw.address.postalCode", is("2390")))
                .andExpect(jsonPath("$.actionImages[0].fileLocation", is("https://http.cat/406.jpg")))
                .andExpect(jsonPath("$.actionImages[0].actionId", is("action4")))
                .andExpect(jsonPath("$.progress", is(0.0)));
    }

    // Gives one 404 back, searched on actionId (action999)
    @Test
    void givenAction_whenGetActionByIdNotExist_thenReturn404() throws Exception {
        given(actionRepository.findById("action999")).willReturn(Optional.empty());

        mockMvc.perform(get("/actions/{id}", "action999"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"The Action with ID action999 doesn't exist\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    // Gives back a list of all Actions in a random order
    @Test
    void givenActions_whenGetRandomActions_thenReturnJsonActions() throws Exception {
        ArrayList<Action> actionList = new ArrayList<>(generateActionsList());
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        given(actionRepository.findAll()).willReturn(actionList);
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzwList.get(0)));
        given(vzwRepository.findById("vzw2")).willReturn(Optional.of(vzwList.get(1)));
        given(vzwRepository.findById("vzw3")).willReturn(Optional.of(vzwList.get(2)));
        given(vzwRepository.findById("vzw4")).willReturn(Optional.of(vzwList.get(3)));
        given(addressRepository.findById("7")).willReturn(Optional.of(addressList.get(0)));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(addressRepository.findById("10")).willReturn(Optional.of(addressList.get(3)));
        given(actionImageRepository.findActionImagesByActionId("action1")).willReturn(List.of(actionImageList.get(0), actionImageList.get(1)));
        given(actionImageRepository.findActionImagesByActionId("action2")).willReturn(List.of(actionImageList.get(2), actionImageList.get(3), actionImageList.get(4)));
        given(actionImageRepository.findActionImagesByActionId("action3")).willReturn(List.of(actionImageList.get(5)));
        given(actionImageRepository.findActionImagesByActionId("action4")).willReturn(List.of(actionImageList.get(6)));
        given(actionImageRepository.findActionImagesByActionId("action5")).willReturn(new ArrayList<>());

        mockMvc.perform(get("/actions/random"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(5)))
                // action1 is correct
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].goal").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].vzw.id").exists())
                .andExpect(jsonPath("$[0].vzw.name").exists())
                .andExpect(jsonPath("$[0].vzw.email").exists())
                .andExpect(jsonPath("$[0].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[0].vzw.bio").exists())
                .andExpect(jsonPath("$[0].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[0].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[0].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[0].vzw.address.id").exists())
                .andExpect(jsonPath("$[0].vzw.address.street").exists())
                .andExpect(jsonPath("$[0].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[0].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[0].vzw.address.city").exists())
                .andExpect(jsonPath("$[0].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[0].actionImages").isArray())
                // action2 is correct
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[1].goal").exists())
                .andExpect(jsonPath("$[1].description").exists())
                .andExpect(jsonPath("$[1].startDate").exists())
                .andExpect(jsonPath("$[1].endDate").exists())
                .andExpect(jsonPath("$[1].vzw.id").exists())
                .andExpect(jsonPath("$[1].vzw.name").exists())
                .andExpect(jsonPath("$[1].vzw.email").exists())
                .andExpect(jsonPath("$[1].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[1].vzw.bio").exists())
                .andExpect(jsonPath("$[1].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[1].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[1].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[1].vzw.address.id").exists())
                .andExpect(jsonPath("$[1].vzw.address.street").exists())
                .andExpect(jsonPath("$[1].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[1].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[1].vzw.address.city").exists())
                .andExpect(jsonPath("$[1].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[1].actionImages").isArray())
                // action3 is correct
                .andExpect(jsonPath("$[2].id").exists())
                .andExpect(jsonPath("$[2].name").exists())
                .andExpect(jsonPath("$[2].goal").exists())
                .andExpect(jsonPath("$[2].description").exists())
                .andExpect(jsonPath("$[2].startDate").exists())
                .andExpect(jsonPath("$[2].endDate").exists())
                .andExpect(jsonPath("$[2].vzw.id").exists())
                .andExpect(jsonPath("$[2].vzw.name").exists())
                .andExpect(jsonPath("$[2].vzw.email").exists())
                .andExpect(jsonPath("$[2].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[2].vzw.bio").exists())
                .andExpect(jsonPath("$[2].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[2].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[2].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[2].vzw.address.id").exists())
                .andExpect(jsonPath("$[2].vzw.address.street").exists())
                .andExpect(jsonPath("$[2].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[2].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[2].vzw.address.city").exists())
                .andExpect(jsonPath("$[2].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[2].actionImages").isArray())
                // action4 is correct
                .andExpect(jsonPath("$[3].id").exists())
                .andExpect(jsonPath("$[3].name").exists())
                .andExpect(jsonPath("$[3].goal").exists())
                .andExpect(jsonPath("$[3].description").exists())
                .andExpect(jsonPath("$[3].startDate").exists())
                .andExpect(jsonPath("$[3].endDate").exists())
                .andExpect(jsonPath("$[3].vzw.id").exists())
                .andExpect(jsonPath("$[3].vzw.name").exists())
                .andExpect(jsonPath("$[3].vzw.email").exists())
                .andExpect(jsonPath("$[3].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[3].vzw.bio").exists())
                .andExpect(jsonPath("$[3].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[3].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[3].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[3].vzw.address.id").exists())
                .andExpect(jsonPath("$[3].vzw.address.street").exists())
                .andExpect(jsonPath("$[3].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[3].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[3].vzw.address.city").exists())
                .andExpect(jsonPath("$[3].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[3].actionImages").isArray())
                // action5 is correct
                .andExpect(jsonPath("$[4].id").exists())
                .andExpect(jsonPath("$[4].name").exists())
                .andExpect(jsonPath("$[4].goal").exists())
                .andExpect(jsonPath("$[4].description").exists())
                .andExpect(jsonPath("$[4].startDate").exists())
                .andExpect(jsonPath("$[4].endDate").exists())
                .andExpect(jsonPath("$[4].vzw.id").exists())
                .andExpect(jsonPath("$[4].vzw.name").exists())
                .andExpect(jsonPath("$[4].vzw.email").exists())
                .andExpect(jsonPath("$[4].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[4].vzw.bio").exists())
                .andExpect(jsonPath("$[4].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[4].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[4].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[4].vzw.address.id").exists())
                .andExpect(jsonPath("$[4].vzw.address.street").exists())
                .andExpect(jsonPath("$[4].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[4].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[4].vzw.address.city").exists())
                .andExpect(jsonPath("$[4].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[4].actionImages").isArray());
    }


    // Gives back a list of all Actions in a random order and progress=true
    @Test
    void givenActions_whenGetRandomActionsWithProgress_thenReturnJsonActions() throws Exception {
        ArrayList<Action> actionList = new ArrayList<>(generateActionsList());
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        given(actionRepository.findAll()).willReturn(actionList);
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzwList.get(0)));
        given(vzwRepository.findById("vzw2")).willReturn(Optional.of(vzwList.get(1)));
        given(vzwRepository.findById("vzw3")).willReturn(Optional.of(vzwList.get(2)));
        given(vzwRepository.findById("vzw4")).willReturn(Optional.of(vzwList.get(3)));
        given(addressRepository.findById("7")).willReturn(Optional.of(addressList.get(0)));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));
        given(addressRepository.findById("9")).willReturn(Optional.of(addressList.get(2)));
        given(addressRepository.findById("10")).willReturn(Optional.of(addressList.get(3)));
        given(actionImageRepository.findActionImagesByActionId("action1")).willReturn(List.of(actionImageList.get(0), actionImageList.get(1)));
        given(actionImageRepository.findActionImagesByActionId("action2")).willReturn(List.of(actionImageList.get(2), actionImageList.get(3), actionImageList.get(4)));
        given(actionImageRepository.findActionImagesByActionId("action3")).willReturn(List.of(actionImageList.get(5)));
        given(actionImageRepository.findActionImagesByActionId("action4")).willReturn(List.of(actionImageList.get(6)));
        given(actionImageRepository.findActionImagesByActionId("action5")).willReturn(new ArrayList<>());
        List<Product> productList = generateProductsList();
        given(productRepository.findProductsByActionId("action1")).willReturn(List.of(productList.get(0), productList.get(1), productList.get(2)));
        given(productRepository.findProductsByActionId("action2")).willReturn(List.of(productList.get(3)));
        given(productRepository.findProductsByActionId("action3")).willReturn(List.of(productList.get(4)));
        given(productRepository.findProductsByActionId("action4")).willReturn(new ArrayList<>());
        given(productRepository.findProductsByActionId("action5")).willReturn(new ArrayList<>());
        List<Purchase> purchaseList = generatePurchasesList();
        given(purchaseRepository.findPurchasesByProductId("product1")).willReturn(List.of(purchaseList.get(0)));
        given(purchaseRepository.findPurchasesByProductId("product2")).willReturn(List.of(purchaseList.get(1)));
        given(purchaseRepository.findPurchasesByProductId("product3")).willReturn(List.of(purchaseList.get(4)));
        given(purchaseRepository.findPurchasesByProductId("product4")).willReturn(List.of(purchaseList.get(2)));
        given(purchaseRepository.findPurchasesByProductId("product5")).willReturn(List.of(purchaseList.get(3)));

        mockMvc.perform(get("/actions/random?progress=true"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(5)))
                // action1 is correct
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].goal").exists())
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].vzw.id").exists())
                .andExpect(jsonPath("$[0].vzw.name").exists())
                .andExpect(jsonPath("$[0].vzw.email").exists())
                .andExpect(jsonPath("$[0].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[0].vzw.bio").exists())
                .andExpect(jsonPath("$[0].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[0].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[0].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[0].vzw.address.id").exists())
                .andExpect(jsonPath("$[0].vzw.address.street").exists())
                .andExpect(jsonPath("$[0].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[0].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[0].vzw.address.city").exists())
                .andExpect(jsonPath("$[0].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[0].actionImages").isArray())
                .andExpect(jsonPath("$[0].progress").exists())
                // action2 is correct
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[1].goal").exists())
                .andExpect(jsonPath("$[1].description").exists())
                .andExpect(jsonPath("$[1].startDate").exists())
                .andExpect(jsonPath("$[1].endDate").exists())
                .andExpect(jsonPath("$[1].vzw.id").exists())
                .andExpect(jsonPath("$[1].vzw.name").exists())
                .andExpect(jsonPath("$[1].vzw.email").exists())
                .andExpect(jsonPath("$[1].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[1].vzw.bio").exists())
                .andExpect(jsonPath("$[1].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[1].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[1].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[1].vzw.address.id").exists())
                .andExpect(jsonPath("$[1].vzw.address.street").exists())
                .andExpect(jsonPath("$[1].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[1].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[1].vzw.address.city").exists())
                .andExpect(jsonPath("$[1].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[1].actionImages").isArray())
                .andExpect(jsonPath("$[1].progress").exists())
                // action3 is correct
                .andExpect(jsonPath("$[2].id").exists())
                .andExpect(jsonPath("$[2].name").exists())
                .andExpect(jsonPath("$[2].goal").exists())
                .andExpect(jsonPath("$[2].description").exists())
                .andExpect(jsonPath("$[2].startDate").exists())
                .andExpect(jsonPath("$[2].endDate").exists())
                .andExpect(jsonPath("$[2].vzw.id").exists())
                .andExpect(jsonPath("$[2].vzw.name").exists())
                .andExpect(jsonPath("$[2].vzw.email").exists())
                .andExpect(jsonPath("$[2].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[2].vzw.bio").exists())
                .andExpect(jsonPath("$[2].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[2].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[2].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[2].vzw.address.id").exists())
                .andExpect(jsonPath("$[2].vzw.address.street").exists())
                .andExpect(jsonPath("$[2].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[2].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[2].vzw.address.city").exists())
                .andExpect(jsonPath("$[2].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[2].actionImages").isArray())
                .andExpect(jsonPath("$[2].progress").exists())
                // action4 is correct
                .andExpect(jsonPath("$[3].id").exists())
                .andExpect(jsonPath("$[3].name").exists())
                .andExpect(jsonPath("$[3].goal").exists())
                .andExpect(jsonPath("$[3].description").exists())
                .andExpect(jsonPath("$[3].startDate").exists())
                .andExpect(jsonPath("$[3].endDate").exists())
                .andExpect(jsonPath("$[3].vzw.id").exists())
                .andExpect(jsonPath("$[3].vzw.name").exists())
                .andExpect(jsonPath("$[3].vzw.email").exists())
                .andExpect(jsonPath("$[3].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[3].vzw.bio").exists())
                .andExpect(jsonPath("$[3].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[3].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[3].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[3].vzw.address.id").exists())
                .andExpect(jsonPath("$[3].vzw.address.street").exists())
                .andExpect(jsonPath("$[3].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[3].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[3].vzw.address.city").exists())
                .andExpect(jsonPath("$[3].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[3].actionImages").isArray())
                .andExpect(jsonPath("$[3].progress").exists())
                // action5 is correct
                .andExpect(jsonPath("$[4].id").exists())
                .andExpect(jsonPath("$[4].name").exists())
                .andExpect(jsonPath("$[4].goal").exists())
                .andExpect(jsonPath("$[4].description").exists())
                .andExpect(jsonPath("$[4].startDate").exists())
                .andExpect(jsonPath("$[4].endDate").exists())
                .andExpect(jsonPath("$[4].vzw.id").exists())
                .andExpect(jsonPath("$[4].vzw.name").exists())
                .andExpect(jsonPath("$[4].vzw.email").exists())
                .andExpect(jsonPath("$[4].vzw.rekeningNR").exists())
                .andExpect(jsonPath("$[4].vzw.bio").exists())
                .andExpect(jsonPath("$[4].vzw.youtubeLink").exists())
                .andExpect(jsonPath("$[4].vzw.profilePicture").exists())
                .andExpect(jsonPath("$[4].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[4].vzw.address.id").exists())
                .andExpect(jsonPath("$[4].vzw.address.street").exists())
                .andExpect(jsonPath("$[4].vzw.address.houseNumber").exists())
                .andExpect(jsonPath("$[4].vzw.address.box").hasJsonPath())
                .andExpect(jsonPath("$[4].vzw.address.city").exists())
                .andExpect(jsonPath("$[4].vzw.address.postalCode").exists())
                .andExpect(jsonPath("$[4].actionImages").isArray())
                .andExpect(jsonPath("$[4].progress").exists());
    }


    // Gives back a list of all Actions with vzwId "vzw1"
    @Test
    void givenActions_whenGetActionsByVzwId1_thenReturnJsonActions() throws Exception {
        List<Action> actionList = generateActionsList();
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        given(actionRepository.findActionsByVzwID("vzw1")).willReturn(List.of(actionList.get(0), actionList.get(4)));
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzwList.get(0)));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));
        given(actionImageRepository.findActionImagesByActionId("action1")).willReturn(List.of(actionImageList.get(0), actionImageList.get(1)));
        given(actionImageRepository.findActionImagesByActionId("action5")).willReturn(new ArrayList<>());

        mockMvc.perform(get("/actions/vzw/{vzwId}", "vzw1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(2)))
                // action1 is correct
                .andExpect(jsonPath("$[0].id", is("action1")))
                .andExpect(jsonPath("$[0].name", is("action1")))
                .andExpect(jsonPath("$[0].goal", is(500)))
                .andExpect(jsonPath("$[0].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[0].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[0].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[0].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[0].vzw.address.id", is("8")))
                .andExpect(jsonPath("$[0].vzw.address.street", is("Markt")))
                .andExpect(jsonPath("$[0].vzw.address.houseNumber", is("22")))
                .andExpect(jsonPath("$[0].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[0].vzw.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].vzw.address.postalCode", is("2460")))
                .andExpect(jsonPath("$[0].actionImages[0].fileLocation", is("https://http.cat/400.jpg")))
                .andExpect(jsonPath("$[0].actionImages[0].actionId", is("action1")))
                .andExpect(jsonPath("$[0].actionImages[1].fileLocation", is("https://http.cat/401.jpg")))
                .andExpect(jsonPath("$[0].actionImages[1].actionId", is("action1")))
                // action5 is correct
                .andExpect(jsonPath("$[1].id", is("action5")))
                .andExpect(jsonPath("$[1].name", is("action5")))
                .andExpect(jsonPath("$[1].goal", is(500)))
                .andExpect(jsonPath("$[1].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].startDate").exists())
                .andExpect(jsonPath("$[1].endDate").exists())
                .andExpect(jsonPath("$[1].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[1].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[1].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[1].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[1].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[1].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[1].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[1].vzw.address.id", is("8")))
                .andExpect(jsonPath("$[1].vzw.address.street", is("Markt")))
                .andExpect(jsonPath("$[1].vzw.address.houseNumber", is("22")))
                .andExpect(jsonPath("$[1].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[1].vzw.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[1].vzw.address.postalCode", is("2460")))
                .andExpect(jsonPath("$[1].actionImages").isArray());
    }


    // Gives back a list of all Actions with vzwId "vzw1" and progress=true
    @Test
    void givenActions_whenGetActionsByVzwId1WithProgress_thenReturnJsonActions() throws Exception {
        List<Action> actionList = generateActionsList();
        List<Vzw> vzwList = generateVzwsList();
        List<Address> addressList = generateAddressList();
        List<ActionImage> actionImageList = generateActionImagesList();
        List<Product> productList = generateProductsList();
        List<Purchase> purchaseList = generatePurchasesList();
        given(actionRepository.findActionsByVzwID("vzw1")).willReturn(List.of(actionList.get(0), actionList.get(4)));
        given(vzwRepository.findById("vzw1")).willReturn(Optional.of(vzwList.get(0)));
        given(addressRepository.findById("8")).willReturn(Optional.of(addressList.get(1)));
        given(actionImageRepository.findActionImagesByActionId("action1")).willReturn(List.of(actionImageList.get(0), actionImageList.get(1)));
        given(actionImageRepository.findActionImagesByActionId("action5")).willReturn(new ArrayList<>());
        given(productRepository.findProductsByActionId("action1")).willReturn(List.of(productList.get(0), productList.get(1), productList.get(2)));
        given(productRepository.findProductsByActionId("action5")).willReturn(new ArrayList<>());
        given(purchaseRepository.findPurchasesByProductId("product1")).willReturn(List.of(purchaseList.get(0)));
        given(purchaseRepository.findPurchasesByProductId("product2")).willReturn(List.of(purchaseList.get(1)));
        given(purchaseRepository.findPurchasesByProductId("product3")).willReturn(List.of(purchaseList.get(4)));

        mockMvc.perform(get("/actions/vzw/{vzwId}?progress=true", "vzw1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Array length is correct
                .andExpect(jsonPath("$", hasSize(2)))
                // action1 is correct
                .andExpect(jsonPath("$[0].id", is("action1")))
                .andExpect(jsonPath("$[0].name", is("action1")))
                .andExpect(jsonPath("$[0].goal", is(500)))
                .andExpect(jsonPath("$[0].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].startDate").exists())
                .andExpect(jsonPath("$[0].endDate").exists())
                .andExpect(jsonPath("$[0].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[0].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[0].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[0].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[0].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[0].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[0].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[0].vzw.address.id", is("8")))
                .andExpect(jsonPath("$[0].vzw.address.street", is("Markt")))
                .andExpect(jsonPath("$[0].vzw.address.houseNumber", is("22")))
                .andExpect(jsonPath("$[0].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[0].vzw.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[0].vzw.address.postalCode", is("2460")))
                .andExpect(jsonPath("$[0].actionImages[0].fileLocation", is("https://http.cat/400.jpg")))
                .andExpect(jsonPath("$[0].actionImages[0].actionId", is("action1")))
                .andExpect(jsonPath("$[0].actionImages[1].fileLocation", is("https://http.cat/401.jpg")))
                .andExpect(jsonPath("$[0].actionImages[1].actionId", is("action1")))
                .andExpect(jsonPath("$[0].progress", is(14.2)))
                // action5 is correct
                .andExpect(jsonPath("$[1].id", is("action5")))
                .andExpect(jsonPath("$[1].name", is("action5")))
                .andExpect(jsonPath("$[1].goal", is(500)))
                .andExpect(jsonPath("$[1].description", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].startDate").exists())
                .andExpect(jsonPath("$[1].endDate").exists())
                .andExpect(jsonPath("$[1].vzw.id", is("vzw1")))
                .andExpect(jsonPath("$[1].vzw.name", is("vzw1")))
                .andExpect(jsonPath("$[1].vzw.email", is("vzw1.kasterlee@mail.com")))
                .andExpect(jsonPath("$[1].vzw.rekeningNR", is("be1234566798")))
                .andExpect(jsonPath("$[1].vzw.bio", is("Lorem ipsum dolor sit amet consectetur adipisicing elit. Tenetur voluptas sequi voluptatum pariatur! Quae cumque quidem dolor maxime enim debitis omnis nemo facilis sequi autem? Quae tenetur, repellat vero deleniti vitae dolores? Cum tempore, mollitia provident placeat fugit earum, sint, quae iusto optio ea officiis consectetur sit necessitatibus itaque explicabo?")))
                .andExpect(jsonPath("$[1].vzw.youtubeLink", is("https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley")))
                .andExpect(jsonPath("$[1].vzw.profilePicture", is("https://http.cat/200.jpg")))
                .andExpect(jsonPath("$[1].vzw.password").doesNotExist())
                .andExpect(jsonPath("$[1].vzw.address.id", is("8")))
                .andExpect(jsonPath("$[1].vzw.address.street", is("Markt")))
                .andExpect(jsonPath("$[1].vzw.address.houseNumber", is("22")))
                .andExpect(jsonPath("$[1].vzw.address.box").isEmpty())
                .andExpect(jsonPath("$[1].vzw.address.city", is("Kasterlee")))
                .andExpect(jsonPath("$[1].vzw.address.postalCode", is("2460")))
                .andExpect(jsonPath("$[1].actionImages").isArray())
                .andExpect(jsonPath("$[1].progress", is(0.0)));
    }


    @Test
    void testGetNewestActions() {


    }
}
