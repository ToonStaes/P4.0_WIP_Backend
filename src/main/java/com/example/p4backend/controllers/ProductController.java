package com.example.p4backend.controllers;

import com.example.p4backend.models.Action;
import com.example.p4backend.models.dto.ProductDTO;
import com.example.p4backend.models.Product;
import com.example.p4backend.models.complete.CompleteProduct;
import com.example.p4backend.repositories.ActionRepository;
import com.example.p4backend.repositories.ProductRepository;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ActionRepository actionRepository;

    @PostConstruct
    public void fillDB() {
        if (productRepository.count() == 0) {
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

            productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5));
        }
    }

    @GetMapping("/products")
    public List<CompleteProduct> getAll() {
        List<CompleteProduct> returnList = new ArrayList<>();
        List<Product> products = productRepository.findAllByIsActiveTrue();

        for (Product product : products) {
            returnList.add(getCompleteProduct(product));
        }
        return returnList;
    }

    @GetMapping("/products/{id}")
    public CompleteProduct getProductById(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return getCompleteProduct(Objects.requireNonNull(product.get()));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The Product with ID " + id + " doesn't exist"
            );
        }
    }

    @GetMapping("/products/action/{id}")
    public List<CompleteProduct> getProductByActionId(@PathVariable String id) {
        List<Product> products = productRepository.findProductsByActionIdAndIsActiveTrue(id);
        List<CompleteProduct> completeProducts = new ArrayList<>();

        for (Product product : products) {
            Optional<Action> action = actionRepository.findById(product.getActionId());
            CompleteProduct completeProduct = new CompleteProduct(product, action);
            completeProducts.add(completeProduct);
        }

        return completeProducts;
    }

    @PostMapping("/product")
    public CompleteProduct addProduct(@RequestBody ProductDTO productDTO) {
        Product product = new Product(productDTO);
        productRepository.save(product);
        return getCompleteProduct(product);
    }

    /**
     * Old product gets disabled and a new product with the updated values gets made (so that old the cost on old purchases doesn't change)
     * @param updateProduct The DTO of the updated product
     * @param id The id of the product you wish to update
     * @return The newly made product with the updated values
     */
    @PutMapping("/product/{id}")
    public CompleteProduct updateProduct(@RequestBody ProductDTO updateProduct, @PathVariable String id) {
        Optional<Product> tempProduct = productRepository.findById(id);

        if (tempProduct.isPresent()) {
            // set old product to inActive
            Product product = Objects.requireNonNull(tempProduct.get());
            product.setActive(false);
            productRepository.save(product);
            // return a newly made product with updated values
            return addProduct(updateProduct);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The Product with ID " + id + " doesn't exist"
            );
        }
    }

    // Set product as inactive
    @DeleteMapping("/product/{id}")
    public Product deleteProduct(@PathVariable String id) {
        Optional<Product> tempProduct = productRepository.findById(id);

        if (tempProduct.isPresent()) {
            Product product = Objects.requireNonNull(tempProduct.get());
            product.setActive(false);
            productRepository.save(product);
            return product;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Product with ID " + id + " doesn't exist");
        }
    }

    // Get the filled CompleteProduct for the given product
    private CompleteProduct getCompleteProduct(Product product) {
        Optional<Action> action = actionRepository.findById(product.getActionId());
        return new CompleteProduct(product, action);
    }
}
