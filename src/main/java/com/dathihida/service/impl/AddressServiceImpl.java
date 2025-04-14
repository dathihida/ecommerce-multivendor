package com.dathihida.service.impl;

import com.dathihida.domain.AccountStatus;
import com.dathihida.model.Address;
import com.dathihida.model.User;
import com.dathihida.repository.AddressRepository;
import com.dathihida.repository.UserRepository;
import com.dathihida.request.CreateAddressRequest;
import com.dathihida.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    @Override
    public Address addAddress(CreateAddressRequest request, User user) {
        Address address = new Address();
        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setPinCode(request.getPinCode());
        address.setMobile(request.getMobile());
        address.setLocality(request.getLocality());
        address.setAccountStatus(AccountStatus.ACTIVE);
        address.setZip(request.getZip());
        address.setName(request.getName());

        boolean alreadyExists = user.getAddersses().stream().anyMatch(addr -> addr.equals(address));
        if (!alreadyExists) {
            user.getAddersses().add(address);
            addressRepository.save(address);
        }
        return address;
    }


    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
