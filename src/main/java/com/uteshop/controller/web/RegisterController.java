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


@WebServlet(urlPatterns = {"/register", "/verify-otp-register"})
public class RegisterController extends HttpServlet {
    private final IUsersService userService = new UsersServiceImpl();
    private final IUserTokensDao userTokensDao = new UserTokensDaoImpl();
    private static final int TOKEN_TYPE_VERIFY_EMAIL = 1; // Từ config.properties

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/register":
                req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
                break;

            case "/verify-otp-register":
                req.setAttribute("otpType", "register");
                req.getRequestDispatcher("/views/web/verify-otp.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();

        switch (path) {
            case "/register":
                handleRegister(req, resp);
                break;

            case "/verify-otp-register":
                handleVerifyOTPRegister(req, resp);
                break;
        }
    }

    // Xử lý đăng ký: Lưu thông tin vào session và gửi OTP
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String fullname = req.getParameter("fullname");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // Validation dữ liệu
        if (email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                fullname == null || fullname.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                confirmPassword == null || confirmPassword.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu không khớp!");
            req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
            return;
        }

        // Kiểm tra email hoặc phone đã tồn tại
        if (userService.checkDuplicate(email, phone)) {
            req.setAttribute("error", "Email hoặc số điện thoại đã tồn tại!");
            req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
            return;
        }

        // Lưu thông tin đăng ký vào session (chưa tạo user)
        HttpSession session = req.getSession();
        session.setAttribute("registerEmail", email);
        session.setAttribute("registerPhone", phone);
        session.setAttribute("registerFullname", fullname);
        session.setAttribute("registerPassword", password);

        // Tạo mã OTP
        String otp = EmailService.generateOTP();

        // Tạo user tạm thời để lưu token (user chưa được insert vào DB)
        Users tempUser = new Users();
        tempUser.setEmail(email);
        tempUser.setPhone(phone);
        tempUser.setFullName(fullname);
        tempUser.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
        tempUser.setIsActive(false); // Chưa active
        tempUser.setUserRole("USER");

        // Lưu user tạm thời vào DB để có ID cho token
        try {
            userService.insertUser(tempUser);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Đã xảy ra lỗi khi đăng ký!");
            req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
            return;
        }

        // Lấy user vừa tạo từ DB
        Users user = userService.findByEmail(email);

        // Lưu OTP vào database
        UserTokens token = UserTokens.builder()
                .user(user)
                .Type(TOKEN_TYPE_VERIFY_EMAIL)
                .Token(otp)
                .ExpiredAt(LocalDateTime.now().plusMinutes(5))
                .build();

        try {
            userTokensDao.insert(token);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống. Vui lòng thử lại!");
            req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
            return;
        }

        // Gửi OTP qua email
        boolean sent = EmailService.sendRegistrationOTP(email, otp);

        if (sent) {
            req.setAttribute("success", "Mã xác thực đã được gửi đến email: " + email);
            req.setAttribute("otpType", "register");
            req.getRequestDispatcher("/views/web/verify-otp.jsp").forward(req, resp);
        } else {
            // Xóa user tạm thời và token nếu gửi email thất bại
            try {
                Users userToDelete = userService.findByEmail(email);
                if (userToDelete != null) {
                    // Xóa token trước (vì có foreign key constraint)
                    userTokensDao.deleteByUserId(userToDelete.getId());
                    // Sau đó xóa user
                    userService.delete(userToDelete.getId());
                }
            } catch (Exception e) {
                System.err.println("Failed to cleanup temp user: " + e.getMessage());
            }
        }
    }

    // Xử lý xác thực OTP đăng ký
    private void handleVerifyOTPRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String inputOTP = req.getParameter("otp");
        HttpSession session = req.getSession();
        String email = (String) session.getAttribute("registerEmail");

        if (email == null) {
            req.setAttribute("error", "Phiên đăng ký đã hết hạn. Vui lòng đăng ký lại!");
            req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
            return;
        }

        // 1. Tìm token từ database
        UserTokens token = userTokensDao.findByTokenAndType(inputOTP, TOKEN_TYPE_VERIFY_EMAIL);

        if (token == null) {
            req.setAttribute("error", "Mã OTP không hợp lệ hoặc đã được sử dụng!");
            req.setAttribute("otpType", "register");
            req.getRequestDispatcher("/views/web/verify-otp.jsp").forward(req, resp);
            return;
        }

        // 2. Kiểm tra OTP đã hết hạn chưa
        if (LocalDateTime.now().isAfter(token.getExpiredAt())) {
            req.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng đăng ký lại!");
            req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
            return;
        }

        // 3. OTP hợp lệ - đánh dấu đã sử dụng
        userTokensDao.markAsUsed(token.getId());

        // 4. Kích hoạt tài khoản user
        Users user = userService.findByEmail(email);
        user.setIsActive(true);
        userService.update(user);

        // 5. Clear session
        session.removeAttribute("registerEmail");
        session.removeAttribute("registerPhone");
        session.removeAttribute("registerFullname");
        session.removeAttribute("registerPassword");

        // 6. Redirect về login
        session.setAttribute("loginMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
