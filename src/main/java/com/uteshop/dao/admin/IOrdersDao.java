package com.uteshop.dao.admin;

import java.math.BigDecimal;
import java.util.Map;

public interface IOrdersDao {

	BigDecimal getRevenueThisMonth();
	long getOrderCountThisMonth();
	Map<String, BigDecimal> getMonthlyRevenueByYear(int year);
	Map<String, BigDecimal> getDailySalesOfMonth(int year, int month);
}
