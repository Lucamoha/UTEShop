package com.uteshop.services.impl.web.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.configs.AppConfig;
import com.uteshop.services.web.payment.IPaymentService;
import com.uteshop.util.JsonUtil;
import com.uteshop.util.SignUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class VnpayServiceImpl implements IPaymentService {
    private final String vnp_Version = AppConfig.get("vnpay.vnp_Version");
    private final String vnp_Command =  AppConfig.get("vnpay.vnp_Command");
    private final String vnp_TmnCode =  AppConfig.get("vnpay.vnp_TmnCode");
    private final String vnp_HashSecret = AppConfig.get("vnpay.vnp_HashSecret");
    private final String vnp_CurrCode = AppConfig.get("vnpay.vnp_CurrCode");
    private final String vnp_OrderType = AppConfig.get("vnpay.vnp_OrderType") ;
    private final String vnp_Url =  AppConfig.get("vnpay.vnp_Url");
    private final String vnp_Query =  AppConfig.get("vnpay.vnp_Query");
    private final String vnp_ReturnUrl =  AppConfig.get("vnpay.vnp_ReturnUrl");
    private final String vnp_IpAddr =  AppConfig.get("vnpay.vnp_IpAddr");

    private static String sendJson(String endpoint, String json) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setConnectTimeout(30000);
        con.setReadTimeout(30000);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            os.write(input);
        }

        int status = con.getResponseCode();
        InputStream is = (status >= 200 && status < 300) ? con.getInputStream() : con.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) response.append(line.trim());
        }

        con.disconnect();
        return response.toString();
    }


    @Override
    public CreatePaymentResponse create(CreatePaymentRequest req) throws PaymentException {
        try
        {
            Map<String, String> vnp_Params = new HashMap<>();

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());

            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", req.amount.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
            vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
            vnp_Params.put("vnp_TxnRef", "UTESHOP_REQ_" + System.currentTimeMillis());
            vnp_Params.put("vnp_OrderInfo", "UTESHOP_ORDER_" + req.orderId);
            vnp_Params.put("vnp_OrderType", vnp_OrderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())).append('&');
                    query.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())).append('&');
                }
            }
            String queryUrl = query.substring(0, query.length() - 1);
            String vnp_SecureHash = SignUtil.hmacSha512(hashData.substring(0, hashData.length() - 1), vnp_HashSecret);
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

            String paymentUrl = vnp_Url + "?" + queryUrl;

            return CreatePaymentResponse.okRedirect(paymentUrl, null, null);
        } catch (Exception e) {
            throw new PaymentException("VnPay create failed", e);
        }
    }

    @Override
    public RefundResponse refund(RefundRequest req) throws PaymentException {
        try
        {
            Map<String, String> vnp_Params = new HashMap<>();

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            vnp_Params.put("vnp_RequestId", "UTESHOP_REQ_" + System.currentTimeMillis());
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", "refund");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_TransactionType", "02"); // Hoàn toàn phần
            vnp_Params.put("vnp_TxnRef", req.gatewayTxnId);
            vnp_Params.put("vnp_Amount", req.amount.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toPlainString());
            vnp_Params.put("vnp_TransactionNo", "");
            vnp_Params.put("vnp_TransactionDate", req.meta.get("vnp_TransactionDate"));
            vnp_Params.put("vnp_CreateBy", "UTESHOP");
            vnp_Params.put("vnp_CreateDate",vnp_CreateDate);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_OrderInfo", "UTESHOP_REFUND_" + req.orderId);

            String hashData = String.join("|",
                    vnp_Params.get("vnp_RequestId"),
                    vnp_Params.get("vnp_Version"),
                    vnp_Params.get("vnp_Command"),
                    vnp_Params.get("vnp_TmnCode"),
                    vnp_Params.get("vnp_TransactionType"),
                    vnp_Params.get("vnp_TxnRef"),
                    vnp_Params.get("vnp_Amount"),
                    vnp_Params.get("vnp_TransactionNo"),
                    vnp_Params.get("vnp_TransactionDate"),
                    vnp_Params.get("vnp_CreateBy"),
                    vnp_Params.get("vnp_CreateDate"),
                    vnp_Params.get("vnp_IpAddr"),
                    vnp_Params.get("vnp_OrderInfo")
            );

            String vnp_SecureHash = SignUtil.hmacSha512(hashData, vnp_HashSecret);
            vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

            String body = new ObjectMapper().writeValueAsString(vnp_Params);
            System.out.println(body);

            String resp = sendJson(vnp_Query, body);
            System.out.println(resp);

            Map<String,String> parsed = JsonUtil.toFlatStringMap(resp);
            String resultCode = parsed.getOrDefault("vnp_ResponseCode","99");

            if ("00".equals(resultCode)) {
                String rfTransId  = Optional.ofNullable(parsed.get("vnp_TransactionNo")).orElse(req.orderId);
                return RefundResponse.ok(rfTransId, parsed);
            }
            return RefundResponse.fail("VnPay error resultCode=" + resultCode + " | msg=" + parsed.get("vnp_Message"), parsed);
        } catch (Exception e) {
            throw new PaymentException("VnPay refund failed", e);
        }
    }

    @Override
    public int queryStatus(QueryRequest req) throws PaymentException {
        return 0;
    }

    @Override
    public boolean verifyCallback(Map<String, String> body) {
        String received = body.get("vnp_SecureHash");
        if (received == null || received.isBlank())
            return false;

        Map<String, String> filtered = new java.util.HashMap<>();
        for (Map.Entry<String, String> e : body.entrySet()) {
            String k = e.getKey();
            if ("vnp_SecureHash".equalsIgnoreCase(k) || "vnp_SecureHashType".equalsIgnoreCase(k))
                continue;
            if (e.getValue() != null)
                filtered.put(k, e.getValue());
        }

        StringBuilder hashData = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(filtered.keySet());
        Collections.sort(fieldNames);

        for (String fieldName : fieldNames) {
            String fieldValue = filtered.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())).append('&');
                }
                catch (Exception e) {
                    System.out.println("VnPay verifyCallback error | msg=" + e.getMessage());
                    return false;
                }
            }
        }

        String calc = SignUtil.hmacSha512(hashData.substring(0, hashData.length() - 1), vnp_HashSecret);
        return calc.equalsIgnoreCase(received);
    }

    public static Map<String, String> parseQueryToMap(String query) {
        Map<String, String> map = new LinkedHashMap<>();
        if (query == null || query.isBlank()) return map;

        // Tách theo dấu &
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx == -1) continue; // bỏ qua nếu không có dấu =
            String key = pair.substring(0, idx);
            String value = pair.substring(idx + 1);
            try {
                // Giải mã URL (đề phòng trường hợp có %20, v.v.)
                key = java.net.URLDecoder.decode(key, java.nio.charset.StandardCharsets.UTF_8);
                value = java.net.URLDecoder.decode(value, java.nio.charset.StandardCharsets.UTF_8);
            } catch (Exception ignored) {}
            map.put(key, value);
        }
        return map;
    }
}
