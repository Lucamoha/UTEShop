package com.uteshop.controller.web.payment;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.entity.order.Orders;
import com.uteshop.enums.OrderEnums;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.impl.web.payment.MomoServiceImpl;
import com.uteshop.services.impl.web.payment.VnpayServiceImpl;
import com.uteshop.services.web.payment.IPaymentService;
import com.uteshop.util.SignUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet(urlPatterns = {"/payment/create"})
public class PaymentController extends HttpServlet {
    private final MomoServiceImpl momoService = new MomoServiceImpl();
    private final VnpayServiceImpl vnpayService = new VnpayServiceImpl();
    private EntityDaoImpl<Orders> ordersDao = new EntityDaoImpl<>(Orders.class);
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            String orderIdStr = req.getParameter("orderId");
            if (orderIdStr == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu orderId");
                return;
            }

            Integer orderId = Integer.parseInt(orderIdStr);
            Orders order = ordersDao.findById(orderId);
            if (order == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng");
                return;
            }

            Integer method = order.getPayment().getMethod();
            // Đơn hàng có phương thức là COD hoặc đã thanh toán
            if (method == PaymentEnums.Method.COD || order.getPaymentStatus() != OrderEnums.PaymentStatus.UNPAID) {
                resp.sendRedirect(req.getContextPath() + "/orders/detail?id=" + orderId);
                return;
            }

            // Gọi service tương ứng
            IPaymentService.CreatePaymentResponse createPaymentResponse = null;
            BigDecimal amount = order.getTotalAmount();

            if (method == PaymentEnums.Method.MOMO) {
                IPaymentService.CreatePaymentRequest createReq = new IPaymentService.CreatePaymentRequest();
                createReq.orderId = String.valueOf(order.getId());
                createReq.amount = amount;

                // Gắn extraData: orderId + paymentId
                var payment = order.getPayment();
                var extraMap = Map.of(
                        "orderDbId", order.getId(),
                        "paymentId", (payment != null ? payment.getId() : null)
                );
                createReq.extraData = SignUtil.b64Json(MAPPER.writeValueAsString(extraMap));
                createPaymentResponse = momoService.create(createReq);
            } else if (method == PaymentEnums.Method.VNPAY) {
                IPaymentService.CreatePaymentRequest createReq = new IPaymentService.CreatePaymentRequest();
                createReq.orderId = String.valueOf(order.getId());
                createReq.amount = amount;

                createPaymentResponse = vnpayService.create(createReq);
            }

            if (createPaymentResponse == null || createPaymentResponse.checkoutUrl == null) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể tạo giao dịch thanh toán");
                return;
            }

            // Redirect sang cổng thanh toán
            resp.sendRedirect(createPaymentResponse.checkoutUrl);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "orderId không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi xử lý thanh toán");
        }
    }
}
