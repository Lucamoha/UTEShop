package com.uteshop.services.web;

import com.uteshop.entity.auth.Users;

public interface IUsersService {
    boolean checkDuplicate(String email, String phone);

    Users findByEmail(String email);

    void insertUser(Users user);

    void update(Users user);
}
