package com.coverstar.repository;

import com.coverstar.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT b FROM Category b " +
            "WHERE b.name LIKE CONCAT('%', :name, '%') " +
            "AND (:productTypeId IS NULL OR b.productTypeId = :productTypeId)" +
            "AND (:status IS NULL OR b.status = :status)" +
            "ORDER BY b.numberOfVisits")
    List<Category> findAllByConditions(Long productTypeId, String name, Boolean status, Pageable pageable);

    List<Category> findAllByProductTypeId(Long id);
}
