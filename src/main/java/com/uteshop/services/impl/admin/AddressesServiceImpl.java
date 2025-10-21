package com.uteshop.services.impl.admin;

import com.uteshop.dao.impl.admin.AddressDaoImpl;
import com.uteshop.entity.auth.Addresses;
import com.uteshop.services.admin.IAddressesService;

import java.util.List;

public class AddressesServiceImpl implements IAddressesService {

    private AddressDaoImpl addressesDao = new AddressDaoImpl();

    @Override
    public List<Addresses> getAll() {
        return addressesDao.findAll();
    }

    @Override
    public Addresses getById(Integer id) {
        return addressesDao.findById(id);
    }

    @Override
    public void save(Addresses address) {
        addressesDao.save(address);
    }

    @Override
    public void delete(Integer id) {
        addressesDao.delete(id);
    }

    @Override
    public List<Addresses> getByUserId(Integer userId) {
        return addressesDao.findByUserId(userId);
    }
    @Override
    public List<Addresses> findByRole(String role)
    {
        return addressesDao.findByRole(role);
    }
}