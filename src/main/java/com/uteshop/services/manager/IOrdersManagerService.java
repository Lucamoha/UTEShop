package com.uteshop.services.manager;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dto.manager.reports.OrderStatsDTO;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IOrdersManagerService {
    List<Object[]> topProducts(Integer branchId, LocalDate from, LocalDate to, int limit);

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

    OrderStatsDTO getOrderStats(LocalDateTime from, LocalDateTime to, Integer branchId);
}
