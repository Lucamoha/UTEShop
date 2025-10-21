<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
    <div class="col-md-6 offset-md-3 mt-4">
        <div class="card shadow-lg">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Sửa Quản Lý</h4>
            </div>
            <div class="card-body bg-light">
                <form action="${pageContext.request.contextPath}/admin/User/Employees/edit" method="post">
                    <input type="hidden" name="id" value="${user.id}"/>

                    <div class="mb-3">
                        <label class="form-label">Họ Tên</label>
                        <input type="text" name="fullName" class="form-control"
                               value="${user.fullName}" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control"
                               value="${user.email}" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Số Điện Thoại</label>
                        <input type="text" name="phone" class="form-control"
                               value="${user.phone}"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Mật Khẩu</label>
                        <input type="password" name="password" class="form-control"
                               placeholder="Nhập mật khẩu mới nếu muốn thay đổi"/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Vai Trò</label>
                        <select name="userRole" class="form-select" required>
                            <option value="MANAGER" ${user.userRole == 'MANAGER' ? 'selected' : ''}>Quản Lý</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary btn-lg"
                            style="background-color:#0d47a1; border-color:#0d47a1;">
                        Cập Nhật
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/User/Employees/list"
                       class="btn btn-success btn-lg"
                       style="background-color:#1b5e20; border-color:#1b5e20;">
                        Hủy
                    </a>
                </form>
            </div>
        </div>
    </div>
</section>