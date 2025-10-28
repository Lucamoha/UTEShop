package com.uteshop.controller.web.auth;

import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.branch.Branches;
import com.uteshop.services.impl.web.account.UsersServiceImpl;
import com.uteshop.services.web.account.IUsersService;
import com.uteshop.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet(urlPatterns = { "/login" })
public class LoginController extends HttpServlet {
    IUsersService userService = new UsersServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/web/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirectUrl = req.getParameter("redirect");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            req.getRequestDispatcher("/views/web/login.jsp").forward(req, resp);
            return;
        }

        Users user = userService.findByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPasswordHash()) && user.getIsActive() == true) {
            // Lấy role thực tế từ database
            String role = user.getUserRole() != null ? user.getUserRole() : "USER";

            // Kiểm tra checkbox "remember me"
            String remember = req.getParameter("remember");
            boolean rememberMe = (remember != null);

            // Tạo JWT token
            String token = JWTUtil.generateToken(email, role, rememberMe);

            // Lưu token vào Cookie
            JWTUtil.addTokenToCookie(resp, token, rememberMe);

            // Lưu thông tin vào session
            req.getSession().setAttribute("user", user);
            req.getSession().setAttribute("email", email);
            req.getSession().setAttribute("role", role);

            // Nếu có redirect URL hợp lệ, ưu tiên redirect về đó
            if (redirectUrl != null && !redirectUrl.isEmpty() && redirectUrl.startsWith(req.getContextPath())) {
                // Đối với MANAGER, vẫn cần set branch info trước khi redirect
                if ("MANAGER".equals(role)) {
                    EntityDaoImpl<Branches> branchesEntityDaoImpl = new EntityDaoImpl<>(Branches.class);
                    Branches branches = branchesEntityDaoImpl.findByUnique("manager", user).orElse(null);
                    
                    if (branches != null) {
                        req.getSession().setAttribute("branchId", branches.getId());
                        req.getSession().setAttribute("branchName", branches.getName());
                    }
                }
                resp.sendRedirect(redirectUrl);
            } else {
                // Không có redirect URL, redirect về dashboard/home mặc định
                if ("ADMIN".equals(role)) {
                    resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
                } else if ("MANAGER".equals(role)) {
                    EntityDaoImpl<Branches> branchesEntityDaoImpl = new EntityDaoImpl<>(Branches.class);
                    Branches branches = branchesEntityDaoImpl.findByUnique("manager", user).orElse(null);

                    if (branches == null) {
                        resp.sendRedirect(req.getContextPath() + "/home");
                        return;
                    }

                    req.getSession().setAttribute("branchId", branches.getId());
                    req.getSession().setAttribute("branchName", branches.getName());
                    resp.sendRedirect(req.getContextPath() + "/manager/dashboard");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/home");
                }
            }
        } else {
            if (user != null && user.getIsActive() == false) {
                req.setAttribute("error", "Tài khoản của bạn đã bị vô hiệu hóa!");
            }  else if (user != null) {
                req.setAttribute("error", "Email hoặc mật khẩu không đúng!");
            }
            req.setAttribute("email", email); // Giữ lại email đã nhập
            req.getRequestDispatcher("/views/web/login.jsp").forward(req, resp);
        }
    }
}