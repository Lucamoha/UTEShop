<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglib.jsp"%>

<c:set var="o" value="${order}" />

<div class="page-inner">
    <div class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-3">
        <div>
            <h3 class="fw-bold mb-1">Chi tiết đơn #${o.id}</h3>
            <div class="text-muted small">
                Tạo lúc:
                ${o.createdAt}
            </div>
        </div>
    </div>

    <!-- Alerts -->
    <c:if test="${not empty sessionScope.flash_ok}">
        <div class="alert alert-success">Cập nhật thành công.</div>
        <c:remove var="flash_ok" scope="session"/>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Thông tin người nhận + tổng tiền -->
    <div class="row">
        <div class="col-lg-8 mb-3">
            <div class="card card-round">
                <div class="card-header"><h5 class="card-title">Thông tin người nhận</h5></div>
                <div class="card-body">
                    <div class="row g-2">
                        <div class="col-md-6"><strong>Họ tên:</strong> ${o.receiverName}</div>
                        <div class="col-md-6"><strong>SĐT:</strong> ${o.phone}</div>
                        <div class="col-md-12"><strong>Địa chỉ:</strong>
                            ${o.addressLine}
                            <c:if test="${not empty o.ward}">, ${o.ward}</c:if>
                            <c:if test="${not empty o.district}">, ${o.district}</c:if>
                            <c:if test="${not empty o.city}">, ${o.city}</c:if>
                        </div>
                        <c:if test="${not empty o.note}">
                            <div class="col-md-12"><strong>Ghi chú:</strong> ${o.note}</div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-4 mb-3">
            <div class="card card-round h-100">
                <div class="card-header"><h5 class="card-title">Tổng tiền</h5></div>
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <span>Tạm tính</span><strong><fmt:formatNumber value="${o.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></strong>
                    </div>
                    <div class="d-flex justify-content-between">
                        <span>Giảm giá</span><strong>- <fmt:formatNumber value="${o.discountAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></strong>
                    </div>
                    <div class="d-flex justify-content-between">
                        <span>Phí vận chuyển</span><strong><fmt:formatNumber value="${o.shippingFee}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></strong>
                    </div>
                    <hr/>
                    <div class="d-flex justify-content-between">
                        <span class="fw-bold">Tổng thanh toán</span><strong class="text-primary">
                        <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></strong>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Items -->
    <div class="card card-round mb-3">
        <div class="card-header"><h5 class="card-title">Sản phẩm</h5></div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table align-items-center mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th>#</th>
                        <th>Sản phẩm</th>
                        <th>Biến thể</th>
                        <th class="text-end">Giá</th>
                        <th class="text-end">SL</th>
                        <th class="text-end">Thành tiền</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="it" items="${o.items}" varStatus="st">
                        <tr>
                            <td>${st.index + 1}</td>
                            <td>${it.product.name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${it.variant != null}">${it.variant.SKU}</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-end"><fmt:formatNumber value="${it.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                            <td class="text-end">${it.quantity}</td>
                            <td class="text-end">
                                <fmt:formatNumber value="${it.price * it.quantity}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Tác vụ cập nhật -->
    <div class="row">
        <div class="card card-round">
            <div class="card-header"><h5 class="mb-0">Cập nhật trạng thái & thanh toán</h5></div>
            <div class="card-body">
                <form method="post" action="${pageContext.request.contextPath}/manager/orders/detail">
                    <input type="hidden" name="id" value="${o.id}"/>
                    <input type="hidden" name="action" value="updateBoth"/>

                    <div class="row g-3">
                        <!-- Cột trái: Trạng thái đơn -->
                        <div class="col-12 col-lg-6">
                            <label class="form-label fw-semibold">Trạng thái hiện tại:</label>
                            <div class="mb-2">
            <span class="badge
              <c:choose>
                <c:when test='${o.orderStatus==0}'>bg-secondary</c:when>
                <c:when test='${o.orderStatus==1}'>bg-info</c:when>
                <c:when test='${o.orderStatus==2}'>bg-primary</c:when>
                <c:when test='${o.orderStatus==3}'>bg-success</c:when>
                <c:when test='${o.orderStatus==4}'>bg-danger</c:when>
                <c:when test='${o.orderStatus==5}'>bg-dark</c:when>
                <c:otherwise>bg-danger</c:otherwise>
              </c:choose>">
              ${STATUS_MAP.get(o.orderStatus)}
            </span>
          </div>

          <label for="status" class="form-label">Chuyển sang</label>
                                <select id="status" name="status" class="form-select">
                                    <c:forEach var="e" items="${STATUS_MAP.entrySet()}">
                                        <option value="${e.key}" ${o.orderStatus==e.key?'selected':''}>${e.value}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Cột phải: Thanh toán -->
                            <div class="col-12 col-lg-6">
                                <label class="form-label fw-semibold">TT hiện tại:</label>
                                <div class="mb-2">
            <span class="badge ${o.paymentStatus==1?'bg-success':(o.paymentStatus==0?'bg-warning':'bg-danger')}">
                ${PAY_MAP.get(o.paymentStatus)}
            </span>
                                </div>

                                <label for="payment" class="form-label">Chuyển sang</label>
                                <select id="payment" name="payment" class="form-select">
                                    <c:forEach var="e" items="${PAY_MAP.entrySet()}">
                                        <option value="${e.key}" ${o.paymentStatus==e.key?'selected':''}>${e.value}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="d-flex justify-content-end mt-3">
                            <button class="btn btn-primary px-4" type="submit">Cập nhật</button>
                        </div>
                </form>
            </div>
        </div>

    </div>
</div>