package com.uteshop.controller.admin.vouchers;

import com.uteshop.entity.cart.Vouchers;
import com.uteshop.services.admin.IVoucherService;
import com.uteshop.services.impl.admin.VoucherServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(urlPatterns = {
        "/admin/Voucher/Vouchers/list",
        "/admin/Voucher/Vouchers/add",
        "/admin/Voucher/Vouchers/edit",
        "/admin/Voucher/Vouchers/view",
        "/admin/Voucher/Vouchers/delete"
})
public class VoucherController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IVoucherService voucherService = new VoucherServiceImpl();

    private Integer safeParseInteger(String paramName, HttpServletRequest req) throws ServletException {
        String value = req.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            throw new ServletException("Định dạng số không hợp lệ cho " + paramName + ": " + value);
        }
    }

    private BigDecimal safeParseBigDecimal(String paramName, HttpServletRequest req) throws ServletException {
        String value = req.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            BigDecimal result = new BigDecimal(value.trim());
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                throw new ServletException(paramName + " không được âm");
            }
            return result;
        } catch (NumberFormatException e) {
            throw new ServletException("Định dạng số thập phân không hợp lệ cho " + paramName + ": " + value);
        }
    }

    private LocalDateTime safeParseDateTime(String paramName, HttpServletRequest req) throws ServletException {
        String value = req.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            throw new ServletException("Thiếu hoặc tham số không hợp lệ: " + paramName);
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            return LocalDateTime.parse(value, formatter);
        } catch (Exception e) {
            throw new ServletException("Định dạng ngày giờ không hợp lệ cho " + paramName + ": " + value);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        String error = null;

        try {
            switch (url) {
                case "/admin/Voucher/Vouchers/list":
                    List<Vouchers> vouchers = voucherService.getAll();
                    req.setAttribute("listVouchers", vouchers);
                    req.getRequestDispatcher("/views/admin/Voucher/Vouchers/list.jsp").forward(req, resp);
                    break;

                case "/admin/Voucher/Vouchers/add":
                    req.getRequestDispatcher("/views/admin/Voucher/Vouchers/add.jsp").forward(req, resp);
                    break;

                case "/admin/Voucher/Vouchers/edit":
                    Integer editId = safeParseInteger("id", req);
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
                    Integer viewId = safeParseInteger("id", req);
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
                    Integer deleteId = safeParseInteger("id", req);
                    if (deleteId == null) {
                        throw new ServletException("Thiếu ID voucher");
                    }
                    voucherService.delete(deleteId);
                    resp.sendRedirect(req.getContextPath() + "/admin/Voucher/Vouchers/list?success=true");
                    break;

                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy trang");
                    break;
            }
        } catch (ServletException e) {
            error = e.getMessage();
            req.setAttribute("error", error);
            req.getRequestDispatcher("/views/admin/Voucher/Vouchers/list.jsp").forward(req, resp);
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
            if (existingVoucher != null && (!isEdit || !existingVoucher.getId().equals(safeParseInteger("id", req)))) {
                throw new ServletException("Mã voucher đã tồn tại");
            }

            // Tạo hoặc lấy voucher để cập nhật
            voucher = isEdit ? voucherService.getById(safeParseInteger("id", req)) : new Vouchers();
            if (isEdit && voucher == null) {
                throw new ServletException("Không tìm thấy voucher để chỉnh sửa");
            }

            // Điền dữ liệu vào voucher
            voucher.setCode(code);
            voucher.setDescText(req.getParameter("descText"));
            voucher.setType(safeParseInteger("type", req));
            voucher.setValue(safeParseBigDecimal("value", req));
            voucher.setMaxUses(safeParseInteger("maxUses", req));
            voucher.setTotalUsed(isEdit ? voucher.getTotalUsed() : 0);
            voucher.setStartsAt(safeParseDateTime("startsAt", req));
            voucher.setEndsAt(safeParseDateTime("endsAt", req));
            voucher.setIsActive(req.getParameter("isActive") != null && req.getParameter("isActive").equals("true"));

            // Lưu voucher
            voucherService.save(voucher);
            resp.sendRedirect(req.getContextPath() + "/admin/Voucher/Vouchers/list?success=true");

        } catch (ServletException e) {
            error = e.getMessage();
            req.setAttribute("voucher", voucher != null ? voucher : new Vouchers());
            req.setAttribute("error", error);
            String forwardPage = isEdit ? "/views/admin/Voucher/Vouchers/edit.jsp" : "/views/admin/Voucher/Vouchers/add.jsp";
            req.getRequestDispatcher(forwardPage).forward(req, resp);
        }
    }
}