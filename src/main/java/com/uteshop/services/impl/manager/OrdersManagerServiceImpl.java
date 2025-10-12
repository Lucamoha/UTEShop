package com.uteshop.services.impl.manager;

import com.uteshop.dao.AbstractDao;
import com.uteshop.dao.manager.IOrdersManagerDao;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dao.impl.manager.OrdersManagerDaoImpl;
import com.uteshop.entity.order.Orders;
import com.uteshop.services.manager.IOrdersManagerService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrdersManagerServiceImpl extends AbstractDao<Orders> implements IOrdersManagerService {
    IOrdersManagerDao dao = new OrdersManagerDaoImpl();

    public OrdersManagerServiceImpl() {
        super(Orders.class);
    }

    @Override
    public long countByStatus(Integer branchId, Integer orderStatus) {
        return dao.countByStatus(branchId, orderStatus);
    }

    @Override
    public long countByPaymentStatus(Integer branchId, Integer paymentStatus) {
        return dao.countByPaymentStatus(branchId, paymentStatus);
    }

    @Override
    public List<Object[]> revenueDaily(Integer branchId, LocalDate from, LocalDate to) {
        return dao.revenueDaily(branchId, from, to);
    }

    @Override
    public BigDecimal sumRevenue(Integer branchId, LocalDate from, LocalDate to) {
        return dao.sumRevenue(branchId, from, to);
    }

    @Override
    public List<Object[]> topProducts(Integer branchId, LocalDate from, LocalDate to, int limit) {
        return dao.topProducts(branchId, from, to, limit);
    }

    @Override
    public Map<Integer, Long> countByStatusRange(Integer branchId, LocalDate from, LocalDate to) {
        return dao.countByStatusRange(branchId, from, to);
    }

    @Override
    public List<Orders> findRecent(Integer branchId, int limit) {
        return dao.findRecent(branchId, limit);
    }

    @Override
    public PageResult<Orders> searchPaged(Integer branchId, Integer orderStatus, Integer paymentStatus, String q, int page, int size) {
        return dao.searchPaged(branchId, orderStatus, paymentStatus, q, page, size);
    }

    @Override
    public Orders findByIdWithItems(Integer orderId, Integer branchId) {
        return dao.findByIdWithItems(orderId, branchId);
    }
}
