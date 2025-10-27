<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
    <div class="col-md-6 offset-md-3 mt-4">
        <div class="card shadow-lg">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Chi Tiết Người Dùng</h4>
            </div>
            <div class="card-body bg-light">

                <div class="mb-3">
                    <label class="form-label fw-bold">Họ Tên</label>
                    <input type="text" class="form-control" value="${user.fullName}" readonly/>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Email</label>
                    <input type="email" class="form-control" value="${user.email}" readonly/>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Số Điện Thoại</label>
                    <input type="text" class="form-control" value="${user.phone}" readonly/>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Vai Trò Người Dùng</label>
                    <input type="text" class="form-control" value="${user.userRole}" readonly/>
                </div>

                <div class="mt-4">
                    <a href="${pageContext.request.contextPath}/admin/User/Users/edit?id=${user.id}"
                       class="btn btn-primary btn-lg"
                       style="background-color:#0d47a1; border-color:#0d47a1;">
                        Chỉnh Sửa
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/User/Users/searchpaginated"
                       class="btn btn-success btn-lg"
                       style="background-color:#1b5e20; border-color:#1b5e20;">
                        Quay Lại
                    </a>
                </div>

            </div>
        </div>
    </div>
</section>