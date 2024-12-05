package com.coverstar.repository;

import com.coverstar.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormatSymbols;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT a FROM Discount a " +
            "WHERE a.name LIKE CONCAT('%', :name, '%') " +
            "AND a.status = :status AND a.code LIKE CONCAT('%', :code, '%')")
    List<Discount> findAllByStatus(String name, Boolean status, String code);

    boolean existsByCode(String name);

    boolean existsByCodeAndIdNot(String name, Long id);

    @Query("SELECT a.percent FROM Discount a WHERE a.code = :code")
    Long findByCode(String code);
}
