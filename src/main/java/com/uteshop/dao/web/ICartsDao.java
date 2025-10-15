package com.uteshop.dao.web;

import com.uteshop.entity.cart.CartItems;
import com.uteshop.entity.cart.Carts;

import java.util.List;

public interface ICartsDao {
    Carts getOrCreateCartByUserId(Integer userId);
    List<CartItems> getCartItemsByUserId(Integer userId);
    CartItems findCartItem(Integer cartId, Integer productId, Integer variantId);
    void addCartItem(CartItems cartItem);
    void updateCartItem(CartItems cartItem);
    void removeCartItem(Integer itemId);
    void clearCart(Integer userId);
    int getCartItemCount(Integer userId);
    Integer findVariantByOptions(Integer productId, List<Integer> optionValueIds);
}
