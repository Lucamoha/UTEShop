package com.uteshop.controller.admin.Catalog;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.uteshop.entity.catalog.OptionTypes;
import com.uteshop.entity.catalog.OptionValues;
import com.uteshop.services.admin.IOptionTypesService;
import com.uteshop.services.admin.IOptionValueService;
import com.uteshop.services.impl.admin.OptionTypesServiceImpl;
import com.uteshop.services.impl.admin.OptionValueServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/Catalog/OptionValues/searchpaginated", "/admin/Catalog/OptionValues/saveOrUpdate",
		"/admin/Catalog/OptionValues/delete", "/admin/Catalog/OptionValues/view" })
public class OptionValuesController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	IOptionValueService optionValueService = new OptionValueServiceImpl();
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

			List<OptionValues> optionValueList = optionValueService.findAllFetchColumns(false, firstResult, size, searchKeyword,
					"Value", List.of("optionType"));

			// Đếm tổng số bản ghi để tính tổng trang
			int totalOptionValues = optionValueService.count(searchKeyword, "Value");
			int totalPages = (int) Math.ceil((double) totalOptionValues / size);

			req.setAttribute("optionValueList", optionValueList);
			req.setAttribute("currentPage", page);
			req.setAttribute("totalPages", totalPages);
			req.setAttribute("size", size);
			req.setAttribute("searchKeyword", searchKeyword);

			req.getRequestDispatcher("/views/admin/Catalog/OptionValues/searchpaginated.jsp").forward(req, resp);

		} else if (uri.contains("saveOrUpdate")) {
			String id = req.getParameter("id");
			OptionValues optionValue = null;
			if (id != null) {
				// dang o che do edit -> nguoc lai la add
				optionValue = optionValueService.findByIdFetchColumn(Integer.parseInt(id), "optionType");
			}
			else {
				optionValue = new OptionValues();
			}
			
			List<OptionTypes> optionTypeList = optionTypesService.findAll();
			req.setAttribute("optionValue", optionValue);
			req.setAttribute("optionTypeList", optionTypeList);
			req.getRequestDispatcher("/views/admin/Catalog/OptionValues/addOrEdit.jsp").forward(req, resp);
		} else if (uri.contains("view")) {
			String id = req.getParameter("id");
			OptionValues optionValue = optionValueService.findByIdFetchColumn(Integer.parseInt(id), "optionType");
			req.setAttribute("optionValue", optionValue);
			req.getRequestDispatcher("/views/admin/Catalog/OptionValues/view.jsp").forward(req, resp);
		} else if (uri.contains("delete")) {
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				optionValueService.delete(id);
				req.getSession().setAttribute("message", "Đã xóa giá trị tùy chọn thành công!");
			} catch (Exception e) {
				req.getSession().setAttribute("errorMessage", "Không thể xóa vì dữ liệu đang được sử dụng ở nơi khác");
			}
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/OptionValues/searchpaginated");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String uri = req.getRequestURI();

		if (uri.contains("saveOrUpdate")) {
			OptionValues optionValue = new OptionValues();

			String idStr = req.getParameter("id");
			String value = req.getParameter("value");
			String optionTypeId = req.getParameter("optionTypeId");

			// Nếu có id -> update
			Integer id = null;
			if (idStr != null && !idStr.isEmpty()) {
				id = Integer.parseInt(idStr);
				optionValue = optionValueService.findByIdFetchColumn(id, "optionType");
			}

			// Gán giá trị tạm thời để giữ lại form nếu lỗi
			optionValue.setValue(value);
			optionValue.setOptionType(optionTypesService.findById(Integer.parseInt(optionTypeId)));

			// Kiểm tra tên trùng (trùng tên + trùng loại)
			OptionValues existing = optionValueService.findByValue(value);
			if (existing != null && !Objects.equals(existing.getId(), id)
					&& existing.getOptionType().getId() == optionValue.getOptionType().getId()) {
				req.setAttribute("error",
						"Giá trị tùy chọn đã tồn tại với giá trị tùy chọn cùng loại! Vui lòng nhập giá trị tùy chọn khác!");
			}

			// foward lại form nếu lỗi
			if (req.getAttribute("error") != null) {
				List<OptionTypes> optionTypeList = optionTypesService.findAll();
				req.setAttribute("optionValue", optionValue);
				req.setAttribute("optionTypeList", optionTypeList);
				req.getRequestDispatcher("/views/admin/Catalog/OptionValues/addOrEdit.jsp").forward(req, resp);
				return;
			}

			// Lưu loại biến thể vào db nếu không lỗi
			String message;
			if (idStr != null && !idStr.isEmpty()) {
				optionValueService.update(optionValue);
				message = "Giá trị tùy chọn sửa thành công!";
			} else {
				optionValueService.insert(optionValue);
				message = "Thêm giá trị tùy chọn thành công!";
			}

			req.getSession().setAttribute("message", message);
			resp.sendRedirect(req.getContextPath() + "/admin/Catalog/OptionValues/searchpaginated");
		}
	}
}
