package com.coverstar.repository;

import com.coverstar.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query("SELECT b FROM Brand b " +
            "WHERE (:name IS NULL OR b.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:status IS NULL OR b.status = :status) " +
            "AND (:type IS NULL OR b.type = :type) " +
            "ORDER BY b.createdDate DESC")
    List<Brand> findBrandsByConditions(
            String name,
            Boolean status,
            Integer type
    );

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT b FROM Brand b WHERE b.id = :id AND b.type = :type")
    Brand findByIdAndType(Long id, Integer type);

}
