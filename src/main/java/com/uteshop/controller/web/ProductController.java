package com.uteshop.controller.web;

import com.uteshop.dto.web.OptionDto;
import com.uteshop.entity.catalog.Products;
import com.uteshop.services.Option.IOptionsService;
import com.uteshop.services.Product.IProductsService;
import com.uteshop.services.impl.Option.OptionsServiceImpl;
import com.uteshop.services.impl.Product.ProductsServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/product", "/product-detail"})
public class ProductController extends HttpServlet {
    IProductsService productsService = new ProductsServiceImpl();
    IOptionsService optionsService = new OptionsServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String path = req.getServletPath();

        switch (path) {
            case "/product":
                break;

            case "/product-detail":
                String id = req.getParameter("id");
                int productId = Integer.parseInt(id);

                Products product = productsService.findById(productId);
                List<Products> relevantProducts = productsService.getRelevantProducts(productId);
                List<OptionDto> options = optionsService.getOptionsByProduct(productId);

                req.setAttribute("product", product);
                req.setAttribute("options", options);
                req.setAttribute("relevantProducts", relevantProducts);

                req.getRequestDispatcher("/views/web/productDetail.jsp").forward(req, resp);

                break;
        }
    }
}
