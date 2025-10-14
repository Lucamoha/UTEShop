package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.admin.ICategoriesService;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.impl.admin.CategoriesServiceImpl;
import com.uteshop.services.impl.admin.ProductsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/Catalog/Products/searchpaginated", "/admin/Catalog/Products/saveOrUpdate",
		"/admin/Catalog/Products/delete", "/admin/Catalog/Products/view" })
public class ProductController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private IProductsService productsService = new ProductsServiceImpl();
	private ICategoriesService categoriesService = new CategoriesServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("searchpaginated")) {

			int page = 1;
			int size = 6;

			if (req.getParameter("page") != null) {
				page = Integer.parseInt(req.getParameter("page"));
			}
			if (req.getParameter("size") != null) {
				size = Integer.parseInt(req.getParameter("size"));
			}

			String searchKeyword = req.getParameter("searchKeyword");
			if (searchKeyword != null) {
				searchKeyword = searchKeyword.trim();
				// if(searchKeyword.isEmpty()) searchKeyword = null;
			}

			// Tính offset (vị trí bắt đầu)
			int firstResult = (page - 1) * size;

			List<Products> productList = productsService.findAll(false, firstResult, size, searchKeyword, "Name");

			// Đếm tổng số bản ghi để tính tổng trang
			// List<Products> allProducts = productsService.findAll(true, 0, 0);
			// int totalProducts = allProducts.size();
			int totalProducts = productsService.count(searchKeyword, "Name");
			int totalPages = (int) Math.ceil((double) totalProducts / size);

			req.setAttribute("productList", productList);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Catalog/Products/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate")) {
			String id = req.getParameter("id");
			List<Categories> categoryList = categoriesService.findAll();
			if (id != null) {
				// dang o che do edit -> nguoc lai la add
				Products product = productsService.findById(Integer.parseInt(id));
				req.setAttribute("product", product);
			}
			req.setAttribute("categoryList", categoryList);
			req.getRequestDispatcher("/views/admin/Catalog/Products/addOrEdit.jsp").forward(req, resp);
		} else if (uri.contains("view")) {
			String id = req.getParameter("id");
			Products product = productsService.findById(Integer.parseInt(id));
			req.setAttribute("product", product);
			req.getRequestDispatcher("/views/admin/Catalog/Products/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			String id = req.getParameter("id");
			productsService.delete(Integer.parseInt(id));
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("saveOrUpdate")) {
			Products product = new Products();

			String idStr = req.getParameter("id");
			String name = req.getParameter("name");
			String slug = req.getParameter("slug");
			String description = req.getParameter("description");
			String basePriceStr = req.getParameter("basePrice");
			String status = req.getParameter("status");
			String categoryIdStr = req.getParameter("categoryId");

			// Nếu có id -> update
			Integer id = null;
			if (idStr != null && !idStr.isEmpty()) {
				id = Integer.parseInt(idStr);
				product = productsService.findById(id);
			}

			// Gán giá trị tạm thời để giữ lại form nếu lỗi
			product.setName(name);
			product.setSlug(slug);
			product.setDescription(description);
			product.setStatus("true".equals(status));

			if (basePriceStr != null && !basePriceStr.isEmpty()) {
				try {
					product.setBasePrice(new BigDecimal(basePriceStr));
				} catch (NumberFormatException e) {
					req.setAttribute("error", "Giá tiền không hợp lệ!");
				}
			}

			Categories category = null;
			try {
				if(categoryIdStr != null && !categoryIdStr.isEmpty()) {
					int categoryId = Integer.parseInt(categoryIdStr);
					category = categoriesService.findById(categoryId);
					product.setCategory(category);
				}
				else{
					req.setAttribute("error", "Vui lòng chọn danh mục!");
				}
				
			} catch (Exception e) {
				req.setAttribute("error", "Danh mục không hợp lệ!");
			}

			// Kiểm tra slug trùng
			Products existing = productsService.findBySlug(slug);
			if (existing != null && !Objects.equals(existing.getId(), id)) {
				req.setAttribute("error", "Slug đã tồn tại! Vui lòng nhập slug khác!");
			}

			// foward lại form nếu lỗi
			if (req.getAttribute("error") != null) {
				List<Categories> categoryList = categoriesService.findAll();
				req.setAttribute("categoryList", categoryList);
				req.setAttribute("product", product);
				req.getRequestDispatcher("/views/admin/Catalog/Products/addOrEdit.jsp").forward(req, resp);
				return;
			}

			//Lưu sản phẩm vào db nếu không lỗi
			String message;
			if (idStr != null && !idStr.isEmpty()) {
				productsService.update(product);
				message = "Product is Edited!";
			} else {
				productsService.insert(product);
				message = "Product is Saved!";
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Products/searchpaginated");
		}
	}
}
