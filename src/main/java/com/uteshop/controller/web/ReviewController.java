package com.uteshop.controller.web;

import com.uteshop.entity.auth.Users;
import com.uteshop.entity.catalog.Products;
import com.uteshop.entity.engagement.ReviewMedia;
import com.uteshop.entity.engagement.Reviews;
import com.uteshop.services.impl.web.ProductsServiceImpl;
import com.uteshop.services.impl.web.ReviewsServiceImpl;
import com.uteshop.services.impl.web.UsersServiceImpl;
import com.uteshop.services.web.IProductsService;
import com.uteshop.services.web.IReviewsService;
import com.uteshop.services.web.IUsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = { "/review/submit" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 100 // 100MB
)
public class ReviewController extends HttpServlet {

    private final IReviewsService reviewsService = new ReviewsServiceImpl();
    private final IUsersService usersService = new UsersServiceImpl();
    private final IProductsService productsService = new ProductsServiceImpl();

    // Thư mục lưu media reviews
    private static final String UPLOAD_DIR_IMAGES = "uploads/reviews/images";
    private static final String UPLOAD_DIR_VIDEOS = "uploads/reviews/videos";

    // file types
    private static final String[] ALLOWED_IMAGE_TYPES = { "image/jpeg", "image/jpg", "image/png", "image/gif",
            "image/webp" };
    private static final String[] ALLOWED_VIDEO_TYPES = { "video/mp4", "video/avi", "video/mov", "video/wmv" };

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            // Lấy thông tin user từ request attribute
            String authenticatedEmail = (String) req.getAttribute("authenticatedEmail");

            if (authenticatedEmail == null) {
                sendJsonResponse(resp, 401, false, "Vui lòng đăng nhập để đánh giá sản phẩm");
                return;
            }

            // Lấy user từ database
            Users user = usersService.findByEmail(authenticatedEmail);
            if (user == null) {
                sendJsonResponse(resp, 404, false, "Không tìm thấy thông tin người dùng");
                return;
            }

            // Lấy thông tin từ form
            int productId = Integer.parseInt(req.getParameter("productId"));
            int rating = Integer.parseInt(req.getParameter("rating"));
            String content = req.getParameter("content");

            // Validate content
            if (content == null || content.trim().isEmpty()) {
                sendJsonResponse(resp, 400, false, "Vui lòng nhập nội dung đánh giá");
                return;
            }

            // Kiểm tra sản phẩm tồn tại
            Products product = productsService.findById(productId);
            if (product == null) {
                sendJsonResponse(resp, 404, false, "Không tìm thấy sản phẩm");
                return;
            }

            // Kiểm tra user đã review chưa
            boolean hasReviewed = reviewsService.hasUserReviewedProduct(user.getId(), productId);
            boolean hasPurchased = reviewsService.hasUserPurchasedProduct(user.getId(), productId);

            if (hasReviewed) {
                sendJsonResponse(resp, 409, false, "Bạn đã đánh giá sản phẩm này rồi");
                return;
            }

            // Kiểm tra user đã mua sản phẩm chưa
            if (!hasPurchased) {
                sendJsonResponse(resp, 403, false, "Bạn cần mua sản phẩm này trước khi đánh giá");
                return;
            }

            // Xử lý upload media (images hoặc video)
            List<ReviewMedia> mediaList = new ArrayList<>();
            List<Part> imageParts = new ArrayList<>();
            Part videoPart = null;

            // Lấy tất cả file parts
            for (Part part : req.getParts()) {
                if (part.getName().equals("images") && part.getSize() > 0) {
                    imageParts.add(part);
                } else if (part.getName().equals("video") && part.getSize() > 0) {
                    videoPart = part;
                }
            }

            // Validate: Chỉ cho phép 3 ảnh HOẶC 1 video
            if (!imageParts.isEmpty() && videoPart != null) {
                sendJsonResponse(resp, 400, false, "Chỉ được upload ảnh HOẶC video, không được cả hai");
                return;
            }

            if (imageParts.size() > 3) {
                sendJsonResponse(resp, 400, false, "Chỉ được upload tối đa 3 ảnh");
                return;
            }

            // Tạo review entity
            Reviews review = Reviews.builder()
                    .product(product)
                    .user(user)
                    .Rating(rating)
                    .Content(content.trim())
                    .HasMedia(!imageParts.isEmpty() || videoPart != null)
                    .PurchaseVerified(true)
                    .Status(true) // Auto-approve (có thể đổi thành false để admin duyệt nếu có chức năng đó nha)
                    .build();

            // Lưu review vào database (để lấy ID)
            reviewsService.addReview(review);

            // Upload và lưu media
            int sortOrder = 0;

            // Upload images
            if (!imageParts.isEmpty()) {
                for (Part imagePart : imageParts) {
                    if (!isValidImageType(imagePart.getContentType())) {
                        sendJsonResponse(resp, 400, false,
                                "Định dạng ảnh không hợp lệ. Chỉ chấp nhận: JPG, PNG, GIF, WEBP");
                        return;
                    }

                    String imageUrl = saveFile(imagePart, UPLOAD_DIR_IMAGES, req);

                    ReviewMedia media = ReviewMedia.builder()
                            .review(review)
                            .MediaUrl(imageUrl)
                            .MediaType(1) // 1 = image
                            .SortOrder(sortOrder++)
                            .build();

                    mediaList.add(media);
                }
            }

            // Upload video
            if (videoPart != null) {
                if (!isValidVideoType(videoPart.getContentType())) {
                    sendJsonResponse(resp, 400, false,
                            "Định dạng video không hợp lệ. Chỉ chấp nhận: MP4, AVI, MOV, WMV");
                    return;
                }

                String videoUrl = saveFile(videoPart, UPLOAD_DIR_VIDEOS, req);

                ReviewMedia media = ReviewMedia.builder()
                        .review(review)
                        .MediaUrl(videoUrl)
                        .MediaType(2) // 2 = video
                        .SortOrder(0)
                        .build();

                mediaList.add(media);
            }

            // Set media list vào review và update
            review.setMedia(mediaList);
            reviewsService.updateReview(review);

            // Trả về success
            sendJsonResponse(resp, 200, true, "Đánh giá của bạn đã được gửi thành công!");

        } catch (NumberFormatException e) {
            sendJsonResponse(resp, 400, false, "Dữ liệu không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(resp, 500, false, "Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private String saveFile(Part filePart, String uploadDir, HttpServletRequest req) throws IOException {
        // Lấy đường dẫn thực tế của webapp
        String applicationPath = req.getServletContext().getRealPath("");
        String uploadPath = applicationPath + File.separator + uploadDir;

        // Tạo thư mục nếu chưa tồn tại
        File uploadDirFile = new File(uploadPath);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // Tạo tên file unique
        String originalFilename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Lưu file
        Path filePath = Paths.get(uploadPath, uniqueFilename);
        Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Trả về URL
        return uploadDir + "/" + uniqueFilename;
    }

    private boolean isValidImageType(String contentType) {
        if (contentType == null)
            return false;
        for (String allowedType : ALLOWED_IMAGE_TYPES) {
            if (contentType.equalsIgnoreCase(allowedType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidVideoType(String contentType) {
        if (contentType == null)
            return false;
        for (String allowedType : ALLOWED_VIDEO_TYPES) {
            if (contentType.equalsIgnoreCase(allowedType)) {
                return true;
            }
        }
        return false;
    }

    private void sendJsonResponse(HttpServletResponse resp, int status, boolean success, String message)
            throws IOException {
        resp.setStatus(status);
        String json = String.format("{\"success\": %b, \"message\": \"%s\"}", success, message);
        resp.getWriter().write(json);
    }
}
