package com.coverstar.service;

import com.coverstar.entity.Product;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product saveOrUpdateProduct(Long id,
                                String productName,
                                Long productTypeId,
                                String size,
                                BigDecimal price,
                                Float percentageReduction,
                                String description,
                                List<MultipartFile> imageFiles,
                                String imageIdsToRemove,
                                MultiValueMap<String, String> productDetailsParams,
                                List<MultipartFile> productDetailsFiles,
                                String listProductDetailIdRemove) throws Exception;

    List<Product> findByNameAndPriceRange(Long productTypeId,
                                          String name,
                                          BigDecimal minPrice,
                                          BigDecimal maxPrice);

    Product getProductById(Long id);

    void deleteProductById(Long id) throws Exception;

    Product updateStatus(Long id, Boolean type);
}