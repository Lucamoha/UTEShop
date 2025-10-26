package com.uteshop.services.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.UserDaoImpl;
import com.uteshop.entity.auth.Users;

public interface IUsersService {
	UserDaoImpl daoImpl = new UserDaoImpl();
	List<Users> findAll();
	Users findById(int id);
	List<Users> getManagerNotManagedBranch();
	void delete(int id);
	List<Users> getNewCustomersByYearAndMonth(int year, int month);
	long getTotalCustomersByYearAndMonth(int year, int month);
    void update(Users user);
}
