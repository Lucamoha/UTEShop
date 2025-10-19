package com.uteshop.dao.web;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.order.Orders;
import java.util.*;


public interface IOrdersDao {
    // Tìm tất cả đơn hàng của user theo userId
    List<Orders> findByUserId(Integer userId);
    // Tìm đơn hàng của user theo userId với phân trang
    PageResult<Orders> findByUserIdPaged(Integer userId, int page, int size);
    // Tìm chi tiết đơn hàng theo ID và userId
    Orders findOrderDetail(Integer orderId, Integer userId);
    // Tìm đơn hàng của user theo userId và trạng thái với phân trang
    PageResult<Orders> findByUserIdAndStatus(Integer userId, Integer orderStatus, int page, int size);
}
