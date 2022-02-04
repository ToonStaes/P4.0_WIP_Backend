package com.example.p4backend.UnitTests;

import com.example.p4backend.controllers.ProductController;
import com.example.p4backend.models.Action;
import com.example.p4backend.models.DTOs.ProductDTO;
import com.example.p4backend.models.Product;
import com.example.p4backend.models.complete.CompleteProduct;
import com.example.p4backend.repositories.ActionRepository;
import com.example.p4backend.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.bson.types.Decimal128;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {
    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ActionRepository actionRepository;
    @Mock
    private ProductController productController;

    private Product generateProduct() {
        Product product = new Product("product1", new Decimal128(new BigDecimal("25.99")), "action1", "https://http.cat/400.jpg");
        product.setId("product1");
        return product;
    }

    private CompleteProduct generateCompleteProduct() {
        Product product = new Product("product1", new Decimal128(new BigDecimal("25.99")), "action1", "https://http.cat/400.jpg");
        product.setId("product1");
        Action action = new Action("action1", new Decimal128(new BigDecimal("200")), "new action", "vzw1", new Date());
        action.setId("action1");
        action.setEndDate(new GregorianCalendar(2022, Calendar.JANUARY, 18).getTime());
        action.setEndDate(new GregorianCalendar(2022, Calendar.MARCH, 18).getTime());
        return new CompleteProduct(product, Optional.of(action));
    }

    private List<Product> generateProducts() {
        int i = 1;
        List<Product> products = new ArrayList<>();
        while (i < 6) {
            Product product = new Product("product" + String.valueOf(i), new Decimal128(new BigDecimal("25.99")), "action1", "https://http.cat/400.jpg");
            product.setId("product" + String.valueOf(i));
            products.add(product);
            i++;
        }
        products.get(4).setActionId("action2");
        return products;
    }

    private List<CompleteProduct> generateCompleteProducts() {
        int i = 1;
        List<CompleteProduct> products = new ArrayList<>();
        Action action = generateAction();
        while (i < 6) {
            Product product = new Product("product" + String.valueOf(i), new Decimal128(new BigDecimal("25.99")), "action1", "https://http.cat/400.jpg");
            product.setId("product" + String.valueOf(i));
            CompleteProduct completeProduct = new CompleteProduct(product, Optional.of(action));
            products.add(completeProduct);
            i++;
        }
        return products;
    }

    private Action generateAction() {
        Action action = new Action("action1", new Decimal128(new BigDecimal("200")), "new action", "vzw1", new Date());
        action.setId("action1");
        action.setStartDate(new GregorianCalendar(2022, Calendar.JANUARY, 18).getTime());
        action.setEndDate(new GregorianCalendar(2022, Calendar.MARCH, 18).getTime());

        return action;
    }

    @Test
    void givenProducts_whenGetAll_thenReturnJsonProducts() throws Exception {
        List<Product> products = generateProducts();
        List<CompleteProduct> completeProducts = generateCompleteProducts();
        Action action = generateAction();
        given(productRepository.findAllByIsActiveTrue()).willReturn(products);
        given(actionRepository.findById("action1")).willReturn(Optional.of(action));

        mockMvc.perform(get("/products"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].id", is(completeProducts.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(completeProducts.get(0).getName())))
                .andExpect(jsonPath("$[0].cost", is(25.99)))
                .andExpect(jsonPath("$[0].action.id", is(completeProducts.get(0).getAction().getId())))
                .andExpect(jsonPath("$[0].action.name", is(completeProducts.get(0).getAction().getName())))
                .andExpect(jsonPath("$[0].action.description", is(completeProducts.get(0).getAction().getDescription())))
                .andExpect(jsonPath("$[0].action.goal", is(completeProducts.get(0).getAction().getGoal().intValue())))
                .andExpect(jsonPath("$[0].action.vzwID", is(completeProducts.get(0).getAction().getVzwID())))
                .andExpect(jsonPath("$[1].id", is(completeProducts.get(1).getId())))
                .andExpect(jsonPath("$[1].name", is(completeProducts.get(1).getName())))
                .andExpect(jsonPath("$[1].cost", is(25.99)))
                .andExpect(jsonPath("$[1].action.id", is(completeProducts.get(1).getAction().getId())))
                .andExpect(jsonPath("$[1].action.name", is(completeProducts.get(1).getAction().getName())))
                .andExpect(jsonPath("$[1].action.description", is(completeProducts.get(1).getAction().getDescription())))
                .andExpect(jsonPath("$[1].action.goal", is(completeProducts.get(1).getAction().getGoal().intValue())))
                .andExpect(jsonPath("$[1].action.vzwID", is(completeProducts.get(1).getAction().getVzwID())))
                .andExpect(jsonPath("$[3].id", is(completeProducts.get(3).getId())))
                .andExpect(jsonPath("$[3].name", is(completeProducts.get(3).getName())))
                .andExpect(jsonPath("$[3].cost", is(25.99)))
                .andExpect(jsonPath("$[3].action.id", is(completeProducts.get(3).getAction().getId())))
                .andExpect(jsonPath("$[3].action.name", is(completeProducts.get(3).getAction().getName())))
                .andExpect(jsonPath("$[3].action.description", is(completeProducts.get(3).getAction().getDescription())))
                .andExpect(jsonPath("$[3].action.goal", is(completeProducts.get(3).getAction().getGoal().intValue())))
                .andExpect(jsonPath("$[3].action.vzwID", is(completeProducts.get(3).getAction().getVzwID())));

    }

    @Test
    void givenProduct_whenGetProductById_thenReturnJsonProduct() throws Exception {
        Product product = generateProduct();
        CompleteProduct completeProduct = generateCompleteProduct();
        Action action = generateAction();

        given(productRepository.findById("product1")).willReturn(Optional.of(product));
        given(actionRepository.findById("action1")).willReturn(Optional.of(action));

        mockMvc.perform(get("/products/{id}", "product1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(completeProduct.getId())))
                .andExpect(jsonPath("$.name", is(completeProduct.getName())))
                .andExpect(jsonPath("$.cost", is(completeProduct.getCost().doubleValue())))
                .andExpect(jsonPath("$.action.id", is(completeProduct.getAction().getId())))
                .andExpect(jsonPath("$.action.name", is(completeProduct.getAction().getName())))
                .andExpect(jsonPath("$.action.description", is(completeProduct.getAction().getDescription())))
                .andExpect(jsonPath("$.action.goal", is(completeProduct.getAction().getGoal().intValue())))
                .andExpect(jsonPath("$.action.vzwID", is(completeProduct.getAction().getVzwID())));

    }

    @Test
    void givenProduct_whenGetProductByIdNotExist_thenReturn404() throws Exception {
        given(productRepository.findById("product999")).willReturn(Optional.empty());

        mockMvc.perform(get("/products/{id}", "product999"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"The Product with ID product999 doesn't exist\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void givenProducts_whenGetProductByActionId_thenReturnProductsWithActionID() throws Exception {
        List<Product> products = generateProducts();
        List<CompleteProduct> completeProducts = generateCompleteProducts();
        Action action = generateAction();

        List<Product> actionProducts = new ArrayList<>();
        List<CompleteProduct> actionCompleteProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getActionId().equals(action.getId())) {
                actionProducts.add(product);
            }
        }

        for (CompleteProduct completeProduct : completeProducts) {
            if (completeProduct.getAction().getId().equals(action.getId())) {
                actionCompleteProducts.add(completeProduct);
            }
        }

        given(productRepository.findProductsByActionIdAndIsActiveTrue("action1")).willReturn(actionProducts);
        given(actionRepository.findById("action1")).willReturn(Optional.of(action));

        mockMvc.perform(get("/products/action/{id}", "action1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(completeProducts.get(0).getId())))
                .andExpect(jsonPath("$[0].name", is(completeProducts.get(0).getName())))
                .andExpect(jsonPath("$[0].cost", is(25.99)))
                .andExpect(jsonPath("$[0].action.id", is(completeProducts.get(0).getAction().getId())))
                .andExpect(jsonPath("$[0].action.name", is(completeProducts.get(0).getAction().getName())))
                .andExpect(jsonPath("$[0].action.description", is(completeProducts.get(0).getAction().getDescription())))
                .andExpect(jsonPath("$[0].action.goal", is(completeProducts.get(0).getAction().getGoal().intValue())))
                .andExpect(jsonPath("$[0].action.vzwID", is(completeProducts.get(0).getAction().getVzwID())))
                .andExpect(jsonPath("$[1].id", is(completeProducts.get(1).getId())))
                .andExpect(jsonPath("$[1].name", is(completeProducts.get(1).getName())))
                .andExpect(jsonPath("$[1].cost", is(25.99)))
                .andExpect(jsonPath("$[1].action.id", is(completeProducts.get(1).getAction().getId())))
                .andExpect(jsonPath("$[1].action.name", is(completeProducts.get(1).getAction().getName())))
                .andExpect(jsonPath("$[1].action.description", is(completeProducts.get(1).getAction().getDescription())))
                .andExpect(jsonPath("$[1].action.goal", is(completeProducts.get(1).getAction().getGoal().intValue())))
                .andExpect(jsonPath("$[1].action.vzwID", is(completeProducts.get(1).getAction().getVzwID())))
                .andExpect(jsonPath("$[3].id", is(completeProducts.get(3).getId())))
                .andExpect(jsonPath("$[3].name", is(completeProducts.get(3).getName())))
                .andExpect(jsonPath("$[3].cost", is(25.99)))
                .andExpect(jsonPath("$[3].action.id", is(completeProducts.get(3).getAction().getId())))
                .andExpect(jsonPath("$[3].action.name", is(completeProducts.get(3).getAction().getName())))
                .andExpect(jsonPath("$[3].action.description", is(completeProducts.get(3).getAction().getDescription())))
                .andExpect(jsonPath("$[3].action.goal", is(completeProducts.get(3).getAction().getGoal().intValue())))
                .andExpect(jsonPath("$[3].action.vzwID", is(completeProducts.get(3).getAction().getVzwID())));

    }

    @Test
    void whenAddProduct_thenReturnJsonProduct() throws Exception {
        Action action = generateAction();
        ProductDTO productDTO = new ProductDTO("testProduct", new Decimal128(new BigDecimal("25.99")), "action1", "https://http.cat/400.jpg");
        given(actionRepository.findById("action1")).willReturn(Optional.of(action));
        Product product = new Product(productDTO);
        CompleteProduct completeProduct = new CompleteProduct(product, Optional.of(action));

        mockMvc.perform(post("/product")
                        .content(mapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(completeProduct.getId())))
                .andExpect(jsonPath("$.cost", is(25.99)))
                .andExpect(jsonPath("$.action.id", is(completeProduct.getAction().getId())))
                .andExpect(jsonPath("$.action.name", is(completeProduct.getAction().getName())))
                .andExpect(jsonPath("$.action.goal", is(completeProduct.getAction().getGoal().intValue())))
                .andExpect(jsonPath("$.action.description", is(completeProduct.getAction().getDescription())))
                .andExpect(jsonPath("$.action.vzwID", is(completeProduct.getAction().getVzwID())));

    }

    @Test
    void givenProduct_whenPutProduct_thenReturnJsonProduct() throws Exception {
        Action action = generateAction();
        ProductDTO productDTO = new ProductDTO("Product Put", new Decimal128(new BigDecimal("3.55")), "action1", "https://http.cat/400.jpg");
        Product product = new Product(productDTO);
        CompleteProduct completeProduct = new CompleteProduct(product, Optional.of(action));

        given(actionRepository.findById("action1")).willReturn(Optional.of(action));
        given(productRepository.findById("product1")).willReturn(Optional.of(product));
        doReturn(completeProduct).when(productController).addProduct(productDTO);

        mockMvc.perform(put("/product/{id}", "product1")
                        .content(mapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(completeProduct.getName())))
                .andExpect(jsonPath("$.cost", is(completeProduct.getCost().doubleValue())))
                .andExpect(jsonPath("$.action.id", is(completeProduct.getAction().getId())))
                .andExpect(jsonPath("$.action.name", is(completeProduct.getAction().getName())))
                .andExpect(jsonPath("$.action.description", is(completeProduct.getAction().getDescription())))
                .andExpect(jsonPath("$.action.goal", is(completeProduct.getAction().getGoal().intValue())))
                .andExpect(jsonPath("$.action.vzwID", is(completeProduct.getAction().getVzwID())))
                .andExpect(jsonPath("$.image", is(completeProduct.getImage())))
                .andExpect(jsonPath("$.active", is(completeProduct.isActive())));
    }

    @Test
    void givenProduct_whenPutProductIdNotExist_thenReturn404() throws Exception {
        ProductDTO productDTO = new ProductDTO("Product Put", new Decimal128(new BigDecimal("3.55")), "action1", "https://http.cat/400.jpg");
        given(productRepository.findById("product999")).willReturn(Optional.empty());

        mockMvc.perform(put("/product/{id}", "product999")
                        .content(mapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"The Product with ID product999 doesn't exist\"", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
