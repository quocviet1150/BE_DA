package com.coverstar.controller;

import com.coverstar.constant.Constants;
import com.coverstar.dto.AddressDto;
import com.coverstar.entity.Address;
import com.coverstar.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/createOrUpdateAddress")
    public ResponseEntity<?> createOrUpdateAddress(@RequestBody @Valid AddressDto addressDto) {
        try {
            Address address = addressService.createOrUpdateAddress(addressDto);
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("/deleteAddress/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok("Address deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getAddressById/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Long id) {
        try {
            Address address = addressService.getAddressById(id);
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @GetMapping("/getAddressByUserId/{userId}")
    public ResponseEntity<?> getAddressByUserId(@PathVariable Long userId) {
        try {
            Address address = addressService.getAddressByUserId(userId);
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }

    @PostMapping("updateDefaultAddress/{id}")
    public ResponseEntity<?> updateDefaultAddress(@PathVariable Long id,
                                                  @RequestParam("isDefault") boolean isDefault) {
        try {
            Address address = addressService.updateDefaultAddress(id, isDefault);
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR);
        }
    }
}
