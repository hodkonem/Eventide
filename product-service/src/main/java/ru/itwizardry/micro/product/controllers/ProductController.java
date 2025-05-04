package ru.itwizardry.micro.product.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itwizardry.micro.product.exceptions.ErrorMessage;
import ru.itwizardry.micro.product.service.ProductService;
import ru.itwizardry.micro.product.service.dto.CreateProductDTO;

import java.util.Date;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        String productId = null;
        try {
            productId = productService.createProduct(createProductDTO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(new Date(), e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}