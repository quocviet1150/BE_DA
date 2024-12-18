package com.coverstar.service;

import com.coverstar.dto.BrandOrCategoryDto;
import com.coverstar.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createOrUpdate(BrandOrCategoryDto brandOrCategoryDto) throws Exception;

    void delete(Long id) throws Exception;

    List<Category> getAllCategory(Long productTypeId, String name, Boolean status, Integer page, Integer size);

    Category getCategoryById(Long id) throws Exception;
}
