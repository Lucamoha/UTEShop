package com.uteshop.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.uteshop.entity.branch.Branches;
import com.uteshop.services.admin.IBranchesService;
import com.uteshop.services.admin.IOrdersService;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.admin.IProductsVariantsService;
import com.uteshop.services.admin.IUsersService;
import com.uteshop.services.impl.admin.BranchesServiceImpl;
import com.uteshop.services.impl.admin.OrdersServiceImpl;
import com.uteshop.services.impl.admin.ProductVariantsServiceImpl;
import com.uteshop.services.impl.admin.ProductsServiceImpl;
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
	IBranchesService branchesService = new BranchesServiceImpl();
	Gson gson = new Gson();
	int currentYear = LocalDate.now().getYear();
	int currentMonth = LocalDate.now().getMonthValue();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int branchId = 0;// Tất cả chi nhánh
		List<Branches> branchList = branchesService.findAll();
		BigDecimal revenue = ordersService.getRevenueByYearAndMonthAndBranch(currentYear, currentMonth, branchId);
		long orderCount = ordersService.getOrdersByMonthAndBranch(currentMonth, branchId).size();
		long newCustomers = usersService.getNewCustomersByYearAndMonth(currentYear, currentMonth).size();
		long totalCustomers = usersService.getTotalCustomersByYearAndMonth(currentYear, currentMonth);
		long lowStock = productsVariantsService.getLowStockCount(10);
		List<Integer> years = new ArrayList<>();
		for (int y = currentYear; y >= currentYear - 20; y--) {
		    years.add(y);
		}

		req.setAttribute("revenue", revenue);
		req.setAttribute("orderCount", orderCount);
		req.setAttribute("newCustomers", newCustomers);
		req.setAttribute("totalCustomers", totalCustomers);
		req.setAttribute("lowStock", lowStock);
		req.setAttribute("branchList", branchList);
		req.setAttribute("branchId", branchId);
		req.setAttribute("years", years);//year để chọn trong select year

		Map<String, BigDecimal> monthlyRevenueMap = ordersService.getMonthlyRevenueByYearAndBranch(currentYear,
				branchId);
		req.setAttribute("revenueLabels", gson.toJson(new ArrayList<>(monthlyRevenueMap.keySet())));
		req.setAttribute("revenueValues", gson.toJson(new ArrayList<>(monthlyRevenueMap.values())));

		Map<String, BigDecimal> dailySalesMap = ordersService.getDailySalesByYearAndMonthAndBranch(currentYear,
				currentMonth, branchId);
		req.setAttribute("dailyLabels", gson.toJson(new ArrayList<>(dailySalesMap.keySet())));
		req.setAttribute("dailyValues", gson.toJson(new ArrayList<>(dailySalesMap.values())));
		req.setAttribute("selectedMonth", currentMonth);
		req.setAttribute("selectedYear", currentYear);

		List<Object[]> lowStockList = productsVariantsService.getLowStockProducts(100, 10);
		req.setAttribute("lowStockList", lowStockList);

		List<Object[]> topSellingList = productsService.getTopSellingProducts(10);
		req.setAttribute("topSellingList", topSellingList);

		req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String branchIdStr = req.getParameter("branchId");
		String month = req.getParameter("month");
		String year = req.getParameter("year");
		List<Integer> years = new ArrayList<>();
		for (int y = currentYear; y >= currentYear - 20; y--) {
		    years.add(y);
		}

		int branchId = 0;// Tất cả chi nhánh
		if (branchIdStr != null && branchIdStr.isBlank()) {
			branchId = Integer.parseInt(branchIdStr);
		}
		List<Branches> branchList = branchesService.findAll();
		BigDecimal revenue = ordersService.getRevenueByYearAndMonthAndBranch(Integer.parseInt(year),
				Integer.parseInt(month), branchId);
		long orderCount = ordersService.getOrdersByMonthAndBranch(Integer.parseInt(month), branchId).size();
		long newCustomers = usersService.getNewCustomersByYearAndMonth(Integer.parseInt(year), Integer.parseInt(month))
				.size();
		long totalCustomers = usersService.getTotalCustomersByYearAndMonth(Integer.parseInt(year),
				Integer.parseInt(month));
		long lowStock = productsVariantsService.getLowStockCount(10);

		req.setAttribute("revenue", revenue);
		req.setAttribute("orderCount", orderCount);
		req.setAttribute("newCustomers", newCustomers);
		req.setAttribute("totalCustomers", totalCustomers);
		req.setAttribute("lowStock", lowStock);
		req.setAttribute("branchList", branchList);
		req.setAttribute("branchId", branchId);
		req.setAttribute("years", years);//year để chọn trong select year

		Map<String, BigDecimal> monthlyRevenueMap = ordersService
				.getMonthlyRevenueByYearAndBranch(Integer.parseInt(year), branchId);
		req.setAttribute("revenueLabels", gson.toJson(new ArrayList<>(monthlyRevenueMap.keySet())));
		req.setAttribute("revenueValues", gson.toJson(new ArrayList<>(monthlyRevenueMap.values())));

		Map<String, BigDecimal> dailySalesMap = ordersService
				.getDailySalesByYearAndMonthAndBranch(Integer.parseInt(year), Integer.parseInt(month), branchId);
		req.setAttribute("dailyLabels", gson.toJson(new ArrayList<>(dailySalesMap.keySet())));
		req.setAttribute("dailyValues", gson.toJson(new ArrayList<>(dailySalesMap.values())));
		req.setAttribute("selectedMonth", Integer.parseInt(month));
		req.setAttribute("selectedYear", Integer.parseInt(year));

		List<Object[]> lowStockList = productsVariantsService.getLowStockProducts(100, 10);
		req.setAttribute("lowStockList", lowStockList);

		List<Object[]> topSellingList = productsService.getTopSellingProducts(10);
		req.setAttribute("topSellingList", topSellingList);

		req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
	}
}
