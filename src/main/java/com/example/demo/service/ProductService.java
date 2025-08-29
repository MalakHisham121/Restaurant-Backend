package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
            private final ProductRepository productRepository;
            ProductService(ProductRepository productRepository){
                this.productRepository =productRepository;
            }

            @Transactional
            public List<Product> showProducts(){
                return productRepository.findAll();
            }
}
