package com.uteshop.controller.web;

import java.io.IOException;
import java.util.List;

import com.uteshop.entity.catalog.Categories;
import com.uteshop.services.Category.ICategoriesService;
import com.uteshop.services.impl.Category.CategoriesServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/category/*" })
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ICategoriesService categoriesService = new CategoriesServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// load menu cha cho header
		List<Categories> parents = categoriesService.findParents();
		request.setAttribute("parentCategories", parents);

		String path = request.getPathInfo(); // "/iphone" hoặc "/iphone/12"
		if (path == null || path.equals("/")) {
			request.getRequestDispatcher("/views/web/home.jsp").forward(request, response);
			return;
		}

		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		String[] parts = path.substring(1).split("/");
		String parentSlug = parts[parts.length - 1];

		Categories parent = categoriesService.findBySlug(parentSlug);
		if (parent == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// children có thể lấy từ parent.getChildren() hoặc gọi dao riêng
		List<Categories> childCategories = parent.getChildren();
		request.setAttribute("selectedParent", parent);
		request.setAttribute("childCategories", childCategories);

		request.getRequestDispatcher("/views/web/category.jsp").forward(request, response);
	}
}
