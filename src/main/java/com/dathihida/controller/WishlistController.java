package com.dathihida.controller;

import com.dathihida.model.Product;
import com.dathihida.model.User;
import com.dathihida.model.Wishlist;
import com.dathihida.service.ProductService;
import com.dathihida.service.UserService;
import com.dathihida.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    private ResponseEntity<Wishlist> wishlist(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(@PathVariable Long productId,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.addProductToWishlist(user, product);
        return ResponseEntity.ok(wishlist);
    }
}
