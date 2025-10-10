<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglib.jsp"%>

<%
    java.util.Map<Integer,String> STATUS_MAP = new java.util.LinkedHashMap<>();
    STATUS_MAP.put(0,"Mới tạo");
    STATUS_MAP.put(1,"Đã xác nhận");
    STATUS_MAP.put(2,"Đang giao hàng");
    STATUS_MAP.put(3,"Đã nhận");
    STATUS_MAP.put(4,"Hủy");
    STATUS_MAP.put(5,"Trả hàng");

    java.util.Map<Integer,String> PAY_MAP = new java.util.LinkedHashMap<>();
    PAY_MAP.put(0,"Chưa thanh toán");
    PAY_MAP.put(1,"Đã thanh toán");
    PAY_MAP.put(2,"Hoàn tiền");
%>

<div class="page-inner">
    <div class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-3">
        <div><h3 class="fw-bold mb-0">Đơn hàng</h3></div>
    </div>

    <!-- Bộ lọc / tìm kiếm -->
    <form class="card card-round mb-3" method="get" action="${pageContext.request.contextPath}/manager/orders">
        <div class="card-body">
            <div class="row g-2 align-items-end">
                <div class="col-12 col-md-4">
                    <label class="form-label" for="q">Tìm kiếm (id, tên, sđt, địa chỉ)</label>
                    <input type="text" class="form-control" id="q" name="q" value="${fn:escapeXml(q)}" placeholder="VD: 1023, Nguyễn Văn A, 090..." />
                </div>
                <div class="col-6 col-md-3">
                    <label class="form-label" for="status">Trạng thái đơn</label>
                    <select class="form-select" id="status" name="status">
                        <option value="">Tất cả</option>
                        <c:forEach var="e" items="<%=STATUS_MAP.entrySet()%>">
                            <option value="${e.key}" <c:if test="${status==e.key}">selected</c:if>>${e.value}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-6 col-md-3">
                    <label class="form-label" for="pay">Trạng thái thanh toán</label>
                    <select class="form-select" id="pay" name="pay">
                        <option value="">Tất cả</option>
                        <c:forEach var="e" items="<%=PAY_MAP.entrySet()%>">
                            <option value="${e.key}" <c:if test="${pay==e.key}">selected</c:if>>${e.value}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-6 col-md-1">
                    <label class="form-label" for="size">Hiển thị</label>
                    <select class="form-select" id="size" name="size">
                        <c:forEach var="s" items="${[10,20,50]}">
                            <option value="${s}" <c:if test="${size==s}">selected</c:if>>${s}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-6 col-md-1 d-grid">
                    <button class="btn btn-dark">Lọc</button>
                </div>
            </div>
        </div>
    </form>

    <!-- Bảng -->
    <div class="card card-round">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table align-items-center mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th>Id đơn hàng</th>
                        <th>Tên người đặt</th>
                        <th>SĐT</th>
                        <th>Địa chỉ</th>
                        <th>Trạng thái đơn</th>
                        <th>TT thanh toán</th>
                        <th>Ngày đặt</th>
                        <th class="text-end">Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="result" value="${requestScope.result}"/>
                    <c:choose>
                        <c:when test="${empty result || empty result.content}">
                            <tr><td colspan="8" class="text-center text-muted py-4">Không có đơn hàng</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="o" items="${result.content}">
                                <tr>
                                    <td>#${o.id}</td>
                                    <td>${o.receiverName}</td>
                                    <td>${o.phone}</td>
                                    <td class="text-truncate" style="max-width:320px;">
                                            ${o.addressLine}<c:if test="${not empty o.ward}">, ${o.ward}</c:if>
                                        <c:if test="${not empty o.district}">, ${o.district}</c:if>
                                        <c:if test="${not empty o.city}">, ${o.city}</c:if>
                                    </td>
                                    <td>
                    <span class="badge
                      <c:choose>
                        <c:when test='${o.orderStatus==0}'>bg-secondary</c:when>
                        <c:when test='${o.orderStatus==1}'>bg-info</c:when>
                        <c:when test='${o.orderStatus==2}'>badge-primary</c:when>
                        <c:when test='${o.orderStatus==3}'>bg-success</c:when>
                        <c:when test='${o.orderStatus==4}'>bg-danger</c:when>
                        <c:when test='${o.orderStatus==5}'>badge-black</c:when>
                        <c:otherwise>bg-danger</c:otherwise>
                      </c:choose>">
                      <%=STATUS_MAP.get(((com.uteshop.entity.order.Orders)pageContext.getAttribute("o")).getOrderStatus())%>
                    </span>
                                    </td>
                                    <td>
                    <span class="badge
                      <c:choose>
                        <c:when test='${o.paymentStatus==0}'>badge-warning</c:when>
                        <c:when test='${o.paymentStatus==1}'>bg-success</c:when>
                        <c:when test='${o.paymentStatus==2}'>bg-danger</c:when>
                        <c:otherwise>bg-danger</c:otherwise>
                      </c:choose>">
                      <%=PAY_MAP.get(((com.uteshop.entity.order.Orders)pageContext.getAttribute("o")).getPaymentStatus())%>
                    </span>
                                    </td>
                                    <td>${o.createdAt}</td>
                                    <td class="text-end">
                                        <a class="btn btn-sm btn-outline-primary"
                                           href="${pageContext.request.contextPath}/manager/orders/detail?id=${o.id}">
                                            Xem chi tiết
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Phân trang -->
        <c:if test="${not empty result && result.total > result.size}">
            <div class="card-footer">
                <%
                    com.uteshop.dao.manager.common.PageResult<?> pr =
                            (com.uteshop.dao.manager.common.PageResult<?>) request.getAttribute("result");
                    int pageNow = (int) request.getAttribute("page");
                    int sizeNow = (int) request.getAttribute("size");
                    long totalPages = (pr.total + sizeNow - 1) / sizeNow;
                %>
                <nav>
                    <ul class="pagination mb-0">
                        <li class="page-item <%= pageNow<=1?"disabled":"" %>">
                            <a class="page-link"
                               href="?q=${fn:escapeXml(q)}&status=${status}&pay=${pay}&size=${size}&page=<%=pageNow-1%>">«</a>
                        </li>
                        <%
                            int tp = (int) totalPages;
                            int start = Math.max(1, pageNow-2);
                            int end = Math.min(tp, pageNow+2);
                            for (int i=start; i<=end; i++) {
                        %>
                        <li class="page-item <%= i==pageNow?"active":"" %>">
                            <a class="page-link"
                               href="?q=${fn:escapeXml(q)}&status=${status}&pay=${pay}&size=${size}&page=<%=i%>"><%=i%></a>
                        </li>
                        <% } %>
                        <li class="page-item <%= pageNow>=totalPages?"disabled":"" %>">
                            <a class="page-link"
                               href="?q=${fn:escapeXml(q)}&status=${status}&pay=${pay}&size=${size}&page=<%=pageNow+1%>">»</a>
                        </li>
                    </ul>
                    <small class="text-muted ms-2">
                        Trang <%=pageNow%>/<%=totalPages%> • Tổng <%=pr.total%> đơn
                    </small>
                </nav>
            </div>
        </c:if>
    </div>
</div>