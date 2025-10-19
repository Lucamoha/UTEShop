package com.uteshop.controller.web;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.Products;
import com.uteshop.entity.order.Orders;
import com.uteshop.enums.OrderEnums;
import com.uteshop.services.impl.web.CategoriesServiceImpl;
import com.uteshop.services.impl.web.OrdersServiceImpl;
import com.uteshop.services.impl.web.ProductsServiceImpl;
import com.uteshop.services.impl.web.UsersServiceImpl;
import com.uteshop.services.web.ICategoriesService;
import com.uteshop.services.web.IProductsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(urlPatterns = {
        "/orders",
        "/orders/detail",
        "/orders/cancel",
        "/orders/return"
})
public class OrdersController extends HttpServlet {
    
    private final OrdersServiceImpl ordersService = new OrdersServiceImpl();
    private final UsersServiceImpl usersService = new UsersServiceImpl();
    private final ICategoriesService categoriesService = new CategoriesServiceImpl();
    private final IProductsService productsService = new ProductsServiceImpl();
    private final int PAGE_SIZE = 10;


    private Integer getCurrentUserId(HttpServletRequest req) {
        String email = (String) req.getAttribute("authenticatedEmail");
        
        if (email == null) {
            return null;
        }
        
        Users user = usersService.findByEmail(email);
        return (user != null) ? user.getId() : null;
    }

    private Integer parseIntParam(String value, Integer defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private int parseIntParam(String value, int defaultValue) {
        try {
            return (value == null || value.trim().isEmpty()) ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = getCurrentUserId(req);
        String servletPath = req.getServletPath();
        List<Categories> parents = categoriesService.findParents();
        List<Products> topLatestProducts = productsService.topLatestProducts();

        if ("/orders".equals(servletPath)) {
            handleOrdersList(req, resp, userId);
        } else if ("/orders/detail".equals(servletPath)) {
            handleOrderDetail(req, resp, userId);
        }
        req.setAttribute("parentCategories", parents);
        req.setAttribute("topLatestProducts", topLatestProducts);
    }

    /**
     * Xử lý trang danh sách đơn hàng
     */
    private void handleOrdersList(HttpServletRequest req, HttpServletResponse resp, Integer userId) 
            throws ServletException, IOException {
        
        // Lấy parameters
        Integer status = parseIntParam(req.getParameter("status"), null);
        int page = parseIntParam(req.getParameter("page"), 1);
        int size = parseIntParam(req.getParameter("size"), PAGE_SIZE);

        try {
            PageResult<Orders> result = ordersService.findByUserIdAndStatus(userId, status, page, size);

            // Set attributes cho JSP với null-safe
            req.setAttribute("orders", result != null ? result.getContent() : Collections.emptyList());
            req.setAttribute("currentPage", page);
            
            // Tổng số đơn hàng
            long total = (result != null) ? result.getTotal() : 0L;
            // Tổng số trang cần hiển thị
            int totalPages = (int) Math.ceil((double) total / size);

            req.setAttribute("totalPages", totalPages);
            req.setAttribute("totalElements", total);
            req.setAttribute("pageSize", size);
            req.setAttribute("selectedStatus", status);
        } catch (Exception e) {
            // Set empty data để tránh lỗi trong JSP
            req.setAttribute("orders", Collections.emptyList());
            req.setAttribute("currentPage", 1);
            req.setAttribute("totalPages", 0);
            req.setAttribute("totalElements", 0L);
            req.setAttribute("pageSize", size);
            req.setAttribute("selectedStatus", status);
        }

        // Set order status constants để sử dụng trong JSP
        req.setAttribute("ORDER_STATUS_NEW", OrderEnums.OrderStatus.NEW);
        req.setAttribute("ORDER_STATUS_CONFIRMED", OrderEnums.OrderStatus.CONFIRMED);
        req.setAttribute("ORDER_STATUS_SHIPPING", OrderEnums.OrderStatus.SHIPPING);
        req.setAttribute("ORDER_STATUS_DELIVERED", OrderEnums.OrderStatus.DELIVERED);
        req.setAttribute("ORDER_STATUS_CANCELED", OrderEnums.OrderStatus.CANCELED);
        req.setAttribute("ORDER_STATUS_RETURNED", OrderEnums.OrderStatus.RETURNED);

        // Forward to JSP
        req.getRequestDispatcher("/views/web/orders/list.jsp").forward(req, resp);
    }

    /**
     * Xử lý trang chi tiết đơn hàng
     */
    private void handleOrderDetail(HttpServletRequest req, HttpServletResponse resp, Integer userId) 
            throws ServletException, IOException {

        Integer orderId = parseIntParam(req.getParameter("id"), null);
        
        if (orderId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đơn hàng không hợp lệ");
            return;
        }

        // Lấy chi tiết đơn hàng 
        Orders order = ordersService.findOrderDetail(orderId, userId);
        
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng");
            return;
        }

        // Set attributes cho JSP
        req.setAttribute("order", order);

        // Set order status constants
        req.setAttribute("ORDER_STATUS_NEW", OrderEnums.OrderStatus.NEW);
        req.setAttribute("ORDER_STATUS_CONFIRMED", OrderEnums.OrderStatus.CONFIRMED);
        req.setAttribute("ORDER_STATUS_SHIPPING", OrderEnums.OrderStatus.SHIPPING);
        req.setAttribute("ORDER_STATUS_DELIVERED", OrderEnums.OrderStatus.DELIVERED);
        req.setAttribute("ORDER_STATUS_CANCELED", OrderEnums.OrderStatus.CANCELED);
        req.setAttribute("ORDER_STATUS_RETURNED", OrderEnums.OrderStatus.RETURNED);

        // Forward to JSP
        req.getRequestDispatcher("/views/web/orders/detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = getCurrentUserId(req);
        Integer orderId = parseIntParam(req.getParameter("id"), null);
        String servletPath = req.getServletPath();
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        
        try {
            if (orderId == null) {
                resp.getWriter().write("{\"success\": false, \"message\": \"ID đơn hàng không hợp lệ\"}");
                return;
            }
            
            // Verify order thuộc user
            Orders order = ordersService.findOrderDetail(orderId, userId);
            if (order == null) {
                resp.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy đơn hàng\"}");
                return;
            }
            
            if ("/orders/cancel".equals(servletPath)) {
                ordersService.updateOrderStatus(orderId, OrderEnums.OrderStatus.CANCELED);
                resp.getWriter().write("{\"success\": true, \"message\": \"Hủy đơn hàng thành công\"}");
            }
            else if("/orders/return".equals(servletPath)) {
                ordersService.updateOrderStatus(orderId, OrderEnums.OrderStatus.RETURNED);
                resp.getWriter().write("{\"success\": true, \"message\": \"Yêu cầu trả hàng đã được gửi thành công\"}");
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            resp.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            resp.getWriter().write("{\"success\": false, \"message\": \"Có lỗi xảy ra: " + e.getMessage() + "\"}");
        }
    }
}