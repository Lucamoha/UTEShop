package com.uteshop.dao.web;

import com.uteshop.entity.auth.Addresses;
import java.util.List;

public interface IAddressesDao {
    List<Addresses> findByUserId(Integer userId);
    Addresses findDefaultAddress(Integer userId);
    Addresses findById(Integer id);
}
