package com.example.rating.controller;

import com.example.rating.entity.Product;
import com.example.rating.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllApprovedProducts() {
        return productService.getApprovedProducts();
    }

    @PostMapping
    @PreAuthorize("hasRole('NORMAL_USER')")
    public Product addProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('MODERATOR')")
    public void approveProduct(@PathVariable Long id) {
        productService.approveProduct(id);
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('NORMAL_USER')")
    public String testNormal() {
        return "hi employee";
    }

    @GetMapping("/test2")
    @PreAuthorize("hasRole('MODERATOR')")
    public String testModerator() {
        return "hi Boss";
    }
}