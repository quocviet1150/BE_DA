package com.coverstar.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "DISCOUNTS")
@Getter
@Setter
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "directory_path")
    private String directoryPath;

    @Column(name = "description")
    private String description;

    @Column(name = "percent")
    private BigDecimal percent;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "expired_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;

    @Column(name = "discount_type")
    private Integer discountType;

    @Column(name = "level_applied")
    private BigDecimal levelApplied;
}