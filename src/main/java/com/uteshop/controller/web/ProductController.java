package com.uteshop.controller.web;

import com.uteshop.dto.web.OptionDto;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.Products;
import com.uteshop.entity.engagement.Reviews;
import com.uteshop.services.impl.web.CategoriesServiceImpl;
import com.uteshop.services.impl.web.OptionsServiceImpl;
import com.uteshop.services.impl.web.ProductsServiceImpl;
import com.uteshop.services.impl.web.ReviewsServiceImpl;
import com.uteshop.services.impl.web.UsersServiceImpl;
import com.uteshop.services.web.ICategoriesService;
import com.uteshop.services.web.IOptionsService;
import com.uteshop.services.web.IProductsService;
import com.uteshop.services.web.IReviewsService;
import com.uteshop.services.web.IUsersService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = { "/product", "/product-detail" })
public class ProductController extends HttpServlet {
    IProductsService productsService = new ProductsServiceImpl();
    IOptionsService optionsService = new OptionsServiceImpl();
    ICategoriesService categoriesService = new CategoriesServiceImpl();
    IReviewsService reviewsService = new ReviewsServiceImpl();
    IUsersService usersService = new UsersServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String path = req.getServletPath();

        switch (path) {
            case "/product":
                break;

            case "/product-detail":
                String id = req.getParameter("id");
                int productId = Integer.parseInt(id);

                List<Categories> parents = categoriesService.findParents();
                Products product = productsService.findById(productId);
                List<Products> relevantProducts = productsService.getRelevantProducts(productId);
                List<OptionDto> options = optionsService.getOptionsByProduct(productId);

                // Lấy danh mục hiện tại và danh mục cha
                Categories selectedCategory = product.getCategory();
                Categories selectedParent = null;
                if (selectedCategory != null && selectedCategory.getParent() != null) {
                    selectedParent = categoriesService.findById(selectedCategory.getParent().getId());
                }

                // Lấy thông tin user từ request attribute
                String authenticatedEmail = (String) req.getAttribute("authenticatedEmail");

                // Lấy reviews và stats
                List<Reviews> reviews = reviewsService.getReviewsByProduct(productId);
                Map<String, Object> reviewStats = reviewsService.getReviewStats(productId);

                // Kiểm tra quyền review (nếu đã đăng nhập)
                boolean canReview = false;
                boolean hasPurchased = false;
                boolean hasReviewed = false;

                if (authenticatedEmail != null) {
                    Users user = usersService.findByEmail(authenticatedEmail);
                    if (user != null) {
                        hasPurchased = reviewsService.hasUserPurchasedProduct(user.getId(), productId);
                        hasReviewed = reviewsService.hasUserReviewedProduct(user.getId(), productId);
                        canReview = hasPurchased && !hasReviewed;
                    }
                }

                req.setAttribute("parentCategories", parents);
                req.setAttribute("product", product);
                req.setAttribute("options", options);
                req.setAttribute("relevantProducts", relevantProducts);
                req.setAttribute("selectedCategory", selectedCategory);
                req.setAttribute("selectedParent", selectedParent);
                req.setAttribute("reviews", reviews);
                req.setAttribute("reviewStats", reviewStats);
                req.setAttribute("canReview", canReview);
                req.setAttribute("hasPurchased", hasPurchased);
                req.setAttribute("hasReviewed", hasReviewed);

                req.getRequestDispatcher("/views/web/productDetail.jsp").forward(req, resp);

                break;
        }
    }
}
