package com.uteshop.dao.impl.web;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dao.web.IOrdersDao;
import com.uteshop.entity.order.Orders;
import com.uteshop.enums.OrderEnums;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class OrdersDaoImpl implements IOrdersDao {

    @Override
    public List<Orders> findByUserId(Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            TypedQuery<Orders> query = em.createQuery(
                    "SELECT o FROM Orders o " +
                            "LEFT JOIN FETCH o.items " +
                            "WHERE o.user.id = :userId " +
                            "ORDER BY o.CreatedAt DESC", Orders.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public PageResult<Orders> findByUserIdPaged(Integer userId, int page, int size) {
        return findByUserIdAndStatus(userId, null, page, size);
    }

    @Override
    public Orders findOrderDetail(Integer orderId, Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            TypedQuery<Orders> query = em.createQuery(
                    "SELECT o FROM Orders o " +
                            "LEFT JOIN FETCH o.items oi " +
                            "LEFT JOIN FETCH oi.product p " +
                            "LEFT JOIN FETCH oi.variant v " +
                            "LEFT JOIN FETCH o.payment " +
                            "WHERE o.id = :orderId AND o.user.id = :userId", Orders.class);
            query.setParameter("orderId", orderId);
            query.setParameter("userId", userId);

            List<Orders> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public PageResult<Orders> findByUserIdAndStatus(Integer userId, Integer orderStatus, int page, int size) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            // Query để đếm tổng số records
            StringBuilder countQueryStr = new StringBuilder(
                    "SELECT COUNT(o) FROM Orders o WHERE o.user.id = :userId");

            // Query để lấy data
            StringBuilder dataQueryStr = new StringBuilder(
                    "SELECT DISTINCT o FROM Orders o " +
                            "LEFT JOIN FETCH o.items " +
                            "WHERE o.user.id = :userId");

            // Thêm điều kiện lọc theo status
            if (orderStatus != null) {
                countQueryStr.append(" AND o.OrderStatus = :orderStatus");
                dataQueryStr.append(" AND o.OrderStatus = :orderStatus");
            }

            dataQueryStr.append(" ORDER BY o.CreatedAt DESC");

            // Count query
            TypedQuery<Long> countQuery = em.createQuery(countQueryStr.toString(), Long.class);
            countQuery.setParameter("userId", userId);
            if (orderStatus != null) {
                countQuery.setParameter("orderStatus", orderStatus);
            }
            long totalElements = countQuery.getSingleResult();

            // Data query với phân trang
            TypedQuery<Orders> dataQuery = em.createQuery(dataQueryStr.toString(), Orders.class);
            dataQuery.setParameter("userId", userId);
            if (orderStatus != null) {
                dataQuery.setParameter("orderStatus", orderStatus);
            }

            dataQuery.setFirstResult((page - 1) * size);
            dataQuery.setMaxResults(size);

            List<Orders> content = dataQuery.getResultList();

            // Tạo PageResult
            return new PageResult<>(content, totalElements, page, size);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean cancelOrder(Integer orderId, Integer userId) {
        EntityManager em = JPAConfigs.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            
            Orders order = em.find(Orders.class, orderId);
            if (order == null || !order.getUser().getId().equals(userId)) {
                transaction.rollback();
                return false;
            }
            
            order.setOrderStatus(OrderEnums.OrderStatus.CANCELED);
            em.merge(order);
            
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
