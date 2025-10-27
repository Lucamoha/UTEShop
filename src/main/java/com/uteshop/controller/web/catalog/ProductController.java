package com.uteshop.controller.web.catalog;

import com.uteshop.dto.web.OptionDto;
import com.uteshop.entity.auth.Users;
import com.uteshop.entity.catalog.Categories;
import com.uteshop.entity.catalog.Products;
import com.uteshop.entity.engagement.Reviews;
import com.uteshop.services.impl.web.*;
import com.uteshop.services.impl.web.account.UsersServiceImpl;
import com.uteshop.services.impl.web.catalog.AttributesServiceImpl;
import com.uteshop.services.impl.web.catalog.CategoriesServiceImpl;
import com.uteshop.services.impl.web.catalog.OptionsServiceImpl;
import com.uteshop.services.impl.web.catalog.ProductsServiceImpl;
import com.uteshop.services.impl.web.review.ReviewsServiceImpl;
import com.uteshop.services.web.*;
import com.uteshop.services.web.account.IUsersService;
import com.uteshop.services.web.catalog.IAttributesService;
import com.uteshop.services.web.catalog.ICategoriesService;
import com.uteshop.services.web.catalog.IOptionsService;
import com.uteshop.services.web.catalog.IProductsService;
import com.uteshop.services.web.review.IReviewsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = { "/product", "/product-detail" })
public class ProductController extends HttpServlet {
    IProductsService productsService = new ProductsServiceImpl();
    IOptionsService optionsService = new OptionsServiceImpl();
    ICategoriesService categoriesService = new CategoriesServiceImpl();
    IReviewsService reviewsService = new ReviewsServiceImpl();
    IUsersService usersService = new UsersServiceImpl();
    IAttributesService attributesService = new AttributesServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String path = req.getServletPath();

        switch (path) {
            case "/product":
                break;

            case "/product-detail":
                String slug = req.getParameter("product");
            
                List<Categories> parents = categoriesService.findParents();
                Products product = productsService.findBySlug(slug);
                List<Products> relevantProducts = productsService.getRelevantProducts(product.getId());
                List<OptionDto> options = optionsService.getOptionsByProduct(product.getId());
                Integer productId = product.getId();

                // Group options theo optionTypeCode để render đúng trong JSP
                Map<String, List<OptionDto>> groupedOptions = options.stream()
                        .collect(Collectors.groupingBy(
                                OptionDto::getOptionTypeCode,
                                LinkedHashMap::new,
                                Collectors.toList()));

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

                // Lấy thông số kỹ thuật
                Map<String, String> productAttributes = attributesService.getProductAttributes(productId);

                req.setAttribute("parentCategories", parents);
                req.setAttribute("product", product);
                req.setAttribute("groupedOptions", groupedOptions); // Thay options bằng groupedOptions
                req.setAttribute("relevantProducts", relevantProducts);
                req.setAttribute("selectedCategory", selectedCategory);
                req.setAttribute("selectedParent", selectedParent);
                req.setAttribute("reviews", reviews);
                req.setAttribute("reviewStats", reviewStats);
                req.setAttribute("canReview", canReview);
                req.setAttribute("hasPurchased", hasPurchased);
                req.setAttribute("hasReviewed", hasReviewed);
                req.setAttribute("productAttributes", productAttributes);

                req.getRequestDispatcher("/views/web/productDetail.jsp").forward(req, resp);

                break;
        }
    }
}
