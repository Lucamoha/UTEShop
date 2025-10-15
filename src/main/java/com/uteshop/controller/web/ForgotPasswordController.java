package com.uteshop.controller.web;

import com.uteshop.dao.impl.web.UserTokensDaoImpl;
import com.uteshop.dao.web.IUserTokensDao;
import com.uteshop.entity.auth.UserTokens;
import com.uteshop.entity.auth.Users;
import com.uteshop.services.impl.web.UsersServiceImpl;
import com.uteshop.services.web.IUsersService;
import com.uteshop.util.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = { "/forgot-password", "/verify-otp", "/reset-password" })
public class ForgotPasswordController extends HttpServlet {

    private final IUsersService usersService = new UsersServiceImpl();
    private final IUserTokensDao userTokensDao = new UserTokensDaoImpl();
    private static final int TOKEN_TYPE_RESET_PASSWORD = 2; // Từ config.properties

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/forgot-password":
                req.getRequestDispatcher("/views/web/forgot-password.jsp").forward(req, resp);
                break;

            case "/verify-otp":
                req.getRequestDispatcher("/views/web/verify-otp.jsp").forward(req, resp);
                break;

            case "/reset-password":
                req.getRequestDispatcher("/views/web/reset-password.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String path = req.getServletPath();

        switch (path) {
            case "/forgot-password":
                handleForgotPassword(req, resp);
                break;

            case "/verify-otp":
                handleVerifyOTP(req, resp);
                break;

            case "/reset-password":
                handleResetPassword(req, resp);
                break;
        }
    }

    // Xử lý: User quên mật khẩu → Nhập email → Gửi OTP
    private void handleForgotPassword(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");

        // 1. Kiểm tra email có tồn tại không
        Users user = usersService.findByEmail(email);

        if (user == null) {
            req.setAttribute("error", "Email không tồn tại trong hệ thống!");
            req.getRequestDispatcher("/views/web/forgot-password.jsp").forward(req, resp);
            return;
        }

        // 2. Tạo mã OTP
        String otp = EmailService.generateOTP();

        // 3. Lưu OTP vào database
        UserTokens token = UserTokens.builder()
                .user(user)
                .Type(TOKEN_TYPE_RESET_PASSWORD)
                .Token(otp)
                .ExpiredAt(LocalDateTime.now().plusMinutes(5))
                .build();

        try {
            userTokensDao.insert(token);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại!");
            req.getRequestDispatcher("/views/web/forgot-password.jsp").forward(req, resp);
            return;
        }

        // 4. Lưu email vào session
        HttpSession session = req.getSession();
        session.setAttribute("resetEmail", email);

        // 5. Gửi OTP qua email
        boolean sent = EmailService.sendOTP(email, otp);

        if (sent) {
            req.setAttribute("success", "Mã xác thực đã được gửi đến email: " + email);
            req.getRequestDispatcher("/views/web/verify-otp.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Không thể gửi email. Vui lòng thử lại!");
            req.getRequestDispatcher("/views/web/forgot-password.jsp").forward(req, resp);
        }
    }

    // Xử lý: User nhập OTP để xác thực
    private void handleVerifyOTP(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String inputOTP = req.getParameter("otp");
        HttpSession session = req.getSession();

        // 1. Tìm token từ database
        UserTokens token = userTokensDao.findByTokenAndType(inputOTP, TOKEN_TYPE_RESET_PASSWORD);

        if (token == null) {
            req.setAttribute("error", "Mã OTP không hợp lệ hoặc đã được sử dụng!");
            req.getRequestDispatcher("/views/web/verify-otp.jsp").forward(req, resp);
            return;
        }

        // 2. Kiểm tra OTP đã hết hạn chưa
        if (LocalDateTime.now().isAfter(token.getExpiredAt())) {
            req.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng yêu cầu mã mới!");
            req.getRequestDispatcher("/views/web/forgot-password.jsp").forward(req, resp);
            return;
        }

        // 3. OTP hợp lệ - đánh dấu đã sử dụng
        userTokensDao.markAsUsed(token.getId());

        // 4. Lưu trạng thái đã xác thực vào session
        session.setAttribute("otpVerified", true);
        session.setAttribute("resetEmail", token.getUser().getEmail());

        req.setAttribute("success", "Xác thực thành công! Vui lòng đặt mật khẩu mới.");
        req.getRequestDispatcher("/views/web/reset-password.jsp").forward(req, resp);
    }

    /**
     * Xử lý: User đặt mật khẩu mới
     */
    private void handleResetPassword(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();

        // 1. Kiểm tra đã verify OTP chưa
        Boolean otpVerified = (Boolean) session.getAttribute("otpVerified");
        if (otpVerified == null || !otpVerified) {
            req.setAttribute("error", "Vui lòng xác thực OTP trước!");
            req.getRequestDispatcher("/views/web/forgot-password.jsp").forward(req, resp);
            return;
        }

        String email = (String) session.getAttribute("resetEmail");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        // 2. Validate
        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher("/views/web/reset-password.jsp").forward(req, resp);
            return;
        }

        if (newPassword.length() < 6) {
            req.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            req.getRequestDispatcher("/views/web/reset-password.jsp").forward(req, resp);
            return;
        }

        // 3. Hash mật khẩu mới
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // 4. Cập nhật mật khẩu
        Users user = usersService.findByEmail(email);
        user.setPasswordHash(hashedPassword);
        usersService.update(user);

        // 5. Clear session
        session.removeAttribute("resetEmail");
        session.removeAttribute("otpVerified");
        session.removeAttribute("otpExpiry");

        // 6. Redirect về login
        session.setAttribute("loginMessage", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
