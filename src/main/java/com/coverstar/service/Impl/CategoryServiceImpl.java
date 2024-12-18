package com.coverstar.service.Impl;

import com.coverstar.constant.Constants;
import com.coverstar.dto.BrandOrCategoryDto;
import com.coverstar.entity.Category;
import com.coverstar.entity.Product;
import com.coverstar.repository.CategoryRepository;
import com.coverstar.repository.ProductRepository;
import com.coverstar.service.CategoryService;
import com.coverstar.service.ProductService;
import com.coverstar.utils.ShopUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Lazy
    @Autowired
    private ProductRepository productRepository;

    @Lazy
    @Autowired
    private ProductService productService;

    @Override
    public Category createOrUpdate(BrandOrCategoryDto brandOrCategoryDto) throws Exception {
        try {
            Category category = new Category();
            if (brandOrCategoryDto.getId() != null) {
                category = categoryRepository.getById(brandOrCategoryDto.getId());
                category.setUpdatedDate(new Date());
            } else {
                if (brandOrCategoryDto.getImageFiles() == null || brandOrCategoryDto.getImageFiles().isEmpty()) {
                    throw new Exception(Constants.NOT_IMAGE);
                }
                category.setCreatedDate(new Date());
                category.setUpdatedDate(new Date());
                category.setProductTypeId(brandOrCategoryDto.getProductTypeId());
            }
            category.setName(brandOrCategoryDto.getName());
            category.setDescription(brandOrCategoryDto.getDescription());
            category.setStatus(brandOrCategoryDto.getStatus());
            category = categoryRepository.save(category);

            if (brandOrCategoryDto.getImageFiles() != null && !brandOrCategoryDto.getImageFiles().isEmpty()) {
                if (category.getDirectoryPath() != null) {
                    File oldFile = new File(category.getDirectoryPath());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
                String fullPath = ShopUtil.handleFileUpload(brandOrCategoryDto.getImageFiles(), "categories", category.getId());
                category.setDirectoryPath(fullPath);
            }

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
    public List<Category> getAllCategory(Long productTypeId, String name, Boolean status, Integer page, Integer size) {
        try {
            String nameValue = name != null ? name : StringUtils.EMPTY;
            Long productTypeIdValue = productTypeId != null ? productTypeId : null;
            Boolean statusValue = status != null ? status : null;
            Pageable pageable = PageRequest.of(page, size);
            return categoryRepository.findAllByConditions(productTypeIdValue, nameValue, statusValue, pageable);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        try {
            Category category = categoryRepository.getById(id);
            if (category == null) {
                throw new Exception(Constants.CATEGORY_NOT_FOUND);
            }
            return category;
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }
}
