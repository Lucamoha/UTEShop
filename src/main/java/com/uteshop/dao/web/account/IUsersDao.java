package com.uteshop.dao.web.account;

import com.uteshop.entity.auth.Users;

public interface IUsersDao {
    boolean checkDuplicate(String email, String phone);
    Users getUserByEmail(String email);
    Users getUserByPhone(String phone);
}
