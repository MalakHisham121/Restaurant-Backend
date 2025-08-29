package com.example.demo.service;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;


    @Transactional
    public List<Category> showCategories(){
        return categoryRepository.findAll();
    }
    @Transactional
    public Category createCategory(CategoryDTO entity){
        if(entity.getName()==null){
            throw new RuntimeException("Category Name couldn't be empty");
        }
        Category category = new Category();
        category.setName(entity.getName());
        Set<Product> products = productRepository.findAllById(entity.getProductIds())
                .stream()
                .collect(Collectors.toSet());
        category.setProducts(products);
        categoryRepository.save(category);
        products.forEach(p->{p.setCategory(category);
            productRepository.save(p);});

        return category;
    }

    @Transactional
    public List<Category>  listAllCategories(){
        return categoryRepository.findAll();
    }
    @Transactional
    public String deleteCategory(Long categoryId){
      categoryRepository.deleteById(categoryId);
      return "The Category with id "+ categoryId.toString() +" deleted successfully";
    }

    @Transactional
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO){
        if(categoryId==null){
            throw new RuntimeException("Order Id couldn't be null");
        }
        Category category  = categoryRepository.findById(categoryId).orElseThrow(() ->new RuntimeException("No such category with given ID"));

        if(categoryDTO.getName()!=null){
            category.setName(categoryDTO.getName());
        }
        if(categoryDTO.getProductIds()!=null){
            Set<Product> products = productRepository.findAllById(categoryDTO.getProductIds())
                    .stream()
                    .collect(Collectors.toSet());
            category.setProducts(products);
        }
        categoryRepository.save(category);
        return category;

    }









}
