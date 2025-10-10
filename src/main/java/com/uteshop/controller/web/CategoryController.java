package com.uteshop.controller.web;

import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.impl.web.CategoriesServiceImpl;
import com.uteshop.services.impl.web.ProductsServiceImpl;
import com.uteshop.services.web.ICategoriesService;
import com.uteshop.services.web.IProductsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = { "/category/*" })
public class CategoryController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ICategoriesService categoriesService = new CategoriesServiceImpl();
	private final IProductsService productsService = new ProductsServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Load menu cha cho header
		List<Categories> parents = categoriesService.findParents();
		request.setAttribute("parentCategories", parents);

		String path = request.getPathInfo();
		if (path == null || path.equals("/")) {
			request.getRequestDispatcher("/views/web/home.jsp").forward(request, response);
			return;
		}

		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		String[] parts = path.substring(1).split("/");

		Categories selectedCategory = null;
		Categories selectedParent = null;
		List<Categories> childCategories = null;

		// Phân trang
		int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
		int pageSize = 12; // Số sản phẩm/trang

		if (parts.length == 1) {
			// Chỉ có parent-slug
			String parentSlug = parts[0];
			selectedCategory = categoriesService.findBySlug(parentSlug);
			if (selectedCategory == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			selectedParent = selectedCategory;
			childCategories = selectedCategory.getChildren();
		} else if (parts.length == 2) {
			// Có parent-slug/child-slug
			String parentSlug = parts[0];
			String childSlug = parts[1];
			selectedParent = categoriesService.findBySlug(parentSlug);
			if (selectedParent == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			selectedCategory = categoriesService.findBySlug(childSlug);
			if (selectedCategory == null || selectedCategory.getParent() == null
					|| selectedCategory.getParent().getId() != selectedParent.getId()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			childCategories = selectedParent.getChildren();
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Load sản phẩm theo category
		// "Tất cả" — nếu đang ở danh mục cha, thì lấy luôn sản phẩm của
		// các danh mục con
		List<Products> products;
		long totalProducts;
		if (parts.length == 1) {
			// Lấy ID danh mục cha + danh mục con
			List<Integer> categoryIds = new ArrayList<>();
			categoryIds.add(selectedParent.getId());
			if (childCategories != null) {
				for (Categories c : childCategories) {
					categoryIds.add(c.getId());
				}
			}

			products = productsService.findByCategoryIds(categoryIds, page, pageSize);
			totalProducts = productsService.countByCategoryIds(categoryIds);

		} else {
			// Danh mục con cụ thể
			products = productsService.findByCategoryId(selectedCategory.getId(), page, pageSize);
			totalProducts = productsService.countByCategoryId(selectedCategory.getId());
		}

		int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

		request.setAttribute("selectedParent", selectedParent);
		request.setAttribute("selectedCategory", selectedCategory);
		request.setAttribute("childCategories", childCategories);
		request.setAttribute("products", products);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);

		request.getRequestDispatcher("/views/web/category.jsp").forward(request, response);
	}
}