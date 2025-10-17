package com.uteshop.controller.web.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;
import com.uteshop.enums.OrderEnums;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.impl.web.payment.MomoServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/payment/momo/return",
                            "/payment/momo/ipn"})
public class MomoController extends HttpServlet {
    private final MomoServiceImpl momoService = new MomoServiceImpl();
    EntityDaoImpl<Orders> orderDao = new EntityDaoImpl<>(Orders.class);

    private Integer extractOrderId(String orderId) {
        try {
            return Integer.valueOf(orderId.substring(orderId.lastIndexOf('_') + 1));
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> readParams(HttpServletRequest req) throws IOException {
        String method = req.getMethod();
        String ct = req.getContentType();

        if ("GET".equalsIgnoreCase(method) ||
                (ct != null && ct.contains("application/x-www-form-urlencoded"))) {
            Map<String, String> m = new LinkedHashMap<>();
            req.getParameterMap().forEach((k, v) -> m.put(k, (v != null && v.length > 0) ? v[0] : ""));
            return m;
        }

        // 2) JSON IPN
        if (ct != null && ct.toLowerCase().contains("application/json")) {
            return new ObjectMapper().readValue(
                    req.getInputStream(),
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String,String>>() {}
            );
        }

        return Collections.emptyMap();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String> map = readParams(req);
        if (map.isEmpty()) {
            resp.sendError(400, "Empty callback payload");
            return;
        }

        boolean valid = momoService.verifyCallback(map);
        if (!valid) {
            return;
        }

        String path = req.getServletPath();
        if (path.equals("/payment/momo/return")) {
            Integer orderId = extractOrderId(req.getParameter("orderId"));
            String ctx = req.getContextPath();
            String url = String.format("%s/order/detail?id=%s",  // trang xem chi tiết đơn
                    ctx,
                    orderId == null ? "" : orderId);
            resp.sendRedirect(url);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String,String> map = readParams(req);
        if (map.isEmpty()) {
            resp.sendError(400, "Empty callback payload");
            return;
        }

        boolean valid = momoService.verifyCallback(map);
        if (!valid) {
            return;
        }

        String path = req.getServletPath();
        if (path.equals("/payment/momo/ipn")) {
            Integer orderId = extractOrderId(String.valueOf(map.get("orderId")));
            if (orderId == null) {
                resp.getWriter().write("{\"message\":\"Order Id not found\"}");
                return;
            }

            int resultCode = Integer.parseInt(String.valueOf(map.get("resultCode")));
            String transId = String.valueOf(map.get("transId"));
            String amount = String.valueOf(map.get("amount"));

            Orders orders = orderDao.findById(orderId);
            Payments payments = orders.getPayment();

            if (resultCode == 0) {
                orders.setPaymentStatus(OrderEnums.PaymentStatus.PAID);

                payments.setStatus(PaymentEnums.Status.SUCCESS);
                payments.setTxnId(transId);
                payments.setPaidAmount(new BigDecimal(amount));
                payments.setPaidAt(LocalDateTime.now());
                payments.setProviderRaw(map.toString());

                orderDao.update(orders);
            }
            resp.getWriter().write("{\"message\":\"ok\"}");
        }
    }
}
