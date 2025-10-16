package com.uteshop.controller.web;

import com.uteshop.dto.web.ProductComparisonDto;
import com.uteshop.services.impl.web.ProductComparisonServiceImpl;
import com.uteshop.services.web.IProductComparisonService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/product-compare"})
public class ProductComparisonController extends HttpServlet {
    
    private final IProductComparisonService productComparisonService;
    
    public ProductComparisonController() {
        this.productComparisonService = new ProductComparisonServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        
        
        String product1Param = req.getParameter("product1");
        String product2Param = req.getParameter("product2");
        try {
            ProductComparisonDto comparison = null;
            comparison = productComparisonService.compareProductsBySlug(product1Param, product2Param);

            req.setAttribute("comparison", comparison);
            req.getRequestDispatcher("/views/web/product-comparison.jsp").forward(req, resp);
            
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "Đã xảy ra lỗi khi so sánh sản phẩm";
            req.setAttribute("error", errorMsg);
            req.getRequestDispatcher("/views/web/product-comparison.jsp").forward(req, resp);
            
        }
    }
}
