package com.uteshop.controller.web;

import com.uteshop.entity.catalog.Products;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.uteshop.entity.catalog.Categories;
import com.uteshop.services.impl.web.CategoriesServiceImpl;
import com.uteshop.services.impl.web.ProductsServiceImpl;
import com.uteshop.services.web.ICategoriesService;
import com.uteshop.services.web.IProductsService;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

/**
 * Servlet implementation class HomeControllerWeb
 */
@WebServlet(urlPatterns = {"/home"})
public class HomeControllerWeb extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ICategoriesService categoriesService = new CategoriesServiceImpl();
    private final IProductsService productsService = new ProductsServiceImpl();
    
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeControllerWeb() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

		List<Categories> parents = categoriesService.findParents();
        List<Products> topLatestProducts = productsService.topLatestProducts();
        
        request.setAttribute("parentCategories", parents);
        request.setAttribute("topLatestProducts", topLatestProducts);
	    
		request.getRequestDispatcher("/views/web/home.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
