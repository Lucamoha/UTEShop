package com.uteshop.services.web;

import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.entity.order.Orders;

import java.util.List;

public interface IOrdersService {
    List<Orders> findByUserId(Integer userId);
    PageResult<Orders> findByUserIdPaged(Integer userId, int page, int size);
    Orders findOrderDetail(Integer orderId, Integer userId);
    PageResult<Orders> findByUserIdAndStatus(Integer userId, Integer orderStatus, int page, int size);
    void updateOrderStatus(Integer orderId, Integer toStatus);
}