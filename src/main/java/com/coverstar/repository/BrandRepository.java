package com.coverstar.repository;

import com.coverstar.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT b FROM Brand b " +
            "WHERE b.name LIKE CONCAT('%', :name, '%') " +
            "AND (:status IS NULL OR b.status = :status)")
    List<Brand> findAllByConditions(String name, Boolean status);
}
