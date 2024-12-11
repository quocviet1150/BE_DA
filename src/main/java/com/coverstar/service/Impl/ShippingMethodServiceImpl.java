package com.coverstar.service.Impl;

import com.coverstar.repository.ShippingMethodRepository;
import com.coverstar.service.ShippingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingMethodServiceImpl implements ShippingMethodService {

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;
}
