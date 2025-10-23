<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<section class="row">
    <div class="col-md-6 offset-md-3 mt-4">
        <div class="card shadow-lg">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Chỉnh Sửa Voucher</h4>
            </div>
            <div class="card-body bg-light">

                <!-- Hiển thị lỗi -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                            ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/admin/Voucher/Vouchers/edit" method="post">
                    <input type="hidden" name="id" value="${voucher.id}">

                    <div class="mb-3">
                        <label for="code" class="form-label">Mã Voucher *</label>
                        <input type="text" class="form-control" id="code" name="code" value="${voucher.code}" required>
                    </div>

                    <div class="mb-3">
                        <label for="descText" class="form-label">Mô Tả</label>
                        <input type="text" class="form-control" id="descText" name="descText" value="${voucher.descText}">
                    </div>

                    <div class="mb-3">
                        <label for="type" class="form-label">Loại *</label>
                        <select class="form-select" id="type" name="type" required>
                            <option value="1" ${voucher.type == 1 ? 'selected' : ''}>Giảm Giá Phần Trăm</option>
                            <option value="2" ${voucher.type == 2 ? 'selected' : ''}>Giảm Giá Cố Định</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="value" class="form-label">Giá Trị *</label>
                        <input type="number" step="0.01" class="form-control" id="value" name="value" value="${voucher.value}" required>
                    </div>

                    <div class="mb-3">
                        <label for="maxUses" class="form-label">Số Lần Sử Dụng Tối Đa *</label>
                        <input type="number" class="form-control" id="maxUses" name="maxUses" value="${voucher.maxUses}" required min="1">
                    </div>

                    <div class="mb-3">
                        <label for="totalUsed" class="form-label">Số Lần Đã Sử Dụng</label>
                        <input type="text" class="form-control-plaintext" value="${voucher.totalUsed}" readonly>
                    </div>

                    <div class="mb-3">
                        <label for="startsAt" class="form-label">Ngày Bắt Đầu *</label>
                        <input type="datetime-local" class="form-control" id="startsAt" name="startsAt"
                               value="${voucher.startsAt}" required>
                    </div>

                    <div class="mb-3">
                        <label for="endsAt" class="form-label">Ngày Kết Thúc *</label>
                        <input type="datetime-local" class="form-control" id="endsAt" name="endsAt"
                               value="${voucher.endsAt}" required>
                    </div>

                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="isActive" name="isActive" value="true" ${voucher.isActive ? 'checked' : ''}>
                        <label class="form-check-label" for="isActive">Hoạt Động</label>
                    </div>

                    <!-- Nút Cập Nhật xanh dương đậm -->
                    <button type="submit" class="btn btn-primary btn-lg"
                            style="background-color:#0d47a1; border-color:#0d47a1;">
                        Cập Nhật
                    </button>
                    <!-- Nút Hủy xanh lá đậm -->
                    <a href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/list"
                       class="btn btn-success btn-lg"
                       style="background-color:#1b5e20; border-color:#1b5e20;">
                        Hủy
                    </a>
                </form>
            </div>
        </div>
    </div>
</section>