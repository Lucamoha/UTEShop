package com.uteshop.controller.admin;

import java.io.IOException;
import java.util.List;

import com.uteshop.entities.Products;
import com.uteshop.services.IProductsService;
import com.uteshop.services.impl.ProductsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/home", "/admin/category/list" })
public class AdminHomeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		if (uri.contains("admin/home")) {
			req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
		} else if (uri.contains("admin/category/list")) {
			IProductsService sysService = new ProductsServiceImpl();
			List<Products> listSys = sysService.findAll();
			
			req.setAttribute("listSys", listSys);
			req.getRequestDispatcher("/views/admin/category/list.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
}
