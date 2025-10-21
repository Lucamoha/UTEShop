package com.uteshop.dao.impl.admin;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.admin.IReviewDao;
import com.uteshop.entity.engagement.Reviews;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

public class ReviewDaoImpl extends AbstractDao<Reviews> implements IReviewDao {
    public ReviewDaoImpl() {
        super(Reviews.class);
    }

    @Override
    public List<Reviews> findAll() {
        EntityManager em = JPAConfigs.getEntityManager();
        return em.createQuery(
                        "SELECT r FROM Reviews r " +
                                "LEFT JOIN FETCH r.product " +
                                "LEFT JOIN FETCH r.user " +
                                "LEFT JOIN FETCH r.media", Reviews.class)
                .getResultList();
    }

    @Override
    public Reviews findById(Integer id) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM Reviews r " +
                                    "LEFT JOIN FETCH r.product " +
                                    "LEFT JOIN FETCH r.user " +
                                    "LEFT JOIN FETCH r.media " +
                                    "WHERE r.Id = :id", Reviews.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void insert(Reviews review) {
        EntityManager em = JPAConfigs.getEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(review);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Insert Review failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Reviews review) {
        EntityManager em = JPAConfigs.getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(review);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Update Review failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = JPAConfigs.getEntityManager();
        em.getTransaction().begin();
        try {
            Reviews review = em.find(Reviews.class, id);
            if (review != null) {
                em.remove(review);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Delete Review failed: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Reviews> findByProductId(Integer productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        return em.createQuery(
                        "SELECT r FROM Reviews r " +
                                "LEFT JOIN FETCH r.user " +
                                "LEFT JOIN FETCH r.media " +
                                "WHERE r.product.Id = :productId", Reviews.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    @Override
    public List<Reviews> findByUserId(Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        return em.createQuery(
                        "SELECT r FROM Reviews r " +
                                "LEFT JOIN FETCH r.product " +
                                "LEFT JOIN FETCH r.media " +
                                "WHERE r.user.Id = :userId", Reviews.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Reviews> findByRating(Integer rating) {
        EntityManager em = JPAConfigs.getEntityManager();
        return em.createQuery(
                        "SELECT r FROM Reviews r " +
                                "LEFT JOIN FETCH r.product " +
                                "LEFT JOIN FETCH r.user " +
                                "LEFT JOIN FETCH r.media " +
                                "WHERE r.Rating = :rating", Reviews.class)
                .setParameter("rating", rating)
                .getResultList();
    }
}