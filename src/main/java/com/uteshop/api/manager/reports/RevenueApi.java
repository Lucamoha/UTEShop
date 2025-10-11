package com.uteshop.api.manager.reports;

import com.uteshop.api.manager.Jsons;
import com.uteshop.dto.manager.reports.RevenueSeriesResponse;
import com.uteshop.services.manager.RevenueService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = "/api/manager/charts/revenue")
public class RevenueApi extends HttpServlet {
    RevenueService revenueService = new RevenueService();

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

        String bucket = param(req, "bucket", "day"); // day|week|month
        LocalDate from = LocalDate.parse(param(req, "from", LocalDate.now().withDayOfMonth(1).toString()));
        LocalDate to = LocalDate.parse(param(req, "to", LocalDate.now().toString()));

        RevenueSeriesResponse data = revenueService.buildSeries(branchId(req), from, to, bucket);
        Jsons.MAPPER.writeValue(resp.getWriter(), data);
    }
}
