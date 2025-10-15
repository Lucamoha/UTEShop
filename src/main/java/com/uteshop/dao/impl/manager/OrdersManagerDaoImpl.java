package com.uteshop.dao.impl.manager;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.manager.IOrdersManagerDao;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;
import com.uteshop.enums.OrderEnums;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class OrdersManagerDaoImpl extends AbstractDao<Orders> implements IOrdersManagerDao {
    public OrdersManagerDaoImpl() {
        super(Orders.class);
    }

    @Override
    public long countOrdersByRange(LocalDateTime from, LocalDateTime to, Integer branchId) {
        EntityManager em = JPAConfigs.getEntityManager();

        var q = em.createQuery("""
                SELECT COUNT(o)
                FROM Orders o
                WHERE o.CreatedAt >= :from AND o.CreatedAt < :to
                  AND (:branchId IS NULL OR o.branch.Id = :branchId)
                """, Long.class);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("branchId", branchId);
        return q.getSingleResult();
    }

    @Override
    public long countOrdersByPaymentMethod(LocalDateTime from, LocalDateTime to, Integer branchId, int method) {
        EntityManager em = JPAConfigs.getEntityManager();

        var q = em.createQuery("""
                SELECT COUNT(p)
                FROM Payments p
                JOIN p.order o
                WHERE o.CreatedAt >= :from AND o.CreatedAt < :to
                  AND p.Method = :method
                  AND (:branchId IS NULL OR o.branch.Id = :branchId)
                """, Long.class);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("branchId", branchId);
        q.setParameter("method", method);
        return q.getSingleResult();
    }

    @Override
    public long countOrdersByStatus(LocalDateTime from, LocalDateTime to, Integer branchId, int status) {
        EntityManager em = JPAConfigs.getEntityManager();

        var q = em.createQuery("""
                SELECT COUNT(o)
                FROM Orders o
                WHERE o.CreatedAt >= :from AND o.CreatedAt < :to
                  AND o.OrderStatus = :status
                  AND (:branchId IS NULL OR o.branch.Id = :branchId)
                """, Long.class);
        q.setParameter("from", from);
        q.setParameter("to", to);
        q.setParameter("branchId", branchId);
        q.setParameter("status", status);
        return q.getSingleResult();
    }

    @Override
    public List<Object[]> revenueDaily(Integer branchId, LocalDate from, LocalDate to) {
        EntityManager enma = JPAConfigs.getEntityManager();

        var q = enma.createQuery("""
            SELECT CAST(o.CreatedAt AS date), SUM(o.TotalAmount)
            FROM Orders o
            WHERE o.branch.Id = :b
              AND o.PaymentStatus = :paymentStatus
              AND o.CreatedAt >= :from AND o.CreatedAt < :toPlus
            GROUP BY CAST(o.CreatedAt AS date)
            ORDER BY CAST(o.CreatedAt AS date)
            """, Object[].class);
        q.setParameter("b", branchId);
        q.setParameter("paymentStatus", OrderEnums.PaymentStatus.PAID);
        q.setParameter("from", from.atStartOfDay());
        q.setParameter("toPlus", to.plusDays(1).atStartOfDay());
        return q.getResultList();
    }

    @Override
    public List<Object[]> topProducts(Integer branchId, LocalDate from, LocalDate to, int limit) {
        EntityManager enma = JPAConfigs.getEntityManager();

        String jpql = """
            SELECT p.Id, p.Name, SUM(oi.Quantity), SUM(oi.Price * oi.Quantity)
            FROM Orders o
            JOIN o.items oi
            JOIN oi.variant v
            JOIN v.product p
            WHERE (:b IS NULL OR o.branch.Id = :b)
              AND o.PaymentStatus = :paymentStatus
              AND o.CreatedAt >= :fromAt AND o.CreatedAt < :toAt
            GROUP BY p.Id, p.Name
            ORDER BY SUM(oi.Price * oi.Quantity) DESC
            """;
        TypedQuery<Object[]> q = enma.createQuery(jpql, Object[].class);
        q.setParameter("b", branchId);
        q.setParameter("paymentStatus", OrderEnums.PaymentStatus.PAID);
        q.setParameter("fromAt", from.atStartOfDay());
        q.setParameter("toAt", to.plusDays(1).atStartOfDay());
        q.setMaxResults(Math.max(1, limit));
        return q.getResultList();
    }

    @Override
    public List<Orders> findRecent(Integer branchId, int limit) {
        EntityManager enma = JPAConfigs.getEntityManager();

        String jpql = "SELECT o FROM Orders o WHERE (:b IS NULL OR o.branch.Id = :b) ORDER BY o.CreatedAt DESC";
        TypedQuery<Orders> q = enma.createQuery(jpql, Orders.class);
        q.setParameter("b", branchId);
        q.setMaxResults(Math.max(1, limit));
        return q.getResultList();
    }

    @Override
    public PageResult<Orders> searchPaged(Integer branchId, Integer orderStatus, Integer paymentStatus, String q, int page, int size) {
        EntityManager enma = JPAConfigs.getEntityManager();

        String base = """
        from Orders o
        where o.branch.Id = :b""";

        StringBuilder where = new StringBuilder();
        if (orderStatus != null) where.append(" and o.OrderStatus = :os");
        if (paymentStatus != null) where.append(" and o.PaymentStatus = :ps");
        if (q != null && !q.isBlank()) {
            where.append("""
            and (
                cast(o.Id as string) like :kw
                or lower(o.ReceiverName) like :kw
                or o.Phone like :kw
                or lower(o.AddressLine) like :kw
            )
        """);
        }

        String orderBy = " order by o.CreatedAt desc";

        var jpqlList = "select o " + base + where + orderBy;
        var jpqlCount = "select count(o) " + base + where;

        var listQuery = enma.createQuery(jpqlList, Orders.class)
                .setParameter("b", branchId);
        var countQuery = enma.createQuery(jpqlCount, Long.class)
                .setParameter("b", branchId);

        if (orderStatus != null) { listQuery.setParameter("os", orderStatus); countQuery.setParameter("os", orderStatus); }
        if (paymentStatus != null){ listQuery.setParameter("ps", paymentStatus); countQuery.setParameter("ps", paymentStatus); }
        if (q != null && !q.isBlank()) {
            String kw = "%" + q.toLowerCase().trim() + "%";
            listQuery.setParameter("kw", kw);
            countQuery.setParameter("kw", kw);
        }

        int first = (Math.max(page,1)-1) * Math.max(size,1);
        listQuery.setFirstResult(first);
        listQuery.setMaxResults(size);

        var content = listQuery.getResultList();
        long total = countQuery.getSingleResult();
        return new PageResult<>(content, total, page, size);
    }

    @Override
    public Orders findByIdWithItems(Integer orderId, Integer branchId) {
        EntityManager enma = JPAConfigs.getEntityManager();

        String jpql = """
            select o from Orders o
            left join fetch o.items i
            left join fetch i.product p
            left join fetch i.variant v
            where o.Id = :id and o.branch.Id = :b
        """;
        var list = enma.createQuery(jpql, Orders.class)
                .setParameter("id", orderId)
                .setParameter("b", branchId)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Payments getPaymentByOrderId(Integer orderId) {
        EntityManager em = JPAConfigs.getEntityManager();
        try {
            Orders order = em.find(Orders.class, orderId);
            if (order == null) {
                return null;
            }
            return order.getPayment();
        } finally {
            em.close();
        }
    }
}
