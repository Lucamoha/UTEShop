package com.uteshop.services.impl.web.review;

import com.uteshop.dao.impl.web.review.ReviewsDaoImpl;
import com.uteshop.dao.web.review.IReviewsDao;
import com.uteshop.entity.engagement.Reviews;
import com.uteshop.services.web.review.IReviewsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsServiceImpl implements IReviewsService {

    private final IReviewsDao reviewsDao = new ReviewsDaoImpl();

    @Override
    public List<Reviews> getReviewsByProduct(int productId) {
        return reviewsDao.findByProductId(productId);
    }

    @Override
    public List<Reviews> getReviewsByUser(int userId) {
        return reviewsDao.findByUserId(userId);
    }

    @Override
    public Reviews addReview(Reviews review) {
        reviewsDao.insert(review);
        return review;
    }

    @Override
    public void updateReview(Reviews review) {
        reviewsDao.update(review);
    }

    @Override
    public void deleteReview(int id) {
        reviewsDao.delete(id);
    }

    @Override
    public boolean canUserReviewProduct(int userId, int productId) {
        // User phải:
        // 1. Đã mua sản phẩm
        // 2. Chưa review sản phẩm này
        return hasUserPurchasedProduct(userId, productId)
                && !hasUserReviewedProduct(userId, productId);
    }

    @Override
    public boolean hasUserPurchasedProduct(int userId, int productId) {
        return reviewsDao.hasUserPurchasedProduct(userId, productId);
    }

    @Override
    public boolean hasUserReviewedProduct(int userId, int productId) {
        return reviewsDao.hasUserReviewedProduct(userId, productId);
    }

    @Override
    public Map<String, Object> getReviewStats(int productId) {
        Map<String, Object> stats = new HashMap<>();

        // Tổng số reviews
        long totalReviews = reviewsDao.countByProductId(productId);
        stats.put("totalReviews", totalReviews);

        // Rating trung bình
        Double averageRating = reviewsDao.getAverageRatingByProductId(productId);
        stats.put("averageRating", averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0);

        // Thống kê số lượng rating (1-5 sao)
        Map<Integer, Long> distribution = reviewsDao.getRatingDistribution(productId);
        stats.put("ratingDistribution", distribution);

        // Tính phần trăm cho mỗi rating
        Map<Integer, Integer> ratingPercentages = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            if (totalReviews > 0) {
                long count = distribution.getOrDefault(i, 0L);
                int percentage = (int) Math.round((count * 100.0) / totalReviews);
                ratingPercentages.put(i, percentage);
            } else {
                ratingPercentages.put(i, 0);
            }
        }
        stats.put("ratingPercentages", ratingPercentages);
        return stats;
    }
}
