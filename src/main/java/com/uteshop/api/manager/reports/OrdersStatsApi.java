package com.uteshop.api.manager.reports;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.api.manager.Jsons;
import com.uteshop.dto.manager.reports.OrderStatsDTO;
import com.uteshop.services.impl.manager.OrdersManagerServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/manager/orders-stats"})
public class OrdersStatsApi extends HttpServlet {
    OrdersManagerServiceImpl ordersManagerService = new OrdersManagerServiceImpl();

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(resp.getOutputStream(), body);
    }

    private Integer branchId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("branchId");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String fromStr = req.getParameter("from"); // yyyy-MM-dd
        String toStr   = req.getParameter("to");   // yyyy-MM-dd

        Integer branchId = branchId(req);
        if (branchId == null) {
            writeJson(resp, 401, Map.of("ok", false, "error", "Unauthenticated/No branch"));
            return;
        }

        if (fromStr == null || toStr == null) {
            writeJson(resp, 400, Map.of("ok", false, "error", "from/to are required (format yyyy-MM-dd)"));
            return;
        }

        try {
            LocalDateTime from = LocalDate.parse(fromStr).atStartOfDay();
            LocalDateTime toEx = LocalDate.parse(toStr).plusDays(1).atStartOfDay();

            // Gọi service lấy thống kê
            OrderStatsDTO result = ordersManagerService.getOrderStats(from, toEx, branchId);

            resp.setStatus(HttpServletResponse.SC_OK);
            Jsons.MAPPER.writeValue(resp.getWriter(), result);

        } catch (DateTimeParseException e) {
            writeJson(resp, 400, Map.of("ok", false, "error", "Invalid date format. Use yyyy-MM-dd"));
        } catch (Exception e) {
            writeJson(resp, 500, Map.of("ok", false, "error", "Internal error"));
            System.err.println("/api/manager/orders-stats error: " + e.getMessage());
        }
    }
}
