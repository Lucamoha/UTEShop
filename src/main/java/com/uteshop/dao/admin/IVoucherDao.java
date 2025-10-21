package com.uteshop.dao.admin;

import com.uteshop.entity.cart.Vouchers;

import java.util.List;

public interface IVoucherDao {
    List<Vouchers> findAll();
    Vouchers findById(Integer id);
    void insert(Vouchers voucher);
    void update(Vouchers voucher);
    void delete(Integer id);
    // Có thể thêm findByCode nếu cần tối ưu
    Vouchers findByCode(String code);
}