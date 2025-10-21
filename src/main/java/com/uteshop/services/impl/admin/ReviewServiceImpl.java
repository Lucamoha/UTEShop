package com.uteshop.services.impl.admin;

import com.uteshop.dao.admin.IReviewDao;
import com.uteshop.dao.impl.admin.ReviewDaoImpl;
import com.uteshop.entity.engagement.Reviews;
import com.uteshop.services.admin.IReviewService;

import java.util.List;

/**
 * Implementation for ReviewService - handles CRUD and queries for Reviews.
 * Uses eager fetch for product/user to avoid lazy loading issues.
 */
public class ReviewServiceImpl implements IReviewService {
    private final ReviewDaoImpl reviewDao = new ReviewDaoImpl();  // Inject via constructor if needed for DI

    @Override
    public List<Reviews> getAll() {
        return reviewDao.findAll();  // Eager fetch included in DAO
    }

    @Override
    public Reviews getById(Integer id) {
        return reviewDao.findById(id);  // Eager fetch included
    }

    @Override
    public void save(Reviews review) {
        if (review.getId() == null) {
            reviewDao.insert(review);
        } else {
            reviewDao.update(review);
        }
    }

    @Override
    public void delete(Integer id) {
        reviewDao.delete(id);
    }

    @Override
    public List<Reviews> getByProductId(Integer productId) {
        return reviewDao.findByProductId(productId);
    }

    @Override
    public List<Reviews> getByUserId(Integer userId) {
        return reviewDao.findByUserId(userId);
    }

    @Override
    public List<Reviews> getByRating(Integer rating) {
        return reviewDao.findByRating(rating);
    }
}