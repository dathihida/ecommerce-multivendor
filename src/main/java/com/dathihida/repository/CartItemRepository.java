package com.dathihida.repository;

import com.dathihida.model.Cart;
import com.dathihida.model.CartItem;
import com.dathihida.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
