package com.uteshop.controller.web.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;
import com.uteshop.enums.OrderEnums;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.impl.web.payment.MomoServiceImpl;
import com.uteshop.services.impl.web.payment.VnpayServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/payment/vnpay/return",
                            "/payment/vnpay/ipn" })
public class VnpayController extends HttpServlet {
    private final VnpayServiceImpl vnpayService = new VnpayServiceImpl();
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

        boolean valid = vnpayService.verifyCallback(map);
        if (!valid) {
            return;
        }

        String path = req.getServletPath();
        if (path.equals("/payment/vnpay/return")) {
            Integer orderId = extractOrderId(req.getParameter("vnp_OrderInfo"));
            String ctx = req.getContextPath();
            String url = String.format("%s/order/detail?id=%s",  // trang xem chi tiết đơn
                    ctx,
                    orderId == null ? "" : orderId);
            resp.sendRedirect(url);
        }
        else if (path.equals("/payment/vnpay/ipn")) {
            Integer orderId = extractOrderId(req.getParameter("vnp_OrderInfo"));

            if (orderId == null) {
                resp.getWriter().write("{\"message\":\"Order Id not found\"}");
                return;
            }

            String resultCode = String.valueOf(map.get("vnp_TransactionStatus"));
            String transId = String.valueOf(map.get("vnp_TxnRef"));
            String amountx100 = String.valueOf(map.get("vnp_Amount"));
            String amount = amountx100.substring(0, amountx100.length() - 2);

            Orders orders = orderDao.findById(orderId);
            Payments payments = orders.getPayment();

            if (resultCode.equals("00")) {
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
