package com.uteshop.services.web.payment;

import java.math.BigDecimal;
import java.util.Map;

public interface IPaymentService {
    CreatePaymentResponse create(CreatePaymentRequest req) throws PaymentException;

    RefundResponse refund(RefundRequest req) throws PaymentException;

    /**
     * Kiểm tra trạng thái giao dịch
     * PENDING = 0, SUCCESS = 1, FAILED = 2, REFUNDED = 3
     */
    int queryStatus(QueryRequest req) throws PaymentException;

    /**
     * Xác minh chữ ký/callback do cổng gửi về (IPN/returnURL).
     * @param body toàn bộ cặp key=value nhận được
     * @return true nếu hợp lệ chữ ký & dữ liệu
     */
    boolean verifyCallback(Map<String, String> body);


    class CreatePaymentRequest {
        public String orderId;          // mã đơn nội bộ (duy nhất trong hệ thống)
        public BigDecimal amount;       // số tiền VND
        public String orderInfo;        // mô tả ngắn
        public String clientIp;         // IP của khách (VNPAY yêu cầu)
        public String extraData;        // dữ liệu thêm (ví dụ userId|branchId)
        public Map<String, String> meta; // tuỳ biến (lang, bankCode, etc.)

        // builder tiện dụng (tuỳ chọn)
        public CreatePaymentRequest withMeta(Map<String, String> m) { this.meta = m; return this; }
    }

    class CreatePaymentResponse {
        public boolean success;
        public String message;

        public String checkoutUrl;
        public String gatewayTxnId;  // mã giao dịch do cổng sinh ra (nếu có)
        public String signature;     // chữ ký request (để debug/đối chiếu)
        public Map<String, String> raw; // dữ liệu gốc gửi đi/nhận về

        public static CreatePaymentResponse okRedirect(String url, String txnId, Map<String,String> raw){
            var r = new CreatePaymentResponse();
            r.success = true; r.checkoutUrl = url; r.gatewayTxnId = txnId; r.raw = raw;
            return r;
        }
        public static CreatePaymentResponse fail(String msg, Map<String,String> raw){
            var r = new CreatePaymentResponse();
            r.success = false; r.message = msg; r.raw = raw;
            return r;
        }
    }

    class RefundRequest {
        public String orderId;        // mã đơn nội bộ
        public String gatewayTxnId;   // mã giao dịch của cổng (MoMo/VNPAY)
        public BigDecimal amount;     // số tiền hoàn (<= số đã thanh toán)
        public Map<String, String> meta;

        public RefundRequest withMeta(Map<String, String> m) { this.meta = m; return this; }
    }

    class RefundResponse {
        public boolean success;
        public String message;
        public String refundTxnId;     // mã giao dịch hoàn bên cổng
        public Map<String, String> raw;

        public static RefundResponse ok(String refundTxn, Map<String,String> raw){
            var r = new RefundResponse();
            r.success = true; r.refundTxnId = refundTxn; r.raw = raw;
            return r;
        }
        public static RefundResponse fail(String msg, Map<String,String> raw){
            var r = new RefundResponse();
            r.success = false; r.message = msg; r.raw = raw;
            return r;
        }
    }

    class QueryRequest {
        public String orderId;       // tuỳ chọn: mã đơn nội bộ
        public String gatewayTxnId;  // tuỳ chọn: mã giao dịch của cổng
        public Map<String, String> meta;
    }

    class PaymentException extends Exception {
        public PaymentException(String message) { super(message); }
        public PaymentException(String message, Throwable cause) { super(message, cause); }
    }
}
