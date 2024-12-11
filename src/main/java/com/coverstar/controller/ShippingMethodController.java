package com.coverstar.controller;

import com.coverstar.service.ShippingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping-methods")
public class ShippingMethodController {

    @Autowired
    private ShippingMethodService shippingMethodService;
}
