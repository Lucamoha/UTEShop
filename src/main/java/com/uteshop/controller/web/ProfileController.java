package com.uteshop.controller.web;

import com.uteshop.entity.auth.Addresses;
import com.uteshop.entity.auth.Users;
import com.uteshop.services.impl.web.AddressesServiceImpl;
import com.uteshop.services.impl.web.UsersServiceImpl;
import com.uteshop.services.web.IAddressesService;
import com.uteshop.services.web.IUsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/profile", "/profile/*"})
public class ProfileController extends HttpServlet {
    
    private final IUsersService usersService = new UsersServiceImpl();
    private final IAddressesService addressesService = new AddressesServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        
        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getRequestURI());
            return;
        }
        
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Hiển thị trang profile chính
            showProfile(req, resp, user);
        } else if (pathInfo.equals("/addresses")) {
            // Hiển thị danh sách địa chỉ
            showAddresses(req, resp, user);
        } else if (pathInfo.equals("/change-password")) {
            // Hiển thị form đổi mật khẩu
            // Kiểm tra xem user có đăng nhập bằng Google/Facebook không
            // User đăng nhập bằng social sẽ có passwordHash là ID của Google/Facebook (không phải BCrypt hash)
            // BCrypt hash luôn bắt đầu bằng $2a$ hoặc $2b$
            boolean isSocialLogin = !user.getPasswordHash().startsWith("$2");
            req.setAttribute("isSocialLogin", isSocialLogin);
            req.getRequestDispatcher("/views/web/profile/change-password.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        
        HttpSession session = req.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Cập nhật thông tin cá nhân
            updateProfile(req, resp, user, session);
        } else if (pathInfo.equals("/change-password")) {
            // Đổi mật khẩu
            changePassword(req, resp, user);
        } else if (pathInfo.equals("/addresses/add")) {
            // Thêm địa chỉ mới
            addAddress(req, resp, user);
        } else if (pathInfo.equals("/addresses/update")) {
            // Cập nhật địa chỉ
            updateAddress(req, resp, user);
        } else if (pathInfo.equals("/addresses/delete")) {
            // Xóa địa chỉ
            deleteAddress(req, resp, user);
        } else if (pathInfo.equals("/addresses/set-default")) {
            // Set địa chỉ mặc định
            setDefaultAddress(req, resp, user);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void showProfile(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        // Refresh user data từ database
        Users freshUser = usersService.findByEmail(user.getEmail());
        if (freshUser != null) {
            req.getSession().setAttribute("user", freshUser);
            user = freshUser;
        }
        
        req.setAttribute("user", user);
        req.getRequestDispatcher("/views/web/profile/profile.jsp").forward(req, resp);
    }
    
    private void showAddresses(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        List<Addresses> addresses = addressesService.getUserAddresses(user.getId());
        req.setAttribute("addresses", addresses);
        req.getRequestDispatcher("/views/web/profile/addresses.jsp").forward(req, resp);
    }
    
    private void updateProfile(HttpServletRequest req, HttpServletResponse resp, Users user, HttpSession session) 
            throws ServletException, IOException {
        try {
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            
            if (fullName == null || fullName.trim().isEmpty()) {
                req.setAttribute("error", "Họ và tên không được để trống!");
                showProfile(req, resp, user);
                return;
            }
            
            // Validate phone format
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
            
            // Cập nhật thông tin (không cho phép thay đổi email)
            user.setFullName(fullName.trim());
            user.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);
            
            usersService.update(user);
            
            // Cập nhật session
            session.setAttribute("user", user);
            
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
                req.getRequestDispatcher("/views/web/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập mật khẩu mới!");
                req.getRequestDispatcher("/views/web/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            if (newPassword.length() < 6) {
                req.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
                req.getRequestDispatcher("/views/web/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                req.setAttribute("error", "Mật khẩu xác nhận không khớp!");
                req.getRequestDispatcher("/views/web/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            // Lấy user mới nhất từ DB
            Users freshUser = usersService.findByEmail(user.getEmail());
            if (freshUser == null) {
                req.setAttribute("error", "Không tìm thấy thông tin người dùng!");
                req.getRequestDispatcher("/views/web/profile/change-password.jsp").forward(req, resp);
                return;
            }
            
            // Kiểm tra mật khẩu cũ
            if (!BCrypt.checkpw(oldPassword, freshUser.getPasswordHash())) {
                resp.sendRedirect(req.getContextPath() + "/profile/change-password?error=wrong_password");
                return;
            }
            
            // Hash mật khẩu mới với BCrypt
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            freshUser.setPasswordHash(hashedPassword);
            
            // Cập nhật vào database
            usersService.update(freshUser);
            
            // Cập nhật session
            req.getSession().setAttribute("user", freshUser);
            
            resp.sendRedirect(req.getContextPath() + "/profile/change-password?success=true");
            
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            req.getRequestDispatcher("/views/web/profile/change-password.jsp").forward(req, resp);
        }
    }
    
    private void addAddress(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        try {
            String label = req.getParameter("label");
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            String addressLine = req.getParameter("addressLine");
            String ward = req.getParameter("ward");
            String district = req.getParameter("district");
            String city = req.getParameter("city");
            String isDefaultStr = req.getParameter("isDefault");
            
            // Validation
            if (fullName == null || fullName.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                addressLine == null || addressLine.trim().isEmpty() ||
                city == null || city.trim().isEmpty()) {
                
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=missing_fields");
                return;
            }
            
            // Validate phone format
            if (!isValidPhone(phone.trim())) {
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=invalid_phone");
                return;
            }
            
            // Kiểm tra địa chỉ trùng lặp
            boolean isDuplicate = addressesService.isDuplicateAddress(
                user.getId(), 
                fullName.trim(), 
                phone.trim(), 
                addressLine.trim(), 
                ward != null ? ward.trim() : null, 
                district != null ? district.trim() : null, 
                city.trim(), 
                null
            );
            
            if (isDuplicate) {
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=duplicate_address");
                return;
            }
            
            Addresses address = new Addresses();
            address.setUser(user);
            address.setLabel(label != null ? label.trim() : null);
            address.setFullName(fullName.trim());
            address.setPhone(phone.trim());
            address.setAddressLine(addressLine.trim());
            address.setWard(ward != null ? ward.trim() : null);
            address.setDistrict(district != null ? district.trim() : null);
            address.setCity(city.trim());
            address.setIsDefault(isDefaultStr != null && isDefaultStr.equals("on"));
            
            addressesService.addAddress(address);
            
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?success=added");
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=" + e.getMessage());
        }
    }
    
    private void updateAddress(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        try {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=missing_id");
                return;
            }
            
            Integer addressId = Integer.parseInt(idStr);
            Addresses address = addressesService.getAddressById(addressId);
            
            if (address == null || !address.getUser().getId().equals(user.getId())) {
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=not_found");
                return;
            }
            
            String label = req.getParameter("label");
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            String addressLine = req.getParameter("addressLine");
            String ward = req.getParameter("ward");
            String district = req.getParameter("district");
            String city = req.getParameter("city");
            String isDefaultStr = req.getParameter("isDefault");
            
            // Validation
            if (fullName == null || fullName.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                addressLine == null || addressLine.trim().isEmpty() ||
                city == null || city.trim().isEmpty()) {
                
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=missing_fields");
                return;
            }
            
            // Validate phone format
            if (!isValidPhone(phone.trim())) {
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=invalid_phone");
                return;
            }
            
            // Kiểm tra địa chỉ trùng lặp (exclude current address)
            boolean isDuplicate = addressesService.isDuplicateAddress(
                user.getId(), 
                fullName.trim(), 
                phone.trim(), 
                addressLine.trim(), 
                ward != null ? ward.trim() : null, 
                district != null ? district.trim() : null, 
                city.trim(), 
                addressId
            );
            
            if (isDuplicate) {
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=duplicate_address");
                return;
            }
            
            address.setLabel(label != null ? label.trim() : null);
            address.setFullName(fullName.trim());
            address.setPhone(phone.trim());
            address.setAddressLine(addressLine.trim());
            address.setWard(ward != null ? ward.trim() : null);
            address.setDistrict(district != null ? district.trim() : null);
            address.setCity(city.trim());
            address.setIsDefault(isDefaultStr != null && isDefaultStr.equals("on"));
            
            addressesService.updateAddress(address);
            
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?success=updated");
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=" + e.getMessage());
        }
    }
    
    private void deleteAddress(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        try {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=missing_id");
                return;
            }
            
            Integer addressId = Integer.parseInt(idStr);
            addressesService.deleteAddress(addressId, user.getId());
            
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?success=deleted");
            
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=system_error");
        }
    }
    
    private void setDefaultAddress(HttpServletRequest req, HttpServletResponse resp, Users user) 
            throws ServletException, IOException {
        try {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=missing_id");
                return;
            }
            
            Integer addressId = Integer.parseInt(idStr);
            addressesService.setDefaultAddress(addressId, user.getId());
            
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?success=default_set");
            
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/profile/addresses?error=system_error");
        }
    }
    
    // Helper methods for validation
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return email.matches(emailPattern);
    }
    
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String phonePattern = "^[0-9]{10,11}$";
        return phone.matches(phonePattern);
    }
}
