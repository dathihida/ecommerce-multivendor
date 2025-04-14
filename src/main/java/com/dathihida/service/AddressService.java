package com.dathihida.service;

import com.dathihida.model.Address;
import com.dathihida.model.User;
import com.dathihida.request.CreateAddressRequest;

public interface AddressService {
    Address addAddress(CreateAddressRequest request, User user);
    void deleteAddress(Long id);
}
