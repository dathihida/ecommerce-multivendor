package com.dathihida.controller;

import com.dathihida.model.Cart;
import com.dathihida.model.CartItem;
import com.dathihida.model.Product;
import com.dathihida.model.User;
import com.dathihida.repository.CartRepository;
import com.dathihida.request.AddItemRequest;
import com.dathihida.response.ApiResponse;
import com.dathihida.service.CartItemService;
import com.dathihida.service.CartService;
import com.dathihida.service.ProductService;
import com.dathihida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemCart(@RequestBody AddItemRequest request,
                                                @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(request.getProductId());
        CartItem item = cartService.addCartItem(user, product, request.getSize(), request.getQuantity());
        ApiResponse response = new ApiResponse();
        response.setMessage("Item added to cart successfully");

        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId,
                                                             @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);
        ApiResponse response = new ApiResponse();
        response.setMessage("Item deleted from cart successfully");
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        CartItem updateCartItem =null;
        if (cartItem.getQuantity() > 0) {
            updateCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }
        return new ResponseEntity<>(updateCartItem,HttpStatus.ACCEPTED);
    }

}
