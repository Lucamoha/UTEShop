package com.uteshop.controller.admin.Reviews;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.uteshop.entity.auth.Users;
import com.uteshop.entity.catalog.Products;
import com.uteshop.entity.engagement.Reviews;
import com.uteshop.services.admin.IProductsService;
import com.uteshop.services.admin.IReviewService;
import com.uteshop.services.admin.IUsersService;
import com.uteshop.services.impl.admin.ProductsServiceImpl;
import com.uteshop.services.impl.admin.ReviewServiceImpl;
import com.uteshop.services.impl.admin.UsersServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Controller for Admin Reviews - handles CRUD operations via GET/POST.
 * URLs mapped to /admin/Review/Reviews/* for consistency.
 */
@WebServlet(urlPatterns = {"/admin/Review/Reviews/list",
        "/admin/Review/Reviews/view",
        "/admin/Review/Reviews/delete",
        "/admin/Review/Reviews/export",
        "/admin/Review/Reviews/filter"})
public class ReviewController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        IReviewService reviewService = new ReviewServiceImpl();

        if (url.contains("admin/Review/Reviews/list")) {
            List<Reviews> listReviews = reviewService.getAll();
            req.setAttribute("listReviews", listReviews);
            req.getRequestDispatcher("/views/admin/Review/Reviews/list.jsp").forward(req, resp);
        } else if (url.contains("/admin/Review/Reviews/export")) {
            List<Reviews> listReviews = reviewService.getAll();
            exportToExcel(listReviews, resp);
            return;

        } else if (url.contains("/admin/Review/Reviews/view")) {
            Integer id = Integer.valueOf(req.getParameter("id"));
            Reviews review = reviewService.getById(id);
            if (review != null) {
                req.setAttribute("review", review);
            }
            req.getRequestDispatcher("/views/admin/Review/Reviews/view.jsp").forward(req, resp);
        } else if (url.contains("/admin/Review/Reviews/delete")) {
            Integer id = Integer.valueOf(req.getParameter("id"));
            reviewService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/Review/Reviews/list");
        } else if (url.contains("/admin/Review/Reviews/filter")) {
            Integer rating = req.getParameter("rating") != null ? Integer.valueOf(req.getParameter("rating")) : null;
            List<Reviews> listReviews = reviewService.getByRating(rating);
            req.setAttribute("listReviews", listReviews);
            req.setAttribute("selectedRating", rating);
            req.getRequestDispatcher("/views/admin/Review/Reviews/list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        IReviewService reviewService = new ReviewServiceImpl();
        req.setCharacterEncoding("UTF-8");
    }

    private void exportToExcel(List<Reviews> reviews, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"reviews_export_" + System.currentTimeMillis() + ".xlsx\"");
        response.setCharacterEncoding("UTF-8");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reviews");

            String[] columns = {"ID", "User ID", "Product ID", "Rating", "Content", "Has Media",
                    "Purchase Verified", "Status", "Created At"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            for (Reviews review : reviews) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(review.getId() != null ? review.getId() : 0);
                row.createCell(1).setCellValue(review.getUser() != null && review.getUser().getId() != null ? review.getUser().getId() : 0);
                row.createCell(2).setCellValue(review.getProduct() != null && review.getProduct().getId() != null ? review.getProduct().getId() : 0);
                row.createCell(3).setCellValue(review.getRating());
                row.createCell(4).setCellValue(review.getContent() != null ? review.getContent() : "");
                row.createCell(5).setCellValue(Boolean.TRUE.equals(review.getHasMedia()) ? "Yes" : "No");
                row.createCell(6).setCellValue(Boolean.TRUE.equals(review.getPurchaseVerified()) ? "Yes" : "No");
                row.createCell(7).setCellValue(review.isStatus() ? "Active" : "Inactive");
                row.createCell(8).setCellValue(review.getCreatedAt() != null ? review.getCreatedAt().format(DATE_FORMATTER) : "");
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Ghi ra output stream
            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Export Excel failed.");
        }
    }
}