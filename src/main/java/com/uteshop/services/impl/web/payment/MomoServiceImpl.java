package com.uteshop.services.impl.web.payment;

import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.web.payment.IPaymentService;
import com.uteshop.configs.AppConfig;
import com.uteshop.util.JsonUtil;
import com.uteshop.util.SignUtil;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class MomoServiceImpl implements IPaymentService {
    private final String partnerCode = AppConfig.get("momo.partnerCode");
    private final String accessKey =  AppConfig.get("momo.accessKey");
    private final String secretKey =  AppConfig.get("momo.secretKey");
    private final String endpointCreate = AppConfig.get("momo.endpointCreate");
    private final String endpointQuery = AppConfig.get("momo.endpointQuery");
    private final String endpointRefund = AppConfig.get("momo.endpointRefund");
    private final String defaultReturnUrl = AppConfig.get("momo.redirectUrl") ;
    private final String defaultNotifyUrl =  AppConfig.get("momo.ipnUrl");

    private static String sendJson(String endpoint, String json) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setConnectTimeout(30000);
        con.setReadTimeout(30000);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int status = con.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? con.getInputStream()
                : con.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }

        con.disconnect();
        return response.toString();
    }

    @Override
    public CreatePaymentResponse create(CreatePaymentRequest req) throws PaymentException {
        try {
            String orderId   = "UTESHOP_ORDER_" + req.orderId;
            String requestId = "UTESHOP_REQ_" + System.currentTimeMillis();
            String amountStr = req.amount.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();
            String orderInfo = "Thanh toan don #" + req.orderId;
            String extraData = Optional.ofNullable(req.extraData).orElse("");

            String requestType = "captureWallet";

            // Raw string để ký theo tài liệu MoMo v2
            String rawSignature = "accessKey=" + accessKey
                    + "&amount=" + amountStr
                    + "&extraData=" + extraData
                    + "&ipnUrl=" + defaultNotifyUrl
                    + "&orderId=" + orderId
                    + "&orderInfo=" + orderInfo
                    + "&partnerCode=" + partnerCode
                    + "&redirectUrl=" + defaultReturnUrl
                    + "&requestId=" + requestId
                    + "&requestType=" + requestType;

            String signature = SignUtil.hmacSHA256(rawSignature, secretKey);

            // JSON body
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("partnerCode", partnerCode);
            body.put("requestId", requestId);
            body.put("amount", amountStr);
            body.put("orderId", orderId);
            body.put("orderInfo", orderInfo);
            body.put("redirectUrl", defaultReturnUrl);
            body.put("ipnUrl", defaultNotifyUrl);
            body.put("requestType", requestType);
            body.put("extraData", extraData);
            body.put("lang", "vi");
            body.put("signature", signature);

            String payload = JsonUtil.toJson(body);

            String resp = sendJson(endpointCreate, JsonUtil.toJson(body));
            Map<String,String> parsed = JsonUtil.toFlatStringMap(resp);

            String resultCode = parsed.getOrDefault("resultCode","9999");
            if ("0".equals(resultCode)) {
                String payUrl   = parsed.get("payUrl");
                String transId  = Optional.ofNullable(parsed.get("transId")).orElse(orderId);
                return CreatePaymentResponse.okRedirect(payUrl, transId, parsed);
            }
            return CreatePaymentResponse.fail("MoMo error resultCode=" + resultCode + " | msg=" + parsed.get("message"), parsed);

        } catch (Exception e) {
            throw new PaymentException("MoMo create failed", e);
        }
    }

    @Override
    public RefundResponse refund(RefundRequest req) throws PaymentException {
        try {
            String orderId   = "UTESHOP_ORDER_" + req.orderId + "_" + + System.currentTimeMillis();
            String requestId = "UTESHOP_REQ_" + System.currentTimeMillis();
            String transId   = req.gatewayTxnId != null ? req.gatewayTxnId : "0";
            String amountStr = req.amount.setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString();

            String raw = "accessKey=" + accessKey
                    + "&amount=" + amountStr
                    + "&description=" + "UTESHOP_REFUND"
                    + "&orderId=" + orderId
                    + "&partnerCode=" + partnerCode
                    + "&requestId=" + requestId
                    + "&transId=" + transId;

            String signature = SignUtil.hmacSHA256(raw, secretKey);
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("partnerCode", partnerCode);
            payload.put("accessKey", accessKey);
            payload.put("requestId", requestId);
            payload.put("orderId", orderId);
            payload.put("amount", amountStr);
            payload.put("transId", transId);
            payload.put("lang", "vi");
            payload.put("description", "UTESHOP_REFUND");
            payload.put("signature", signature);

            String resp = sendJson(endpointRefund, JsonUtil.toJson(payload));
            Map<String,String> parsed = JsonUtil.toFlatStringMap(resp);

            String resultCode = parsed.getOrDefault("resultCode","9999");
            if ("0".equals(resultCode)) {
                String rfTransId  = Optional.ofNullable(parsed.get("transId")).orElse(orderId);
                return RefundResponse.ok(rfTransId, parsed);
            }
            return RefundResponse.fail("MoMo error resultCode=" + resultCode + " | msg=" + parsed.get("message"), parsed);
        } catch (Exception e) {
            throw new PaymentException("MoMo refund failed", e);
        }
    }

    @Override
    public int queryStatus(QueryRequest req) throws PaymentException {
        return 0;
    }

    @Override
    public boolean verifyCallback(Map<String, String> body) {
        String raw = "accessKey=" + accessKey + "&" + SignUtil.canonicalize(body, Set.of("signature"));
        String calc = SignUtil.hmacSHA256(raw, secretKey);
        return calc.equalsIgnoreCase(String.valueOf(body.get("signature")));
    }

    private int momoMapStatus(String resultCode) {
        if ("0".equals(resultCode)) return PaymentEnums.Status.SUCCESS;
        if (Arrays.asList("9000","7002", "7000").contains(resultCode)) return PaymentEnums.Status.PENDING;
        return PaymentEnums.Status.FAILED;
    }
}
