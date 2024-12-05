package com.coverstar.service;

import com.coverstar.entity.Discount;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountService {

    Discount createOrUpdateDiscount(Long id, String name, String code,
                                    String description, BigDecimal percent, MultipartFile imageFiles,
                                    String expiredDate) throws Exception;

    List<Discount> searchDiscount(String name, Boolean status, String code);

    Discount getDiscount(Long id);

    Long getByCode(String code);

    void deleteDiscount(Long id);

    Discount updateStatus(Long id, boolean status);
}