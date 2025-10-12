package com.uteshop.api.manager.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteshop.services.impl.manager.InventoryManagerServiceImpl;
import com.uteshop.services.manager.IInventoryManagerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/api/manager/inventory/bulk-import")
public class BulkImportApi extends HttpServlet {

    private final IInventoryManagerService iInventoryManagerService = new InventoryManagerServiceImpl();
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Pattern LINE = Pattern.compile(
            "^\\s*([A-Za-z0-9._\\-]{1,80})\\s*\\|\\s*([+-]?\\d+)\\s*$"
    );
    private static final Pattern SEP = Pattern.compile("\\s*\\|\\s*|\\s*,\\s*|\\s+"); // | , hoặc space
    private static final int MAX_ITEMS = 5000; // giới hạn an toàn

    private Integer branchId(HttpServletRequest req){
        return (Integer) req.getSession().getAttribute("branchId");
    }

    private static String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder(4096);
        try (BufferedReader br = req.getReader()) {
            char[] buf = new char[4096];
            int n;
            while ((n = br.read(buf)) != -1) sb.append(buf, 0, n);
        }
        String s = sb.toString();
        if (!s.isEmpty() && s.charAt(0) == '\uFEFF') {
            s = s.substring(1); // strip BOM
        }
        return s;
    }

    private static void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(resp.getOutputStream(), body);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        public String sku;
        public Integer delta;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestBody {
        public List<Item> items;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Integer branchId = branchId(req);
        if (branchId == null) {
            writeJson(resp, 401, Map.of("ok", false, "error", "Unauthenticated/No branch"));
            return;
        }

        // 1) Ưu tiên JSON body: { "items":[{"sku":"A","delta":10}, ...] }
        List<Item> items = null;
        String ct = Optional.ofNullable(req.getContentType()).orElse("");
        String body = null;

        if (ct.toLowerCase(Locale.ROOT).contains("application/json")) {
            body = readBody(req).trim();
            if (!body.isEmpty()) {
                try {
                    RequestBody rb = MAPPER.readValue(body, RequestBody.class);
                    items = rb != null ? rb.items : null;
                } catch (Exception ignored) {}
            }
        }

        // 2) Fallback: form-urlencoded ?payload=...
        if (items == null || items.isEmpty()) {
            String payload = req.getParameter("payload");
            if (payload == null) {
                // 3) Fallback: raw text body
                if (body == null) body = readBody(req);
                payload = body;
            }
            items = parseLoosePayload(payload);
        }

        if (items == null || items.isEmpty()) {
            writeJson(resp, 400, Map.of("ok", false, "error", "Không có dòng hợp lệ"));
            return;
        }
        if (items.size() > MAX_ITEMS) {
            writeJson(resp, 413, Map.of("ok", false, "error", "Quá nhiều dòng (>" + MAX_ITEMS + ")"));
            return;
        }

        // Chuẩn hóa: sku hợp lệ, delta là số, gom trùng
        Map<String, Integer> deltaBySku = new LinkedHashMap<>();
        List<String> invalid = new ArrayList<>();

        for (Item it : items) {
            if (it == null || it.sku == null || it.delta == null) { continue; }
            String sku = it.sku.trim();
            if (!sku.matches("^[A-Za-z0-9._\\-]{1,80}$")) {
                invalid.add("SKU không hợp lệ: " + sku);
                continue;
            }
            deltaBySku.put(sku, deltaBySku.getOrDefault(sku, 0) + it.delta);
        }

        if (deltaBySku.isEmpty()) {
            String err = invalid.isEmpty() ? "Không có dòng hợp lệ" : invalid.get(0);
            writeJson(resp, 400, Map.of("ok", false, "error", err));
            return;
        }

        // Gọi DAO
        Map<String, Integer> result = iInventoryManagerService.bulkAdjustBySku(branchId, deltaBySku);

        // Xây not_found (những sku gửi lên nhưng DAO không trả về)
        Set<String> requested = deltaBySku.keySet();
        Set<String> found = result != null ? result.keySet() : Set.of();
        List<String> notFound = requested.stream()
                .filter(sku -> !found.contains(sku))
                .collect(Collectors.toList());

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("total", items.size());
        summary.put("valid", deltaBySku.size());
        summary.put("updated", found.size());
        summary.put("not_found", notFound);
        if (!invalid.isEmpty()) summary.put("invalid_lines", invalid);

        writeJson(resp, 200, Map.of(
                "ok", true,
                "summary", summary,
                "result", result == null ? Map.of() : result
        ));
    }


    private static List<Item> parseLoosePayload(String payload) {
        if (payload == null) return Collections.emptyList();
        payload = payload.trim();
        if (payload.isEmpty()) return Collections.emptyList();

        // Thử JSON trước (trong trường hợp client gửi JSON nhưng content-type không đúng)
        try {
            RequestBody rb = MAPPER.readValue(payload, RequestBody.class);
            if (rb != null && rb.items != null && !rb.items.isEmpty()) return rb.items;
        } catch (Exception ignored) {}

        List<Item> out = new ArrayList<>();
        String[] lines = payload.split("\\r?\\n");
        for (String raw : lines) {
            if (raw == null) continue;
            String s = raw.trim();
            if (s.isEmpty()) continue;
            if (s.startsWith("#") || s.startsWith("//")) continue;
            if (s.charAt(0) == '\uFEFF') s = s.substring(1);

            // Ưu tiên regex dạng SKU|±N
            var m = LINE.matcher(s);
            if (m.matches()) {
                Item it = new Item();
                it.sku = m.group(1);
                it.delta = Integer.parseInt(m.group(2));
                out.add(it);
                continue;
            }

            // Thử tách bởi |, , hoặc whitespace
            String[] parts = SEP.split(s);
            if (parts.length == 2) {
                String sku = parts[0].trim();
                String num = parts[1].trim();
                if (sku.matches("^[A-Za-z0-9._\\-]{1,80}$") && num.matches("^[+-]?\\d+$")) {
                    Item it = new Item();
                    it.sku = sku;
                    it.delta = Integer.parseInt(num);
                    out.add(it);
                }
            }
        }
        return out;
    }
}
