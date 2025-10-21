<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglib.jsp"%>

<c:set var="u" value="${user}" />
<c:set var="b" value="${branch}" />

<div class="page-inner">
    <c:if test="${not empty sessionScope.flash_ok}">
        <div class="alert alert-success">Cập nhật thành công.</div>
        <c:remove var="flash_ok" scope="session"/>
    </c:if>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="row">
        <div class="col-12 col-lg-6 mb-3">
            <div class="card">
                <div class="card-header">
                    <div class="card-head-row">
                        <div class="card-title">
                            <h5 class="mb-0 fw-bold">Thông tin chi nhánh</h5>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-sm-4">Tên chi nhánh</dt>
                        <dd class="col-sm-8"><span class="fw-semibold" id="branchName"><c:out value="${b.name}" /></span></dd>

                        <dt class="col-sm-4">Địa chỉ</dt>
                        <dd class="col-sm-8" id="branchAddress"><c:out value="${b.address}" /></dd>

                        <dt class="col-sm-4">Điện thoại</dt>
                        <dd class="col-sm-8" id="branchPhone"><c:out value="${b.phone}" /></dd>

                        <dt class="col-sm-4">Ngày tạo</dt>
                        <dd class="col-sm-8" id="branchCreated">
                            <c:out value="${b.createdAt}" />
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
        <!-- Chart Top sản phẩm -->
        <div class="col-12 col-lg-6 mb-3">
            <div class="card">
                <div class="card-header">
                    <div class="card-head-row">
                        <div class="card-title">
                            <h5 class="mb-0 fw-bold">Thông tin cá nhân</h5>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-sm-4">Email</dt>
                        <dd class="col-sm-8" id="profileEmail"><c:out value="${u.email}" /></dd>

                        <dt class="col-sm-4">Họ tên</dt>
                        <dd class="col-sm-8" id="profileFullName"><c:out value="${u.fullName}" /></dd>

                        <dt class="col-sm-4">Điện thoại</dt>
                        <dd class="col-sm-8" id="profilePhone"><c:out value="${u.phone}" /></dd>

                        <dt class="col-sm-4">Vai trò</dt>
                        <dd class="col-sm-8" id="profileRole"><span class="badge bg-primary"><c:out value="${u.userRole}" /></span></dd>
                    </dl>
                </div>
                <div class="card-footer">
                    <button type="button"
                            class="btn btn-primary"
                            data-bs-toggle="modal"
                            data-bs-target="#confirmModal"
                            id="btnOpenEdit">Chỉnh sửa
                    </button>
                    <button type="button"
                            class="btn btn-danger"
                            data-bs-toggle="modal"
                            data-bs-target="#resetPasswordModal"
                            id="btnOpenResetPass">Đặt lại mật khẩu
                    </button>
                </div>
            </div>
        </div>
    </div>

    <%-- Modal cập nhật thông tin cá nhân--%>
    <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered"> <!-- centered -->
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold">Cập nhật thông tin</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <!-- FORM cập nhật -->
                <form id="editProfileForm" method="post" action="${pageContext.request.contextPath}/manager/profile" novalidate>
                    <div class="modal-body">
                        <input type="hidden" name="userId" value="${u.id}" />

                        <div class="mb-3">
                            <label for="editEmail" class="form-label">Email</label>
                            <input type="email" class="form-control" id="editEmail" value="${u.email}" disabled>
                            <div class="form-text">Email không thể thay đổi.</div>
                        </div>

                        <div class="mb-3">
                            <label for="editFullName" class="form-label">Họ tên</label>
                            <input  type="text"
                                    class="form-control"
                                    id="editFullName"
                                    name="fullName"
                                    value="${u.fullName}"
                                    required
                                    maxlength="120">
                            <div class="invalid-feedback">Vui lòng nhập họ tên (tối đa 120 ký tự).</div>
                        </div>

                        <div class="mb-3">
                            <label for="editPhone" class="form-label">Điện thoại</label>
                            <input  type="tel"
                                    class="form-control"
                                    id="editPhone"
                                    name="phone"
                                    value="${u.phone}"
                                    pattern="^(0|\+84)[0-9]{8,10}$"
                                    maxlength="15"
                                    placeholder="VD: 098xxxxxxx hoặc +8498xxxxxxx">
                            <div class="invalid-feedback">Số điện thoại chưa hợp lệ (bắt đầu bằng 0 hoặc +84).</div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button id="btnCancelConfirm" type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button id="btnDoConfirm" type="submit" class="btn btn-primary">
                            Xác nhận
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <%-- Modal đặt lại mật khẩu --%>
    <div class="modal fade" id="resetPasswordModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered"> <!-- centered -->
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title fw-bold">Đặt lại mật khẩu</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>

                <!-- FORM cập nhật -->
                <form id="resetPasswordForm"
                      method="post"
                      action="${pageContext.request.contextPath}/manager/profile/reset-password"
                      novalidate>

                    <div class="modal-body">
                        <input type="hidden" name="userId" value="${u.id}" />

                        <div class="mb-3">
                            <label for="currentPassword" class="form-label">Mật khẩu hiện tại</label>
                            <input type="password"
                                   class="form-control"
                                   id="currentPassword"
                                   name="currentPassword"
                                   required
                                   placeholder="Nhập mật khẩu hiện tại">
                            <div class="invalid-feedback">Vui lòng nhập mật khẩu hiện tại.</div>
                        </div>

                        <div class="mb-3">
                            <label for="newPassword" class="form-label">Mật khẩu mới</label>
                            <input type="password"
                                   class="form-control"
                                   id="newPassword"
                                   name="newPassword"
                                   required
                                   minlength="6"
                                   maxlength="50"
                                   placeholder="Tối thiểu 6 ký tự">
                            <div class="invalid-feedback">Mật khẩu mới không hợp lệ (tối thiểu 6 ký tự).</div>
                        </div>

                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">Xác nhận mật khẩu mới</label>
                            <input type="password"
                                   class="form-control"
                                   id="confirmPassword"
                                   name="confirmPassword"
                                   required
                                   placeholder="Nhập lại mật khẩu mới">
                            <div class="invalid-feedback">Mật khẩu xác nhận không khớp.</div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button"
                                class="btn btn-outline-secondary"
                                data-bs-dismiss="modal">Hủy</button>
                        <button type="submit"
                                class="btn btn-primary">Xác nhận</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const form = document.getElementById("resetPasswordForm");

            form.addEventListener("submit", function (e) {
                e.preventDefault();
                e.stopPropagation();

                const newPass = form.newPassword.value.trim();
                const confirm = form.confirmPassword.value.trim();

                if (newPass !== confirm) {
                    form.confirmPassword.classList.add("is-invalid");
                    return;
                } else {
                    form.confirmPassword.classList.remove("is-invalid");
                }

                if (!form.checkValidity()) {
                    form.classList.add("was-validated");
                } else {
                    form.submit(); // submit nếu hợp lệ
                }
            });
        });
    </script>
</div>