package com.uteshop.services.impl;

import java.util.List;

import com.uteshop.dao.impl.User.UserDaoImpl;
import com.uteshop.entity.auth.Users;
import com.uteshop.services.IUserService;

public class UserServiceImpl implements IUserService{

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

}
