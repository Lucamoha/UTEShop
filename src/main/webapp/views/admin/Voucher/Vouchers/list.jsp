<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="row">
    <div class="col mt-4">
        <div class="card">
            <div class="card-header">DANH SÁCH VOUCHER</div>
            <div class="card-body">

                <!-- Hiển thị thông báo -->
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success" role="alert">
                        Thao tác thành công!
                    </div>
                </c:if>

                <div class="table-responsive">
                    <a href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/add"
                       class="btn btn-dark mb-2">Thêm Mới</a>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>Mã Số</th>
                            <th>Mã</th>
                            <th>Mô Tả</th>
                            <th>Giá Trị</th>
                            <th>Số Lần Sử Dụng Tối Đa</th>
                            <th>Số Lần Đã Sử Dụng</th>
                            <th>Ngày Bắt Đầu</th>
                            <th>Ngày Kết Thúc</th>
                            <th>Trạng Thái</th>
                            <th>Hành Động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="voucher" items="${listVouchers}">
                            <tr>
                                <td>${voucher.Id}</td>
                                <td>${voucher.Code}</td>
                                <td>${voucher.DescText}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${voucher.Type == 1}">
                                            <fmt:formatNumber value="${voucher.Value}" maxFractionDigits="2"/>%
                                        </c:when>
                                        <c:when test="${voucher.Type == 2}">
                                            <fmt:formatNumber value="${voucher.Value}" type="currency" currencySymbol="₫"/>
                                        </c:when>
                                        <c:otherwise>
                                            ${voucher.Value} (Loại không xác định)
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${voucher.MaxUses}</td>
                                <td>${voucher.TotalUsed}</td>
                                <td><fmt:formatDate value="${voucher.StartsAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td><fmt:formatDate value="${voucher.EndsAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td>${voucher.IsActive ? 'Hoạt Động' : 'Không Hoạt Động'}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/view?id=${voucher.Id}"
                                       class="btn btn-outline-info btn-sm"><i class="fa fa-info"></i> Xem</a>
                                    <a href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/edit?id=${voucher.Id}"
                                       class="btn btn-outline-warning btn-sm"><i class="fa fa-edit"></i> Sửa</a>
                                    <a href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/delete?id=${voucher.Id}"
                                       class="btn btn-outline-danger btn-sm" onclick="return confirm('Xóa?')"><i class="fa fa-trash"></i> Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>
</section>