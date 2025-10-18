package com.uteshop.services.impl.web;

import com.uteshop.dao.impl.web.CartsDaoImpl;
import com.uteshop.dao.web.ICartsDao;
import com.uteshop.entity.cart.CartItems;
import com.uteshop.entity.cart.Carts;
import com.uteshop.entity.catalog.ProductVariants;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.web.ICartsService;
import com.uteshop.services.web.IVouchersService;

import jakarta.persistence.EntityManager;
import com.uteshop.configs.JPAConfigs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartsServiceImpl implements ICartsService {
    
    private final ICartsDao cartsDao = new CartsDaoImpl();
    private final IVouchersService vouchersService = new VouchersServiceImpl();

    @Override
    public boolean addToCart(Integer userId, Integer productId, List<Integer> optionValueIds, int quantity) {
        try {
            // 1. Tìm variant dựa vào options
            Integer variantId = cartsDao.findVariantByOptions(productId, optionValueIds);
            if (variantId == null) {
                System.err.println("Không tìm thấy variant với options: " + optionValueIds);
                return false;
            }
            
            // 2. Lấy hoặc tạo cart
            Carts cart = cartsDao.getOrCreateCartByUserId(userId);
            if (cart == null) {
                return false;
            }
            
            // 3. Kiểm tra xem item đã có trong cart chưa
            CartItems existingItem = cartsDao.findCartItem(cart.getId(), productId, variantId);
            
            EntityManager em = JPAConfigs.getEntityManager();
            try {
                Products product = em.find(Products.class, productId);
                ProductVariants variant = em.find(ProductVariants.class, variantId);
                
                if (product == null || variant == null) {
                    return false;
                }
                
                if (existingItem != null) {
                    // Cập nhật số lượng
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                    cartsDao.updateCartItem(existingItem);
                } else {
                    // Tạo mới cart item
                    CartItems newItem = new CartItems();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setVariant(variant);
                    newItem.setQuantity(quantity);
                    cartsDao.addCartItem(newItem);
                }
                
                return true;
            } finally {
                em.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CartItems> getCartItems(Integer userId) {
        return cartsDao.getCartItemsByUserId(userId);
    }

    @Override
    public boolean updateCartItemQuantity(Integer userId, Integer itemId, int quantity) {
        try {
            List<CartItems> userItems = cartsDao.getCartItemsByUserId(userId);
            CartItems item = userItems.stream()
                    .filter(ci -> ci.getId().equals(itemId))
                    .findFirst()
                    .orElse(null);
            
            if (item == null) {
                return false;
            }
            
            if (quantity <= 0) {
                cartsDao.removeCartItem(itemId);
            } else {
                item.setQuantity(quantity);
                cartsDao.updateCartItem(item);
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeCartItem(Integer userId, Integer itemId) {
        try {
            List<CartItems> userItems = cartsDao.getCartItemsByUserId(userId);
            boolean exists = userItems.stream()
                    .anyMatch(ci -> ci.getId().equals(itemId));
            
            if (!exists) {
                return false;
            }
            
            cartsDao.removeCartItem(itemId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void clearCart(Integer userId) {
        cartsDao.clearCart(userId);
    }

    @Override
    public int getCartItemCount(Integer userId) {
        return cartsDao.getCartItemCount(userId);
    }

    @Override
    public Map<String, Object> calculateCartTotal(Integer userId) {
        List<CartItems> items = cartsDao.getCartItemsByUserId(userId);
        
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalItems = 0;
        
        for (CartItems item : items) {
            BigDecimal price = item.getVariant() != null 
                    ? item.getVariant().getPrice() 
                    : item.getProduct().getBasePrice();
            
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(itemTotal);
            totalItems += item.getQuantity();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("subtotal", subtotal);
        result.put("totalItems", totalItems);
        result.put("itemCount", items.size());
        
        return result;
    }

    @Override
    public Map<String, Object> calculateCartTotalWithVoucher(Integer userId, String voucherCode) {
        // Get base cart calculation
        Map<String, Object> result = calculateCartTotal(userId);
        BigDecimal subtotal = (BigDecimal) result.get("subtotal");
        
        // Initialize voucher-related fields
        result.put("voucherApplied", false);
        result.put("voucherCode", null);
        result.put("discountAmount", BigDecimal.ZERO);
        result.put("total", subtotal);
        
        // Validate và apply voucher nếu có
        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            Map<String, Object> voucherResult = vouchersService.validateVoucher(voucherCode, subtotal);
            
            if ((Boolean) voucherResult.get("valid")) {
                BigDecimal discountAmount = (BigDecimal) voucherResult.get("discountAmount");
                BigDecimal total = subtotal.subtract(discountAmount);
                
                // Không cho total < 0
                if (total.compareTo(BigDecimal.ZERO) < 0) {
                    total = BigDecimal.ZERO;
                }
                
                result.put("voucherApplied", true);
                result.put("voucherCode", voucherCode);
                result.put("voucherType", voucherResult.get("voucherType"));
                result.put("voucherValue", voucherResult.get("voucherValue"));
                result.put("discountAmount", discountAmount);
                result.put("total", total);
                result.put("voucherMessage", voucherResult.get("message"));
                result.put("typeName", voucherResult.get("typeName"));
            } else {
                // Voucher không hợp lệ
                result.put("voucherError", voucherResult.get("message"));
                result.put("total", subtotal);
            }
        } else {
            result.put("total", subtotal);
        }
        
        return result;
    }

    @Override
    public List<java.util.Map<String, String>> validateAndRemoveInactiveItems(Integer userId) {
        return cartsDao.removeInactiveVariantItems(userId);
    }
}
