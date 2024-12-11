package com.coverstar.repository;

import com.coverstar.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c " +
            "WHERE c.name LIKE CONCAT('%', :name, '%') " +
            "AND (:productTypeId IS NULL OR c.productTypeId = :productTypeId) " +
            "AND (:status IS NULL OR c.status = :status)")
    List<Category> findAllByConditions(String name, Boolean status, Long productTypeId);

    List<Category> findAllByProductTypeId(Long id);
}
