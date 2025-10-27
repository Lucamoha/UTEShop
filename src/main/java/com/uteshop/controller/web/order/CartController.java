package com.uteshop.controller.web.order;

import com.google.gson.Gson;
import com.uteshop.entity.auth.Addresses;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.branch.Branches;
import com.uteshop.entity.cart.Vouchers;
import com.uteshop.services.impl.web.account.AddressesServiceImpl;
import com.uteshop.services.impl.web.account.UsersServiceImpl;
import com.uteshop.services.impl.web.branch.BranchesServiceImpl;
import com.uteshop.services.impl.web.catalog.CategoriesServiceImpl;
import com.uteshop.services.impl.web.order.CartsServiceImpl;
import com.uteshop.services.impl.web.order.VouchersServiceImpl;
import com.uteshop.services.web.account.IAddressesService;
import com.uteshop.services.web.account.IUsersService;
import com.uteshop.services.web.branch.IBranchesService;
import com.uteshop.services.web.catalog.ICategoriesService;
import com.uteshop.services.web.order.ICartsService;
import com.uteshop.services.web.order.IVouchersService;
import com.uteshop.entity.catalog.Categories;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/count", "/cart/items", "/cart/branches", "/cart/addresses", "/cart/stock", "/cart/apply-voucher", "/cart/vouchers"})
public class CartController extends HttpServlet {
    
    private final ICartsService cartsService = new CartsServiceImpl();
    private final IUsersService usersService = new UsersServiceImpl();
    private final IBranchesService branchesService = new BranchesServiceImpl();
    private final IAddressesService addressesService = new AddressesServiceImpl();
    private final IVouchersService vouchersService = new VouchersServiceImpl();
    private final ICategoriesService categoriesService = new CategoriesServiceImpl();
    private final Gson gson = new Gson();
    
    /**
     * Lấy userId từ authenticated email
     */
    private Integer getUserId(HttpServletRequest req) {
        String email = (String) req.getAttribute("authenticatedEmail");
        if (email == null) {
            return null;
        }
        Users user = usersService.findByEmail(email);
        return user != null ? user.getId() : null;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        
        // Branches và stock không cần authentication (public data)
        if ("/cart/branches".equals(path)) {
            handleGetBranches(req, resp);
            return;
        }
        
        if ("/cart/stock".equals(path)) {
            handleGetStock(req, resp);
            return;
        }
        
        // Kiểm tra login cho các endpoints còn lại
        Integer userId = getUserId(req);
        if (userId == null) {
            // AJAX endpoints - trả JSON
            if ("/cart/count".equals(path) || "/cart/items".equals(path) || "/cart/addresses".equals(path)) {
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"success\": false, \"needLogin\": true, \"message\": \"Vui lòng đăng nhập\"}");
                return;
            }
            // Page request - redirect to login
            req.getSession().setAttribute("loginMessage", "Vui lòng đăng nhập để sử dụng giỏ hàng");
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + 
                    java.net.URLEncoder.encode("/cart", "UTF-8"));
            return;
        }
        
        switch (path) {
            case "/cart":
                // Kiểm tra và xóa các sản phẩm có variant.status = false
                List<Map<String, String>> removedItems = cartsService.validateAndRemoveInactiveItems(userId);
                System.out.println("[DEBUG] validateAndRemoveInactiveItems returned " + removedItems.size() + " items");
                
                if (!removedItems.isEmpty()) {
                    // Lưu vào session để thông báo hiển thị lần đầu tiên user vào cart sau khi bị xóa
                    // Session sẽ được clear trong JSP sau khi hiển thị
                    req.getSession().setAttribute("removedItems", removedItems);
                    System.out.println("[DEBUG] Set removedItems in session: " + removedItems);
                } else {
                    System.out.println("[DEBUG] No items removed, session removedItems: " + 
                        req.getSession().getAttribute("removedItems"));
                }
                
                // Load menu cha cho header
                List<Categories> parents = categoriesService.findParents();
                req.setAttribute("parentCategories", parents);
                
                // View cart page
                Map<String, Object> cartData = cartsService.calculateCartTotal(userId);
                req.setAttribute("cartData", cartData);
                req.getRequestDispatcher("/views/web/cart.jsp").forward(req, resp);
                break;
                
            case "/cart/count":
                // Get cart item count (AJAX)
                int count = cartsService.getCartItemCount(userId);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"count\": " + count + ", \"needLogin\": false}");
                break;
                
            case "/cart/items":
                // Get cart items for popup (AJAX)
                handleGetCartItems(req, resp, userId);
                break;
                
            case "/cart/addresses":
                // Get user addresses (AJAX)
                handleGetAddresses(req, resp, userId);
                break;
                
            case "/cart/vouchers":
                // Get available vouchers (AJAX)
                handleGetVouchers(req, resp);
                break;
                
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        
        String path = req.getServletPath();
        
        // Kiểm tra login
        Integer userId = getUserId(req);
        if (userId == null) {
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "needLogin", true,
                "message", "Vui lòng đăng nhập để sử dụng giỏ hàng"
            )));
            return;
        }
        
        try {
            switch (path) {
                case "/cart/add":
                    handleAddToCart(req, resp, userId);
                    break;
                    
                case "/cart/update":
                    handleUpdateCart(req, resp, userId);
                    break;
                    
                case "/cart/remove":
                    handleRemoveFromCart(req, resp, userId);
                    break;
                    
                case "/cart/apply-voucher":
                    handleApplyVoucher(req, resp, userId);
                    break;
                    
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Có lỗi xảy ra: " + e.getMessage()
            )));
        }
    }
    
    private void handleAddToCart(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws IOException {
        try {
            String productIdStr = req.getParameter("productId");
            String quantityStr = req.getParameter("quantity");
            String optionsJson = req.getParameter("options");
            
            if (productIdStr == null || quantityStr == null) {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Thiếu thông tin sản phẩm"
                )));
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            // Parse option value IDs
            List<Integer> optionValueIds = new ArrayList<>();
            if (optionsJson != null && !optionsJson.isEmpty()) {
                int[] options = gson.fromJson(optionsJson, int[].class);
                for (int opt : options) {
                    optionValueIds.add(opt);
                }
            }
            
            boolean success = cartsService.addToCart(userId, productId, optionValueIds, quantity);
            
            if (success) {
                int newCount = cartsService.getCartItemCount(userId);
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", true,
                    "message", "Đã thêm vào giỏ hàng",
                    "cartCount", newCount
                )));
            } else {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Không thể thêm vào giỏ hàng. Vui lòng chọn đầy đủ thông tin sản phẩm."
                )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi: " + e.getMessage()
            )));
        }
    }
    
    private void handleUpdateCart(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws IOException {
        try {
            String itemIdStr = req.getParameter("itemId");
            String quantityStr = req.getParameter("quantity");
            
            if (itemIdStr == null || quantityStr == null) {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Thiếu thông tin"
                )));
                return;
            }
            
            int itemId = Integer.parseInt(itemIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            boolean success = cartsService.updateCartItemQuantity(userId, itemId, quantity);
            
            if (success) {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", true,
                    "message", "Đã cập nhật giỏ hàng"
                )));
            } else {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Không thể cập nhật"
                )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi: " + e.getMessage()
            )));
        }
    }
    
    private void handleRemoveFromCart(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws IOException {
        try {
            String itemIdStr = req.getParameter("itemId");
            
            if (itemIdStr == null) {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Thiếu thông tin"
                )));
                return;
            }
            
            int itemId = Integer.parseInt(itemIdStr);
            boolean success = cartsService.removeCartItem(userId, itemId);
            
            if (success) {
                int newCount = cartsService.getCartItemCount(userId);
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", true,
                    "message", "Đã xóa khỏi giỏ hàng",
                    "cartCount", newCount
                )));
            } else {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Không thể xóa"
                )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi: " + e.getMessage()
            )));
        }
    }
    
    /**
     * Lấy danh sách items trong giỏ hàng cho popup (AJAX)
     */
    private void handleGetCartItems(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws IOException {
        try {
            resp.setContentType("application/json;charset=UTF-8");
            
            // Kiểm tra và xóa các sản phẩm có variant.status = false
            List<Map<String, String>> removedItems = cartsService.validateAndRemoveInactiveItems(userId);
            System.out.println("[DEBUG /cart/items] validateAndRemoveInactiveItems returned " + removedItems.size() + " items");
            
            // CRITICAL: Lưu vào session để thông báo hiện khi user vào /cart page sau này
            if (!removedItems.isEmpty()) {
                req.getSession().setAttribute("removedItems", removedItems);
                System.out.println("[DEBUG /cart/items] Saved removedItems to session: " + removedItems);
            }
            
            List<com.uteshop.entity.cart.CartItems> items = cartsService.getCartItems(userId);
            Map<String, Object> cartData = cartsService.calculateCartTotal(userId);
            
            // Build JSON response
            List<java.util.HashMap<String, Object>> itemList = new ArrayList<>();
            
            for (com.uteshop.entity.cart.CartItems item : items) {
                java.util.HashMap<String, Object> itemMap = new java.util.HashMap<>();
                itemMap.put("id", item.getId());
                itemMap.put("productName", item.getProduct().getName());
                itemMap.put("productSlug", item.getProduct().getSlug());
                itemMap.put("productImage", item.getProduct().getImages().isEmpty() ? "" : item.getProduct().getImages().get(0).getImageUrl());
                itemMap.put("variantSKU", item.getVariant() != null ? item.getVariant().getSKU() : "");
                itemMap.put("price", item.getVariant() != null ? item.getVariant().getPrice() : item.getProduct().getBasePrice());
                itemMap.put("quantity", item.getQuantity());
                itemList.add(itemMap);
            }
            
            java.util.HashMap<String, Object> response = new java.util.HashMap<>();
            response.put("success", true);
            response.put("items", itemList);
            response.put("total", cartData.get("total") != null ? cartData.get("total") : 0);
            response.put("count", items.size());
            
            // Thêm thông tin về sản phẩm đã bị xóa (nếu có)
            if (!removedItems.isEmpty()) {
                response.put("removedItems", removedItems);
                StringBuilder message = new StringBuilder("Các sản phẩm sau đã ngừng kinh doanh và đã được xóa khỏi giỏ hàng: ");
                for (int i = 0; i < removedItems.size(); i++) {
                    Map<String, String> item = removedItems.get(i);
                    message.append(item.get("productName")).append(" (SKU: ").append(item.get("sku")).append(")");
                    if (i < removedItems.size() - 1) {
                        message.append(", ");
                    }
                }
                response.put("warningMessage", message.toString());
            }
            
            resp.getWriter().write(gson.toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi: " + errorMsg
            )));
        }
    }
    
    /**
     * API lấy danh sách chi nhánh active
     */
    private void handleGetBranches(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json;charset=UTF-8");
            
            List<Branches> branches = branchesService.getAllActiveBranches();
            
            List<Map<String, Object>> branchList = new ArrayList<>();
            for (Branches branch : branches) {
                Map<String, Object> branchMap = Map.of(
                    "id", branch.getId(),
                    "name", branch.getName(),
                    "address", branch.getAddress() != null ? branch.getAddress() : "",
                    "phone", branch.getPhone() != null ? branch.getPhone() : ""
                );
                branchList.add(branchMap);
            }
            
            resp.getWriter().write(gson.toJson(Map.of(
                "success", true,
                "branches", branchList
            )));
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi khi lấy danh sách chi nhánh"
            )));
        }
    }
    
    /**
     * API lấy danh sách địa chỉ của user
     */
    private void handleGetAddresses(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws IOException {
        try {
            resp.setContentType("application/json;charset=UTF-8");
            
            System.out.println("[CartController] Getting addresses for userId: " + userId);
            List<Addresses> addresses = addressesService.getUserAddresses(userId);
            System.out.println("[CartController] Found " + addresses.size() + " addresses");
            
            List<Map<String, Object>> addressList = new ArrayList<>();
            for (Addresses address : addresses) {
                String fullAddress = address.getAddressLine();
                if (address.getWard() != null && !address.getWard().isEmpty()) {
                    fullAddress += ", " + address.getWard();
                }
                if (address.getDistrict() != null && !address.getDistrict().isEmpty()) {
                    fullAddress += ", " + address.getDistrict();
                }
                if (address.getCity() != null && !address.getCity().isEmpty()) {
                    fullAddress += ", " + address.getCity();
                }
                
                Map<String, Object> addressMap = Map.of(
                    "id", address.getId(),
                    "label", address.getLabel() != null ? address.getLabel() : "Địa chỉ",
                    "fullName", address.getFullName(),
                    "phone", address.getPhone(),
                    "fullAddress", fullAddress,
                    "isDefault", address.getIsDefault()
                );
                addressList.add(addressMap);
            }
            
            resp.getWriter().write(gson.toJson(Map.of(
                "success", true,
                "addresses", addressList
            )));
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi khi lấy danh sách địa chỉ"
            )));
        }
    }
    
    /**
     * Apply voucher code
     */
    private void handleApplyVoucher(HttpServletRequest req, HttpServletResponse resp, Integer userId) throws IOException {
        try {
            resp.setContentType("application/json;charset=UTF-8");
            
            String voucherCode = req.getParameter("voucherCode");
            
            if (voucherCode == null || voucherCode.trim().isEmpty()) {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Vui lòng nhập mã voucher"
                )));
                return;
            }
            
            // Calculate cart total with voucher
            Map<String, Object> cartData = cartsService.calculateCartTotalWithVoucher(userId, voucherCode);
            
            if (cartData.get("voucherError") != null) {
                // Voucher không hợp lệ
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", cartData.get("voucherError")
                )));
                return;
            }
            
            if ((Boolean) cartData.get("voucherApplied")) {
                // Voucher hợp lệ
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", true,
                    "message", cartData.get("voucherMessage"),
                    "voucherCode", cartData.get("voucherCode"),
                    "typeName", cartData.get("typeName"),
                    "voucherType", cartData.get("voucherType"),
                    "voucherValue", cartData.get("voucherValue"),
                    "discountAmount", cartData.get("discountAmount"),
                    "total", cartData.get("total"),
                    "subtotal", cartData.get("subtotal")
                )));
            } else {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Không thể áp dụng voucher"
                )));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi khi áp dụng voucher: " + e.getMessage()
            )));
        }
    }
    
    /**
     * API check tồn kho theo chi nhánh và variants
     * Parameters: branchId, variantIds (comma separated)
     */
    private void handleGetStock(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json;charset=UTF-8");
            
            String branchIdStr = req.getParameter("branchId");
            String variantIdsStr = req.getParameter("variantIds");
            
            if (branchIdStr == null || variantIdsStr == null) {
                resp.getWriter().write(gson.toJson(Map.of(
                    "success", false,
                    "message", "Thiếu tham số branchId hoặc variantIds"
                )));
                return;
            }
            
            Integer branchId = Integer.parseInt(branchIdStr);
            String[] variantIdArray = variantIdsStr.split(",");
            List<Integer> variantIds = new ArrayList<>();
            for (String id : variantIdArray) {
                variantIds.add(Integer.parseInt(id.trim()));
            }
            
            Map<Integer, Integer> stockMap = branchesService.getBranchStockForVariants(branchId, variantIds);
            
            resp.getWriter().write(gson.toJson(Map.of(
                "success", true,
                "stocks", stockMap
            )));
        } catch (NumberFormatException e) {
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Tham số không hợp lệ"
            )));
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi khi lấy thông tin tồn kho"
            )));
        }
    }
    
    /**
     * API lấy danh sách vouchers còn hiệu lực
     */
    private void handleGetVouchers(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json;charset=UTF-8");
            
            List<Vouchers> vouchers = vouchersService.getAvailableVouchers();
            
            // Convert to simple DTO for JSON response
            List<Map<String, Object>> voucherList = new ArrayList<>();
            for (Vouchers v : vouchers) {
                Map<String, Object> voucherData = new java.util.HashMap<>();
                voucherData.put("code", v.getCode());
                voucherData.put("description", v.getDescText());
                voucherData.put("type", v.getType());
                voucherData.put("value", v.getValue());
                voucherData.put("typeName", v.getTypeName());
                voucherData.put("remainingUses", v.getMaxUses() - v.getTotalUsed());
                voucherList.add(voucherData);
            }
            
            resp.getWriter().write(gson.toJson(Map.of(
                "success", true,
                "vouchers", voucherList
            )));
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write(gson.toJson(Map.of(
                "success", false,
                "message", "Lỗi khi lấy danh sách vouchers"
            )));
        }
    }
}
