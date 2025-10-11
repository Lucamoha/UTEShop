package com.uteshop.util;

import com.uteshop.configs.JWTConfigs;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

public class JWTUtil {
    private static final String SIGNER_KEY = JWTConfigs.getSignerKey();
    private static final long EXPIRATION_TIME = JWTConfigs.getExpiration();
    private static final String JWT_COOKIE_NAME = "JWT_TOKEN";

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SIGNER_KEY.getBytes());
    }

    /**
     * Tạo JWT token với email và role
     */
    public static String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validate và parse JWT token
     * 
     * @throws io.jsonwebtoken.JwtException nếu token không hợp lệ
     */
    public static Claims validateToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Lấy token từ request header (Authorization: Bearer <token>)
     */
    public static String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    /**
     * Lấy token từ Cookie
     */
    public static String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (JWT_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Lấy token từ request
     */
    public static String extractTokenFromRequest(HttpServletRequest request) {
        // Ưu tiên lấy từ Cookie
        String token = extractTokenFromCookie(request);
        if (token != null) {
            return token;
        }
        // Nếu không có trong Cookie, thử lấy từ Header
        return extractTokenFromHeader(request);
    }

    /**
     * Lưu JWT token vào Cookie
     * 
     * @param response HttpServletResponse
     * @param token    JWT token
     * @param remember true nếu "Ghi nhớ đăng nhập" được chọn (7 ngày), false cho
     *                 session cookie
     */
    public static void addTokenToCookie(HttpServletResponse response, String token, boolean remember) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, token);
        cookie.setHttpOnly(true); // Bảo mật: không cho JavaScript truy cập
        cookie.setPath("/"); // Cookie có hiệu lực trên toàn bộ website
        cookie.setSecure(false); // Set true nếu dùng HTTPS trong production

        if (remember) {
            // Ghi nhớ 7 ngày
            cookie.setMaxAge(7 * 24 * 60 * 60);
        } else {
            // Session cookie - hết hạn khi đóng browser
            cookie.setMaxAge(-1);
        }

        response.addCookie(cookie);
    }

    /**
     * Xóa JWT token khỏi Cookie (dùng khi logout)
     */
    public static void removeTokenFromCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Xóa ngay lập tức
        response.addCookie(cookie);
    }

    /**
     * Kiểm tra xem user có role cụ thể không
     */
    public static boolean hasRole(HttpServletRequest request, String requiredRole) {
        try {
            String token = extractTokenFromRequest(request);
            if (token == null)
                return false;

            Claims claims = validateToken(token);
            String role = claims.get("role", String.class);
            return requiredRole.equals(role);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Lấy email từ token trong request
     */
    public static String getEmailFromRequest(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            if (token == null)
                return null;

            Claims claims = validateToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Lấy role từ token trong request
     */
    public static String getRoleFromRequest(HttpServletRequest request) {
        try {
            String token = extractTokenFromRequest(request);
            if (token == null)
                return null;

            Claims claims = validateToken(token);
            return claims.get("role", String.class);
        } catch (Exception e) {
            return null;
        }
    }
}