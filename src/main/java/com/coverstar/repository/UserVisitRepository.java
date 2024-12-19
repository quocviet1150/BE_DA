package com.coverstar.repository;

import com.coverstar.entity.UserVisits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserVisitRepository extends JpaRepository<UserVisits, Long> {

    @Query("SELECT u FROM UserVisits u WHERE u.userId = :userId")
    List<UserVisits> findByUserId(Long userId);

    @Query("SELECT uv.visitDate, SUM(uv.visitCount) " +
            "FROM UserVisits uv " +
            "WHERE uv.visitDate BETWEEN :startDate AND :endDate " +
            "GROUP BY uv.visitDate " +
            "ORDER BY uv.visitDate DESC ")
    List<Object[]> findVisitCountsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}