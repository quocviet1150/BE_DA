package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.dto.BrandOrCategoryDto;
import com.coverstar.entity.Category;
import com.coverstar.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid BrandOrCategoryDto categoryDto) {
        try {
            Category category = categoryService.createOrUpdate(categoryDto);
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            if (e.getMessage().equals(Constants.CATEGORY_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.CATEGORY_NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.ok(Constants.SUCCESS);
        } catch (Exception e) {
            if (e.getMessage().equals(Constants.CATEGORY_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.CATEGORY_NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getAllCategory")
    public ResponseEntity<?> getAllCategory(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "productTypeId", required = false) Long productTypeId,
                                            @RequestParam(value = "status", required = false) Boolean status) {
        try {
            return ResponseEntity.ok(categoryService.getAllCategory(name, status, productTypeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getCategoryById/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(categoryService.getCategoryById(id));
        } catch (Exception e) {
            if (e.getMessage().equals(Constants.CATEGORY_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.CATEGORY_NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }
}
