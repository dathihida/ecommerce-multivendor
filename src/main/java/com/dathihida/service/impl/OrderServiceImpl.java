package com.dathihida.service.impl;

import com.dathihida.domain.OrderStatus;
import com.dathihida.domain.PaymentStatus;
import com.dathihida.model.*;
import com.dathihida.repository.*;
import com.dathihida.service.OrderService;
import jakarta.transaction.Transactional;
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
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

        Optional<Address> existingAddress = addressRepository
                .findAddressByNameAndLocalityAndAddressAndCityAndZipAndPinCodeAndMobileAndAccountStatus
                        (shippingAddress.getName(), shippingAddress.getLocality(), shippingAddress.getAddress(),
                         shippingAddress.getCity(), shippingAddress.getZip(), shippingAddress.getPinCode(),
                         shippingAddress.getMobile(), shippingAddress.getAccountStatus());
        Address addressToUser = existingAddress.orElseGet(()-> addressRepository.save(shippingAddress));
        if(!user.getAddersses().contains(addressToUser)) {
            user.getAddersses().add(addressToUser);
        }

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

            int totalOrderPrice = cartItemList.stream().mapToInt(CartItem::getMrpPrice).sum();
            int totalOrderPriceSelling = cartItemList.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem = cartItemList.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPriceSelling);
            createdOrder.setShippingAddress(addressToUser);
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

        // xoa cartItem
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        // xet lai gia tri trong cart
        cart.setCouponCode(null);
        cart.setDiscount(0);
        cart.setTotalItem(0);
        cart.setTotalMrpPrice(0);
        cart.setTotalSellingPrice(0);
        cartRepository.save(cart);
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
