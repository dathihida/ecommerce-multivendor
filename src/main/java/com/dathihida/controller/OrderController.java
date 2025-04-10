package com.dathihida.controller;

import com.dathihida.domain.PaymentMethod;
import com.dathihida.model.*;
import com.dathihida.repository.PaymentOrderRepository;
import com.dathihida.response.PaymentLinkResponse;
import com.dathihida.service.*;
import com.razorpay.PaymentLink;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final PaymentService paymentService;
    private final PaymentOrderRepository paymentOrderRepository;

    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);

        PaymentOrder paymentOrder = paymentService.createOrder(user, orders);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink paymentLink = paymentService.createRazorpayPaymentLink(user, paymentOrder.getAmount(), paymentOrder.getId());
            String paymentUrl = paymentLink.get("short_url");
            String paymentUrlId = paymentLink.get("id");

            paymentLinkResponse.setPayment_link_url(paymentUrl);
            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(paymentOrder);

        }else
            if(paymentMethod.equals((PaymentMethod.STRIPE) )){
            String paymentUrl = paymentService.createStripePaymentLink(
                    user, paymentOrder.getAmount(), paymentOrder.getId());
            paymentLinkResponse.setPayment_link_url(paymentUrl);
        }else
            if(paymentMethod.equals((PaymentMethod.VNPAY) )){
                // truyen vao 2 thu
                // amount: 10000 * 100;
                // language: "vn"

            String paymentUrl = paymentService.createVNPaymentLink(
                    user, paymentOrder.getAmount(), paymentOrder.getId());
            paymentLinkResponse.setPayment_link_url(paymentUrl);
        }

        return new ResponseEntity<>(paymentLinkResponse, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.usersOrdersHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId,
                                                      @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.cancelOrder(orderId, user);

        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport report = sellerReportService.getSellerReport(seller);

        report.setCanceledOrders(report.getCanceledOrders() + 1);
        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);

        return ResponseEntity.ok(order);
    }

}
