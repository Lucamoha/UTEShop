package com.uteshop.dao.impl.admin;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IUsersDao;
import com.uteshop.entity.auth.Users;

import jakarta.persistence.EntityManager;

public class UserDaoImpl extends AbstractDao<Users> implements IUsersDao {

	public UserDaoImpl() {
		super(Users.class);
	}

	@Override
	public long getNewCustomersThisMonth() {
		EntityManager em = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT COUNT(u) FROM Users u "
					+ "WHERE u.UserRole = 'USER' "
					+ "AND MONTH(u.CreatedAt) = MONTH(CURRENT_DATE) AND YEAR(u.CreatedAt) = YEAR(CURRENT_DATE)";
			return (Long) em.createQuery(jpql, Long.class).getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public long getTotalCustomers() {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
		
			String jpql = "SELECT COUNT(u) FROM Users u WHERE u.UserRole = 'USER'";
			return (Long)enma.createQuery(jpql, Long.class).getSingleResult();
		} finally {
			enma.close();
		}
		
	}

}
