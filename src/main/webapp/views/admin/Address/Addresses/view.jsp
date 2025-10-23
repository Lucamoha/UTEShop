<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section class="row">
    <div class="col-md-8 offset-md-2 mt-4">
        <div class="card shadow-lg">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Chi Tiết Địa Chỉ</h4>
            </div>
            <div class="card-body bg-light">

                <div class="row">
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label class="form-label fw-bold">ID</label>
                            <input type="text" class="form-control" value="${address.id}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Người Dùng (ID)</label>
                            <input type="text" class="form-control" value="${address.user != null && not empty address.user.id ? address.user.id : 'N/A'}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Nhà</label>
                            <input type="text" class="form-control" value="${address.label}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Họ Tên</label>
                            <input type="text" class="form-control" value="${address.fullName}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Điện Thoại</label>
                            <input type="text" class="form-control" value="${address.phone}" readonly/>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="mb-3">
                            <label class="form-label fw-bold">Địa Chỉ Chi Tiết</label>
                            <input type="text" class="form-control" value="${address.addressLine}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Phường/Xã</label>
                            <input type="text" class="form-control" value="${address.ward}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Quận/Huyện</label>
                            <input type="text" class="form-control" value="${address.district}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Thành Phố</label>
                            <input type="text" class="form-control" value="${address.city}" readonly/>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Mặc Định</label>
                            <input type="text" class="form-control" value="${address.isDefault ? 'Có' : 'Không'}" readonly/>
                        </div>
                    </div>
                </div>

                <div class="mt-4">
                    <a href="${pageContext.request.contextPath}/admin/Address/Addresses/edit?id=${address.id}"
                       class="btn btn-primary btn-lg"
                       style="background-color:#0d47a1; border-color:#0d47a1;">Sửa</a>
                    <a href="${pageContext.request.contextPath}/admin/Address/Addresses/list"
                       class="btn btn-success btn-lg"
                       style="background-color:#1b5e20; border-color:#1b5e20;">Quay Lại</a>
                </div>

            </div>
        </div>
    </div>
</section>