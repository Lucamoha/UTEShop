package com.uteshop.dao.impl.admin;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IOrdersDao;
import com.uteshop.entity.order.Orders;

import jakarta.persistence.EntityManager;

public class OrdersDaoImpl extends AbstractDao<Orders> implements IOrdersDao {
	public OrdersDaoImpl() {
		super(Orders.class);
	}

	@Override
	public BigDecimal getRevenueByYearAndMonthAndBranch(int year, int month, int branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = """
					SELECT COALESCE(SUM(o.TotalAmount), 0)
					FROM Orders o
					WHERE o.PaymentStatus = 1
					AND MONTH(o.UpdatedAt) = :month AND YEAR(o.UpdatedAt) = :year""";// PaymentStatus: Đơn hàng đã
																						// thanh toán
			if (branchId != 0) {
				jpql += "WHERE o.Branches.Id = :branchId";
				return (BigDecimal) enma.createQuery(jpql, BigDecimal.class).setParameter("BranchId", branchId)
						.setParameter("month", month).setParameter("year", year).getSingleResult();
			}
			return (BigDecimal) enma.createQuery(jpql, BigDecimal.class).setParameter("month", month)
					.setParameter("year", year).getSingleResult();
		} finally {
			enma.close();
		}
	}

	@Override
	public List<Orders> getOrdersByMonthAndBranch(int year, int month, int branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		try {
			String jpql = """
					SELECT o
					FROM Orders o
					WHERE MONTH(o.UpdatedAt) = :month
					AND YEAR(o.UpdatedAt) = :year
					AND o.PaymentStatus = 1
					""";
			if (branchId != 0) {
				jpql += "WHERE o.Branches.Id = :branchId";
				return enma.createQuery(jpql, Orders.class).setParameter("year", year).setParameter("month", month)
						.setParameter("BranchId", branchId).getResultList();
			}
			return enma.createQuery(jpql, Orders.class).setParameter("year", year).setParameter("month", month)
					.getResultList();
		} finally {
			enma.close();
		}
	}

	@Override
	public Map<String, BigDecimal> getMonthlyRevenueByYearAndBranch(int year, int branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		Map<String, BigDecimal> result = new LinkedHashMap<>();
		try {
			String sql = """
					SELECT  MONTH(o.UpdatedAt) AS MonthNum,
					DATENAME(MONTH, o.UpdatedAt) AS MonthLabel,
					SUM(o.TotalAmount) AS Revenue
					FROM Orders o
					WHERE YEAR(o.UpdatedAt) = :year
					AND o.PaymentStatus = 1
					GROUP BY MONTH(o.UpdatedAt), DATENAME(MONTH, o.UpdatedAt)
					ORDER BY MONTH(o.UpdatedAt)

					""";
			List<Object[]> rows = null;
			if (branchId != 0) {
				sql += "WHERE o.BranchId = :branchId";//
				rows = enma.createNativeQuery(sql).setParameter("year", year).setParameter("branchId", branchId)
						.getResultList();
			} else {
				rows = enma.createNativeQuery(sql).setParameter("year", year).getResultList();
			}

			for (int i = 1; i <= 12; i++) {
				result.put(String.format("%02d", i), BigDecimal.ZERO);
			}

			for (Object[] row : rows) {
				int month = ((Number) row[0]).intValue();
				BigDecimal revenue = (BigDecimal) row[2];
				result.put(String.format("%02d", month), revenue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			enma.close();
		}
		return result;
	}

	@Override
	public Map<String, BigDecimal> getDailySalesByYearAndMonthAndBranch(int year, int month, int branchId) {
		EntityManager enma = JPAConfigs.getEntityManager();
		Map<String, BigDecimal> result = new LinkedHashMap<>();
		try {
			String sql = """
					SELECT DAY(o.UpdatedAt) AS DayNum, SUM(o.TotalAmount) AS Revenue
					FROM Orders o
					WHERE YEAR(o.UpdatedAt) = :year
					AND MONTH (o.UpdatedAt) = :month
					AND o.PaymentStatus = 1
					GROUP BY DAY(o.UpdatedAt)
					""";
			List<Object[]> rows = null;
			if (branchId != 0) {
				sql += "WHERE o.BranchId = :branchId";//
				rows = enma.createNativeQuery(sql).setParameter("year", year).setParameter("month", month)
						.setParameter("branchId", branchId).getResultList();
			} else {
				rows = enma.createNativeQuery(sql).setParameter("year", year).setParameter("month", month)
						.getResultList();
			}

			YearMonth yearMonth = YearMonth.of(year, month);
			int daysInMonth = yearMonth.lengthOfMonth();
			for (int i = 1; i <= daysInMonth; i++) {
				result.put(String.format("%02d", i), BigDecimal.ZERO);
			}

			for (Object[] row : rows) {
				String day = String.format("%02d", ((Number) row[0]).intValue());
				BigDecimal revenue = (BigDecimal) row[1];
				result.put(day, revenue);
			}

			return result;

		} finally {
			enma.close();
		}
	}
}
