<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/templates/css/orders.css">

<div class="bg0 p-b-85" style="padding-top: 130px;">
<div class="container">
    <div class="m-b-20 d-flex justify-content-between align-items-center">
        <a href="${pageContext.request.contextPath}/orders" class="btn-back">
            <i class="zmdi zmdi-arrow-left"></i>  Quay lại danh sách
        </a>
        
        <div>
            <!-- Cancel button - cho NEW và CONFIRMED -->
            <c:if test="${order.orderStatus == ORDER_STATUS_NEW || order.orderStatus == ORDER_STATUS_CONFIRMED}">
                <button type="button" class="btn btn-danger" onclick="showCancelConfirmation()">
                    <i class="zmdi zmdi-close"></i> Hủy đơn hàng
                </button>
            </c:if>
            
            <!-- Return button - cho DELIVERED -->
            <c:if test="${order.orderStatus == ORDER_STATUS_DELIVERED}">
                <button type="button" class="btn btn-warning" onclick="showReturnConfirmation()">
                    <i class="zmdi zmdi-undo"></i> Trả hàng
                </button>
            </c:if>
        </div>
    </div>

    <!-- Order Header -->
    <div class="section-card">
        <div class="section-header">
            <i class="zmdi zmdi-receipt"></i> Mã đơn hàng: #${order.id}
        </div>
        <div class="section-body">
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <strong>Ngày đặt hàng:</strong><br>
                        <c:set var="createdAt" value="${order.createdAt.toString().replace('T', ' ')}" />
                        ${createdAt}
                    </div>
                    <div class="mb-3">
                        <strong>Trạng thái đơn hàng:</strong><br>
                        <c:choose>
                            <c:when test="${order.orderStatus == ORDER_STATUS_NEW}">
                                <span class="order-status status-new">
                                    <i class="zmdi zmdi-time"></i> Mới tạo
                                </span>
                            </c:when>
                            <c:when test="${order.orderStatus == ORDER_STATUS_CONFIRMED}">
                                <span class="order-status status-confirmed">
                                    <i class="zmdi zmdi-check"></i> Đã xác nhận
                                </span>
                            </c:when>
                            <c:when test="${order.orderStatus == ORDER_STATUS_SHIPPING}">
                                <span class="order-status status-shipping">
                                    <i class="zmdi zmdi-truck"></i> Đang giao hàng
                                </span>
                            </c:when>
                            <c:when test="${order.orderStatus == ORDER_STATUS_DELIVERED}">
                                <span class="order-status status-delivered">
                                    <i class="zmdi zmdi-check-circle"></i> Đã nhận
                                </span>
                            </c:when>
                            <c:when test="${order.orderStatus == ORDER_STATUS_CANCELED}">
                                <span class="order-status status-canceled">
                                    <i class="zmdi zmdi-close"></i> Đã hủy
                                </span>
                            </c:when>
                            <c:when test="${order.orderStatus == ORDER_STATUS_RETURNED}">
                                <span class="order-status status-returned">
                                    <i class="zmdi zmdi-undo"></i> Đã trả hàng
                                </span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="mb-3">
                        <strong>Trạng thái thanh toán:</strong><br>
                        <c:choose>
                            <c:when test="${order.paymentStatus == 0}">
                                <span class="payment-status payment-unpaid">
                                    <i class="zmdi zmdi-time"></i> Chưa thanh toán
                                </span>
                                <!-- Payment button cho VNPAY và MOMO -->
                                <c:if test="${(order.orderStatus == 0 || order.orderStatus == 1) && (order.payment.method == 1 || order.payment.method == 2) && order.payment.status == 0}">
                                    <div style="margin-top: 10px;">
                                        <button type="button" class="btn btn-primary btn-sm" data-order-id="${order.id}" onclick="redirectToPayment(this.getAttribute('data-order-id'))">
                                            <i class="zmdi zmdi-card"></i> Thanh toán ngay
                                        </button>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:when test="${order.paymentStatus == 1}">
                                <span class="payment-status payment-paid">
                                    <i class="zmdi zmdi-check"></i> Đã thanh toán
                                </span>
                            </c:when>
                            <c:when test="${order.paymentStatus == 2}">
                                <span class="payment-status payment-refunded">
                                    <i class="zmdi zmdi-undo"></i> Đã hoàn tiền
                                </span>
                            </c:when>
                        </c:choose>
                    </div>
                    <c:if test="${not empty order.note}">
                        <div class="mb-3">
                            <strong>Ghi chú:</strong><br>
                            ${order.note}
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- Delivery Information -->
    <div class="section-card">
        <div class="section-header">
            <i class="zmdi zmdi-pin"></i> 
            Thông tin giao hàng
        </div>
        <div class="section-body">
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-2"><strong>Người nhận:</strong> ${order.receiverName}</div>
                    <div class="mb-2"><strong>Số điện thoại:</strong> ${order.phone}</div>
                </div>
                <div class="col-md-6">
                    <div class="mb-2"><strong>Địa chỉ:</strong></div>
                    <div>
                        ${order.addressLine}<br>
                        <c:if test="${not empty order.ward}">${order.ward}, </c:if>
                        <c:if test="${not empty order.district}">${order.district}, </c:if>
                        <c:if test="${not empty order.city}">${order.city}</c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Order Items -->
    <div class="section-card">
        <div class="section-header">
            <i class="zmdi zmdi-shopping-basket"></i> 
            Sản phẩm đã đặt
        </div>
        <div class="section-body">
            <c:forEach var="item" items="${order.items}">
                <div class="product-item">
                    <div class="row align-items-center">
                        <div class="col-md-6">
                            <div class="d-flex align-items-center">
                                <c:url value="/image?fname=${item.product.images[0].imageUrl}" var="imgUrl"></c:url>
                                <img src="${imgUrl}" 
                                     alt="${item.product.name}" class="product-image me-3">
                                <div style="margin-left: 15px;">
                                    <h6 class="mb-1">${item.product.name}</h6>
                                    <c:if test="${item.variant != null}">
                                        <small class="text-muted" style="display: block; margin-top: 8px;">
                                               SKU:  ${item.variant.SKU}
                                        </small>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2 text-center">
                            <div class="product-price">
                                <fmt:formatNumber value="${item.price}" type="number" groupingUsed="true"/> VND
                            </div>
                        </div>
                        <div class="col-md-2 text-center">
                            <strong>x${item.quantity}</strong>
                        </div>
                        <div class="col-md-2 text-end">
                            <div class="product-price">
                                <fmt:formatNumber value="${item.price * item.quantity}" type="number" groupingUsed="true"/> VND
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Order Summary -->
    <div class="section-card">
        <div class="section-header">
            <i class="zmdi zmdi-money"></i> 
            Tổng kết đơn hàng
        </div>
        <div class="section-body">
            <div class="row justify-content-end">
                <div class="col-md-6">
                    <div class="summary-row">
                        <span>Tạm tính:</span>
                        <span>
                            <fmt:formatNumber value="${order.subtotal}" type="number" groupingUsed="true"/> VND
                        </span>
                    </div>
                    <c:if test="${order.discountAmount > 0}">
                        <div class="summary-row">
                            <span>Giảm giá:</span>
                            <span class="text-success">
                                -<fmt:formatNumber value="${order.discountAmount}" type="number" groupingUsed="true"/> VND
                            </span>
                        </div>
                    </c:if>
                    <div class="summary-row">
                        <span><strong>Tổng cộng:</strong></span>
                        <span class="text-danger">
                            <strong>
                                <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true"/> VND
                            </strong>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Payment Information (if exists) -->
    <c:if test="${order.payment != null}">
        <div class="section-card">
            <div class="section-header">
                <i class="zmdi zmdi-card"></i> 
                Thông tin thanh toán
            </div>
            <div class="section-body">
                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-2">
                            <strong>Phương thức:</strong>
                            <c:choose>
                                <c:when test="${order.payment.method == 0}">
                                    <i class="zmdi zmdi-money"></i> Thanh toán khi nhận hàng (COD)
                                </c:when>
                                <c:when test="${order.payment.method == 1}">
                                    <i class="zmdi zmdi-card"></i> VNPay
                                </c:when>
                                <c:when test="${order.payment.method == 2}">
                                    <i class="zmdi zmdi-smartphone"></i> MoMo
                                </c:when>
                            </c:choose>
                        </div>
                        <c:if test="${not empty order.payment.txnId}">
                            <div class="mb-2">
                                <strong>Mã giao dịch:</strong> ${order.payment.txnId}
                            </div>
                        </c:if>
                    </div>
                    <div class="col-md-6">
                        <c:if test="${order.payment.paidAt != null}">
                            <div class="mb-2">
                                <strong>Thời gian thanh toán:</strong><br>
                                <c:set var="paidAt" value="${order.payment.paidAt.toString().replace('T', ' ')}" />
                                ${paidAt}
                            </div>
                        </c:if>
                        <c:if test="${order.payment.paidAmount != null}">
                            <div class="mb-2">
                                <strong>Số tiền đã thanh toán:</strong>
                                <fmt:formatNumber value="${order.payment.paidAmount}" type="number" groupingUsed="true"/> VND
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="col-md-6">
                    <c:if test="${order.payment.paidAt != null}">
                        <div class="mb-2">
                            <strong>Thời gian thanh toán:</strong><br>
                            <c:set var="paidAt2" value="${order.payment.paidAt.toString().replace('T', ' ')}" />
                            ${paidAt2}
                        </div>
                    </c:if>
                    <c:if test="${order.payment.paidAmount != null}">
                        <div class="mb-2">
                            <strong>Số tiền đã thanh toán:</strong>
                            <fmt:formatNumber value="${order.payment.paidAmount}" type="number" groupingUsed="true"/> VND
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </c:if>
</div>
</div>

<!-- Modal hủy đơn hàng -->
<div class="modal fade" id="cancelOrderModal" tabindex="-1" role="dialog" aria-labelledby="cancelOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="cancelOrderModalLabel">Xác nhận hủy đơn hàng</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>Bạn có chắc chắn muốn hủy đơn hàng #${order.id}?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                <button type="button" class="btn btn-danger" onclick="cancelOrder()">Xác nhận hủy</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal Trả hàng -->
<div class="modal fade" id="returnOrderModal" tabindex="-1" role="dialog" aria-labelledby="returnOrderModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="returnOrderModalLabel">Xác nhận trả hàng</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>Bạn có chắc chắn muốn trả lại đơn hàng #${order.id}?</p>
                <div class="alert alert-warning">
                    <i class="zmdi zmdi-alert-triangle"></i> 
                    <strong>Điều kiện trả hàng:</strong>
                    <ul style="margin-top: 10px; margin-bottom: 0;">
                        <li>Sản phẩm còn nguyên vẹn, chưa qua sử dụng</li>
                        <li>Đầy đủ bao bì, phụ kiện kèm theo</li>
                        <li>Trả hàng trong vòng 7 ngày kể từ ngày nhận hàng</li>
                    </ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                <button type="button" class="btn btn-warning" onclick="returnOrder()">Xác nhận trả hàng</button>
            </div>
        </div>
    </div>
</div>

<script>
function showCancelConfirmation() {
    $('#cancelOrderModal').modal('show');
}

function showReturnConfirmation() {
    $('#returnOrderModal').modal('show');
}

function redirectToPayment(orderId) {
    // Redirect to payment gateway
    window.location.href = '${pageContext.request.contextPath}/payment/create?orderId=' + orderId;
}

function cancelOrder() {
    const orderId = parseInt('${order.id}');
    const button = event.target;
    
    // Disable button to prevent double click
    button.disabled = true;
    button.innerHTML = '<i class="zmdi zmdi-spinner zmdi-hc-spin"></i> Đang xử lý...';
    
    fetch('${pageContext.request.contextPath}/orders/cancel', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'id=' + orderId
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            alert('Hủy đơn hàng thành công!');
            // Reload page to show updated status
            window.location.reload();
        } else {
            alert('Lỗi: ' + data.message);
            button.disabled = false;
            button.innerHTML = 'Xác nhận hủy';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi hủy đơn hàng. Vui lòng thử lại.');
        button.disabled = false;
    });
}

function returnOrder() {
    const orderId = parseInt('${order.id}');
    const button = event.target;
    
    // Disable button to prevent double click
    button.disabled = true;
    button.innerHTML = '<i class="zmdi zmdi-spinner zmdi-hc-spin"></i> Đang xử lý...';
    
    fetch('${pageContext.request.contextPath}/orders/return', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'id=' + orderId
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            alert('Yêu cầu trả hàng đã được gửi thành công!');
            // Reload page to show updated status
            window.location.reload();
        } else {
            alert('Lỗi: ' + data.message);
            button.disabled = false;
            button.innerHTML = 'Xác nhận trả hàng';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi gửi yêu cầu trả hàng. Vui lòng thử lại.');
        button.disabled = false;
    });
}
</script>