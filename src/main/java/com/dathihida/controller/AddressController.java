package com.dathihida.controller;

import com.dathihida.domain.PaymentMethod;
import com.dathihida.model.Address;
import com.dathihida.model.User;
import com.dathihida.request.CreateAddressRequest;
import com.dathihida.response.ApiResponse;
import com.dathihida.service.AddressService;
import com.dathihida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController {
    private final AddressService addressService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody CreateAddressRequest shippingAddress,
                                              @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Address saveAddress = addressService.addAddress(shippingAddress, user);
        return new ResponseEntity<>(saveAddress, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable("id") long id) {
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
