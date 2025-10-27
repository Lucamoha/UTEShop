package com.uteshop.dao.admin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.uteshop.entity.order.Orders;

public interface IOrdersDao {

	BigDecimal getRevenueByYearAndMonthAndBranch(int year, int month, int branchId);
	List<Orders> getOrdersByMonthAndBranch(int year, int month, int branchId);
	Map<String, BigDecimal> getMonthlyRevenueByYearAndBranch(int year, int branchId);
	Map<String, BigDecimal> getDailySalesByYearAndMonthAndBranch(int year, int month, int branchId);
}
