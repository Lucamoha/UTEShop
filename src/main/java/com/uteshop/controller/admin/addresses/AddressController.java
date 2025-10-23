package com.uteshop.controller.admin.addresses;

import java.io.IOException;
import java.util.List;

import com.uteshop.entity.auth.Addresses;
import com.uteshop.entity.auth.Users;
import com.uteshop.services.admin.IAddressesService;
import com.uteshop.services.admin.IUsersService;
import com.uteshop.services.impl.admin.AddressesServiceImpl;
import com.uteshop.services.impl.admin.UsersServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/admin/Address/Addresses/list",
        "/admin/Address/Addresses/add",
        "/admin/Address/Addresses/edit",
        "/admin/Address/Addresses/view",
        "/admin/Address/Addresses/delete"})
public class AddressController extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        IAddressesService addressService = new AddressesServiceImpl();
        IUsersService usersService = new UsersServiceImpl();
        List<Users> listUser = usersService.findAll();

        if(url.contains("admin/Address/Addresses/list")) {
            List<Addresses> listAddress = addressService.getAll();

            req.setAttribute("listAddress", listAddress);
            req.getRequestDispatcher("/views/admin/Address/Addresses/list.jsp").forward(req, resp);
        }
        else if(url.contains("/admin/Address/Addresses/add"))
        {
            req.setAttribute("listUser", listUser);
            req.getRequestDispatcher("/views/admin/Address/Addresses/add.jsp").forward(req, resp);
        }
        else if (url.contains("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Addresses a = addressService.getById(id);
            req.setAttribute("address", a);
            req.setAttribute("listUser", listUser);
            req.getRequestDispatcher("/views/admin/Address/Addresses/edit.jsp").forward(req, resp);

        } else if (url.contains("view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Addresses a = addressService.getById(id);
            req.setAttribute("address", a);
            req.getRequestDispatcher("/views/admin/Address/Addresses/view.jsp").forward(req, resp);

        } else if (url.contains("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            addressService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/Address/Addresses/list");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        IAddressesService addressService = new AddressesServiceImpl();
        req.setCharacterEncoding("UTF-8");

        if (url.contains("/admin/Address/Addresses/add")) {
            String label = req.getParameter("label");
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            String addressLine = req.getParameter("addressLine");
            String ward = req.getParameter("ward");
            String district = req.getParameter("district");
            String city = req.getParameter("city");
            String isDefaultStr = req.getParameter("isDefault");
            Boolean isDefault = Boolean.parseBoolean(isDefaultStr);
            String userIdStr = req.getParameter("userId");
            Users user = null;
            if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                IUsersService usersService = new UsersServiceImpl();
                user = usersService.findById(Integer.parseInt(userIdStr));
            }

            Addresses newAddress = new Addresses();
            newAddress.setLabel(label);
            newAddress.setFullName(fullName);
            newAddress.setPhone(phone);
            newAddress.setAddressLine(addressLine);
            newAddress.setWard(ward);
            newAddress.setDistrict(district);
            newAddress.setCity(city);
            newAddress.setIsDefault(isDefault);
            newAddress.setUser(user);
            addressService.save(newAddress);
            resp.sendRedirect(req.getContextPath() + "/admin/Address/Addresses/list");
        } else if (url.contains("/admin/Address/Addresses/edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Addresses address = addressService.getById(id);

            if (address != null) {
                address.setLabel(req.getParameter("label"));
                address.setFullName(req.getParameter("fullName"));
                address.setPhone(req.getParameter("phone"));
                address.setAddressLine(req.getParameter("addressLine"));
                address.setWard(req.getParameter("ward"));
                address.setDistrict(req.getParameter("district"));
                address.setCity(req.getParameter("city"));
                String isDefaultStr = req.getParameter("isDefault");
                address.setIsDefault(Boolean.parseBoolean(isDefaultStr));

                String userIdStr = req.getParameter("userId");
                if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                    IUsersService usersService = new UsersServiceImpl();
                    Users user = usersService.findById(Integer.parseInt(userIdStr));
                    address.setUser(user);
                }

                addressService.save(address);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/Address/Addresses/list");
        }
    }
}