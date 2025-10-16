package com.uteshop.services.web;

import com.uteshop.entity.auth.Addresses;
import java.util.List;

public interface IAddressesService {
    List<Addresses> getUserAddresses(Integer userId);
    Addresses getDefaultAddress(Integer userId);
    Addresses getAddressById(Integer id);
}
