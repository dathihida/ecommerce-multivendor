package com.dathihida.service;

import com.dathihida.model.Order;
import com.dathihida.model.PaymentOrder;
import com.dathihida.model.User;
import com.razorpay.PaymentLink;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(String orderId);
    PaymentOrder getPaymentOrderByPaymentId(String orderId);
    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,
                                String paymentId,
                                String paymentLinkId);
    PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId);
    String createStripePaymentLink(User user, Long amount, Long orderId);
}
