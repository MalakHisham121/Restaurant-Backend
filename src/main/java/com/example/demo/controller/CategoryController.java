package com.example.demo.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    CategoryController(CategoryService categoryService){

        this.categoryService= categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> showCategory(){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.showCategories());
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDTO));

    }

    @GetMapping("/list")
    public ResponseEntity<List<Category>> listAllCategories(){
       return ResponseEntity.status(HttpStatus.OK).body(categoryService.listAllCategories());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String>  deleteCategory(@RequestParam  Long categoryid ){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.deleteCategory(categoryid));
    }

    @PostMapping("/update")
    public ResponseEntity<Category> updateCategory(@RequestParam Long CategoryId, @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(CategoryId,categoryDTO));

    }
}
