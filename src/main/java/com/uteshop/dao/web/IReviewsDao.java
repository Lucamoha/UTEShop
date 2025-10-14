package com.uteshop.dao.web;

import com.uteshop.entity.engagement.Reviews;

import java.util.List;
import java.util.Map;

public interface IReviewsDao {

    List<Reviews> findByProductId(int productId);

    List<Reviews> findByUserId(int userId);

    Reviews findById(int id);

    void insert(Reviews review);

    void update(Reviews review);

    void delete(int id);

    long countByProductId(int productId);

    Double getAverageRatingByProductId(int productId);

    boolean hasUserReviewedProduct(int userId, int productId);

    /**
     * Lấy phân bố rating (1 sao có bao nhiêu, 2 sao có bao nhiêu...)
     */
    Map<Integer, Long> getRatingDistribution(int productId);

    boolean hasUserPurchasedProduct(int userId, int productId);
}
