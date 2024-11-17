
package com.example.rating.service;

import com.example.rating.entity.Product;
import com.example.rating.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getApprovedProducts() {
        return productRepository.findAll()
            .stream()
            .filter(Product::isApproved)
            .collect(Collectors.toList());
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void approveProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setApproved(true);
        productRepository.save(product);
    }
}
