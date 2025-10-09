package com.uteshop.services.impl.admin;

import java.math.BigDecimal;
import java.util.Map;

import com.uteshop.dao.admin.IOrdersDao;
import com.uteshop.dao.impl.admin.OrdersDaoImpl;
import com.uteshop.services.admin.IOrdersService;



public class OrdersServiceImpl implements IOrdersService {

	IOrdersDao ordersDao = new OrdersDaoImpl();
	
	@Override
	public BigDecimal getRevenueThisMonth() {
		return ordersDao.getRevenueThisMonth();
	}

	@Override
	public long getOrderCountThisMonth() {
		return ordersDao.getOrderCountThisMonth();
	}

	@Override
	public Map<String, BigDecimal> getMonthlyRevenueByYear(int year) {
		return ordersDao.getMonthlyRevenueByYear(year);
	}

	@Override
	public Map<String, BigDecimal> getDailySalesOfMonth(int year, int month) {
		return ordersDao.getDailySalesOfMonth(year, month);
	}

}
