package com.uteshop.controller.admin.Vouchers;

import com.uteshop.entity.cart.Vouchers;
import com.uteshop.services.admin.IVoucherService;
import com.uteshop.services.impl.admin.VoucherServiceImpl;
import com.uteshop.util.ValidInput;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(urlPatterns = { "/admin/Voucher/Vouchers/searchpaginated", "/admin/Voucher/Vouchers/add",
		"/admin/Voucher/Vouchers/edit", "/admin/Voucher/Vouchers/view", "/admin/Voucher/Vouchers/delete" })
public class VoucherController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final IVoucherService voucherService = new VoucherServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		String error = null;

		try {
			switch (url) {
			case "/admin/Voucher/Vouchers/searchpaginated":
				List<Vouchers> vouchers = voucherService.getAll();
				req.setAttribute("listVouchers", vouchers);
				req.getRequestDispatcher("/views/admin/Voucher/Vouchers/searchpaginated.jsp").forward(req, resp);
				break;

			case "/admin/Voucher/Vouchers/add":
				req.getRequestDispatcher("/views/admin/Voucher/Vouchers/add.jsp").forward(req, resp);
				break;

			case "/admin/Voucher/Vouchers/edit":
				Integer editId = ValidInput.safeParseInteger("id", req);
				if (editId == null) {
					throw new ServletException("Thiếu ID voucher");
				}
				Vouchers editVoucher = voucherService.getById(editId);
				if (editVoucher == null) {
					throw new ServletException("Không tìm thấy voucher với ID: " + editId);
				}
				req.setAttribute("voucher", editVoucher);
				req.getRequestDispatcher("/views/admin/Voucher/Vouchers/edit.jsp").forward(req, resp);
				break;

			case "/admin/Voucher/Vouchers/view":
				Integer viewId = ValidInput.safeParseInteger("id", req);
				if (viewId == null) {
					throw new ServletException("Thiếu ID voucher");
				}
				Vouchers viewVoucher = voucherService.getById(viewId);
				if (viewVoucher == null) {
					throw new ServletException("Không tìm thấy voucher với ID: " + viewId);
				}
				req.setAttribute("voucher", viewVoucher);
				req.getRequestDispatcher("/views/admin/Voucher/Vouchers/view.jsp").forward(req, resp);
				break;

			case "/admin/Voucher/Vouchers/delete":
				Integer deleteId = ValidInput.safeParseInteger("id", req);
				if (deleteId == null) {
					throw new ServletException("Thiếu ID voucher");
				}
				voucherService.delete(deleteId);
				resp.sendRedirect(req.getContextPath() + "/admin/Voucher/Vouchers/searchpaginated?success=true");
				break;

			default:
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy trang");
				break;
			}
		} catch (ServletException e) {
			error = e.getMessage();
			req.setAttribute("error", error);
			req.getRequestDispatcher("/views/admin/Voucher/Vouchers/searchpaginated.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getServletPath();
		String error = null;
		Vouchers voucher = null;
		boolean isEdit = url.equals("/admin/Voucher/Vouchers/edit");

		try {
			if (!url.equals("/admin/Voucher/Vouchers/add") && !url.equals("/admin/Voucher/Vouchers/edit")) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy trang");
				return;
			}

			// Validate và lấy dữ liệu từ form
			String code = req.getParameter("code");
			if (code == null || code.trim().isEmpty()) {
				throw new ServletException("Mã voucher không được để trống");
			}

			// Kiểm tra mã voucher duy nhất
			Vouchers existingVoucher = voucherService.getByCode(code);
			if (existingVoucher != null
					&& (!isEdit || !existingVoucher.getId().equals(ValidInput.safeParseInteger("id", req)))) {
				throw new ServletException("Mã voucher đã tồn tại");
			}

			// Lấy dữ liệu từ form trước
			String descText = req.getParameter("descText");
			Integer type = ValidInput.safeParseInteger("type", req);
			if (type == null) {
				throw new ServletException("Loại voucher không được để trống");
			}
			BigDecimal value = ValidInput.safeParseBigDecimal("value", req);
			if (value == null) {
				throw new ServletException("Giá trị voucher không được để trống");
			}
			Integer maxUses = ValidInput.safeParseInteger("maxUses", req);
			if (maxUses == null) {
				throw new ServletException("Số lần sử dụng tối đa không được để trống");
			}
			LocalDateTime startsAt = ValidInput.safeParseDateTime("startsAt", req);
			LocalDateTime endsAt = ValidInput.safeParseDateTime("endsAt", req);
			if (startsAt.isAfter(endsAt)) {
				throw new ServletException("Ngày bắt đầu phải trước ngày kết thúc");
			}
			boolean isActive = req.getParameter("isActive") != null && req.getParameter("isActive").equals("true");

			Integer id = ValidInput.safeParseInteger("id", req);
			
			if(isEdit) {
				voucher = voucherService.getById(id);
				if (voucher == null) {
					throw new ServletException("Không tìm thấy voucher để chỉnh sửa");
				}
			}
			else {
				voucher = new Vouchers();
			}
			
			voucher.setCode(code);
			voucher.setDescText(descText);
			voucher.setType(type);
			voucher.setValue(value);
			voucher.setMaxUses(maxUses);
			voucher.setStartsAt(startsAt);
			voucher.setEndsAt(endsAt);
			voucher.setIsActive(isActive);
			
			if (isEdit) {
				voucherService.update(voucher);
			} else {			
				voucherService.insert(voucher);
			}

			resp.sendRedirect(req.getContextPath() + "/admin/Voucher/Vouchers/searchpaginated?success=true");

		} catch (ServletException e) {
			error = e.getMessage();
			req.setAttribute("error", error);
			if (isEdit) {
				req.setAttribute("voucher", voucher);
			} else {
				voucher = new Vouchers();
				voucher.setCode(req.getParameter("code"));
				voucher.setDescText(req.getParameter("descText"));
				try {
					voucher.setType(ValidInput.safeParseInteger("type", req));
					voucher.setValue(ValidInput.safeParseBigDecimal("value", req));
					voucher.setMaxUses(ValidInput.safeParseInteger("maxUses", req));
					voucher.setStartsAt(ValidInput.safeParseDateTime("startsAt", req));
					voucher.setEndsAt(ValidInput.safeParseDateTime("endsAt", req));
					voucher.setIsActive(
							req.getParameter("isActive") != null && req.getParameter("isActive").equals("true"));
				} catch (Exception ignored) {
				} // Ignore parse errors in error case
				req.setAttribute("voucher", voucher);
			}
			String forwardPage = isEdit ? "/views/admin/Voucher/Vouchers/edit.jsp"
					: "/views/admin/Voucher/Vouchers/add.jsp";
			req.getRequestDispatcher(forwardPage).forward(req, resp);
		}
	}
}
