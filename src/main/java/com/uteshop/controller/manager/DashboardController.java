package com.uteshop.controller.manager;

import com.uteshop.dao.manager.impl.OrdersManagerDaoImpl;
import com.uteshop.entity.order.Orders;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/manager/dashboard"})
public class DashboardController extends HttpServlet {
    OrdersManagerDaoImpl ordersManagerDao = new OrdersManagerDaoImpl();

    private Integer branchId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("branchId");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer bId = branchId(req);
        if (bId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        List<Orders> ordersRecent = ordersManagerDao.findRecent(bId, 10);

        req.setAttribute("ordersRecent", ordersRecent);
        req.getRequestDispatcher("/views/manager/dashboard.jsp").forward(req,resp);
    }
}
