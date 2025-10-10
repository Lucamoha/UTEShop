package com.uteshop.api.manager.reports;

import com.uteshop.api.manager.Jsons;
import com.uteshop.dao.manager.IOrdersManagerDao;
import com.uteshop.dao.manager.impl.OrdersManagerDaoImpl;
import com.uteshop.dto.manager.reports.TopProductDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/api/manager/top-products")
public class TopProductsApi extends HttpServlet {
    IOrdersManagerDao ordsManagerDao = new OrdersManagerDaoImpl();

    private String param(HttpServletRequest req, String name, String defVal) {
        String v = req.getParameter(name);
        return (v == null || v.isBlank()) ? defVal : v;
    }

    private Integer branchId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("branchId");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        LocalDate from = LocalDate.parse(param(req, "from", LocalDate.now().withDayOfMonth(1).toString()));
        LocalDate to = LocalDate.parse(param(req, "to", LocalDate.now().toString()));
        int limit = Integer.parseInt(param(req, "limit", "10"));

        List<Object[]> rows = ordsManagerDao.topProducts(branchId(req), from, to, limit);

        List<TopProductDto> data = new ArrayList<>();
        for (Object[] r : rows) {
            Integer productId = (Integer) r[0];
            String name = (String) r[1];
            long qty = ((Number) r[2]).longValue();
            BigDecimal amount = (BigDecimal) r[3];
            data.add(new TopProductDto(productId, name, qty, amount));
        }

        Jsons.MAPPER.writeValue(resp.getWriter(), data);
    }
}
