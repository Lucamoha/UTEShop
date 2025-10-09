package com.uteshop.services.impl.admin;

import java.util.List;

import com.uteshop.dao.impl.admin.UserDaoImpl;
import com.uteshop.entity.auth.Users;
import com.uteshop.services.admin.IUsersService;

public class UsersServiceImpl implements IUsersService {

	UserDaoImpl userDaoImpl = new UserDaoImpl();
	@Override
	public List<Users> findAll() {
		return userDaoImpl.findAll();
	}
	@Override
	public Users findById(int id)
	{
		return userDaoImpl.findById(id);
	}
	@Override
	public void delete(int id)
	{
		userDaoImpl.delete(id);
	}
	@Override
	public long getNewCustomersThisMonth() {
		return userDaoImpl.getNewCustomersThisMonth();
	}
	@Override
	public long getTotalCustomers() {
		return userDaoImpl.getTotalCustomers();
	}

}
