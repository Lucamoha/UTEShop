package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.UserDaoImpl;
import com.uteshop.entity.auth.Users;

public interface IUsersService {
	UserDaoImpl daoImpl = new UserDaoImpl();
	List<Users> findAll();
	Users findById(int id);
	void delete(int id);
	long getNewCustomersThisMonth();
	long getTotalCustomers();
}
