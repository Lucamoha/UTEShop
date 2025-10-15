package com.uteshop.controller.manager;

import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.order.Orders;
import com.uteshop.enums.OrderEnums;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.impl.manager.OrdersManagerServiceImpl;
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
    EntityDaoImpl<Orders> ordersDao = new EntityDaoImpl<>(Orders.class);

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

            Integer paymentMethod = ordersManagerService.getPaymentByOrderId(orders.getId()).getMethod();

            String paymentMethodStr = PaymentEnums.Method.label(paymentMethod);
            req.setAttribute("paymentMethod", paymentMethodStr);

            java.util.Map<Integer,String> STATUS_MAP = new java.util.LinkedHashMap<>();
            STATUS_MAP.put(OrderEnums.OrderStatus.NEW,"Mới tạo");
            STATUS_MAP.put(OrderEnums.OrderStatus.CONFIRMED,"Đã xác nhận");
            STATUS_MAP.put(OrderEnums.OrderStatus.SHIPPING,"Đang giao hàng");
            STATUS_MAP.put(OrderEnums.OrderStatus.DELIVERED,"Đã nhận");
            STATUS_MAP.put(OrderEnums.OrderStatus.CANCELED,"Hủy");
            STATUS_MAP.put(OrderEnums.OrderStatus.RETURNED,"Trả hàng");

            java.util.Map<Integer,String> PAY_MAP = new java.util.LinkedHashMap<>();
            PAY_MAP.put(OrderEnums.PaymentStatus.UNPAID,"Chưa thanh toán");
            PAY_MAP.put(OrderEnums.PaymentStatus.PAID,"Đã thanh toán");
            PAY_MAP.put(OrderEnums.PaymentStatus.REFUNDED,"Hoàn tiền");

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

            Orders orders = ordersDao.findById(id);

            if (orders == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (orders.getBranch() == null || !bId.equals(orders.getBranch().getId())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN); // không thuộc chi nhánh hiện tại
                return;
            }

            String action = req.getParameter("action");
            if (action == null || action.isBlank() || !action.equals("updateOrderStatus")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            Integer toStatus = parseInt(req.getParameter("status"));

            try {
                if (toStatus == null)
                    throw new IllegalArgumentException("Thiếu 'status'");

                if (orders.getOrderStatus() != toStatus)
                    ordersManagerService.updateOrderStatus(id, toStatus);

                req.getSession().setAttribute("flash_ok", true);
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
