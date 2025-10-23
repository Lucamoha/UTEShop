<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section class="row">
    <div class="col-md-6 offset-md-3 mt-4">
        <div class="card shadow-lg">
            <div class="card-header bg-dark text-white">
                <h4 class="mb-0">Thêm Địa Chỉ Mới</h4>
            </div>
            <div class="card-body bg-light">

                <!-- Hiển thị lỗi -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                            ${error}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/admin/Address/Addresses/add" method="post">

                    <div class="mb-3">
                        <label for="userId" class="form-label">Người Dùng *</label>
                        <select class="form-select" id="userId" name="userId" required>
                            <c:forEach var="user" items="${listUser}">
                                <option value="${user.id}">${user.fullName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="label" class="form-label">Nhà</label>
                        <input type="text" class="form-control" id="label" name="label" value="">
                    </div>

                    <div class="mb-3">
                        <label for="fullName" class="form-label">Họ Tên *</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" value="" required>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label">Điện Thoại *</label>
                        <input type="text" class="form-control" id="phone" name="phone" value="" required>
                    </div>

                    <div class="mb-3">
                        <label for="addressLine" class="form-label">Địa Chỉ Chi Tiết *</label>
                        <input type="text" class="form-control" id="addressLine" name="addressLine" value="" required>
                    </div>

                    <div class="mb-3">
                        <label for="ward" class="form-label">Phường/Xã</label>
                        <input type="text" class="form-control" id="ward" name="ward" value="">
                    </div>

                    <div class="mb-3">
                        <label for="district" class="form-label">Quận/Huyện</label>
                        <input type="text" class="form-control" id="district" name="district" value="">
                    </div>

                    <div class="mb-3">
                        <label for="city" class="form-label">Thành Phố</label>
                        <input type="text" class="form-control" id="city" name="city" value="">
                    </div>

                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="isDefault" name="isDefault" value="true">
                        <label class="form-check-label" for="isDefault">Mặc Định</label>
                    </div>

                    <button type="submit" class="btn btn-success btn-lg"
                            style="background-color:#1b5e20; border-color:#1b5e20;">Thêm</button>
                    <a href="${pageContext.request.contextPath}/admin/Address/Addresses/list"
                       class="btn btn-secondary btn-lg">Hủy</a>
                </form>
            </div>
        </div>
    </div>
</section>