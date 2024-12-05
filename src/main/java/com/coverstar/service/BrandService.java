package com.coverstar.service;

import com.coverstar.dto.BrandSearchDto;
import com.coverstar.entity.Brand;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BrandService {
    Brand createOrUpdateBrand(Long id, String name, MultipartFile imageFiles, String description,Integer type) throws Exception;

    List<Brand> searchBrand(BrandSearchDto brandSearchDto);

    Brand getBrand(Long id,Integer type);

    void deleteBrand(Long id) throws Exception;

    Brand updateStatus(Long id, boolean status);
}