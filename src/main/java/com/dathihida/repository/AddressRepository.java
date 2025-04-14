package com.dathihida.repository;

import com.dathihida.domain.AccountStatus;
import com.dathihida.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByNameAndLocalityAndAddressAndCityAndZipAndPinCodeAndMobileAndAccountStatus(String name, String locality, String address, String city, String zip, String pinCode, String mobile, AccountStatus accountStatus);
}
