package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.dto.SearchProductDto;
import com.coverstar.entity.Product;
import com.coverstar.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createOrUpdateProduct")
    public ResponseEntity<?> createOrUpdateProduct(@RequestParam(value = "id", required = false) Long id,
                                                   @RequestParam("productName") String productName,
                                                   @RequestParam("brandId") Long brandId,
                                                   @RequestParam("quantity") Long quantity,
                                                   @RequestParam("price") BigDecimal price,
                                                   @RequestParam("priceBeforeDiscount") BigDecimal priceBeforeDiscount,
                                                   @RequestParam("color") String color,
                                                    @RequestParam("size") String size,
                                                   @RequestParam(value = "description", required = false) String description,
                                                   @RequestParam("file") List<MultipartFile> imageFiles,
                                                   @RequestParam(value = "imageIdsToRemove", required = false) List<Long> imageIdsToRemove) {
        try {
            Product product = productService.saveOrUpdateProduct(
                    id,
                    productName,
                    brandId,
                    quantity,
                    price,
                    priceBeforeDiscount,
                    color,
                    size,
                    description,
                    imageFiles,
                    imageIdsToRemove);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestBody @Valid SearchProductDto searchProductDto) {
        try {
            List<Product> products = productService.findByNameAndPriceRange(
                    searchProductDto.getBrandId(),
                    searchProductDto.getName(),
                    searchProductDto.getMinPrice(),
                    searchProductDto.getMaxPrice());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/deleteProduct/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestParam("id") Long id,
                                          @RequestParam("type") Boolean type) {
        try {
            Product product = productService.updateStatus(id, type);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }
}