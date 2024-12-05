package com.coverstar.service;

import com.coverstar.dto.CommentDto;
import com.coverstar.entity.Comment;
import com.coverstar.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product saveOrUpdateProduct(Long id,
                                String productName,
                                Long brand,
                                Long quantity,
                                BigDecimal price,
                                String color,
                                String description,
                                List<MultipartFile> imageFiles,
                                List<Long> imageIdsToRemove) throws Exception;

    List<Product> findByNameAndPriceRange(Long brandId,
            String name,
                                          BigDecimal minPrice,
                                          BigDecimal maxPrice);

    Product getProductById(Long id);

    void deleteProductById(Long id) throws Exception;

    Product updateStatus(Long id, boolean type);
}