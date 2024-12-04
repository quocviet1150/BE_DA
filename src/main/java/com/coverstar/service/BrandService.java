package com.coverstar.service;

import com.coverstar.entity.Brand;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BrandService {
    Brand createOrUpdateBrand(Long id, String name, MultipartFile imageFiles) throws Exception;

    List<Brand> searchBrand(String name);

    Brand getBrand(Long id);

    void deleteBrand(Long id) throws Exception;

    Brand updateStatus(Long id, boolean status);
}