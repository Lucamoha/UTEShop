package com.uteshop.controller.admin.Product.Products;

import java.io.IOException;
import java.util.List;

import com.uteshop.entities.Categories;
import com.uteshop.entities.Products;
import com.uteshop.services.Category.ICategoriesService;
import com.uteshop.services.Product.IProductsService;
import com.uteshop.services.impl.Category.CategoriesServiceImpl;
import com.uteshop.services.impl.Product.ProductsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns =  {"/admin/Product/Products/list", "/admin/Product/Products/add", "/admin/Product/Products/edit", "/admin/Product/Products/view"})
public class ProductController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		
		if (uri.contains("admin/Product/Products/list")) {
			IProductsService productsService = new ProductsServiceImpl();
			List<Products> productList = productsService.findAll();
			
			req.setAttribute("productList", productList);
			req.getRequestDispatcher("/views/admin/Product/Products/list.jsp").forward(req, resp);
		}
		else if (uri.contains("admin/Product/Products/edit")) {
			IProductsService productsService = new ProductsServiceImpl();
			ICategoriesService categoriesService = new CategoriesServiceImpl();
			String id = req.getParameter("id");
			
			Products product = productsService.findById(Integer.parseInt(id));
			List<Categories> categoryList = categoriesService.findAll();
			
			req.setAttribute("product", product);
			req.setAttribute("categoryList", categoryList);
			req.getRequestDispatcher("/views/admin/Product/Products/addOrEdit.jsp").forward(req, resp);
		}
		else if (uri.contains("admin/Product/Products/add")) {
			ICategoriesService categoriesService = new CategoriesServiceImpl();	
			List<Categories> categoryList = categoriesService.findAll();
			
			req.setAttribute("categoryList", categoryList);
			req.getRequestDispatcher("/views/admin/Product/Products/addOrEdit.jsp").forward(req, resp);
		} else if (uri.contains("admin/Product/Products/view")) {
			IProductsService productsService = new ProductsServiceImpl();
			ICategoriesService categoriesService = new CategoriesServiceImpl();
			String id = req.getParameter("id");
			
			Products product = productsService.findById(Integer.parseInt(id));
			
			req.setAttribute("product", product);
			req.getRequestDispatcher("/views/admin/Product/Products/view.jsp").forward(req, resp);
		}
	}
}
