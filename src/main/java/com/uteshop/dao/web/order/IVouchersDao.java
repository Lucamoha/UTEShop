package com.uteshop.dao.web.order;

import java.util.List;

import com.uteshop.entity.cart.Vouchers;

public interface IVouchersDao {
    /**
     * Tìm voucher theo code
     */
    Vouchers findByCode(String code);
    
    /**
     * Tăng số lần sử dụng voucher
     */
    void incrementUsage(Integer voucherId);
    
    /**
     * Lấy tất cả vouchers còn hiệu lực (active, chưa hết hạn, còn lượt dùng)
     */
    List<Vouchers> findAvailableVouchers();
}
