package com.uteshop.services.impl.manager;

import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dao.impl.manager.OrdersManagerDaoImpl;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;
import com.uteshop.services.manager.IOrdersManagerService;
import com.uteshop.util.AppConfig;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrdersManagerServiceImpl implements IOrdersManagerService {
    OrdersManagerDaoImpl dao = new OrdersManagerDaoImpl();
    EntityDaoImpl<Payments> paymentsEntityDao = new EntityDaoImpl<>(Payments.class);

    private static final int NEW        = AppConfig.getInt("orders.status.new");
    private static final int CONFIRMED  = AppConfig.getInt("orders.status.confirmed");
    private static final int SHIPPING   = AppConfig.getInt("orders.status.shipping");
    private static final int DELIVERED  = AppConfig.getInt("orders.status.delivered");
    private static final int CANCELED   = AppConfig.getInt("orders.status.canceled");
    private static final int RETURNED   = AppConfig.getInt("orders.status.returned");

    private static final int UNPAID     = AppConfig.getInt("payment.status.unpaid");
    private static final int PAID       = AppConfig.getInt("payment.status.paid");
    private static final int REFUNDED   = AppConfig.getInt("payment.status.refunded");

    private static final int PM_PENDING  = AppConfig.getInt("payments.status.pending");
    private static final int PM_SUCCESS  = AppConfig.getInt("payments.status.success");
    private static final int PM_FAILED   = AppConfig.getInt("payments.status.failed");
    private static final int PM_REFUNDED = AppConfig.getInt("payments.status.refunded");

    private static final int METHOD_COD  = AppConfig.getInt("payments.method.cod");

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

    private void ensureAllowedTransition(int from, int to) {
        // Kho vận:
        // new -> confirmed -> shipping -> delivered
        // new -> canceled
        // confirmed -> canceled
        // delivered -> returned

        boolean ok = false;
        if (from == NEW)
            ok = (to == CONFIRMED || to == CANCELED);
        else if (from == CONFIRMED)
            ok = (to == SHIPPING  || to == CANCELED);
        else if (from == SHIPPING)
            ok = (to == DELIVERED);
        else if (from == DELIVERED)
            ok = (to == RETURNED);

        if (!ok) {
            throw new IllegalStateException("Trạng thái không hợp lệ");
        }
    }

    @Transactional
    @Override
    public void updateOrderStatus(Integer orderId, Integer toStatus) {
        Orders order = dao.findById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng có mã: " + orderId);
        }

        int fromStatus = order.getOrderStatus();
        ensureAllowedTransition(fromStatus, toStatus);

        Payments payment = dao.getPaymentByOrderId(orderId);
        if (payment == null) {
            throw new IllegalStateException("Đơn hàng này chưa có bản ghi thanh toán.");
        }

        boolean isOnline = payment.getMethod() != METHOD_COD;

        // ====== RÀNG BUỘC THEO TRẠNG THÁI ĐÍCH ======
        if (toStatus == SHIPPING) {
            // Online: chỉ được SHIPPING khi đã thanh toán xong
            if (isOnline) {
                if (payment.getStatus() != PM_SUCCESS) {
                    throw new IllegalStateException("Đơn thanh toán online phải ở trạng thái đã thanh toán (payment = SUCCESS) trước khi chuyển sang SHIPPING.");
                }
                if (order.getPaymentStatus() != PAID) {
                    order.setPaymentStatus(PAID);
                }
            }
            // COD: không yêu cầu trả trước

        } else if (toStatus == DELIVERED) {
            if (!isOnline) {
                // COD: giao thành công coi như thu tiền thành công
                if (payment.getStatus() != PM_SUCCESS) {
                    payment.setStatus(PM_SUCCESS);
                    payment.setPaidAt(java.time.LocalDateTime.now());
                    if (payment.getPaidAmount() == null) {
                        payment.setPaidAmount(order.getTotalAmount());
                    }
                    paymentsEntityDao.update(payment);
                }
                order.setPaymentStatus(PAID);
            } else {
                // Online: tới đây phải SUCCESS từ lúc SHIPPING
                if (payment.getStatus() != PM_SUCCESS) {
                    throw new IllegalStateException("Đơn thanh toán online phải ở trạng thái đã thanh toán (payment = SUCCESS) trước khi chuyển sang DELIVERED.");
                }
                if (order.getPaymentStatus() != PAID) {
                    order.setPaymentStatus(PAID);
                }
            }

        } else if (toStatus == RETURNED) {
            if (isOnline) {
                // Online: chỉ RETURNED khi đã hoàn tiền xong
                if (payment.getStatus() != PM_REFUNDED) {
                    throw new IllegalStateException("Đơn thanh toán online chỉ được chuyển sang RETURNED sau khi thanh toán đã hoàn tiền (payment = REFUNDED).");
                }
                order.setPaymentStatus(REFUNDED);
            } else {
                // COD: khi trả hàng thì ghi nhận hoàn tiền
                if (payment.getStatus() != PM_REFUNDED) {
                    payment.setStatus(PM_REFUNDED);
                    paymentsEntityDao.update(payment);
                }
                order.setPaymentStatus(REFUNDED);
            }

        } else if (toStatus == CANCELED) {
            // Có thể hủy từ NEW/CONFIRMED. Với online đã thu tiền → bắt buộc refund trước.
            if (isOnline && payment.getStatus() == PM_SUCCESS) {
                throw new IllegalStateException("Không thể hủy đơn online đã thanh toán. Vui lòng hoàn tiền trước khi hủy.");
            }
            // Đồng bộ PaymentStatus theo trạng thái thanh toán hiện tại
            if (payment.getStatus() == PM_REFUNDED) {
                order.setPaymentStatus(REFUNDED);
            } else if (payment.getStatus() == PM_SUCCESS) {
                order.setPaymentStatus(PAID);
            } else {
                order.setPaymentStatus(UNPAID);
            }
        } else {
            // NEW / CONFIRMED: không có ràng buộc thanh toán bổ sung
        }

        // ====== CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG ======
        order.setOrderStatus(toStatus);

        // ====== ĐỒNG BỘ PaymentStatus CHO CÁC BƯỚC TRUNG GIAN (nếu chưa set ở trên) ======
        if (toStatus != CANCELED && toStatus != RETURNED && toStatus != DELIVERED) {
            int ps;
            if (payment.getStatus() == PM_SUCCESS) {
                ps = PAID;
            } else if (payment.getStatus() == PM_REFUNDED) {
                ps = REFUNDED;
            } else {
                ps = UNPAID;
            }
            order.setPaymentStatus(ps);
        }

        dao.update(order);
    }

    @Override
    public Payments getPaymentByOrderId(Integer orderId) {
        return dao.getPaymentByOrderId(orderId);
    }
}
