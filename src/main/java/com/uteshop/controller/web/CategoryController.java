package com.uteshop.controller.web;

import com.uteshop.dto.web.FilterOptionsDto;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		// Lấy danh sách category IDs
		List<Integer> categoryIds = new ArrayList<>();
		if (parts.length == 1) {
			// Lấy ID danh mục cha + danh mục con
			categoryIds.add(selectedParent.getId());
			if (childCategories != null) {
				for (Categories c : childCategories) {
					categoryIds.add(c.getId());
				}
			}
		} else {
			// Danh mục con cụ thể
			categoryIds.add(selectedCategory.getId());
		}

		// ===== XỬ LÝ SEARCH & FILTER =====
		String keyword = request.getParameter("keyword");
		String sortBy = request.getParameter("sortBy");
		String minPriceParam = request.getParameter("minPrice");
		String maxPriceParam = request.getParameter("maxPrice");

		BigDecimal minPrice = null;
		BigDecimal maxPrice = null;
		try {
			if (minPriceParam != null && !minPriceParam.trim().isEmpty()) {
				minPrice = new BigDecimal(minPriceParam);
			}
			if (maxPriceParam != null && !maxPriceParam.trim().isEmpty()) {
				maxPrice = new BigDecimal(maxPriceParam);
			}
		} catch (NumberFormatException e) {
			// Ignore invalid price format
		}
		
		// Parse attribute filters from request
		// Format: attr_<attributeId>=<value> for TEXT, NUMBER, and BOOLEAN types
		// Now supports multiple values: attr_<attributeId>=<value1>&attr_<attributeId>=<value2>
		Map<Integer, Object> attributeFilters = new HashMap<>();
		Map<String, String[]> paramMap = request.getParameterMap();
		
		for (String paramName : paramMap.keySet()) {
			if (paramName.startsWith("attr_")) {
				String[] attrParts = paramName.split("_");
				if (attrParts.length == 2) {
					try {
						Integer attrId = Integer.parseInt(attrParts[1]);
						String[] values = request.getParameterValues(paramName);
						
						if (values != null && values.length > 0) {
							// Filter out empty values
							List<String> validValues = new ArrayList<>();
							for (String val : values) {
								if (val != null && !val.trim().isEmpty()) {
									validValues.add(val.trim());
								}
							}
							
							if (!validValues.isEmpty()) {
								// Store as List<String> for multiple values
								attributeFilters.put(attrId, validValues);
								System.out.println("DEBUG Controller - AttrId: " + attrId + ", Values: " + validValues);
							}
						}
					} catch (NumberFormatException e) {
						// Ignore invalid attribute filter
					}
				}
			}
		}

		// Load sản phẩm với search & filter
		List<Products> products;
		long totalProducts;

		if (keyword != null || minPrice != null || maxPrice != null || sortBy != null || !attributeFilters.isEmpty()) {
			// Có filter/search
			products = productsService.searchAndFilter(categoryIds, keyword, null, 
			                                            minPrice, maxPrice, sortBy, attributeFilters, page, pageSize);
			totalProducts = productsService.countSearchAndFilter(categoryIds, keyword, 
			                                                       null, minPrice, maxPrice, attributeFilters);
		} else {
			// Không có filter
			products = productsService.findByCategoryIds(categoryIds, page, pageSize);
			totalProducts = productsService.countByCategoryIds(categoryIds);
		}

		int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

		// Load filter options
		FilterOptionsDto filterOptions = productsService.getFilterOptions(categoryIds);

		request.setAttribute("selectedParent", selectedParent);
		request.setAttribute("selectedCategory", selectedCategory);
		request.setAttribute("childCategories", childCategories);
		request.setAttribute("products", products);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("filterOptions", filterOptions);
		
		// Giữ lại filter params
		request.setAttribute("currentKeyword", keyword);
		request.setAttribute("currentSortBy", sortBy);
		request.setAttribute("currentMinPrice", minPrice);
		request.setAttribute("currentMaxPrice", maxPrice);
		request.setAttribute("currentAttributeFilters", attributeFilters);

		request.getRequestDispatcher("/views/web/category.jsp").forward(request, response);
	}
}