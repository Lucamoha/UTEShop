package com.uteshop.api.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.entity.order.Orders;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.impl.web.PlaceOrderServiceImpl;
import com.uteshop.services.impl.web.payment.MomoServiceImpl;
import com.uteshop.services.web.payment.IPaymentService;
import com.uteshop.util.JWTUtil;
import com.uteshop.util.SignUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * /api/web/place-order: nhận payload JSON:
 * {
 *   "branchId": 1,
 *   "addressId": 10,
 *   "voucherCode": "BACK2SCHOOL", // có thể null/missing
 *   "totalAmount": 123456,
 *   "note": "abcxyz",
 *   "paymentMethod": "COD" | "MOMO" | "VNPAY",
 *   "items": [ { "variantId": 123, "quantity": 2 }, ... ]
 * }
 */
@WebServlet(urlPatterns = { "/api/web/place-order" })
public class PlaceOrderApi extends HttpServlet {
    private final PlaceOrderServiceImpl placeOrderService = new PlaceOrderServiceImpl();
    private final MomoServiceImpl momoService = new MomoServiceImpl();
    // private final VnpayServiceImpl vnpayService = new VnpayServiceImpl();

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(resp.getOutputStream(), body);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlaceOrderRequest {
        public Integer branchId;
        public Integer addressId;
        public String voucherCode;
        public Long totalAmount;             // VND integer
        public String note;
        public String paymentMethod;         // COD | MOMO | VNPAY
        public List<Item> items;
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Item {
            public Integer variantId;
            public Integer quantity;
        }
    }

    public static class ApiResponse {
        public final boolean success;
        public final String message;
        public final Integer orderId;
        public final String paymentUrl; // null nếu COD
        public ApiResponse(boolean success, String message, Integer orderId, String paymentUrl) {
            this.success = success; this.message = message; this.orderId = orderId; this.paymentUrl = paymentUrl;
        }
    }

    private static String validateBody(PlaceOrderRequest b) {
        if (b == null) return "Thiếu body";
        if (b.branchId == null) return "Thiếu branchId";
        if (b.addressId == null) return "Thiếu addressId";
        if (b.totalAmount == null || b.totalAmount < 0) return "totalAmount không hợp lệ";
        if (b.paymentMethod == null || b.paymentMethod.isBlank()) return "Thiếu paymentMethod";
        if (b.items == null || b.items.isEmpty()) return "Danh sách items rỗng";
        for (PlaceOrderRequest.Item it : b.items) {
            if (it.variantId == null || it.variantId <= 0) return "variantId không hợp lệ";
            if (it.quantity == null || it.quantity <= 0) return "quantity không hợp lệ";
        }
        return null;
    }

    private static Integer parsePaymentMethod(String s) {
        if (s == null) return null;
        switch (s.trim().toUpperCase(Locale.ROOT)) {
            case "COD":   return PaymentEnums.Method.COD;
            case "MOMO":  return PaymentEnums.Method.MOMO;
            case "VNPAY": return PaymentEnums.Method.VNPAY;
            default: return null;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1) JWT
        String userEmail = JWTUtil.getEmailFromRequest(req);
        if (userEmail == null || userEmail.isBlank()) {
            writeJson(resp, HttpServletResponse.SC_UNAUTHORIZED,
                    new ApiResponse(false, "Thiếu hoặc token không hợp lệ", null, null));
            return;
        }

        // 2) Parse body
        final PlaceOrderRequest body;
        try {
            body = MAPPER.readValue(req.getInputStream(), PlaceOrderRequest.class);
        } catch (Exception e) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ApiResponse(false, "Request không hợp lệ", null, null));
            return;
        }

        // 3) Validate
        String mErr = validateBody(body);
        if (mErr != null) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ApiResponse(false, mErr, null, null));
            return;
        }

        // 4) Items map
        Map<Integer, Integer> listVarAndQty = body.items.stream()
                .collect(Collectors.toMap(it -> it.variantId, it -> it.quantity, (a, b) -> a));

        // 5) Payment method
        Integer method = parsePaymentMethod(body.paymentMethod);
        if (method == null) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ApiResponse(false, "Phương thức không hợp lệ", null, null));
            return;
        }

        try {
            // 6) Gọi service
            BigDecimal totalAmount = BigDecimal.valueOf(body.totalAmount);
            Orders order = placeOrderService.placeOrder(
                    userEmail,
                    body.addressId,
                    body.branchId,
                    listVarAndQty,
                    (body.voucherCode == null || body.voucherCode.isBlank()) ? null : body.voucherCode.trim(),
                    method,
                    totalAmount,
                    body.note
            );

            // 7) Khởi tạo thanh toán nếu online
            String paymentUrl = null;
            if (method != PaymentEnums.Method.COD)
                paymentUrl = req.getContextPath() + "/payment/create?orderId=" + order.getId();
            else
                paymentUrl = req.getContextPath() + "/orders/detail?id=" + order.getId();

            // 8) Response
            writeJson(resp, HttpServletResponse.SC_OK,
                    new ApiResponse(true, "Đặt hàng thành công", order.getId(), paymentUrl));

        } catch (IllegalArgumentException iae) {
            writeJson(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ApiResponse(false, "Có lỗi: " + iae.getMessage(), null, null));
        } catch (SecurityException se) {
            writeJson(resp, HttpServletResponse.SC_FORBIDDEN,
                    new ApiResponse(false, "Không được phép: " + se.getMessage(), null, null));
        } catch (Exception ex) {
            ex.printStackTrace();
            writeJson(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ApiResponse(false, "Có lỗi xảy ra. Vui lòng thử lại sau!", null, null));
        }
    }
}

