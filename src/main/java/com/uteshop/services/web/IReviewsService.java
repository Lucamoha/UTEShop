package com.uteshop.services.web;

import com.uteshop.entity.engagement.Reviews;

import java.util.List;
import java.util.Map;

public interface IReviewsService {

    // Lấy tất cả reviews của sản phẩm
    List<Reviews> getReviewsByProduct(int productId);

    // Lấy tất cả reviews của user
    List<Reviews> getReviewsByUser(int userId);

    // Thêm mới review
    Reviews addReview(Reviews review);

    // Cập nhật review
    void updateReview(Reviews review);

    // Xóa review
    void deleteReview(int id);

    // Kiểm tra user có thể review sản phẩm không
    // Điều kiện: Đã mua hàng + Chưa review
    boolean canUserReviewProduct(int userId, int productId);

    // Kiểm tra user đã mua sản phẩm chưa
    boolean hasUserPurchasedProduct(int userId, int productId);

    // Kiểm tra user đã review sản phẩm chưa
    boolean hasUserReviewedProduct(int userId, int productId);

    // Lấy thống kê review của sản phẩm
    Map<String, Object> getReviewStats(int productId);
}
