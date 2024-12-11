package com.coverstar.service.Impl;

import com.coverstar.constant.Constants;
import com.coverstar.dto.BrandOrCategoryDto;
import com.coverstar.entity.Brand;
import com.coverstar.entity.Product;
import com.coverstar.repository.BrandRepository;
import com.coverstar.repository.ProductRepository;
import com.coverstar.service.BrandService;
import com.coverstar.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public Brand createOrUpdate(BrandOrCategoryDto brandOrCategoryDto) throws Exception {
        try {
            Brand brand = new Brand();
            if (brandOrCategoryDto.getId() != null) {
                brand = brandRepository.getById(brandOrCategoryDto.getId());
                brand.setUpdatedDate(new Date());
            } else {
                brand.setCreatedDate(new Date());
                brand.setUpdatedDate(new Date());
            }
            brand.setProductTypeId(brandOrCategoryDto.getProductTypeId());
            brand.setName(brandOrCategoryDto.getName());
            brand.setDescription(brandOrCategoryDto.getDescription());
            brand.setStatus(brandOrCategoryDto.getStatus());
            return brandRepository.save(brand);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try {
            List<Product> products = productRepository.findAllByBrandId(id);

            if (!CollectionUtils.isEmpty(products)) {
                for (Product product : products) {
                    productService.deleteProductById(product.getId());
                }
            }
            Brand brand = brandRepository.getById(id);
            if (brand == null) {
                throw new Exception(Constants.BRAND_NOT_FOUND);
            }
            brandRepository.delete(brand);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public List<Brand> getAllBrand(Long productTypeId, String name, Boolean status) {
        try {
            String nameValue = name != null ? name : StringUtils.EMPTY;
            Long productTypeIdValue = productTypeId != null ? productTypeId : null;
            Boolean statusValue = status != null ? status : null;
            return brandRepository.findAllByConditions(productTypeIdValue, nameValue, statusValue);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }
}
