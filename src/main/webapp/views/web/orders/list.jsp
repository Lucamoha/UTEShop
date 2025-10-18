<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/templates/css/orders.css">

<!-- Orders List -->
<div class="bg0 p-b-85" style="padding-top: 130px;">
<div class="container">
    <!-- Page Header -->
    <div class="m-b-30">
        <h4 class="mtext-109 cl2 p-b-10">
            <i class="zmdi zmdi-shopping-cart"></i> Đơn hàng của tôi
        </h4>
    </div>

    <!-- Filter Section -->
    <div class="orders-filter">
        <div class="filter-tabs">
            <label><i class="fa fa-filter"></i> Lọc theo trạng thái:</label>
            <a href="${pageContext.request.contextPath}/orders" 
               class="filter-tab ${empty selectedStatus ? 'active' : ''}">
                Tất cả
            </a>
            <a href="${pageContext.request.contextPath}/orders?status=${ORDER_STATUS_NEW}" 
               class="filter-tab ${selectedStatus == ORDER_STATUS_NEW ? 'active' : ''}">
                Mới tạo
            </a>
            <a href="${pageContext.request.contextPath}/orders?status=${ORDER_STATUS_CONFIRMED}" 
               class="filter-tab ${selectedStatus == ORDER_STATUS_CONFIRMED ? 'active' : ''}">
                Đã xác nhận
            </a>
            <a href="${pageContext.request.contextPath}/orders?status=${ORDER_STATUS_SHIPPING}" 
               class="filter-tab ${selectedStatus == ORDER_STATUS_SHIPPING ? 'active' : ''}">
                Đang giao
            </a>
            <a href="${pageContext.request.contextPath}/orders?status=${ORDER_STATUS_DELIVERED}" 
               class="filter-tab ${selectedStatus == ORDER_STATUS_DELIVERED ? 'active' : ''}">
                Đã giao
            </a>
            <a href="${pageContext.request.contextPath}/orders?status=${ORDER_STATUS_CANCELED}" 
               class="filter-tab ${selectedStatus == ORDER_STATUS_CANCELED ? 'active' : ''}">
                Đã hủy
            </a>
        </div>
    </div>

    <!-- Orders List -->
    <c:choose>
        <c:when test="${empty orders}">
            <div class="empty-orders">
                <i class="fa fa-shopping-bag"></i>
                <h3>Chưa có đơn hàng nào</h3>
                <p>Hãy mua sắm ngay để tạo đơn hàng đầu tiên!</p>
                <a href="${pageContext.request.contextPath}/" class="btn-order btn-view-detail">
                    <i class="fa fa-shopping-cart"></i> Tiếp tục mua sắm
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="orders-list">
                <c:forEach var="order" items="${orders}">
                    <div class="order-card">
                        <div class="order-card-header">
                            <div>
                                <span class="order-id"><i class="fa fa-file-text"></i> Mã đơn hàng: #${order.id}</span>
                                <span class="order-date">
                                    <i class="fa fa-calendar"></i>
                                    <c:set var="createdAt" value="${order.createdAt.toString().replace('T', ' ').substring(0, 16)}" />
                                    ${createdAt}
                                </span>
                            </div>
                            <div>
                                <c:choose>
                                    <c:when test="${order.orderStatus == ORDER_STATUS_NEW}">
                                        <span class="order-status-badge status-new">Mới tạo</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == ORDER_STATUS_CONFIRMED}">
                                        <span class="order-status-badge status-confirmed">Đã xác nhận</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == ORDER_STATUS_SHIPPING}">
                                        <span class="order-status-badge status-shipping">Đang giao hàng</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == ORDER_STATUS_DELIVERED}">
                                        <span class="order-status-badge status-delivered">Đã giao hàng</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == ORDER_STATUS_CANCELED}">
                                        <span class="order-status-badge status-canceled">Đã hủy</span>
                                    </c:when>
                                    <c:when test="${order.orderStatus == ORDER_STATUS_RETURNED}">
                                        <span class="order-status-badge status-returned">Đã trả hàng</span>
                                    </c:when>
                                </c:choose>
                            </div>
                        </div>
                        
                        <div class="order-card-body">
                            <div class="order-items">
                                <div style="margin-bottom: 15px;">
                                    <strong><i class="fa fa-user"></i> Người nhận:</strong> ${order.receiverName}
                                </div>
                                <div style="margin-bottom: 15px;">
                                    <strong><i class="fa fa-map-marker"></i> Địa chỉ:</strong> ${order.addressLine}, ${order.ward}, ${order.district}, ${order.city}
                                </div>
                                <div style="margin-bottom: 15px;">
                                    <strong><i class="fa fa-phone"></i> Số điện thoại:</strong> ${order.phone}
                                </div>
                                <div>
                                    <strong><i class="fa fa-cube"></i> Số lượng sản phẩm:</strong> 
                                    <c:set var="totalItems" value="0" />
                                    <c:forEach var="item" items="${order.items}">
                                        <c:set var="totalItems" value="${totalItems + item.quantity}" />
                                    </c:forEach>
                                    ${totalItems} sản phẩm
                                </div>
                            </div>
                        </div>
                        
                        <div class="order-card-footer">
                            <div class="order-total">
                                Tổng tiền: 
                                <span class="order-total-amount">
                                    <fmt:formatNumber value="${order.totalAmount}" type="number" groupingUsed="true"/>  VND
                                </span>
                            </div>
                            <div class="order-actions">
                                <a href="${pageContext.request.contextPath}/orders/detail?id=${order.id}" 
                                   class="btn-order btn-view-detail">
                                    <i class="fa fa-eye"></i> Xem chi tiết
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Pagination -->
    <c:if test="${totalPages > 1}">
        <div class="pagination-container">
            <ul class="pagination">
                <!-- Previous button -->
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="?page=${currentPage - 1}<c:if test='${not empty selectedStatus}'>&status=${selectedStatus}</c:if>">
                        <i class="fa fa-chevron-left"></i>
                    </a>
                </li>

                <!-- Page numbers -->
                <c:forEach begin="1" end="${totalPages}" var="pageNum">
                    <li class="page-item ${pageNum == currentPage ? 'active' : ''}">
                        <a class="page-link" href="?page=${pageNum}<c:if test='${not empty selectedStatus}'>&status=${selectedStatus}</c:if>">
                            ${pageNum}
                        </a>
                    </li>
                </c:forEach>

                <!-- Next button -->
                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="?page=${currentPage + 1}<c:if test='${not empty selectedStatus}'>&status=${selectedStatus}</c:if>">
                        <i class="fa fa-chevron-right"></i>
                    </a>
                </li>
            </ul>
        </div>
    </c:if>
</div>
</div>
