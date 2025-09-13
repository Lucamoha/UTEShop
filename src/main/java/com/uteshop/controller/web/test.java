package com.uteshop.controller.web;

import java.io.IOException;
import java.util.List;

import com.uteshop.entities.Products;
import com.uteshop.entities.SystemSettings;
import com.uteshop.services.IAttributesService;
import com.uteshop.services.IProductsService;
import com.uteshop.services.impl.AttributesServiceImpl;
import com.uteshop.services.impl.ProductsServiceImpl;
import com.uteshop.services.impl.SystemSettingServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/test"})
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * ISystemSettingsService sysService = new SystemSettingServiceImpl();
		 * List<SystemSettings> listSys = sysService.findAll();
		 */
		
		IProductsService sysService = new ProductsServiceImpl();
		List<Products> listSys = sysService.findAll();
		
		req.setAttribute("listSys", listSys);
		RequestDispatcher dispatcher = req.getRequestDispatcher("list-test.jsp");
		dispatcher.forward(req, resp);
	}
}
