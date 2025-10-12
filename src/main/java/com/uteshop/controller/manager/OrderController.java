package com.uteshop.controller.manager;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.order.Orders;
import com.uteshop.services.impl.manager.OrdersManagerServiceImpl;
import com.uteshop.util.AppConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {
        "/manager/orders",
        "/manager/orders/detail"
})
public class OrderController extends HttpServlet {
    OrdersManagerServiceImpl ordersManagerService = new OrdersManagerServiceImpl();

    private Integer branchId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("branchId");
    }

    private String param(HttpServletRequest req, String name, String def) {
        String v = req.getParameter(name);
        return (v == null || v.isBlank()) ? def : v;
    }
    private Integer parseInt(String s) {
        try { return (s==null||s.isBlank())? null : Integer.valueOf(s); }
        catch (Exception e) { return null; }
    }
    private int parseInt(String s, int def) {
        try { return (s==null||s.isBlank())? def : Integer.parseInt(s); }
        catch (Exception e) { return def; }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer bId = branchId(req);
        if (bId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String path = req.getServletPath();
        if (path.equals("/manager/orders")) {
            // Params
            String q = param(req, "q", null);
            Integer orderStatus = parseInt(req.getParameter("status"));
            Integer paymentStatus = parseInt(req.getParameter("pay"));
            int page = parseInt(req.getParameter("page"), 1);
            int size = parseInt(req.getParameter("size"), 10);

            PageResult<Orders> result = ordersManagerService.searchPaged(bId, orderStatus, paymentStatus, q, page, size);

            req.setAttribute("q", q);
            req.setAttribute("status", orderStatus);
            req.setAttribute("pay", paymentStatus);
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("result", result);

            req.getRequestDispatcher("/views/manager/orders/listOrders.jsp").forward(req, resp);
        }
        else if (path.equals("/manager/orders/detail")) {
            Integer id = parseInt(req.getParameter("id"));

            if (id == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Orders orders = ordersManagerService.findByIdWithItems(id, bId);
            if (orders == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (orders.getBranch() == null || !bId.equals(orders.getBranch().getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN); // không thuộc chi nhánh hiện tại
                return;
            }

            req.setAttribute("order", orders);

            java.util.Map<Integer,String> STATUS_MAP = new java.util.LinkedHashMap<>();
            STATUS_MAP.put(AppConfig.getInt("orders.status.new"),"Mới tạo");
            STATUS_MAP.put(AppConfig.getInt("orders.status.confirmed"),"Đã xác nhận");
            STATUS_MAP.put(AppConfig.getInt("orders.status.shipping"),"Đang giao hàng");
            STATUS_MAP.put(AppConfig.getInt("orders.status.delivered"),"Đã nhận");
            STATUS_MAP.put(AppConfig.getInt("orders.status.canceled"),"Hủy");
            STATUS_MAP.put(AppConfig.getInt("orders.status.returned"),"Trả hàng");

            java.util.Map<Integer,String> PAY_MAP = new java.util.LinkedHashMap<>();
            PAY_MAP.put(AppConfig.getInt("payment.status.unpaid"),"Chưa thanh toán");
            PAY_MAP.put(AppConfig.getInt("payment.status.paid"),"Đã thanh toán");
            PAY_MAP.put(AppConfig.getInt("payment.status.refunded"),"Hoàn tiền");

            req.setAttribute("STATUS_MAP", STATUS_MAP);
            req.setAttribute("PAY_MAP", PAY_MAP);

            req.getRequestDispatcher("/views/manager/orders/orderDetail.jsp").forward(req, resp);
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();
        if (path.equals("/manager/orders/detail")) {
            Integer bId = branchId(req);
            Integer id = parseInt(req.getParameter("id"));
            if (bId == null || id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            Orders orders = ordersManagerService.findById(id);
            if (orders == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (orders.getBranch() == null || !bId.equals(orders.getBranch().getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN); // không thuộc chi nhánh hiện tại
                return;
            }

            String action = req.getParameter("action");
            if (action == null || action.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            try {
                boolean changed = false;

                switch (action) {

                    case "updateStatus": {
                        Integer toStatus = parseInt(req.getParameter("status"));
                        if (toStatus == null) throw new IllegalArgumentException("Thiếu tham số 'status'");
                        if (orders.getOrderStatus() != toStatus) {
                            orders.setOrderStatus(toStatus);
                            changed = true;
                        }
                        break;
                    }
                    case "updatePayment": {
                        Integer toPay = parseInt(req.getParameter("payment"));
                        if (toPay == null) throw new IllegalArgumentException("Thiếu tham số 'payment'");
                        if (orders.getPaymentStatus() != toPay) {
                            orders.setPaymentStatus(toPay);
                            changed = true;
                        }
                        break;
                    }
                    case "updateBoth": { // gộp 1 nút
                        Integer toStatus = parseInt(req.getParameter("status"));
                        Integer toPay    = parseInt(req.getParameter("payment"));
                        if (toStatus == null || toPay == null) {
                            throw new IllegalArgumentException("Thiếu 'status' hoặc 'payment'");
                        }
                        if (orders.getOrderStatus() != toStatus) {
                            orders.setOrderStatus(toStatus);
                            changed = true;
                        }
                        if (orders.getPaymentStatus() != toPay) {
                            orders.setPaymentStatus(toPay);
                            changed = true;
                        }
                        break;
                    }
                    default:
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                }

                if (changed) {
                    ordersManagerService.update(orders);
                }

                req.getSession().setAttribute("flash_ok", true);
                // PRG
                resp.sendRedirect(req.getContextPath() + "/manager/orders/detail?id=" + id);
            } catch (Exception e) {
                req.setAttribute("error", "Cập nhật thất bại: " + e.getMessage());
                doGet(req, resp);
            }
        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
