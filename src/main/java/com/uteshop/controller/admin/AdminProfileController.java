package com.uteshop.controller.admin;

import com.uteshop.entity.auth.Users;
import com.uteshop.services.impl.web.account.UsersServiceImpl;
import com.uteshop.services.web.account.IUsersService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/profile", "/admin/profile/*"})
public class AdminProfileController extends HttpServlet {
    
    private final IUsersService usersService = new UsersServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        
        // Lấy authenticatedEmail từ request attribute (do filter set)
        String authenticatedEmail = (String) req.getAttribute("authenticatedEmail");
        
        if (authenticatedEmail == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Users user = usersService.findByEmail(authenticatedEmail);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Hiển thị trang profile chính
            showProfile(req, resp, user);
        } else if (pathInfo.equals("/change-password")) {
            // Hiển thị form đổi mật khẩu
            // Kiểm tra xem user có đăng nhập bằng Google/Facebook không
            // User đăng nhập bằng social sẽ có passwordHash là ID của Google/Facebook (không phải BCrypt hash)
            // BCrypt hash luôn bắt đầu bằng $2a$ hoặc $2b$
            boolean isSocialLogin = !user.getPasswordHash().startsWith("$2");
            req.setAttribute("isSocialLogin", isSocialLogin);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/views/admin/profile/change-password.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        
        // Lấy authenticatedEmail từ request attribute (do filter set)
        String authenticatedEmail = (String) req.getAttribute("authenticatedEmail");
        
        if (authenticatedEmail == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        Users user = usersService.findByEmail(authenticatedEmail);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Cập nhật thông tin cá nhân
            updateProfile(req, resp, user);
        } else if (pathInfo.equals("/change-password")) {
            // Đổi mật khẩu
            changePassword(req, resp, user);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void showProfile(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        // Refresh user data từ database
        Users freshUser = usersService.findByEmail(user.getEmail());
        if (freshUser != null) {
            user = freshUser;
        }
        
        req.setAttribute("user", user);
        req.getRequestDispatcher("/views/admin/profile/profile.jsp").forward(req, resp);
    }
    
    private void updateProfile(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        try {
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            
            if (fullName == null || fullName.trim().isEmpty()) {
                req.setAttribute("error", "Họ và tên không được để trống!");
                showProfile(req, resp, user);
                return;
            }
            
            // Validate phone format if provided
            if (phone != null && !phone.trim().isEmpty() && !isValidPhone(phone.trim())) {
                req.setAttribute("error", "Số điện thoại không hợp lệ! (10-11 chữ số)");
                showProfile(req, resp, user);
                return;
            }
            
            // Kiểm tra SĐT đã tồn tại chưa (nếu user đổi SĐT)
            if (phone != null && !phone.trim().isEmpty()) {
                String currentPhone = user.getPhone();
                if (currentPhone == null || !phone.trim().equals(currentPhone)) {
                    Users existingUser = usersService.findByPhone(phone.trim());
                    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                        req.setAttribute("error", "Số điện thoại đã được sử dụng bởi tài khoản khác!");
                        showProfile(req, resp, user);
                        return;
                    }
                }
            }
            
            // Cập nhật thông tin
            user.setFullName(fullName.trim());
            user.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);
            
            usersService.update(user);
            
            req.setAttribute("success", "Cập nhật thông tin thành công!");
            showProfile(req, resp, user);
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            showProfile(req, resp, user);
        }
    }
    
    private void changePassword(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        try {
            String oldPassword = req.getParameter("oldPassword");
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");
            
            // Validation
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập mật khẩu cũ!");
                req.setAttribute("user", user);
                req.getRequestDispatcher("/views/admin/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập mật khẩu mới!");
                req.setAttribute("user", user);
                req.getRequestDispatcher("/views/admin/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            if (newPassword.length() < 6) {
                req.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                req.setAttribute("user", user);
                req.getRequestDispatcher("/views/admin/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                req.setAttribute("error", "Mật khẩu xác nhận không khớp!");
                req.setAttribute("user", user);
                req.getRequestDispatcher("/views/admin/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            // Lấy user mới nhất từ DB
            Users freshUser = usersService.findByEmail(user.getEmail());
            if (freshUser == null) {
                req.setAttribute("error", "Không tìm thấy thông tin người dùng!");
                req.setAttribute("user", user);
                req.getRequestDispatcher("/views/admin/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            // Kiểm tra mật khẩu cũ
            if (!BCrypt.checkpw(oldPassword, freshUser.getPasswordHash())) {
                resp.sendRedirect(req.getContextPath() + "/admin/profile/change-password?error=wrong_password");
                return;
            }
            
            // Hash mật khẩu mới với BCrypt
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            freshUser.setPasswordHash(hashedPassword);
            
            // Cập nhật vào database
            usersService.update(freshUser);
            
            resp.sendRedirect(req.getContextPath() + "/admin/profile/change-password?success=true");
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            req.setAttribute("user", user);
            req.getRequestDispatcher("/views/admin/profile/change-password.jsp").forward(req, resp);
        }
    }
    
    // Helper method for validation
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String phonePattern = "^[0-9]{10,11}$";
        return phone.matches(phonePattern);
    }
}
