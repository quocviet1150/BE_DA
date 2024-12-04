package com.coverstar.repository;

import com.coverstar.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT a FROM Brand a WHERE a.name LIKE CONCAT('%', :name, '%')  AND a.status = true")
    List<Brand> findAllByStatus(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
