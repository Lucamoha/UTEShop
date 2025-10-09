package com.uteshop.services.admin;

import java.math.BigDecimal;
import java.util.Map;

public interface IOrdersService {

	BigDecimal getRevenueThisMonth();
	long getOrderCountThisMonth();
	Map<String, BigDecimal> getMonthlyRevenueByYear(int year);
	Map<String, BigDecimal> getDailySalesOfMonth(int year, int month);
}
