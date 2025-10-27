package com.uteshop.dao.impl.admin;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

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
	public long getTotalCustomersByYearAndMonth(int year, int month) {
		// Tạo mốc thời gian đầu tháng kế tiếp
		LocalDateTime beforeDate = YearMonth.of(year, month)
		    .plusMonths(1)
		    .atDay(1)
		    .atStartOfDay();
		
		EntityManager enma = JPAConfigs.getEntityManager();
		try {

			String jpql = "SELECT COUNT(u) FROM Users u WHERE u.UserRole = 'USER'" + "AND u.CreatedAt < :beforeDate";
			return (Long) enma.createQuery(jpql, Long.class).setParameter("beforeDate", beforeDate).getSingleResult();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Users> getManagerNotManagedBranch() {
		EntityManager enma = JPAConfigs.getEntityManager();
		String jpql = """
					SELECT u
					FROM Users u
					WHERE u.UserRole = 'MANAGER'
					AND u.isActive = true
					AND u.Id NOT IN  (
						SELECT b.manager.Id
						FROM Branches b
						WHERE b.manager IS NOT NULL
					)
				""";
		return enma.createQuery(jpql, Users.class).getResultList();
	}

	@Override
	public List<Users> getNewCustomersByYearAndMonth(int year, int month) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = "SELECT u FROM Users u " + "WHERE u.UserRole = 'USER' " + "AND MONTH(u.CreatedAt) = "
					+ month + " AND YEAR(u.CreatedAt) = " + year;
			return enma.createQuery(jpql, Users.class).getResultList();
		} finally {
			enma.close();
		}
	}
}
