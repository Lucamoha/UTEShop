package com.uteshop.controller.web;

import java.io.IOException;
import java.util.List;

import com.uteshop.entities.Products;
import com.uteshop.services.IProductsService;
import com.uteshop.services.impl.ProductsServiceImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/login"})
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * ISystemSettingsService sysService = new SystemSettingServiceImpl();
		 * List<SystemSettings> listSys = sysService.findAll();
		 */

		req.getRequestDispatcher("/views/web/login.jsp").forward(req,resp);
	}
}
