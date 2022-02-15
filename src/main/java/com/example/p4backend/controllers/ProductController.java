package com.example.p4backend.controllers;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.Product;
import com.example.p4backend.models.complete.CompleteProduct;
import com.example.p4backend.models.dto.ProductDTO;
import com.example.p4backend.repositories.ActionRepository;
import com.example.p4backend.repositories.ProductRepository;
import lombok.Getter;
import org.bson.types.Decimal128;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@RestController
@CrossOrigin(origins = {"http://www.wip-shop.be","http://wip-shop.be"}, allowedHeaders = "*")
public class ProductController {
    private final ProductRepository productRepository;
    private final ActionRepository actionRepository;

    public ProductController(ProductRepository productRepository, ActionRepository actionRepository) {
        this.productRepository = productRepository;
        this.actionRepository = actionRepository;
    }

    @PostConstruct
    public void fillDB() {
        if (getProductRepository().count() == 0) {
            Product product1 = new Product("Roze koeken", new Decimal128(new BigDecimal(5)), "action1", "https://upload.wikimedia.org/wikipedia/commons/1/12/Roze_koek.jpg");
            product1.setId("product1");
            Product product2 = new Product("Cupcakes", new Decimal128(new BigDecimal(15)), "action1", "https://cdn.pixabay.com/photo/2015/04/25/13/45/cake-739151_1280.jpg");
            product2.setId("product2");
            Product product3 = new Product("Cake", new Decimal128(new BigDecimal(12)), "action1", "https://cdn.pixabay.com/photo/2015/01/03/09/32/cake-587274_1280.jpg");
            product3.setId("product3");
            Product product4 = new Product("Appeltaart", new Decimal128(new BigDecimal("2.5")), "action2", "https://img.static-rmg.be/a/food/image/q100/w480/h360/1096415/snelle-appeltaart.jpg");
            product4.setId("product4");
            Product product5 = new Product("Hollandse appeltaart", new Decimal128(new BigDecimal("6.5")), "action3", "https://watschaftdepodcast.com/wp-content/uploads/2020/03/WSDP-18-Hollandse-Appeltaart-SQ-lofi-scaled.jpg");
            product5.setId("product5");

            getProductRepository().saveAll(List.of(product1, product2, product3, product4, product5));
        }
    }

    @GetMapping("/products")
    public List<CompleteProduct> getAll() {
        List<CompleteProduct> returnList = new ArrayList<>();
        List<Product> products = getProductRepository().findAllByIsActiveTrue();

        for (Product product : products) {
            returnList.add(getCompleteProduct(product));
        }
        return returnList;
    }

    @GetMapping("/products/{id}")
    public CompleteProduct getProductById(@PathVariable String id) {
        Product product = getProductRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Product with ID " + id + " doesn't exist"));
        return getCompleteProduct(product);
    }

    @GetMapping("/products/action/{id}")
    public List<CompleteProduct> getProductByActionId(@PathVariable String id) {
        List<Product> products = getProductRepository().findProductsByActionIdAndIsActiveTrue(id);
        List<CompleteProduct> completeProducts = new ArrayList<>();

        for (Product product : products) {
            Optional<Action> action = getActionRepository().findById(product.getActionId());
            CompleteProduct completeProduct = new CompleteProduct(product, action);
            completeProducts.add(completeProduct);
        }

        return completeProducts;
    }

    @PostMapping("/product")
    public CompleteProduct addProduct(@RequestBody ProductDTO productDTO) {
        Product product = new Product(productDTO);
        getProductRepository().save(product);
        return getCompleteProduct(product);
    }

    /**
     * Old product gets disabled and a new product with the updated values gets made (so that old the cost on old purchases doesn't change)
     *
     * @param updateProduct The DTO of the updated product
     * @param id            The id of the product you wish to update
     * @return The newly made product with the updated values
     */
    @PutMapping("/product/{id}")
    public CompleteProduct updateProduct(@RequestBody ProductDTO updateProduct, @PathVariable String id) {
        Product product = getProductRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Product with ID " + id + " doesn't exist"));

        // set old product to inActive
        product.setActive(false);
        getProductRepository().save(product);
        // return a newly made product with updated values
        return addProduct(updateProduct);
    }

    // Set product as inactive
    @DeleteMapping("/product/{id}")
    public Product deleteProduct(@PathVariable String id) {
        Product product = getProductRepository().findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The Product with ID " + id + " doesn't exist"));

        product.setActive(false);
        getProductRepository().save(product);
        return product;
    }

    // Get the filled CompleteProduct for the given product
    private CompleteProduct getCompleteProduct(Product product) {
        Optional<Action> action = getActionRepository().findById(product.getActionId());
        return new CompleteProduct(product, action);
    }
}
