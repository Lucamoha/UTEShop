package com.uteshop.dao.manager;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IOrdersManagerDao {
    /**
     * Đếm tổng số đơn hàng trong khoảng thời gian (lọc theo branch nếu có)
     */
    long countOrdersByRange(LocalDateTime from, LocalDateTime to, Integer branchId);

    /**
     * Đếm số đơn có phương thức thanh toán (COD / VNPAY / MoMo)
     * method: 0 = COD, 1 = VNPAY, 2 = MoMo
     */
    long countOrdersByPaymentMethod(LocalDateTime from, LocalDateTime to, Integer branchId, int method);

    /**
     * Đếm số đơn hàng theo trạng thái (0=new,1=confirmed,2=shipping,3=delivered,4=canceled,5=returned)
     */
    long countOrdersByStatus(LocalDateTime from, LocalDateTime to, Integer branchId, int status);

    /**
     * Doanh thu theo ngày (chỉ trong branch):
     * Trả về list [date, amount].
     */
    List<Object[]> revenueDaily(Integer branchId, LocalDate from, LocalDate to);

    /**
     * Lấy Top N sản phẩm theo doanh thu/số lượng trong khoảng ngày.
     * Trả về [productId, productName, qty, amount].
     */
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

    Payments getPaymentByOrderId(Integer orderId);
}
