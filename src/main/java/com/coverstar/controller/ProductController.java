package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.dto.SearchProductDto;
import com.coverstar.entity.Product;
import com.coverstar.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
                                                   @RequestParam("productTypeId") Long productTypeId,
                                                   @RequestParam("size") String size,
                                                   @RequestParam("price") BigDecimal price,
                                                   @RequestParam("percentageReduction") Float percentageReduction,
                                                   @RequestParam(value = "description", required = false) String description,
                                                   @RequestParam("file") List<MultipartFile> imageFiles,
                                                   @RequestParam(value = "imageIdsToRemove", required = false) String imageIdsToRemove,
                                                   @RequestParam MultiValueMap<String, String> productDetailsParams,
                                                   @RequestParam(value = "productDetailsFiles", required = false) List<MultipartFile> productDetailsFiles,
                                                   @RequestParam(value = "listProductDetailIdRemove", required = false) String listProductDetailIdRemove,
                                                   @RequestParam List<String> shippingMethodIds,
                                                   @RequestParam Long brandId,
                                                   @RequestParam Long categoryId,
                                                   @RequestParam("status") Boolean status) {
        try {

            Product product = productService.saveOrUpdateProduct(
                    id,
                    productName,
                    productTypeId,
                    size,
                    price,
                    percentageReduction,
                    description,
                    imageFiles,
                    imageIdsToRemove,
                    productDetailsParams,
                    productDetailsFiles,
                    listProductDetailIdRemove,
                    shippingMethodIds,
                    brandId,
                    categoryId,
                    status);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "ProductDetail not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductDetail not found");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestBody @Valid SearchProductDto searchProductDto) {
        try {
            List<Product> products = productService.findByNameAndPriceRange(searchProductDto);
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
            if (Objects.equals(e.getMessage(), "Product not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }
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