package com.coverstar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandOrCategoryDto implements Serializable {
    private Long id;
    private Long productTypeId;
    private String name;
    private Boolean status;
    private String description;
}
