package com.coverstar.service;

import com.coverstar.dto.BrandOrCategoryDto;
import com.coverstar.entity.Brand;

import java.util.List;

public interface BrandService {
    Brand createOrUpdate(BrandOrCategoryDto brandOrCategoryDto) throws Exception;

    void delete(Long id) throws Exception;

    List<Brand> getAllBrand(String name, Boolean status);
}