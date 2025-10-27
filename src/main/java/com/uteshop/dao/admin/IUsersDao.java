package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.auth.Users;

public interface IUsersDao{
	List<Users> getNewCustomersByYearAndMonth(int year, int month);
	long getTotalCustomersByYearAndMonth(int year, int month);
	List<Users> getManagerNotManagedBranch();
}
