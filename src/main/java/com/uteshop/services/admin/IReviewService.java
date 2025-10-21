package com.uteshop.services.admin;

import com.uteshop.entity.engagement.Reviews;

import java.util.List;

public interface IReviewService {
    List<Reviews> getAll();
    Reviews getById(Integer id);
    void save(Reviews review);
    void delete(Integer id);
    List<Reviews> getByProductId(Integer productId);
    List<Reviews> getByUserId(Integer userId);
    List<Reviews> getByRating(Integer rating);
}