package com.uteshop.services.impl.web.order;

import com.uteshop.dao.impl.web.order.VouchersDaoImpl;
import com.uteshop.dao.web.order.IVouchersDao;
import com.uteshop.entity.cart.Vouchers;
import com.uteshop.services.admin.IVoucherService;
import com.uteshop.services.web.order.IVouchersService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VouchersServiceImpl implements IVouchersService {
    
    private final IVouchersDao vouchersDao = new VouchersDaoImpl();

    @Override
    public Map<String, Object> validateVoucher(String code, BigDecimal subtotal) {
        Map<String, Object> result = new HashMap<>();
        
        if (code == null || code.trim().isEmpty()) {
            result.put("valid", false);
            result.put("message", "Vui lòng nhập mã voucher");
            return result;
        }

        Vouchers voucher = vouchersDao.findByCode(code.trim());
        
        if (voucher == null) {
            result.put("valid", false);
            result.put("message", "Mã voucher không tồn tại");
            return result;
        }

        // Kiểm tra voucher có active không
        if (!voucher.getIsActive()) {
            result.put("valid", false);
            result.put("message", "Mã voucher đã bị vô hiệu hóa");
            return result;
        }

        // Kiểm tra thời gian
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(voucher.getStartsAt())) {
            result.put("valid", false);
            result.put("message", "Mã voucher chưa có hiệu lực");
            return result;
        }

        if (now.isAfter(voucher.getEndsAt())) {
            result.put("valid", false);
            result.put("message", "Mã voucher đã hết hạn");
            return result;
        }

        // Kiểm tra số lần sử dụng
        if (voucher.getTotalUsed() >= voucher.getMaxUses()) {
            result.put("valid", false);
            result.put("message", "Mã voucher đã hết lượt sử dụng");
            return result;
        }

        // Tính discount amount
        BigDecimal discountAmount = voucher.calculateDiscount(subtotal);
        
        result.put("valid", true);
        result.put("voucher", voucher);
        result.put("voucherCode", voucher.getCode());
        result.put("voucherType", voucher.getType());
        result.put("voucherValue", voucher.getValue());
        result.put("discountAmount", discountAmount);
        result.put("typeName", voucher.getTypeName());
        result.put("message", "Áp dụng voucher thành công! " + voucher.getTypeName());
        
        return result;
    }
    @Override
    public Vouchers findByCode(String code) {
        return vouchersDao.findByCode(code);
    }

    @Override
    public void incrementUsage(Integer voucherId) {
        vouchersDao.incrementUsage(voucherId);
    }

    @Override
    public List<Vouchers> getAvailableVouchers() {
        return vouchersDao.findAvailableVouchers();
    }

}
