package com.coverstar.repository;

import com.coverstar.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT c FROM Brand c " +
            "WHERE c.name LIKE CONCAT('%', :name, '%') " +
            "AND (:productTypeId IS NULL OR c.productTypeId = :productTypeId) " +
            "AND (:status IS NULL OR c.status = :status)")
    List<Brand> findAllByConditions(String name, Boolean status, Long productTypeId);

    List<Brand> findAllByProductTypeId(Long id);
}
