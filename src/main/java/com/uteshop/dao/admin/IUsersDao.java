package com.uteshop.dao.admin;

import java.util.List;

import com.uteshop.entity.auth.Users;

public interface IUsersDao{
	long getNewCustomersThisMonth();
	long getTotalCustomers();
	List<Users> getManagerNotManagedBranch();
}
