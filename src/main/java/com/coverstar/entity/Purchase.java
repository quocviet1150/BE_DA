package com.coverstar.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PURCHASES")
@Getter
@Setter
public class Purchase implements Serializable {

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "discount_id")
    private Integer discountId;

    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "total", nullable = false)
    private Long total;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "number_of_visits")
    private Long numberOfVisits;
}
