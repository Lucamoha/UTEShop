package com.uteshop.services.impl.manager;

import com.uteshop.dao.impl.manager.EntityDaoImpl;
import com.uteshop.dao.manager.common.PageResult;
import com.uteshop.dao.impl.manager.OrdersManagerDaoImpl;
import com.uteshop.dto.manager.reports.OrderStatsDTO;
import com.uteshop.dto.manager.reports.PaymentBreakdownDTO;
import com.uteshop.entity.order.Orders;
import com.uteshop.entity.order.Payments;
import com.uteshop.enums.OrderEnums;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.impl.web.payment.MomoServiceImpl;
import com.uteshop.services.impl.web.payment.VnpayServiceImpl;
import com.uteshop.services.manager.IOrdersManagerService;
import com.uteshop.services.web.payment.IPaymentService;
import com.uteshop.util.EmailService;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersManagerServiceImpl implements IOrdersManagerService {
    OrdersManagerDaoImpl dao = new OrdersManagerDaoImpl();
    IPaymentService paymentService = null;

    private static final int NEW        = OrderEnums.OrderStatus.NEW;
    private static final int CONFIRMED  = OrderEnums.OrderStatus.CONFIRMED;
    private static final int SHIPPING   = OrderEnums.OrderStatus.SHIPPING;
    private static final int DELIVERED  = OrderEnums.OrderStatus.DELIVERED;
    private static final int CANCELED   = OrderEnums.OrderStatus.CANCELED;
    private static final int RETURNED   = OrderEnums.OrderStatus.RETURNED;

    private static final int UNPAID     = OrderEnums.PaymentStatus.UNPAID;
    private static final int PAID       = OrderEnums.PaymentStatus.PAID;
    private static final int REFUNDED   = OrderEnums.PaymentStatus.REFUNDED;

    private static final int PM_PENDING  = PaymentEnums.Status.PENDING;
    private static final int PM_SUCCESS  = PaymentEnums.Status.SUCCESS;
    private static final int PM_FAILED   = PaymentEnums.Status.FAILED;
    private static final int PM_REFUNDED = PaymentEnums.Status.REFUNDED;

    private static final int METHOD_COD   = PaymentEnums.Method.COD;
    private static final int METHOD_MOMO  = PaymentEnums.Method.MOMO;
    private static final int METHOD_VNPAY = PaymentEnums.Method.VNPAY;

    @Override
    public List<Object[]> topProducts(Integer branchId, LocalDate from, LocalDate to, int limit) {
        return dao.topProducts(branchId, from, to, limit);
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

        Payments payment = order.getPayment();
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
                // ONLINE: Nếu chưa hoàn tiền thì thực hiện refund qua cổng thanh toán
                if (payment.getStatus() != PM_REFUNDED) {
                    if (payment.getTxnId() == null || payment.getTxnId().isBlank()) {
                        throw new IllegalStateException("Thiếu mã giao dịch gốc (txnId) để hoàn tiền online.");
                    }

                    IPaymentService.RefundRequest refundRequest = new IPaymentService.RefundRequest();
                    if (order.getPayment().getMethod() ==  METHOD_MOMO)
                        paymentService = new MomoServiceImpl();
                    else if (order.getPayment().getMethod() ==  METHOD_VNPAY) {
                        paymentService = new VnpayServiceImpl();
                        Map<String, String> m = new HashMap<String, String>();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                        m.put("vnp_TransactionDate", payment.getPaidAt().format(formatter));
                        refundRequest.withMeta(m);
                    }
                    else
                        throw new IllegalStateException("Phương thức thanh toán không hợp lệ!");

                    refundRequest.orderId = orderId.toString();
                    refundRequest.gatewayTxnId = payment.getTxnId();
                    refundRequest.amount = payment.getPaidAmount();

                    IPaymentService.RefundResponse rr;
                    try {
                        rr = paymentService.refund(refundRequest);
                    } catch (Exception ex) {
                        throw new IllegalStateException("Lỗi gọi cổng thanh toán khi hoàn tiền: " + ex.getMessage(), ex);
                    }

                    if (rr == null || !rr.success) {
                        // Không hoàn tiền được -> không cho trả
                        throw new IllegalStateException("Hoàn tiền thất bại, không thể trả đơn. Chi tiết: " + (rr != null ? rr.message : "unknown"));
                    }

                    // Nếu refund thành công → cập nhật Payment
                    payment.setStatus(PM_REFUNDED);
                    payment.setRefundTxnId(rr.refundTxnId);
                }

                // Đồng bộ paymentStatus của đơn
                order.setPaymentStatus(REFUNDED);

            } else {
                // COD: khi trả hàng thì ghi nhận hoàn tiền nội bộ
                if (payment.getStatus() != PM_REFUNDED) {
                    payment.setStatus(PM_REFUNDED);
                    payment.setPaidAt(java.time.LocalDateTime.now());
                    payment.setPaidAmount(order.getTotalAmount());
                }
                order.setPaymentStatus(REFUNDED);
            }

            // Cuối cùng, cập nhật trạng thái đơn
            order.setOrderStatus(RETURNED);

        } else if (toStatus == CANCELED) {
            // Hủy đơn
            if (isOnline && payment.getStatus() == PM_SUCCESS) {
                if (payment.getTxnId() == null || payment.getTxnId().isBlank()) {
                    throw new IllegalStateException("Thiếu mã giao dịch gốc (txnId) để hoàn tiền online.");
                }

                IPaymentService.RefundRequest refundRequest = new IPaymentService.RefundRequest();
                if (order.getPayment().getMethod() ==  METHOD_MOMO)
                    paymentService = new MomoServiceImpl();
                else if (order.getPayment().getMethod() ==  METHOD_VNPAY) {
                    paymentService = new VnpayServiceImpl();
                    Map<String, String> m = new HashMap<String, String>();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                    m.put("vnp_TransactionDate", payment.getPaidAt().format(formatter));
                    refundRequest.withMeta(m);
                }
                else
                    throw new IllegalStateException("Phương thức thanh toán không hợp lệ!");

                refundRequest.orderId = orderId.toString();
                refundRequest.gatewayTxnId = payment.getTxnId();
                refundRequest.amount = payment.getPaidAmount();

                IPaymentService.RefundResponse rr;
                try {
                    rr = paymentService.refund(refundRequest);
                } catch (Exception ex) {
                    throw new IllegalStateException("Lỗi gọi cổng thanh toán khi hoàn tiền: " + ex.getMessage(), ex);
                }

                if (rr == null || !rr.success) {
                    // Không hoàn tiền được -> không cho hủy
                    throw new IllegalStateException("Hoàn tiền thất bại, không thể hủy đơn. Chi tiết: " + (rr != null ? rr.message : "unknown"));
                }

                payment.setStatus(PM_REFUNDED);
                payment.setRefundTxnId(rr.refundTxnId);

                // Đồng bộ PaymentStatus của đơn
                order.setPaymentStatus(REFUNDED);
            } else {
                // Online chưa thu tiền, hoặc COD
                if (payment.getStatus() == PM_REFUNDED) {
                    order.setPaymentStatus(REFUNDED);
                } else if (payment.getStatus() == PM_SUCCESS) {
                    order.setPaymentStatus(PAID);
                } else {
                    order.setPaymentStatus(UNPAID);
                }
            }
        } else if (toStatus == CONFIRMED){
            EmailService.sendOrderConfirmation(order.getId(), order.getBranch().getId());
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

    @Override
    public OrderStatsDTO getOrderStats(LocalDateTime from, LocalDateTime to, Integer branchId) {
        // 1) Tổng đơn
        long total = dao.countOrdersByRange(from, to, branchId);

        // 2) Theo trạng thái
        long delivered = dao.countOrdersByStatus(from, to, branchId, DELIVERED);
        long canceled  = dao.countOrdersByStatus(from, to, branchId, CANCELED);
        long returned  = dao.countOrdersByStatus(from, to, branchId, RETURNED);

        double dRatio = total == 0 ? 0d : (double) delivered / total;
        double cRatio = total == 0 ? 0d : (double) canceled  / total;
        double rRatio = total == 0 ? 0d : (double) returned  / total;

        // 3) Phân bổ phương thức thanh toán
        long cod   = dao.countOrdersByPaymentMethod(from, to, branchId, METHOD_COD);
        long vnpay = dao.countOrdersByPaymentMethod(from, to, branchId, METHOD_VNPAY);
        long momo  = dao.countOrdersByPaymentMethod(from, to, branchId, METHOD_MOMO);

        long sumPay = cod + vnpay + momo;
        double shareCOD   = sumPay == 0 ? 0d : (double) cod   / sumPay;
        double shareVNPAY = sumPay == 0 ? 0d : (double) vnpay / sumPay;
        double shareMOMO  = sumPay == 0 ? 0d : (double) momo  / sumPay;

        List<PaymentBreakdownDTO> breakdown = new ArrayList<>(3);
        breakdown.add(new PaymentBreakdownDTO("COD",   cod,   shareCOD));
        breakdown.add(new PaymentBreakdownDTO("VNPAY", vnpay, shareVNPAY));
        breakdown.add(new PaymentBreakdownDTO("MOMO",  momo,  shareMOMO));

        return new OrderStatsDTO(
                total,
                delivered, dRatio,
                canceled,  cRatio,
                returned,  rRatio,
                breakdown
        );
    }
}
