package com.uteshop.services.impl.admin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.uteshop.dao.admin.IOrdersDao;
import com.uteshop.dao.impl.admin.OrdersDaoImpl;
import com.uteshop.entity.order.Orders;
import com.uteshop.services.admin.IOrdersService;



public class OrdersServiceImpl implements IOrdersService {

	IOrdersDao ordersDao = new OrdersDaoImpl();

	@Override
	public List<Orders> getOrdersByMonthAndBranch(int year, int month, int branchId) {
		return ordersDao.getOrdersByMonthAndBranch(year, month, branchId);
	}
	@Override
	public Map<String, BigDecimal> getMonthlyRevenueByYearAndBranch(int year, int branchId) {
		return ordersDao.getMonthlyRevenueByYearAndBranch(year, branchId);
	}
	@Override
	public Map<String, BigDecimal> getDailySalesByYearAndMonthAndBranch(int year, int month, int branchId) {
		return ordersDao.getDailySalesByYearAndMonthAndBranch(year, month, branchId);
	}
	@Override
	public BigDecimal getRevenueByYearAndMonthAndBranch(int year, int month, int branchId) {
		return ordersDao.getRevenueByYearAndMonthAndBranch(year, month, branchId);
	}
}

