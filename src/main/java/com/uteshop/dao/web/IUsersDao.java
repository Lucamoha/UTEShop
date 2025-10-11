package com.uteshop.dao.web;

import com.uteshop.entity.auth.Users;

public interface IUsersDao {
    boolean checkDuplicate(String email, String phone);
    Users getUserByEmail(String email);
}
