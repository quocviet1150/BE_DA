package com.coverstar.repository;

import com.coverstar.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p " +
            "FROM Product p " +
            "INNER JOIN Image a ON p.id = a.productId " +
            "WHERE p.productName LIKE CONCAT('%', :name, '%') " +
            "AND p.price BETWEEN :minPrice AND :maxPrice " +
            "AND (:brandId IS NULL OR p.brandId = :brandId) " +
            "AND a.type = 1 ORDER BY p.createdDate DESC")
    List<Product> findByNameContainingAndPriceBetweenWithDetails(Long brandId, String name, BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT p " +
            "FROM Product p " +
            "INNER JOIN Image a ON p.id = a.productId " +
            "WHERE p.id = :id " +
            "AND a.type = 1 " +
            "ORDER BY p.id ASC")
    Product getProductById(Long id);

    List<Product> findAllByBrandId(Long id);
}