package com.uteshop.services.impl.web;

import com.uteshop.dao.impl.web.AddressesDaoImpl;
import com.uteshop.dao.web.IAddressesDao;
import com.uteshop.entity.auth.Addresses;
import com.uteshop.services.web.IAddressesService;

import java.util.List;

public class AddressesServiceImpl implements IAddressesService {
    
    private final IAddressesDao addressesDao = new AddressesDaoImpl();
    
    @Override
    public List<Addresses> getUserAddresses(Integer userId) {
        if (userId == null) {
            return List.of();
        }
        return addressesDao.findByUserId(userId);
    }
    
    @Override
    public Addresses getDefaultAddress(Integer userId) {
        if (userId == null) {
            return null;
        }
        return addressesDao.findDefaultAddress(userId);
    }
    
    @Override
    public Addresses getAddressById(Integer id) {
        if (id == null) {
            return null;
        }
        return addressesDao.findById(id);
    }
}
