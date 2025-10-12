package com.uteshop.controller.manager;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.dto.manager.inventory.InventoryRow;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.services.impl.manager.InventoryManagerServiceImpl;
import com.uteshop.services.manager.IInventoryManagerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/manager/inventory")
public class InventoryController extends HttpServlet {

    private final IInventoryManagerService iInventoryManagerService = new InventoryManagerServiceImpl();
    private final EntityDaoImpl<Categories> categoriesDao = new EntityDaoImpl<>(Categories.class);

    private Integer branchId(HttpServletRequest req){ return (Integer) req.getSession().getAttribute("branchId"); }
    private Integer parseInt(String s){ try { return (s==null||s.isBlank())? null : Integer.valueOf(s);} catch(Exception e){return null;}}
    private int parseIntDefault(String s, int d){ try { return (s==null||s.isBlank())? d : Integer.parseInt(s);} catch(Exception e){return d;}}
    private String emptyToNull(String s){ return (s==null||s.isBlank())? null : s; }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer branchId = branchId(req);
        if (branchId == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

        Integer categoryId = parseInt(req.getParameter("category"));
        String keyword     = emptyToNull(req.getParameter("q"));
        int page           = parseIntDefault(req.getParameter("page"), 1);
        int size           = parseIntDefault(req.getParameter("size"), 20);
        String sort        = req.getParameter("sort");
        String dir         = req.getParameter("dir");

        PageResult<InventoryRow> result = iInventoryManagerService.search(branchId, categoryId, keyword, page, size, sort, dir);

        List<Categories> categories = categoriesDao.findAll();

        req.setAttribute("categories", categories);
        req.setAttribute("result", result);
        req.setAttribute("q", keyword);
        req.setAttribute("category", categoryId);
        req.setAttribute("page", page);
        req.setAttribute("size", size);
        req.setAttribute("sort", sort);
        req.setAttribute("dir", dir);

        req.getRequestDispatcher("/views/manager/inventory.jsp").forward(req, resp);
    }
}
