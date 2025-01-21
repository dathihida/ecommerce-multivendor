package com.dathihida.service;

import com.dathihida.model.Cart;
import com.dathihida.model.CartItem;
import com.dathihida.model.Product;
import com.dathihida.model.User;

public interface CartService {
    public CartItem addCartItem(User user,
                                Product product,
                                String size,
                                int quantity);
    public Cart findUserCart(User user);
}
