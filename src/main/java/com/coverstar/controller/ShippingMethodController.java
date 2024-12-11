package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.service.ShippingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping-methods")
public class ShippingMethodController {

    @Autowired
    private ShippingMethodService shippingMethodService;

//    @GetMapping("/getAllShippingMethod")
//    public ResponseEntity<?> getAllShippingMethod() {
//        try {
//            return ResponseEntity.ok(shippingMethodService.getAllShippingMethod());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
//        }
//    }
}
