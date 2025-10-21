<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
    <div class="col mt-4">
        <div class="card">
            <div class="card-header bg-dark text-white">Danh Sách Quản Lý</div>
            <div class="card-body">

                <!-- Hiển thị thông báo -->
                <c:if test="${message != null}">
                    <div class="alert alert-primary" role="alert">
                        <i>${message}</i>
                    </div>
                </c:if>

                <div class="table-responsive">
                    <a href="${pageContext.request.contextPath}/admin/User/Employees/add"
                       class="btn btn-dark mb-2">Thêm Quản Lý</a>
                    <a href="${pageContext.request.contextPath}/admin/User/Users/list"
                       class="btn btn-secondary mb-2 ml-3">Người Dùng</a>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>Họ Tên</th>
                            <th>Email</th>
                            <th>Số Điện Thoại</th>
                            <th>Vai Trò</th>
                            <th>Hành Động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${listUser}" var="item">
                            <tr>
                                <td>${item.fullName}</td>
                                <td>${item.email}</td>
                                <td>${item.phone}</td>
                                <td>${item.userRole == 'MANAGER' ? 'Quản Lý' : 'Không xác định'}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/User/Employees/view?id=${item.id}"
                                       class="btn btn-outline-info"><i class="fa fa-info"></i> Xem</a>
                                    <a href="${pageContext.request.contextPath}/admin/User/Employees/edit?id=${item.id}"
                                       class="btn btn-outline-warning"><i class="fa fa-edit"></i> Sửa</a>
                                    <a href="${pageContext.request.contextPath}/admin/User/Employees/delete?id=${item.id}"
                                       class="btn btn-outline-danger" onclick="return confirm('Xóa?')"><i class="fa fa-trash"></i> Xóa</a>
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