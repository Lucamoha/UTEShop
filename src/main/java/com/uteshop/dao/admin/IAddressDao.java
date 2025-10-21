package com.uteshop.dao.admin;

import com.uteshop.entity.auth.Addresses;

import java.util.List;

public interface IAddressDao
{
    List<Addresses> findByUserId(Integer userId);
    List<Addresses> findByRole(String role);
}
