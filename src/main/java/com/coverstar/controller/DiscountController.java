package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.entity.Discount;
import com.coverstar.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping("/createDiscount")
    public ResponseEntity<?> createDiscount(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("name") String name,
            @RequestParam("code") String code,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("percent") BigDecimal percent,
            @RequestParam(value = "file", required = false) MultipartFile imageFiles,
            @RequestParam("expiredDate") String expiredDate) {
        try {
            Discount discount = discountService.createOrUpdateDiscount(id, name, code,
                    description, percent, imageFiles, expiredDate);
            return ResponseEntity.ok(discount);
        } catch (Exception e) {
            if (e.getMessage().equals("Discount code already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Discount code already exists");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchDiscount(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "status", required = false) Boolean status,
                                            @RequestParam(value = "code", required = false) String code) {
        try {
            List<Discount> discounts = discountService.searchDiscount(name, status, code);
            return ResponseEntity.ok(discounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getDiscount/{id}")
    public ResponseEntity<?> getDiscount(@PathVariable Long id) {
        try {
            Discount discount = discountService.getDiscount(id);
            return ResponseEntity.ok(discount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getByCode/{code}")
    public ResponseEntity<?> getByCode(@PathVariable String code) {
        try {
            Long percent = discountService.getByCode(code);
            return ResponseEntity.ok(percent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/deleteDiscount/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Long id) {
        try {
            discountService.deleteDiscount(id);
            return ResponseEntity.ok("Discount deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam("status") boolean status) {
        try {
            Discount discount = discountService.updateStatus(id, status);
            return ResponseEntity.ok(discount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }
}