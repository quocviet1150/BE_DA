package com.coverstar.service;

import com.coverstar.dto.ShippingMethodDto;
import com.coverstar.entity.ShippingMethod;

import java.util.List;

public interface ShippingMethodService {
    List<ShippingMethod> getAllShippingMethod(String name);

    ShippingMethod createOrUpdate(ShippingMethodDto shippingMethodDto) throws Exception;
}
