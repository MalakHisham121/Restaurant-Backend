package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;

    }

    @Transactional
    public List<Category> showCategories(){
        return categoryRepository.findAll();
    }
}
