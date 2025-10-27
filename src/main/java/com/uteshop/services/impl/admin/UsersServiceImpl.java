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
    public void update(Users user) {
        userDaoImpl.update(user);
    }

    @Override
	public List<Users> getManagerNotManagedBranch() {
		return userDaoImpl.getManagerNotManagedBranch();
	}
	@Override
	public List<Users> getNewCustomersByYearAndMonth(int year, int month) {
		return userDaoImpl.getNewCustomersByYearAndMonth(year, month);
	}
	@Override
	public long getTotalCustomersByYearAndMonth(int year, int month) {
		return userDaoImpl.getTotalCustomersByYearAndMonth(year, month);
	}

}
