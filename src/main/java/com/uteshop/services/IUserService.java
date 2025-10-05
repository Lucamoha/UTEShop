package com.uteshop.services;

import java.util.List;

import com.uteshop.dao.impl.User.UserDaoImpl;
import com.uteshop.entity.auth.Users;

public interface IUserService {
	UserDaoImpl daoImpl = new UserDaoImpl();
	List<Users> findAll();
	Users findById(int id);
	void delete(int id);
}
