package com.uteshop.services.impl.web.order;

import com.uteshop.configs.JPAConfigs;
import com.uteshop.dao.impl.web.order.OrdersDaoImpl;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dao.web.order.IOrdersDao;
import com.uteshop.entity.order.Orders;
import com.uteshop.enums.OrderEnums;
import com.uteshop.services.impl.manager.OrdersManagerServiceImpl;
import com.uteshop.services.manager.IOrdersManagerService;
import com.uteshop.services.web.order.IOrdersService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class OrdersServiceImpl implements IOrdersService {
    IOrdersDao ordersDao = new OrdersDaoImpl();
    IOrdersManagerService ordersManagerService = new OrdersManagerServiceImpl();

    @Override
    public List<Orders> findByUserId(Integer userId) {
        return ordersDao.findByUserId(userId);
    }

    @Override
    public PageResult<Orders> findByUserIdPaged(Integer userId, int page, int size) {
        return ordersDao.findByUserIdPaged(userId, page, size);
    }

    @Override
    public Orders findOrderDetail(Integer orderId, Integer userId) {
        return ordersDao.findOrderDetail(orderId, userId);
    }

    @Override
    public PageResult<Orders> findByUserIdAndStatus(Integer userId, Integer orderStatus, int page, int size) {
        return ordersDao.findByUserIdAndStatus(userId, orderStatus, page, size);
    }

    @Override
    public void updateOrderStatus(Integer orderId, Integer toStatus) {
        ordersManagerService.updateOrderStatus(orderId, toStatus);
    }
}