package com.coverstar.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class SearchProductDto implements Serializable {
    private Long productTypeId;
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
