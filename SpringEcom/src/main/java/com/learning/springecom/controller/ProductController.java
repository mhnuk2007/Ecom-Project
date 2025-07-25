package com.learning.springecom.controller;

import com.learning.springecom.model.Product;
import com.learning.springecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173/")
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }
}
