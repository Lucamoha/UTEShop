package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.uteshop.entity.catalog.Attributes;
import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.services.admin.IAttributesService;
import com.uteshop.services.admin.IOptionTypesService;
import com.uteshop.services.impl.admin.AttributesServiceImpl;
import com.uteshop.services.impl.admin.OptionTypesServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/Catalog/Attributes/searchpaginated", "/admin/Catalog/Attributes/saveOrUpdate",
		"/admin/Catalog/Attributes/delete", "/admin/Catalog/Attributes/view" })
public class AttributesController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	IAttributesService attributesService = new AttributesServiceImpl();

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

			List<Attributes> attributeList = attributesService.findAll(false, firstResult, size, searchKeyword,
					"Name");

			// Đếm tổng số bản ghi để tính tổng trang
			int totalAttributes = attributesService.count(searchKeyword, "Name");
			int totalPages = (int) Math.ceil((double) totalAttributes / size);

			req.setAttribute("attributeList", attributeList);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Catalog/Attributes/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate")) {
			String id = req.getParameter("id");
			if (id != null) {
				// dang o che do edit -> nguoc lai la add
				Attributes attribute = attributesService.findById(Integer.parseInt(id));
				req.setAttribute("attribute", attribute);
			}
			req.getRequestDispatcher("/views/admin/Catalog/Attributes/addOrEdit.jsp").forward(req, resp);
		} else if (uri.contains("view")) {
			String id = req.getParameter("id");
			Attributes attribute = attributesService.findById(Integer.parseInt(id));
			req.setAttribute("attribute", attribute);
			req.getRequestDispatcher("/views/admin/Catalog/Attributes/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				attributesService.delete(id);
				req.getSession().setAttribute("message", "Đã xóa thông số kỹ thuật thành công!");
			} catch (Exception e) {
				req.getSession().setAttribute("errorMessage", "Không thể xóa vì dữ liệu đang được sử dụng ở nơi khác");
			}
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Attributes/searchpaginated");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("saveOrUpdate")) {
			Attributes attribute = new Attributes();

			String idStr = req.getParameter("id");
			String name = req.getParameter("name");
			String dataType = req.getParameter("dataType");
			String unit = req.getParameter("unit");

			// Nếu có id -> update
			Integer id = null;
			if (idStr != null && !idStr.isEmpty()) {
				id = Integer.parseInt(idStr);
				attribute = attributesService.findById(id);
			}

			// Gán giá trị tạm thời để giữ lại form nếu lỗi
			attribute.setName(name);
			attribute.setDataType(Integer.parseInt(dataType));
			attribute.setUnit(unit);

			// Kiểm tra tên trùng
			Attributes existing = attributesService.findByName(name);
			if (existing != null && !Objects.equals(existing.getId(), id)) {
				req.setAttribute("error", "Tên thông số kỹ thuật đã tồn tại! Vui lòng nhập tên khác!");
			}

			// foward lại form nếu lỗi
			if (req.getAttribute("error") != null) {
				req.setAttribute("attribute", attribute);
				req.getRequestDispatcher("/views/admin/Catalog/Attributes/addOrEdit.jsp").forward(req, resp);
				return;
			}

			// Lưu loại biến thể vào db nếu không lỗi
			String message;
			if (idStr != null && !idStr.isEmpty()) {
				attributesService.update(attribute);
				message = "Thông số kỹ thuật sửa thành công!";
			} else {
				attributesService.insert(attribute);
				message = "Thông số kỹ thuật thêm thành công!";
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/Attributes/searchpaginated");
		}
	}
}
