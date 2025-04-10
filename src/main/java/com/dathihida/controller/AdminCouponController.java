package com.dathihida.controller;

import com.dathihida.model.Cart;
import com.dathihida.model.Coupon;
import com.dathihida.model.User;
import com.dathihida.service.CartService;
import com.dathihida.service.CouponService;
import com.dathihida.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class AdminCouponController {
    private final CouponService couponService;
    private final UserService userService;
    private final CartService cartService;

    @PostMapping("/apply")
    public ResponseEntity<Cart> applyCoupon(@RequestParam String apply,
                                            @RequestParam String code,
                                            @RequestParam double orderValue,
                                            @RequestHeader("Authorization") String jwt) throws Exception {
        System.out.println("Apply: " + apply);
        System.out.println("Code: " + code);
        System.out.println("OrderValue: " + orderValue);
        System.out.println("JWT: " + jwt);
        User user = userService.findUserByJwtToken(jwt);
        Cart cart;

        if(apply.equals("true")){
            cart = couponService.applyCoupon(code, orderValue, user);
        }else{
            cart = couponService.removeCoupon(code, user);
        }
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupon);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) throws Exception {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        List<Coupon> coupons = couponService.findAllCoupons();
        return ResponseEntity.ok(coupons);
    }
}
