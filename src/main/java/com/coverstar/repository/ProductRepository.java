package com.coverstar.repository;

import com.coverstar.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p " +
            "FROM Product p " +
            "INNER JOIN Image a ON p.id = a.productId " +
            "INNER JOIN ProductDetail pd ON p.id = pd.productId " +
            "INNER JOIN p.shippingMethods sm " +
            "WHERE p.productName LIKE CONCAT('%', :name, '%') " +
            "AND p.price BETWEEN :minPrice AND :maxPrice " +
            "AND (:brandId IS NULL OR p.brandId = :brandId) " +
            "AND (:categoryId IS NULL OR p.categoryId = :categoryId) " +
            "AND (COALESCE(:shippingMethodIds, NULL) IS NULL OR sm.id IN :shippingMethodIds) " +
            "AND (:productTypeId IS NULL OR p.productTypeId = :productTypeId) " +
            "AND a.type = 1 " +
            "AND p.status = :status")
    List<Product> findByNameContainingAndPriceBetweenWithDetails(
            @Param("productTypeId") Long productTypeId,
            @Param("name") String name,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brandId") Long brandId,
            @Param("categoryId") Long categoryId,
            @Param("shippingMethodIds") List<Long> shippingMethodIds,
            @Param("status") Boolean status,
            Pageable pageable);


    @Query("SELECT p " +
            "FROM Product p " +
            "INNER JOIN Image a ON p.id = a.productId " +
            "INNER JOIN ProductDetail pd ON p.id = pd.productId " +
            "WHERE p.id = :id " +
            "AND a.type = 1 " +
            "ORDER BY p.id ASC")
    Product getProductById(Long id);

    List<Product> findAllByProductTypeId(Long id);
}