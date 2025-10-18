package com.uteshop.services.web;

import com.uteshop.entity.auth.Addresses;
import java.util.List;

public interface IAddressesService {
    List<Addresses> getUserAddresses(Integer userId);
    Addresses getDefaultAddress(Integer userId);
    Addresses getAddressById(Integer id);
    void addAddress(Addresses address);
    void updateAddress(Addresses address);
    void deleteAddress(Integer id, Integer userId);
    void setDefaultAddress(Integer addressId, Integer userId);
    boolean isDuplicateAddress(Integer userId, String fullName, String phone, String addressLine, String ward, String district, String city, Integer excludeAddressId);
}
