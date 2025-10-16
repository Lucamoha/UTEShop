package com.uteshop.services.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.uteshop.entity.cart.Vouchers;

public interface IVouchersService {
    /**
     * Validate và lấy thông tin voucher
     */
    Map<String, Object> validateVoucher(String code, BigDecimal subtotal);
    
    /**
     * Tìm voucher theo code
     */
    Vouchers findByCode(String code);
    
    /**
     * Tăng số lần sử dụng voucher
     */
    void incrementUsage(Integer voucherId);
    
    /**
     * Lấy danh sách vouchers còn hiệu lực
     */
    List<Vouchers> getAvailableVouchers();
}
