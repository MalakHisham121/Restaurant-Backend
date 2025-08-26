package com.example.demo.service;

import com.example.demo.dto.AboutResponseDTO;
import com.example.demo.dto.QRScanRequestDTO;
import com.example.demo.dto.QRScanResponseDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Setting;
import com.example.demo.entity.Table;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.SettingRepository;
import com.example.demo.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MiscService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public AboutResponseDTO getAboutInfo() {
        // Get the first setting record from the database
        // Assuming there's only one settings record for the restaurant
        Setting setting = settingRepository.findAll().stream()
                .findFirst()
                .orElse(null);

        if (setting == null) {
            // Fallback if no settings found in database
            return new AboutResponseDTO(
                    "Restaurant Name Not Found",
                    "Address Not Available",
                    "Description Not Available",
                    "Contact Not Available"
            );
        }

        // Map database fields to response DTO
        return new AboutResponseDTO(
                setting.getName() != null ? setting.getName() : "Restaurant Name Not Available",
                setting.getAddress() != null ? setting.getAddress() : "Address Not Available",
                setting.getAboutDescription() != null ? setting.getAboutDescription() : "Description Not Available",
                setting.getPhoneNumber() != null ? setting.getPhoneNumber() : "Contact Not Available"
        );
    }

    public QRScanResponseDTO scanQRCode(QRScanRequestDTO request) {
        // Find table by QR code
        Table table = tableRepository.findByQrCode(request.getQrCode())
                .orElseThrow(() -> new RuntimeException("Invalid QR code. Table not found."));

        // Get all categories with their products
        List<Category> categories = categoryRepository.findAllWithProducts();

        // Map categories to DTO
        List<QRScanResponseDTO.CategoryWithItemsDTO> categoryDTOs = categories.stream()
                .map(this::mapCategoryToDTO)
                .collect(Collectors.toList());

        return new QRScanResponseDTO(table.getId(), categoryDTOs);
    }

    private QRScanResponseDTO.CategoryWithItemsDTO mapCategoryToDTO(Category category) {
        List<QRScanResponseDTO.ProductItemDTO> productDTOs = category.getProducts().stream()
                .map(this::mapProductToDTO)
                .collect(Collectors.toList());

        return new QRScanResponseDTO.CategoryWithItemsDTO(
                category.getId(),
                category.getName(),
                productDTOs
        );
    }

    private QRScanResponseDTO.ProductItemDTO mapProductToDTO(Product product) {
        return new QRScanResponseDTO.ProductItemDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice()
        );
    }
}
