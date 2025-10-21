package com.uteshop.services.admin;

import com.uteshop.entity.cart.Vouchers;

import java.util.List;

public interface IVoucherService {
    List<Vouchers> getAll();
    Vouchers getById(Integer id);
    void save(Vouchers voucher);
    void delete(Integer id);
    Vouchers getByCode(String code); // Thêm để kiểm tra uniqueness
}
