package com.uteshop.services.web;

import com.uteshop.entity.cart.CartItems;

import java.util.List;
import java.util.Map;

public interface ICartsService {
    /**
     * Thêm sản phẩm vào giỏ hàng
     * @param userId ID người dùng
     * @param productId ID sản phẩm
     * @param optionValueIds Danh sách ID của option values (color, storage...)
     * @param quantity Số lượng
     * @return true nếu thành công
     */
    boolean addToCart(Integer userId, Integer productId, List<Integer> optionValueIds, int quantity);
    
    /**
     * Lấy danh sách cart items của user
     */
    List<CartItems> getCartItems(Integer userId);
    
    /**
     * Cập nhật số lượng cart item
     */
    boolean updateCartItemQuantity(Integer userId, Integer itemId, int quantity);
    
    /**
     * Xóa cart item
     */
    boolean removeCartItem(Integer userId, Integer itemId);
    
    /**
     * Xóa toàn bộ giỏ hàng
     */
    void clearCart(Integer userId);
    
    /**
     * Lấy số lượng items trong giỏ hàng
     */
    int getCartItemCount(Integer userId);
    
    /**
     * Tính tổng tiền giỏ hàng
     */
    Map<String, Object> calculateCartTotal(Integer userId);
    
    /**
     * Tính tổng tiền giỏ hàng với voucher
     * @param userId ID người dùng
     * @param voucherCode Mã voucher (nullable)
     * @return Map chứa thông tin giỏ hàng và discount
     */
    Map<String, Object> calculateCartTotalWithVoucher(Integer userId, String voucherCode);
    
    /**
     * Kiểm tra và xóa các cart items có variant.status = false
     * @param userId ID người dùng
     * @return List chứa thông tin các sản phẩm bị xóa (tên sản phẩm và SKU)
     */
    List<java.util.Map<String, String>> validateAndRemoveInactiveItems(Integer userId);
}
