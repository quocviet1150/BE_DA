package com.coverstar.service.Impl;

import com.coverstar.dto.BrandSearchDto;
import com.coverstar.entity.Brand;
import com.coverstar.entity.Product;
import com.coverstar.repository.BrandRepository;
import com.coverstar.repository.ProductRepository;
import com.coverstar.service.BrandService;
import com.coverstar.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Value("${image.directory}")
    private String imageDirectory;

    @Override
    public Brand createOrUpdateBrand(Long id, String name, MultipartFile imageFile, String description, Integer type) throws Exception {
        Brand brand = new Brand();
        try {
            boolean isNameExist = id == null
                    ? brandRepository.existsByName(name)
                    : brandRepository.existsByNameAndIdNot(name, id);

            if (isNameExist) {
                throw new Exception("Brand name already exists");
            }

            if (id != null) {
                brand = brandRepository.findById(id).orElse(null);
                brand.setUpdatedDate(new Date());
            } else {
                brand.setCreatedDate(new Date());
                brand.setUpdatedDate(new Date());
            }
            brand.setStatus(true);
            brand.setName(name);
            brand.setDescription(description);
            brand.setType(type);
            brand = brandRepository.save(brand);

            if (imageFile != null && !imageFile.isEmpty()) {
                if (brand.getDirectoryPath() != null) {
                    File oldFile = new File(brand.getDirectoryPath());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
                String fullPath = handleFileUpload(imageFile, "brand", brand.getId());
                brand.setDirectoryPath(fullPath);
            }
            brandRepository.save(brand);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
        return brand;
    }

    @Override
    public List<Brand> searchBrand(BrandSearchDto brandSearchDto) {
        try {
            String name = brandSearchDto.getName();
            Boolean status = brandSearchDto.getStatus();
            Integer type = brandSearchDto.getType();
            return brandRepository.findBrandsByConditions(name, status, type);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Brand getBrand(Long id,Integer type) {
        try {
            return brandRepository.findByIdAndType(id, type);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteBrand(Long id) throws Exception {
        try {
            List<Product> products = productRepository.findAllByBrandId(id);

            if (!CollectionUtils.isEmpty(products)) {
                for (Product product : products) {
                    productService.deleteProductById(product.getId());
                }
            }

            Brand brand = brandRepository.findById(id).orElse(null);
            if (brand != null && brand.getDirectoryPath() != null) {
                File oldFile = new File(brand.getDirectoryPath());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            brandRepository.deleteById(id);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Brand updateStatus(Long id, boolean status) {
        try {
            List<Product> products = productRepository.findAllByBrandId(id);

            if (!CollectionUtils.isEmpty(products)) {
                for (Product product : products) {
                    productService.updateStatus(product.getId(), status);
                }
            }
            Brand brand = brandRepository.findById(id).orElse(null);
            brand.setStatus(status);
            return brandRepository.save(brand);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    private String handleFileUpload(MultipartFile file, String type, Long id) throws Exception {
        String filePath = imageDirectory + type + File.separator + id;
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fullPath = filePath + File.separator + file.getOriginalFilename();
        file.transferTo(new File(fullPath));
        return fullPath;
    }
}