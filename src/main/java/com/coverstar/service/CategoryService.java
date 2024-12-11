package com.coverstar.service;

import com.coverstar.dto.BrandOrCategoryDto;
import com.coverstar.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createOrUpdate(BrandOrCategoryDto categoryDto);

    void delete(Long id) throws Exception;

    List<Category> getAllCategory(String name, Boolean status);
}
