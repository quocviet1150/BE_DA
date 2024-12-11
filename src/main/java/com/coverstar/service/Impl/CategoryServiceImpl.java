package com.coverstar.service.Impl;

import com.coverstar.constant.Constants;
import com.coverstar.dto.BrandOrCategoryDto;
import com.coverstar.entity.Category;
import com.coverstar.entity.Product;
import com.coverstar.repository.CategoryRepository;
import com.coverstar.repository.ProductRepository;
import com.coverstar.service.CategoryService;
import com.coverstar.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Category createOrUpdate(BrandOrCategoryDto categoryDto) {
        try {
            Category category = new Category();
            if (categoryDto.getId() != null) {
                category = categoryRepository.getById(categoryDto.getId());
                category.setUpdatedDate(new Date());
            } else {
                category.setCreatedDate(new Date());
                category.setUpdatedDate(new Date());
            }
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());
            category.setStatus(categoryDto.getStatus());
            return categoryRepository.save(category);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try {
            List<Product> products = productRepository.findAllByCategoryId(id);

            if (!CollectionUtils.isEmpty(products)) {
                for (Product product : products) {
                    productService.deleteProductById(product.getId());
                }
            }

            Category category = categoryRepository.getById(id);
            if (category == null) {
                throw new Exception(Constants.CATEGORY_NOT_FOUND);
            }
            categoryRepository.delete(category);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public List<Category> getAllCategory(String name, Boolean status) {
        try {
            String nameValue = name != null ? name : StringUtils.EMPTY;
            Boolean statusValue = status != null ? status : null;
            return categoryRepository.findAllByConditions(nameValue, statusValue);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }
}
