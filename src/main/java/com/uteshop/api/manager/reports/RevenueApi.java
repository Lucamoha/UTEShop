package com.uteshop.api.manager.reports;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.util.JsonUtil;
import com.uteshop.dto.manager.reports.RevenueSeriesResponse;
import com.uteshop.services.impl.manager.RevenueServiceImpl;
import com.uteshop.services.manager.IRevenueService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@WebServlet(urlPatterns = "/api/manager/charts/revenue")
public class RevenueApi extends HttpServlet {
    IRevenueService revenueServiceImpl = new RevenueServiceImpl();
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String param(HttpServletRequest req, String name, String defVal) {
        String v = req.getParameter(name);
        return (v == null || v.isBlank()) ? defVal : v;
    }

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
        resp.setContentType("application/json; charset=UTF-8");

        Integer branchId = branchId(req);
        if (branchId == null) {
            writeJson(resp, 401, Map.of("ok", false, "error", "Unauthenticated/No branch"));
            return;
        }

        String bucket = param(req, "bucket", "day"); // day|week|month
        LocalDate from = LocalDate.parse(param(req, "from", LocalDate.now().withDayOfMonth(1).toString()));
        LocalDate to = LocalDate.parse(param(req, "to", LocalDate.now().toString()));

        RevenueSeriesResponse data = revenueServiceImpl.buildSeries(branchId(req), from, to, bucket);
        JsonUtil.MAPPER.writeValue(resp.getWriter(), data);
    }
}
