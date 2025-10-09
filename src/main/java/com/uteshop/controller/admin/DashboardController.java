package com.uteshop.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.uteshop.services.Product.IProductsService;
import com.uteshop.services.admin.IOrdersService;
import com.uteshop.services.admin.IProductsVariantsService;
import com.uteshop.services.admin.IUsersService;
import com.uteshop.services.impl.Product.ProductsServiceImpl;
import com.uteshop.services.impl.admin.OrdersServiceImpl;
import com.uteshop.services.impl.admin.ProductVariantsServiceImpl;
import com.uteshop.services.impl.admin.UsersServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/dashboard")
public class DashboardController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IOrdersService ordersService = new OrdersServiceImpl();
	IProductsService productsService = new ProductsServiceImpl();
	IUsersService usersService = new UsersServiceImpl();
	IProductsVariantsService productsVariantsService = new ProductVariantsServiceImpl();
	Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BigDecimal revenue = ordersService.getRevenueThisMonth();
		long orderCount = ordersService.getOrderCountThisMonth();
		long newCustomers = usersService.getNewCustomersThisMonth();
		long totalCustomers = usersService.getTotalCustomers();
		long lowStock = productsVariantsService.getLowStockCount(10);

		req.setAttribute("revenue", revenue);
		req.setAttribute("orderCount", orderCount);
		req.setAttribute("newCustomers", newCustomers);
		req.setAttribute("totalCustomers", totalCustomers);
		req.setAttribute("lowStock", lowStock);
		
		int currentYear = Year.now().getValue();
		int currentMonth = LocalDate.now().getMonthValue();
		Map<String, BigDecimal> monthlyRevenueMap = ordersService.getMonthlyRevenueByYear(currentYear);
		req.setAttribute("revenueLabels", gson.toJson(new ArrayList<>(monthlyRevenueMap.keySet())));
		req.setAttribute("revenueValues", gson.toJson(new ArrayList<>(monthlyRevenueMap.values())));

		Map<String, BigDecimal> dailySalesMap  = ordersService.getDailySalesOfMonth(currentYear, currentMonth);
		req.setAttribute("dailyLabels", gson.toJson(new ArrayList<>(dailySalesMap.keySet())));
		req.setAttribute("dailyValues", gson.toJson(new ArrayList<>(dailySalesMap.values())));
		req.setAttribute("currentMonth", currentMonth);
		
		req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
	}
}
