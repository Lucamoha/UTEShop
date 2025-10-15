package com.uteshop.filters;

import com.uteshop.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter để kiểm tra JWT token cho các trang cần authentication
 */
@WebFilter(urlPatterns = {"/*"})
public class JWTAuthenticationFilter implements Filter {

    // Danh sách các URL không cần authentication
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/login",
        "/register",
        "/logout",
        "/templates/",
        "/uploads/",
        "/image"
    );

    // Danh sách các URL cần authentication (protected)
    private static final List<String> PROTECTED_URLS = Arrays.asList(
        "/admin/",
        "/manager/",
        "/profile",
        "/cart",
        "/checkout",
        "/orders",
        "/api/manager"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("JWTAuthenticationFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = requestURI.substring(contextPath.length());


        // Cho phép các URL public đi qua không cần kiểm tra
        if (isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Kiểm tra các URL protected
        if (isProtectedUrl(path)) {
            String token = JWTUtil.extractTokenFromRequest(req);

            if (token == null) {
                // Kiểm tra nếu là AJAX request (cart APIs)
                if (isAjaxRequest(req, path)) {
                    // Trả về JSON cho AJAX request
                    resp.setContentType("application/json;charset=UTF-8");
                    
                    // Different response format for different endpoints
                    if ("/cart/count".equals(path)) {
                        resp.getWriter().write("{\"count\": 0, \"needLogin\": true}");
                    } else {
                        resp.getWriter().write("{\"success\": false, \"needLogin\": true, \"message\": \"Vui lòng đăng nhập để sử dụng giỏ hàng\"}");
                    }
                    return;
                } else {
                    // Redirect to login cho page request
                    redirectToLogin(req, resp, "Vui lòng đăng nhập để tiếp tục");
                    return;
                }
            }

            try {
                // Validate token
                Claims claims = JWTUtil.validateToken(token);
                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                // Lưu thông tin user vào request attribute
                req.setAttribute("authenticatedEmail", email);
                req.setAttribute("authenticatedRole", role);

                // Kiểm tra quyền truy cập dựa trên role
                if (path.startsWith("/admin/") && !"ADMIN".equals(role)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này");
                    return;
                }

                if (path.startsWith("/manager/") && !("MANAGER".equals(role) || "ADMIN".equals(role))) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này");
                    return;
                }

                // Token hợp lệ -> cho phép tiếp tục
                chain.doFilter(request, response);

            } catch (Exception e) {
                // Token không hợp lệ hoặc hết hạn
                System.err.println("JWT validation failed: " + e.getMessage());

                // Xóa token không hợp lệ
                JWTUtil.removeTokenFromCookie(resp);

                redirectToLogin(req, resp, "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại");
                return;
            }
        } else {
            // URL không protected -> cho phép đi qua nhưng vẫn set user info nếu có token
            String token = JWTUtil.extractTokenFromRequest(req);

            if (token != null) {
                try {
                    Claims claims = JWTUtil.validateToken(token);
                    String email = claims.getSubject();
                    String role = claims.get("role", String.class);

                    req.setAttribute("authenticatedEmail", email);
                    req.setAttribute("authenticatedRole", role);
                } catch (Exception e) {
                    // Token không hợp lệ -> bỏ qua, không redirect
                    System.err.println("Invalid token: " + e.getMessage());
                }
            }
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        System.out.println("JWTAuthenticationFilter destroyed");
    }

    private boolean isPublicUrl(String path) {
        return PUBLIC_URLS.stream().anyMatch(path::startsWith);
    }

    private boolean isProtectedUrl(String path) {
        return PROTECTED_URLS.stream().anyMatch(path::startsWith);
    }

    private boolean isAjaxRequest(HttpServletRequest req, String path) {
        // Check if it's an AJAX request based on:
        // 1. Path pattern (cart APIs: /cart/add, /cart/count, /cart/update, /cart/remove, /cart/items, /cart/branches, /cart/addresses, /cart/stock)
        // 2. X-Requested-With header
        // 3. Accept header contains application/json
        
        String requestedWith = req.getHeader("X-Requested-With");
        String accept = req.getHeader("Accept");
        
        boolean isCartApi = path.matches("/cart/(add|count|update|remove|items|branches|addresses|stock)");
        boolean hasAjaxHeader = "XMLHttpRequest".equals(requestedWith);
        boolean acceptsJson = accept != null && accept.contains("application/json");
        
        return isCartApi || hasAjaxHeader || acceptsJson;
    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp, String message)
            throws IOException {
        req.getSession().setAttribute("loginMessage", message);
        resp.sendRedirect(req.getContextPath() + "/login?redirect=" +
                         java.net.URLEncoder.encode(req.getRequestURI(), "UTF-8"));
    }
}
