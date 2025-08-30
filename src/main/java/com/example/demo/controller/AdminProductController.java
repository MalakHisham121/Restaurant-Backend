package com.example.demo.controller;

import com.example.demo.dto.ProductCreateDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.dto.ProductUpdateDTO;
import com.example.demo.service.AdminProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    @Autowired
    private AdminProductService adminProductService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        try {
            // Basic validation
            if (productCreateDTO.getName() == null || productCreateDTO.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Product name is required");
            }
            if (productCreateDTO.getPrice() == null || productCreateDTO.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Price must be greater than 0");
            }
            if (productCreateDTO.getCategoryId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Category ID is required");
            }
            if (productCreateDTO.getInStockQuantity() == null || productCreateDTO.getInStockQuantity() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Stock quantity must be non-negative");
            }

            ProductResponseDTO createdProduct = adminProductService.createProduct(productCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating product: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while creating the product: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllProducts() {
        try {
            return ResponseEntity.ok(adminProductService.getAllProducts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving products: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductUpdateDTO productUpdateDTO) {
        try {
            // Basic validation
            if (productUpdateDTO.getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Product ID is required for update");
            }
            if (productUpdateDTO.getPrice() != null && productUpdateDTO.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Price must be greater than 0");
            }
            if (productUpdateDTO.getName() != null && productUpdateDTO.getName().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Product name cannot be empty");
            }
            if (productUpdateDTO.getInStockQuantity() != null && productUpdateDTO.getInStockQuantity() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Stock quantity must be non-negative");
            }

            ProductResponseDTO updatedProduct = adminProductService.updateProduct(productUpdateDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating product: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while updating the product: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            // Basic validation
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Valid product ID is required for deletion");
            }

            adminProductService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error deleting product: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while deleting the product: " + e.getMessage());
        }
    }
}
