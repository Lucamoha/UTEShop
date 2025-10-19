package com.uteshop.controller.manager;

import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.branch.Branches;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/manager/profile")
public class ProfileController extends HttpServlet {
    EntityDaoImpl<Branches> branchesDao = new EntityDaoImpl<>(Branches.class);
    EntityDaoImpl<Users> usersDao = new EntityDaoImpl<>(Users.class);

    private Integer branchId(HttpServletRequest req) {
        return (Integer) req.getSession().getAttribute("branchId");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer bId = branchId(req);
        if (bId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Branches branch = branchesDao.findById(bId);
        if (branch == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Users user = usersDao.findById(branch.getManager().getId());
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("user", user);
        req.setAttribute("branch", branch);
        req.getRequestDispatcher("/views/manager/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer bId = branchId(req);
        if (bId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Integer userId = Integer.parseInt(req.getParameter("userId"));
        Users user = usersDao.findById(userId);
        if (user == null) {
            req.setAttribute("error", "Không tìm thấy người dùng");
        }

        String name = req.getParameter("fullName");
        String phone = req.getParameter("phone");

        try {
            user.setFullName(name);
            user.setPhone(phone);
            usersDao.update(user);

            req.getSession().setAttribute("flash_ok", true);
            resp.sendRedirect(req.getContextPath() + "/manager/profile");
        }
        catch (Exception e) {
            req.setAttribute("error", "Cập nhật thất bại: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
