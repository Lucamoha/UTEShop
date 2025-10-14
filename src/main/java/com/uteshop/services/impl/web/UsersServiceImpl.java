package com.uteshop.services.impl.web;

import com.uteshop.dao.impl.web.UserDaoImpl;
import com.uteshop.entity.auth.Users;
import com.uteshop.services.web.IUsersService;

public class UsersServiceImpl implements IUsersService {
    UserDaoImpl userDaoImpl = new UserDaoImpl();

    @Override
    public boolean checkDuplicate(String email, String phone) {
        return userDaoImpl.checkDuplicate(email, phone);
    }

    @Override
    public Users findByEmail(String email) {
        return userDaoImpl.getUserByEmail(email);
    }

    @Override
    public void insertUser(Users user) {
        userDaoImpl.insert(user);
    }
}
