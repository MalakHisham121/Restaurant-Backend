package com.example.demo.service;

import com.example.demo.dto.ProductCreateDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.dto.ProductUpdateDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {
        // Check if category exists
        Category category = categoryRepository.findById(productCreateDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productCreateDTO.getCategoryId()));

        // Create new product
        Product product = new Product();
        product.setName(productCreateDTO.getName());
        product.setDescription(productCreateDTO.getDescription());
        product.setImageUrl(productCreateDTO.getImageUrl());
        product.setPrice(productCreateDTO.getPrice());
        product.setCategory(category);
        product.setInStockQuantity(productCreateDTO.getInStockQuantity());

        // Save product
        Product savedProduct = productRepository.save(product);

        // Return response DTO
        return new ProductResponseDTO(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getImageUrl(),
                savedProduct.getPrice(),
                savedProduct.getCategory().getId(),
                savedProduct.getCategory().getName(),
                savedProduct.getInStockQuantity()
        );
    }

    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> new ProductResponseDTO(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getImageUrl(),
                        product.getPrice(),
                        product.getCategory() != null ? product.getCategory().getId() : null,
                        product.getCategory() != null ? product.getCategory().getName() : null,
                        product.getInStockQuantity()
                ))
                .collect(Collectors.toList());
    }

    public ProductResponseDTO updateProduct(ProductUpdateDTO productUpdateDTO) {
        // Check if product exists
        Product existingProduct = productRepository.findById(productUpdateDTO.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productUpdateDTO.getId()));

        // Check if category exists (if categoryId is provided)
        Category category = null;
        if (productUpdateDTO.getCategoryId() != null) {
            category = categoryRepository.findById(productUpdateDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + productUpdateDTO.getCategoryId()));
        }

        // Update product fields
        if (productUpdateDTO.getName() != null) {
            existingProduct.setName(productUpdateDTO.getName());
        }
        if (productUpdateDTO.getDescription() != null) {
            existingProduct.setDescription(productUpdateDTO.getDescription());
        }
        if (productUpdateDTO.getImageUrl() != null) {
            existingProduct.setImageUrl(productUpdateDTO.getImageUrl());
        }
        if (productUpdateDTO.getPrice() != null) {
            existingProduct.setPrice(productUpdateDTO.getPrice());
        }
        if (category != null) {
            existingProduct.setCategory(category);
        }
        if (productUpdateDTO.getInStockQuantity() != null) {
            existingProduct.setInStockQuantity(productUpdateDTO.getInStockQuantity());
        }

        // Save updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // Return response DTO
        return new ProductResponseDTO(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getImageUrl(),
                updatedProduct.getPrice(),
                updatedProduct.getCategory() != null ? updatedProduct.getCategory().getId() : null,
                updatedProduct.getCategory() != null ? updatedProduct.getCategory().getName() : null,
                updatedProduct.getInStockQuantity()
        );
    }
}
