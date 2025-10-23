<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section class="row">
    <div class="col mt-4">
        <div class="card">
            <div class="card-header">DANH SÁCH ĐỊA CHỈ</div>
            <div class="card-body">

                <!-- Hiển thị thông báo -->
                <c:if test="${not empty param.success}">
                    <div class="alert alert-success" role="alert">
                        Thao tác thành công!
                    </div>
                </c:if>

                <div class="table-responsive">
                    <a href="${pageContext.request.contextPath}/admin/Address/Addresses/add"
                       class="btn btn-dark mb-2">Thêm Mới</a>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Người Dùng (ID)</th>
                            <th>Nhà</th>
                            <th>Họ Tên</th>
                            <th>Điện Thoại</th>
                            <th>Địa Chỉ Chi Tiết</th>
                            <th>Phường/Xã</th>
                            <th>Quận/Huyện</th>
                            <th>Thành Phố</th>
                            <th>Mặc Định</th>
                            <th>Hành Động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="address" items="${listAddress}">
                            <tr>
                                <td>${address.id}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty address.user && not empty address.user.id}">
                                            ${address.user.id}
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${address.label}</td>
                                <td>${address.fullName}</td>
                                <td>${address.phone}</td>
                                <td>${address.addressLine}</td>
                                <td>${address.ward}</td>
                                <td>${address.district}</td>
                                <td>${address.city}</td>
                                <td>${address.isDefault ? 'Có' : 'Không'}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/Address/Addresses/view?id=${address.id}"
                                       class="btn btn-outline-info btn-sm"><i class="fa fa-info"></i> Xem</a>
                                    <a href="${pageContext.request.contextPath}/admin/Address/Addresses/edit?id=${address.id}"
                                       class="btn btn-outline-warning btn-sm"><i class="fa fa-edit"></i> Sửa</a>
                                    <a href="${pageContext.request.contextPath}/admin/Address/Addresses/delete?id=${address.id}"
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