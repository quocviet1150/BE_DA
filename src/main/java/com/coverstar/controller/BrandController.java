package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.entity.Brand;
import com.coverstar.entity.Product;
import com.coverstar.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/createOrUpdateBrand")
    public ResponseEntity<?> createOrComment(@RequestParam(value = "id", required = false) Long id,
                                             @RequestParam("name") String name,
                                             @RequestParam(value = "file", required = false) MultipartFile imageFiles) {
        try {
            Brand brand = brandService.createOrUpdateBrand(id, name, imageFiles);
            return ResponseEntity.ok(brand);
        } catch (Exception e) {
            if (e.getMessage().equals("Brand name already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Brand name already exists");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBrand(@RequestParam(value = "name", required = false) String name) {
        try {
            List<Brand> brands = brandService.searchBrand(name);
            return ResponseEntity.ok(brands);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getBrand/{id}")
    public ResponseEntity<?> getBrand(@PathVariable Long id) {
        try {
            Brand brand = brandService.getBrand(id);
            return ResponseEntity.ok(brand);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/deleteBrand/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        try {
            brandService.deleteBrand(id);
            return ResponseEntity.ok("Brand deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestParam("id") Long id,
                                          @RequestParam("status") boolean status) {
        try {
            Brand brand = brandService.updateStatus(id, status);
            return ResponseEntity.ok(brand);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }
}