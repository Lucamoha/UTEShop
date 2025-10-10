package com.uteshop.dao.manager;

import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.order.Orders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IOrdersManagerDao {
    long countByStatus(Integer branchId, Integer orderStatus);
    long countByPaymentStatus(Integer branchId, Integer paymentStatus);
    /**
     * Doanh thu theo ngày (chỉ trong branch):
     * Trả về list [date, amount].
     */
    List<Object[]> revenueDaily(Integer branchId, LocalDate from, LocalDate to);

    /**
     * Tổng doanh thu thô (sum TotalAmount) theo filter ngày.
     */
    BigDecimal sumRevenue(Integer branchId, LocalDate from, LocalDate to);

    /**
     * Lấy Top N sản phẩm theo doanh thu/số lượng trong khoảng ngày.
     * Trả về [productId, productName, qty, amount].
     */
    List<Object[]> topProducts(Integer branchId, LocalDate from, LocalDate to, int limit);

    Map<Integer, Long> countByStatusRange(Integer branchId, LocalDate from, LocalDate to);

    List<Orders> findRecent(Integer branchId, int limit);

    PageResult<Orders> searchPaged(
            Integer branchId,
            Integer orderStatus,    // nullable
            Integer paymentStatus,  // nullable
            String q,               // nullable (id / name / phone / address)
            int page,               // 1-based
            int size                // page size
    );

    Orders findByIdWithItems(Integer orderId, Integer branchId);
}
