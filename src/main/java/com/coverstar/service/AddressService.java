package com.coverstar.service;

import com.coverstar.dto.AddressDto;
import com.coverstar.entity.Address;

public interface AddressService {
    Address createOrUpdateAddress(AddressDto addressDto);

    void deleteAddress(Long id);

    Address getAddressById(Long id);

    Address getAddressByUserId(Long userId);

    Address updateDefaultAddress(Long id, boolean isDefault);
}
