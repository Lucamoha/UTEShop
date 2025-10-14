package com.uteshop.services.manager;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IOrdersManagerService {
    long countByStatus(Integer branchId, Integer orderStatus);

    long countByPaymentStatus(Integer branchId, Integer paymentStatus);

    List<Object[]> revenueDaily(Integer branchId, LocalDate from, LocalDate to);


    BigDecimal sumRevenue(Integer branchId, LocalDate from, LocalDate to);

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

    void updateOrderStatus(Integer orderId, Integer toStatus);

    Payments getPaymentByOrderId(Integer orderId);
}
