package com.uteshop.controller.web;

import com.uteshop.entity.auth.Users;
import com.uteshop.services.impl.web.UsersServiceImpl;
import com.uteshop.services.web.IUsersService;
import com.uteshop.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;


@WebServlet(urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {
    IUsersService userService = new UsersServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/web/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

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

        // Hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Users user = new Users();
        user.setEmail(email);
        user.setPhone(phone);
        user.setFullName(fullname);
        user.setPasswordHash(hashedPassword);
        user.setIsActive(true);
        user.setUserRole("USER");

        // Lưu user vào cơ sở dữ liệu
        try {
            userService.insertUser(user);
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Đã xảy ra lỗi khi đăng ký!");
            req.getRequestDispatcher("/views/web/register.jsp").forward(req, resp);
        }
    }
}
