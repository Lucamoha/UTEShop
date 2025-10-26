package com.uteshop.services.impl.admin;

import com.uteshop.dao.admin.IVoucherDao;
import com.uteshop.dao.impl.admin.VoucherDaoImpl;
import com.uteshop.entity.cart.Vouchers;
import com.uteshop.services.admin.IVoucherService;

import java.util.List;

public class VoucherServiceImpl implements IVoucherService {
    private final IVoucherDao voucherDao = new VoucherDaoImpl();

    @Override
    public List<Vouchers> getAll() {
        return voucherDao.findAll();
    }

    @Override
    public Vouchers getById(Integer id) {
        return voucherDao.findById(id);
    }

    @Override
    public void insert(Vouchers voucher) {
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher không thể null");
        }
        voucherDao.insert(voucher);
    }
    
    @Override
    public void update(Vouchers voucher) {
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher không thể null");
        }
        voucherDao.update(voucher);
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Voucher ID không thể null");
        }
        voucherDao.delete(id);
    }

    @Override
    public Vouchers getByCode(String code) {
        return voucherDao.findByCode(code);
    }
}