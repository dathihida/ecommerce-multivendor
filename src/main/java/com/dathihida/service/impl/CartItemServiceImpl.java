package com.dathihida.service.impl;

import com.dathihida.model.Cart;
import com.dathihida.model.CartItem;
import com.dathihida.model.User;
import com.dathihida.repository.CartItemRepository;
import com.dathihida.repository.CartRepository;
import com.dathihida.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;


    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item = findCartItemById(id);
        User cartItemUser = item.getCart().getUser();

        if(cartItemUser.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());

            CartItem updatedItem = cartItemRepository.save(item);

            Cart cart = item.getCart();
            int totalMrp = 0;
            int totalSelling = 0;
            int totalItem = 0;

            for (CartItem ci : cart.getCartItems()) {
                totalMrp += ci.getMrpPrice();
                totalSelling += ci.getSellingPrice();
                totalItem += ci.getQuantity();
            }

            cart.setTotalMrpPrice(totalMrp);
            cart.setTotalSellingPrice(totalSelling);
            cart.setTotalItem(totalItem);
//            cart.setDiscount(calculateDiscountPercentage(totalMrp, totalSelling));

            cartRepository.save(cart);

            return updatedItem;
        }

        throw new Exception("you can't update this cartItem");
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice <= 0 || sellingPrice >= mrpPrice) {
            return 0;
        }
        return (int) (((double) (mrpPrice - sellingPrice) / mrpPrice) * 100);
    }


    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem item = findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();

        if(cartItemUser.getId().equals(userId)){
            cartItemRepository.delete(item);
        }else throw new Exception("you can't delete this item");

    }

    @Override
    public CartItem findCartItemById(Long userId) throws Exception {

        return cartItemRepository.findById(userId).orElseThrow(
                ()->new Exception("cart item not found with id"+userId));
    }
}
