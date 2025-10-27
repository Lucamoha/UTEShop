<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
    <div class="col-md-6 offset-md-3 mt-4">
        <div class="card shadow-lg">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Thêm Quản Lý</h4>
            </div>
            <div class="card-body bg-light">
                <form
                        action="${pageContext.request.contextPath}/admin/User/Employees/add"
                        method="post">
                    <input type="hidden" name="action" value="add" />

                    <div class="mb-3">
                        <label class="form-label">Họ Tên</label>
                        <input type="text" name="fullName" class="form-control" required />
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control" required />
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Số Điện Thoại</label>
                        <input type="text" name="phone" class="form-control" />
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Mật Khẩu</label>
                        <input type="password" name="password" class="form-control" required />
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Vai Trò</label>
                        <select name="userRole" class="form-select" required>
                            <option value="MANAGER">Quản Lý</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary btn-lg"
                            style="background-color: #0d47a1; border-color: #0d47a1;">
                        Lưu
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/User/Employees/searchpaginated"
                       class="btn btn-success btn-lg"
                       style="background-color: #1b5e20; border-color: #1b5e20;">
                        Hủy
                    </a>
                </form>
            </div>
        </div>
    </div>
</section>