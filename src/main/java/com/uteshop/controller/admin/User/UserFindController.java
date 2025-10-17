package com.uteshop.controller.admin.User;

import java.io.IOException;

import com.uteshop.entity.auth.Users;
import com.uteshop.services.impl.admin.UsersServiceImpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/Users/find")
public class UserFindController extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UsersServiceImpl userService = new UsersServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        Users user = userService.findById(id);

        if (user != null) {
            String json = String.format("{\"id\":%d, \"fullName\":\"%s\", \"phone\":\"%s\"}",
                    user.getId(), user.getFullName(), user.getPhone());
            resp.getWriter().write(json);
        } else {
            resp.getWriter().write("{}");
        }
    }
}
