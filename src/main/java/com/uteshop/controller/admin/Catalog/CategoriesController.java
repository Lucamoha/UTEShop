package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.CategoryAttributes;
import com.uteshop.entity.catalog.CategoryAttributes.Id;
import com.uteshop.services.admin.IAttributesService;
import com.uteshop.services.admin.ICategoriesService;
import com.uteshop.services.admin.ICategoryAttributeService;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.impl.admin.AttributesServiceImpl;
import com.uteshop.services.impl.admin.CategoriesServiceImpl;
import com.uteshop.services.impl.admin.CategoryAttributeServiceImpl;
import com.uteshop.services.impl.admin.ProductsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/Catalog/Categories/searchpaginated", "/admin/Catalog/Categories/saveOrUpdate",
		"/admin/Catalog/Categories/delete", "/admin/Catalog/Categories/view",
		"/admin/Catalog/Categories/saveOrUpdateCategoryAttribute" })
public class CategoriesController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	ICategoriesService categoriesService = new CategoriesServiceImpl();
	IProductsService productsService = new ProductsServiceImpl();
	ICategoryAttributeService categoryAttributeService = new CategoryAttributeServiceImpl();
	IAttributesService attributeService = new AttributesServiceImpl();

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

			List<Categories> categoryList = categoriesService.findAllFetchColumns(false, firstResult, size,
					searchKeyword, "Name",
					new ArrayList<>(List.of("parent", "categoryAttributes", "categoryAttributes.attribute")));

			// Đếm tổng số bản ghi để tính tổng trang
			int totalCategories = categoriesService.count(searchKeyword, "Name");
			int totalPages = (int) Math.ceil((double) totalCategories / size);

			req.setAttribute("categoryList", categoryList);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Catalog/Categories/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate") && !uri.contains("CategoryAttribute")) {
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
			int page = 1;
			int size = 6;

			if (req.getParameter("page") != null) {
				page = Integer.parseInt(req.getParameter("page"));
			}
			if (req.getParameter("size") != null) {
				size = Integer.parseInt(req.getParameter("size"));
			}

			String searchKeyword = "";

			// Tính offset (vị trí bắt đầu)
			int firstResult = (page - 1) * size;

			String id = req.getParameter("id");
			Categories category = categoriesService.findByIdFetchColumns(Integer.parseInt(id), firstResult, size,
					new ArrayList<>(List.of("parent", "categoryAttributes", "categoryAttributes.attribute")));

			// Đếm tổng số bản ghi để tính tổng trang
			int totalCategories = categoriesService.count(searchKeyword, "Name");
			int totalPages = (int) Math.ceil((double) totalCategories / size);

			req.setAttribute("category", category);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);
			req.getRequestDispatcher("/views/admin/Catalog/Categories/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			String id = req.getParameter("id");
			categoriesService.delete(Integer.parseInt(id));
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Categories/searchpaginated");
		} else if (uri.contains("saveOrUpdateCategoryAttribute")) {
			String categoryId = req.getParameter("categoryId");
			String attributeId = req.getParameter("attributeId");
			List<Attributes> attributeList = attributeService.findAll();
			if (attributeId != null) {
				// dang o che do edit -> nguoc lai la add
				try {
					Id id = new Id();
					id.setAttributeId(Integer.parseInt(attributeId));
					id.setCategoryId(Integer.parseInt(categoryId));
					CategoryAttributes categoryAttribute = categoryAttributeService.findByIdFetchColumns(id,
							List.of("category", "attribute"));
					if (categoryAttribute != null) {
						req.setAttribute("categoryAttribute", categoryAttribute);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else {
				req.setAttribute("categoryId", categoryId);// khi add
			}
			req.setAttribute("attributeList", attributeList);
			req.getRequestDispatcher("/views/admin/Catalog/Categories/categoryAttributesAddOrEdit.jsp").forward(req,
					resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("saveOrUpdate") && !uri.contains("CategoryAttribute")) {
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
			} else {
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
		} else if (uri.contains("saveOrUpdateCategoryAttribute")) {
			String attributeId = req.getParameter("attributeId");
			String isFilterable = req.getParameter("isFilterable");
			String isComparable = req.getParameter("isComparable");
			String categoryId = req.getParameter("categoryId");

			CategoryAttributes categoryAttribute = null;
			Id id = new Id(Integer.parseInt(categoryId), Integer.parseInt(attributeId));

			// Nếu có attributeId -> update
			if (attributeId != null && !attributeId.isEmpty()) {
				categoryAttribute = categoryAttributeService.findById(id);
			}

			if (categoryAttribute == null) {
				categoryAttribute = new CategoryAttributes();
			}

			// Gán ID tổng hợp
			categoryAttribute.setId(id);
			// Fetch entity thật để set vào quan hệ
			categoryAttribute.setCategory(categoriesService.findById(Integer.parseInt(categoryId)));
			categoryAttribute.setAttribute(attributeService.findById(Integer.parseInt(attributeId)));

			categoryAttribute.setIsComparable(Boolean.parseBoolean(isComparable));
			categoryAttribute.setIsFilterable(Boolean.parseBoolean(isFilterable));

			// Kiểm tra attribute trùng
			CategoryAttributes existing = categoryAttributeService
					.findByCategoryIdAndAttributeId(Integer.parseInt(categoryId), Integer.parseInt(attributeId));
			if (existing != null && !Objects.equals(existing.getId(), id)) {
				req.setAttribute("error",
						"Thông số kỹ thuật theo danh mục đã tồn tại! Vui lòng chọn thông số kỹ thuật khác!");
			}

			// foward lại form nếu lỗi
			if (req.getAttribute("error") != null) {
				List<Attributes> attributeList = attributeService.findAll();
				req.setAttribute("attributeList", attributeList);
				req.setAttribute("categoryAttribute", categoryAttribute);
				req.setAttribute("categoryId", categoryId);
				req.getRequestDispatcher("/views/admin/Catalog/Categories/categoryAttributesAddOrEdit.jsp").forward(req,
						resp);
				return;
			}

			// Lưu vào db
			String message;
			if (attributeId != null && !attributeId.isEmpty()) {
				categoryAttributeService.update(categoryAttribute);
				message = "Thông số danh mục sửa thành công!";
			} else {
				categoryAttributeService.insert(categoryAttribute);
				message = "Thông số danh mục thêm thành công!";
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Categories/view?id=" + categoryId);
		}
	}
}
