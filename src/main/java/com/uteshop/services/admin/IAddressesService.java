package com.uteshop.services.admin;

import com.uteshop.entity.auth.Addresses;

import java.util.List;

public interface IAddressesService {
    List<Addresses> getAll();
    Addresses getById(Integer id);
    void save(Addresses address);
    void delete(Integer id);
    List<Addresses> getByUserId(Integer userId);

    List<Addresses> findByRole(String role);
}
