package com.uteshop.dao.admin;

import com.uteshop.entity.engagement.Reviews;
import java.util.List;

public interface IReviewDao {
    List<Reviews> findAll();
    Reviews findById(Integer id);
    void insert(Reviews review);
    void update(Reviews review);
    void delete(Integer id);
    List<Reviews> findByProductId(Integer productId);
    List<Reviews> findByUserId(Integer userId);
    List<Reviews> findByRating(Integer rating);
}