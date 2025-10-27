package com.uteshop.controller.web.catalog;

import com.uteshop.dto.web.VariantDto;
import com.uteshop.services.impl.web.catalog.ProductVariantsServiceImpl;
import com.uteshop.services.web.catalog.IProductVariantsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = { "/variant/find" })
public class VariantController extends HttpServlet {

    private final IProductVariantsService variantsService = new ProductVariantsServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            String productIdStr = req.getParameter("productId");
            String optionValuesStr = req.getParameter("optionValues");

            if (productIdStr == null || optionValuesStr == null || optionValuesStr.trim().isEmpty()) {
                sendJsonResponse(resp, 400, false, "Missing parameters", null);
                return;
            }

            // Lấy OptionValueId từ url
            Integer productId = Integer.parseInt(productIdStr);
            List<Integer> optionValueIds = Arrays.stream(optionValuesStr.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            VariantDto variant = variantsService.findVariantByOptions(productId, optionValueIds);

            if (variant == null) {
                sendJsonResponse(resp, 404, false, "Variant not found", null);
                return;
            }
            sendJsonResponse(resp, 200, true, "Success", variant);

        } catch (NumberFormatException e) {
            sendJsonResponse(resp, 400, false, "Invalid parameter format", null);
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(resp, 500, false, "Server error: " + e.getMessage(), null);
        }
    }

    private void sendJsonResponse(HttpServletResponse resp, int status, boolean success,
            String message, VariantDto data) throws IOException {
        resp.setStatus(status);

        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"success\": ").append(success).append(",");
        json.append("\"message\": \"").append(message).append("\"");

        if (data != null) {
            json.append(",\"data\": {");
            json.append("\"variantId\": ").append(data.getVariantId()).append(",");
            json.append("\"sku\": \"").append(data.getSku()).append("\",");
            json.append("\"price\": ").append(data.getPrice());
            json.append(",\"status\": \"").append(data.getStatus()).append("\"");
            json.append("}");
        }

        json.append("}");

        resp.getWriter().write(json.toString());
    }
}
