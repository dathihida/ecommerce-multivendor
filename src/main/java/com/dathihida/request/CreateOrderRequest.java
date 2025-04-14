package com.dathihida.request;

import com.dathihida.domain.PaymentMethod;
import com.dathihida.model.Address;
import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long addressId;
    private Address address;
    private PaymentMethod paymentMethod;
}
