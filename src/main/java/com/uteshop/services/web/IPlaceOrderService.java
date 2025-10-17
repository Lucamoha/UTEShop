package com.uteshop.services.web;

import com.uteshop.entity.order.Orders;

import java.math.BigDecimal;
import java.util.Map;

public interface IPlaceOrderService {
    Orders placeOrder(String userEmail,
                      Integer addressId,
                      Integer branchId,
                      Map<Integer, Integer> listVarAndQty,
                      String voucherCode,
                      int paymentMethod,
                      BigDecimal totalAmount,
                      String note);
}
