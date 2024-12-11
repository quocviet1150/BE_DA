package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.dto.BrandOrCategoryDto;
import com.coverstar.entity.Brand;
import com.coverstar.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid BrandOrCategoryDto brandOrCategoryDto) {
        try {
            Brand brand = brandService.createOrUpdate(brandOrCategoryDto);
            return ResponseEntity.ok(brand);
        } catch (Exception e) {
            if (e.getMessage().equals(Constants.BRAND_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.BRAND_NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getAllBrand")
    public ResponseEntity<?> getAllBrand(@RequestParam(value = "productTypeId", required = false) Long productTypeId,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "status", required = false) Boolean status) {
        try {
            return ResponseEntity.ok(brandService.getAllBrand(productTypeId, name, status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getBrandById/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(brandService.getBrandById(id));
        } catch (Exception e) {
            if (e.getMessage().equals(Constants.BRAND_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.BRAND_NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            brandService.delete(id);
            return ResponseEntity.ok(Constants.SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }
}
