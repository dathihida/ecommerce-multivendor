package com.dathihida.service;

import com.dathihida.model.Product;
import com.dathihida.model.User;
import com.dathihida.model.Wishlist;

public interface WishlistService {
    Wishlist createWishlist(User user);
    Wishlist getWishlistByUserId(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
