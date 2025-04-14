package com.dathihida.request;

import com.dathihida.domain.AccountStatus;
import lombok.Data;

@Data
public class CreateAddressRequest {
    private Long id;

    private String name;

    private String locality;

    private String address;

    private String city;

    private String zip;

    private String pinCode;

    private String mobile;

    private AccountStatus accountStatus;
}
