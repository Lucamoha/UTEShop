package com.uteshop.controller.admin.User;

import java.io.IOException;
import java.util.List;

import com.uteshop.entity.auth.Users;
import com.uteshop.services.admin.IUsersService;
import com.uteshop.services.impl.admin.UsersServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/admin/User/Users/list", 
							"/admin/User/Users/add", 
							"/admin/User/Users/edit", 
							"/admin/User/Users/view", 
							"/admin/User/Users/delete"})
public class UserController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURI();
		IUsersService userService = new UsersServiceImpl();
		
		if(url.contains("admin/User/Users/list")) {
			List<Users> listUser = userService.findAll();
			
			req.setAttribute("listUser", listUser);
			req.getRequestDispatcher("/views/admin/User/Users/list.jsp").forward(req, resp);
		}
		else if(url.contains("/admin/User/Users/add"))
		{
			req.getRequestDispatcher("/views/admin/User/Users/add.jsp").forward(req, resp);
		}
		else if (url.contains("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Users u = userService.findById(id);
            req.setAttribute("user", u);
            req.getRequestDispatcher("/views/admin/User/Users/edit.jsp").forward(req, resp);

        } else if (url.contains("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Users u = userService.findById(id);
            req.setAttribute("user", u);
            req.getRequestDispatcher("/views/admin/User/Users/view.jsp").forward(req, resp);

        } else if (url.contains("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            userService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/User/Users/list");
        }
    }
	
}
