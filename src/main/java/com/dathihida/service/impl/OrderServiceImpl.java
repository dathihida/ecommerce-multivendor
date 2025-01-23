package com.dathihida.service.impl;

import com.dathihida.domain.OrderStatus;
import com.dathihida.domain.PaymentStatus;
import com.dathihida.model.*;
import com.dathihida.repository.AddressRepository;
import com.dathihida.repository.OrderItemRepository;
import com.dathihida.repository.OrderRepository;
import com.dathihida.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

        if(!user.getAddersses().contains(shippingAddress)) {
            user.getAddersses().add(shippingAddress);
        }
        Address address = addressRepository.save(shippingAddress);

        //sp1 => 4 shirt
        //sp2 => 1 pants
        //sp3 => 1 watch

        Map<Long, List<CartItem>> cartItems = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct()
                        .getSeller().getId()));

        Set<Order> orders = new HashSet<>();

        for(Map.Entry<Long, List<CartItem>> entry : cartItems.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> cartItemList = entry.getValue();

            int totalOrderPrice = cartItemList.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem = cartItemList.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems = new ArrayList<>();
            for(CartItem cartItem : cartItemList) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(cartItem.getMrpPrice());
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSize(cartItem.getSize());
                orderItem.setUserId(cartItem.getUserId());
                orderItem.setSellingPrice(cartItem.getSellingPrice());

                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(()-> new Exception("order npt found"));
    }

    @Override
    public List<Order> usersOrdersHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellerOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);
        if(user.getId().equals(order.getUser().getId())){
            throw new Exception("you don't have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id)
                .orElseThrow(()->new Exception("order item not exist ..."));
    }
}
