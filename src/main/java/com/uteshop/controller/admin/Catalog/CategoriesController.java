package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.uteshop.entity.catalog.Categories;
import com.uteshop.services.admin.ICategoriesService;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.impl.admin.CategoriesServiceImpl;
import com.uteshop.services.impl.admin.ProductsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/Catalog/Categories/searchpaginated", "/admin/Catalog/Categories/saveOrUpdate",
		"/admin/Catalog/Categories/delete", "/admin/Catalog/Categories/view" })
public class CategoriesController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	ICategoriesService categoriesService = new CategoriesServiceImpl();

	IProductsService productsService = new ProductsServiceImpl();

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
			}

			// Tính offset (vị trí bắt đầu)
			int firstResult = (page - 1) * size;

			List<Categories> categoryList = categoriesService.findAll(false, firstResult, size,
					searchKeyword, "Name");

			// Đếm tổng số bản ghi để tính tổng trang
			int totalCategories = categoriesService.count(searchKeyword, "Name");
			int totalPages = (int) Math.ceil((double) totalCategories / size);

			req.setAttribute("categoryList", categoryList);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Catalog/Categories/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate")) {
			String id = req.getParameter("id");
			List<Categories> categoryList = categoriesService.findAll();
			if (id != null) {
				// dang o che do edit -> nguoc lai la add
				Categories category = categoriesService.findById(Integer.parseInt(id));
				req.setAttribute("category", category);
			}
			req.setAttribute("categoryList", categoryList);
			req.getRequestDispatcher("/views/admin/Catalog/Categories/addOrEdit.jsp").forward(req, resp);
		} else if (uri.contains("view")) {
			String id = req.getParameter("id");
			Categories category = categoriesService.findById(Integer.parseInt(id));
			req.setAttribute("category", category);
			req.getRequestDispatcher("/views/admin/Catalog/Categories/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			String id = req.getParameter("id");
			categoriesService.delete(Integer.parseInt(id));
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Categories/searchpaginated");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("saveOrUpdate")) {
			Categories category = new Categories();

			String idStr = req.getParameter("id");
			String name = req.getParameter("name");
			String slug = req.getParameter("slug");
			String parentIdStr = req.getParameter("parentId");

			// Nếu có id -> update
			Integer id = null;
			if (idStr != null && !idStr.isEmpty()) {
				id = Integer.parseInt(idStr);
				category = categoriesService.findById(id);
			}

			// Gán giá trị tạm thời để giữ lại form nếu lỗi
			category.setName(name);
			category.setSlug(slug);

			Categories parent = null;
			if (parentIdStr != null && !parentIdStr.isEmpty()) {
				int parentId = Integer.parseInt(parentIdStr);
				parent = categoriesService.findById(parentId);
				category.setParent(parent);
			}
			else {
				category.setParent(null);
			}

			// Kiểm tra slug trùng
			Categories existing = categoriesService.findBySlug(slug);
			if (existing != null && !Objects.equals(existing.getId(), id)) {
				req.setAttribute("error", "Slug đã tồn tại! Vui lòng nhập slug khác!");
			}

			// foward lại form nếu lỗi
			if (req.getAttribute("error") != null) {
				List<Categories> categoryList = categoriesService.findAll();
				req.setAttribute("categoryList", categoryList);
				req.setAttribute("category", category);
				req.getRequestDispatcher("/views/admin/Catalog/Categories/addOrEdit.jsp").forward(req, resp);
				return;
			}

			// Lưu danh mục vào db nếu không lỗi
			String message;
			if (idStr != null && !idStr.isEmpty()) {
				categoriesService.update(category);
				message = "Danh mục sửa thành công!";
			} else {
				categoriesService.insert(category);
				message = "Danh mục thêm thành công!";
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Categories/searchpaginated");
		}
	}
}
