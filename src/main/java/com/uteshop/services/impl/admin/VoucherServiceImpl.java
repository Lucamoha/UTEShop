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
    public void save(Vouchers voucher) {
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher cannot be null");
        }
        if (voucher.getId() == null) {
            voucherDao.insert(voucher);
        } else {
            voucherDao.update(voucher);
        }
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Voucher ID cannot be null");
        }
        voucherDao.delete(id);
    }

    @Override
    public Vouchers getByCode(String code) {
        // Giả sử DAO có method này; nếu không, implement JPQL query ở đây hoặc thêm vào DAO
        // Để đơn giản, tôi giả sử sử dụng getAll() và filter (không hiệu quả cho prod, nhưng demo)
        return getAll().stream()
                .filter(v -> code.equals(v.getCode()))
                .findFirst()
                .orElse(null);
    }
}