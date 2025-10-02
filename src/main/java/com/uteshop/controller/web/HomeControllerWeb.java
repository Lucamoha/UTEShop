package com.uteshop.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.uteshop.entities.Categories;
import com.uteshop.services.ICategoriesService;
import com.uteshop.services.Product.IProductsService;
import com.uteshop.services.impl.CategoriesServiceImpl;
import com.uteshop.services.impl.Product.ProductsServiceImpl;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Categories> parents = categoriesService.findParents();
        request.setAttribute("parentCategories", parents);
	    
		request.getRequestDispatcher("/views/web/home.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
