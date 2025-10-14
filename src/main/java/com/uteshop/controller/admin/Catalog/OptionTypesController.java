package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.services.admin.IOptionTypesService;
import com.uteshop.services.impl.admin.OptionTypesServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/Catalog/OptionTypes/searchpaginated", "/admin/Catalog/OptionTypes/saveOrUpdate",
		"/admin/Catalog/OptionTypes/delete", "/admin/Catalog/OptionTypes/view" })
public class OptionTypesController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	IOptionTypesService optionTypesService = new OptionTypesServiceImpl();

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

			List<OptionTypes> optionTypeList = optionTypesService.findAll(false, firstResult, size,
					searchKeyword, "Code");

			// Đếm tổng số bản ghi để tính tổng trang
			int totalOptionTypes = optionTypesService.count(searchKeyword, "Code");
			int totalPages = (int) Math.ceil((double) totalOptionTypes / size);

			req.setAttribute("optionTypeList", optionTypeList);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Catalog/OptionTypes/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate")) {
			String id = req.getParameter("id");
			if (id != null) {
				// dang o che do edit -> nguoc lai la add
				OptionTypes optionType = optionTypesService.findById(Integer.parseInt(id));
				req.setAttribute("optionType", optionType);
			}
			req.getRequestDispatcher("/views/admin/Catalog/OptionTypes/addOrEdit.jsp").forward(req, resp);
		} else if (uri.contains("view")) {
			String id = req.getParameter("id");
			OptionTypes optionType = optionTypesService.findById(Integer.parseInt(id));
			req.setAttribute("optionType", optionType);
			req.getRequestDispatcher("/views/admin/Catalog/OptionTypes/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			String id = req.getParameter("id");
			optionTypesService.delete(Integer.parseInt(id));
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/OptionTypes/searchpaginated");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("saveOrUpdate")) {
			OptionTypes optionType = new OptionTypes();

			String idStr = req.getParameter("id");
			String code = req.getParameter("code");

			// Nếu có id -> update
			Integer id = null;
			if (idStr != null && !idStr.isEmpty()) {
				id = Integer.parseInt(idStr);
				optionType = optionTypesService.findById(id);
			}

			// Gán giá trị tạm thời để giữ lại form nếu lỗi
			optionType.setCode(code);

			// Kiểm tra code trùng
			OptionTypes existing = optionTypesService.findByCode(code);
			if (existing != null && !Objects.equals(existing.getId(), id)) {
				req.setAttribute("error", "Mã Code đã tồn tại! Vui lòng nhập mã Code khác!");
			}

			// foward lại form nếu lỗi
			if (req.getAttribute("error") != null) {
				req.setAttribute("optionType", optionType);
				req.getRequestDispatcher("/views/admin/Catalog/OptionTypes/addOrEdit.jsp").forward(req, resp);
				return;
			}

			// Lưu loại biến thể vào db nếu không lỗi
			String message;
			if (idStr != null && !idStr.isEmpty()) {
				optionTypesService.update(optionType);
				message = "Loại biến thể sửa thành công!";
			} else {
				optionTypesService.insert(optionType);
				message = "Loại biến thể thêm thành công!";
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/OptionTypes/searchpaginated");
		}
	}
}
