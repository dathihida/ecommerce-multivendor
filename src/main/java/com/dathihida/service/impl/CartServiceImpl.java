package com.dathihida.service.impl;

import com.dathihida.model.Cart;
import com.dathihida.model.CartItem;
import com.dathihida.model.Product;
import com.dathihida.model.User;
import com.dathihida.repository.CartItemRepository;
import com.dathihida.repository.CartRepository;
import com.dathihida.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem existingItem = cartItemRepository.findByCartAndProductAndSize(cart, product, size);

        if (existingItem == null) {
            // Nếu chưa có, tạo mới
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);
            cartItem.setSellingPrice(quantity * product.getSellingPrice());
            cartItem.setMrpPrice(quantity * product.getMrpPrice());
            cartItem.setCart(cart);

            cart.getCartItems().add(cartItem);
            return cartItemRepository.save(cartItem);
        } else {
            // ✅ Nếu đã có, cộng dồn số lượng và cập nhật giá
            int newQuantity = existingItem.getQuantity() + quantity;
            existingItem.setQuantity(newQuantity);
            existingItem.setSellingPrice(newQuantity * product.getSellingPrice());
            existingItem.setMrpPrice(newQuantity * product.getMrpPrice());

            return cartItemRepository.save(existingItem);
        }
    }


    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice(); // tong tien gia goc
            totalDiscountedPrice += cartItem.getSellingPrice();// tong tien gia khi selling
            totalItem += cartItem.getQuantity(); // tong so luong san pham trong cartItem
        }

        cart.setTotalMrpPrice(totalPrice); // gia goc
        cart.setTotalItem(totalItem); // tong so luong san pham
        cart.setTotalSellingPrice(totalDiscountedPrice); // tong tien khi selling
//        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
        return cart;
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0 || sellingPrice >= mrpPrice) {
            return 0;
        }
        return (int) (((double) (mrpPrice - sellingPrice) / mrpPrice) * 100);
    }
}
