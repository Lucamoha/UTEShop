package com.uteshop.dao.impl.admin;

import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IVoucherDao;
import com.uteshop.entity.cart.Vouchers;
import java.util.List;

public class VoucherDaoImpl extends AbstractDao<Vouchers> implements IVoucherDao {
    public VoucherDaoImpl() {
        super(Vouchers.class);
    }

    @Override
    public List<Vouchers> findAll() {
        return super.findAll();
    }

    @Override
    public Vouchers findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public void insert(Vouchers voucher) {
        super.insert(voucher);
    }

    @Override
    public void update(Vouchers voucher) {
        super.update(voucher);
    }

    @Override
    public void delete(Integer id) {
        super.delete(id);
    }
    @Override
    public Vouchers findByCode(String code) {
        List<Vouchers> vouchers = super.findByColumnHasExactWord("Code", code);
        if(vouchers.isEmpty()) {
        	return null;
        }
        return vouchers.get(0);
    }
}