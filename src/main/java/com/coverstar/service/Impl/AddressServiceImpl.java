package com.coverstar.service.Impl;

import com.coverstar.dto.AddressDto;
import com.coverstar.entity.Address;
import com.coverstar.repository.AddressRepository;
import com.coverstar.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address createOrUpdateAddress(AddressDto addressDto) {
        try {
            Address address = new Address();
            if (addressDto.getId() != null) {
                address = addressRepository.findById(addressDto.getId()).orElse(null);
                if (address != null) {
                    address.setUpdatedDate(new Date());
                }
            } else {
                address.setCreatedDate(new Date());
                address.setUpdatedDate(new Date());
            }
            address.setAddress(addressDto.getAddress());
            address.setFullName(addressDto.getFullName());
            address.setPhoneNumber(addressDto.getPhoneNumber());
            address.setUserId(addressDto.getUserId());
            address.setDefault(addressDto.isDefault());
            address.setProvinceId(addressDto.getProvinceId());
            address.setDistrictId(addressDto.getDistrictId());
            address.setWardId(addressDto.getWardId());
            address.setType(addressDto.getType());
            address.setMap(addressDto.getMap());
            return addressRepository.save(address);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteAddress(Long id) {
        try {
            addressRepository.deleteById(id);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Address getAddressById(Long id) {
        try {
            return addressRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Address getAddressByUserId(Long userId) {
        try {
            return addressRepository.findByUserId(userId);
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @Override
    public Address updateDefaultAddress(Long id, boolean isDefault) {
        try {
            Address address = addressRepository.findById(id).orElse(null);
            assert address != null;
            address.setDefault(isDefault);
            addressRepository.save(address);
            return address;
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }
}
