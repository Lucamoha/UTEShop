package com.uteshop.dao.impl.web;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.web.IReviewsDao;
import com.uteshop.entity.engagement.Reviews;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewsDaoImpl extends AbstractDao<Reviews> implements IReviewsDao {

    public ReviewsDaoImpl() {
        super(Reviews.class);
    }

    @Override
    public List<Reviews> findByProductId(int productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reviews r " +
                    "LEFT JOIN FETCH r.user " +
                    "LEFT JOIN FETCH r.media " +
                    "WHERE r.product.Id = :productId " +
                    "AND r.Status = true " +
                    "ORDER BY r.CreatedAt DESC";

            TypedQuery<Reviews> query = em.createQuery(jpql, Reviews.class);
            query.setParameter("productId", productId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Reviews> findByUserId(int userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT r FROM Reviews r " +
                    "LEFT JOIN FETCH r.product " +
                    "WHERE r.user.Id = :userId " +
                    "ORDER BY r.CreatedAt DESC";

            TypedQuery<Reviews> query = em.createQuery(jpql, Reviews.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Reviews findById(int id) {
        return super.findById(id);
    }

    @Override
    public void delete(int id) {
        super.delete(id);
    }

    @Override
    public long countByProductId(int productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM Reviews r " +
                    "WHERE r.product.Id = :productId " +
                    "AND r.Status = true";

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("productId", productId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Double getAverageRatingByProductId(int productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT AVG(r.Rating) FROM Reviews r " +
                    "WHERE r.product.Id = :productId " +
                    "AND r.Status = true";

            TypedQuery<Double> query = em.createQuery(jpql, Double.class);
            query.setParameter("productId", productId);
            Double result = query.getSingleResult();
            return result != null ? result : 0.0;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean hasUserReviewedProduct(int userId, int productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM Reviews r " +
                    "WHERE r.user.Id = :userId " +
                    "AND r.product.Id = :productId";

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("userId", userId);
            query.setParameter("productId", productId);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public Map<Integer, Long> getRatingDistribution(int productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            String jpql = "SELECT r.Rating, COUNT(r) FROM Reviews r " +
                    "WHERE r.product.Id = :productId " +
                    "AND r.Status = true " +
                    "GROUP BY r.Rating";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("productId", productId);
            List<Object[]> results = query.getResultList();

            Map<Integer, Long> distribution = new HashMap<>();
            // Khởi tạo cho 1-5 sao
            for (int i = 1; i <= 5; i++) {
                distribution.put(i, 0L);
            }

            // Cập nhật từ database
            for (Object[] row : results) {
                Integer rating = (row[0] instanceof Number) ? ((Number) row[0]).intValue() : null;
                Long count = (row[1] instanceof Number) ? ((Number) row[1]).longValue() : 0L;
                if (rating == null) {
                    continue;
                }
                distribution.put(rating, count);
            }

            return distribution;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean hasUserPurchasedProduct(int userId, int productId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            // Kiểm tra xem user đã mua sản phẩm này chưa
            // OrderStatus = 3 (Nhận hàng) và PaymentStatus = 1 (Đã thanh toán)
            String jpql = "SELECT COUNT(oi) FROM OrderItems oi " +
                    "JOIN oi.order o " +
                    "WHERE o.user.Id = :userId " +
                    "AND oi.product.Id = :productId " +
                    "AND o.OrderStatus = 3 " +
                    "AND o.PaymentStatus = 1";

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("userId", userId);
            query.setParameter("productId", productId);
            return query.getSingleResult() > 0;
        } catch (NoResultException e) {
            return false;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        ReviewsDaoImpl dao = new ReviewsDaoImpl();
        System.out.println(dao.hasUserReviewedProduct(5, 2));
    }
}
