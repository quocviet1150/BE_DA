package com.coverstar.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "CATEGORIES")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "productType_id", nullable = false)
    private Long productTypeId;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "directory_path", nullable = false)
    private String directoryPath;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "description")
    private String description;

    @Column(name = "number_of_visits")
    private Long numberOfVisits;

    @Column(name = "quantity_sold")
    private Long quantitySold;
}