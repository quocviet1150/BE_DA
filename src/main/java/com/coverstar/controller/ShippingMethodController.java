package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.dto.ShippingMethodDto;
import com.coverstar.entity.ShippingMethod;
import com.coverstar.service.ShippingMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shipping-methods")
public class ShippingMethodController {

    @Autowired
    private ShippingMethodService shippingMethodService;

    @GetMapping("/getAllShippingMethod")
    public ResponseEntity<?> getAllShippingMethod(@RequestParam(value = "name", required = false) String name) {
        try {
            List<ShippingMethod> shippingMethods = shippingMethodService.getAllShippingMethod(name);
            return ResponseEntity.ok(shippingMethods);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid ShippingMethodDto shippingMethodDto) {
        try {
            ShippingMethod shippingMethod = shippingMethodService.createOrUpdate(shippingMethodDto);
            return ResponseEntity.ok(shippingMethod);
        } catch (Exception e) {
            if (e.getMessage().equals(Constants.SHIPPING_METHOD_NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.SHIPPING_METHOD_NOT_FOUND);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }
}
