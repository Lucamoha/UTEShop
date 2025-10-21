<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/commons/taglib.jsp"%>

<div class="page-inner">
	<div class="page-header">
		<h3 class="fw-bold mb-3">Trang Cá Nhân</h3>
		<ul class="breadcrumbs mb-3">
			<li class="nav-home"><a
				href="${pageContext.request.contextPath}/admin/dashboard"> <i
					class="icon-home"></i>
			</a></li>
			<li class="separator"><i class="icon-arrow-right"></i></li>
			<li class="nav-item"><a href="#">Trang cá nhân</a></li>
		</ul>
	</div>

	<!-- Success/Error Messages -->
	<c:if test="${not empty success}">
		<div class="alert alert-success alert-dismissible fade show"
			role="alert">
			<strong>Thành công!</strong> ${success}
			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="alert alert-danger alert-dismissible fade show"
			role="alert">
			<strong>Lỗi!</strong> ${error}
			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>
		</div>
	</c:if>

	<div class="row">
		<!-- Thông tin cá nhân -->
		<div class="col-md-8">
			<div class="card">
				<div class="card-header">
					<div class="card-title">Thông Tin Cá Nhân</div>
				</div>
				<div class="card-body">
					<form method="post"
						action="${pageContext.request.contextPath}/admin/profile"
						id="profileForm">
						<div class="row">
							<div class="col-md-12 mb-3">
								<label for="email" class="form-label">Email</label> <input
									type="email" class="form-control" id="email" name="email"
									value="${user.email}" disabled> <small
									class="form-text text-muted">Email không thể thay đổi</small>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12 mb-3">
								<label for="fullName" class="form-label">Họ và Tên <span
									class="text-danger">*</span></label> <input type="text"
									class="form-control" id="fullName" name="fullName"
									value="${user.fullName}" required placeholder="Nhập họ và tên">
							</div>
						</div>

						<div class="row">
							<div class="col-md-12 mb-3">
								<label for="phone" class="form-label">Số Điện Thoại</label> <input
									type="tel" class="form-control" id="phone" name="phone"
									value="${user.phone}"
									placeholder="Nhập số điện thoại (10-11 số)"> <small
									class="form-text text-muted">Định dạng: 10-11 chữ số</small>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12">
								<button type="submit" class="btn btn-primary">
									<i class="fas fa-save"></i> Lưu Thay Đổi
								</button>
								<button type="button" class="btn btn-secondary"
									onclick="resetForm()">
									<i class="fas fa-undo"></i> Hủy
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>

		<!-- Quick Links -->
		<div class="col-md-4">
			<div class="card">
				<div class="card-header">
					<div class="card-title">Liên Kết Nhanh</div>
				</div>
				<div class="card-body">
					<div class="d-grid gap-2">
						<a
							href="${pageContext.request.contextPath}/admin/profile/change-password"
							class="btn btn-outline-primary"> <i class="fas fa-key"></i>
							Đổi Mật Khẩu
						</a> <a href="${pageContext.request.contextPath}/admin/dashboard"
							class="btn btn-outline-secondary"> <i
							class="fas fa-tachometer-alt"></i> Về Dashboard
						</a>
					</div>
				</div>
			</div>

			<!-- User Info Card -->
			<div class="card mt-3">
				<div class="card-header">
					<div class="card-title">Thông Tin Tài Khoản</div>
				</div>
				<div class="card-body">
					<div class="d-flex align-items-center mb-3">
						<div
							class="avatar avatar-xl d-flex align-items-center justify-content-center rounded-circle bg-light"
							style="width: 80px; height: 80px;">
							<i class="bi bi-person-circle"
								style="font-size: 48px; color: #6c757d;"></i>
						</div>
						<div class="ms-3">
							<h5 class="mb-1">${user.fullName}</h5>
							<p class="text-muted mb-0">${user.email}</p>
						</div>
					</div>

					<hr>
					<div class="info-item">
						<label class="text-muted">Vai trò:</label>
						<p class="mb-2">
							<span class="badge bg-primary">${user.userRole}</span>
						</p>
					</div>
					<div class="info-item">
						<label class="text-muted">Số điện thoại:</label>
						<p class="mb-2">${empty user.phone ? 'Chưa cập nhật' : user.phone}</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
	// Store original values
	const originalFullName = document.getElementById('fullName').value;
	const originalPhone = document.getElementById('phone').value;

	function resetForm() {
		document.getElementById('fullName').value = originalFullName;
		document.getElementById('phone').value = originalPhone;
	}

	// Validate form before submit
	document
			.getElementById('profileForm')
			.addEventListener(
					'submit',
					function(e) {
						const fullName = document.getElementById('fullName').value
								.trim();
						const phone = document.getElementById('phone').value
								.trim();

						if (!fullName) {
							e.preventDefault();
							swal("Lỗi!", "Vui lòng nhập họ và tên!", "error");
							return;
						}

						if (phone && !/^[0-9]{10,11}$/.test(phone)) {
							e.preventDefault();
							swal("Lỗi!", "Số điện thoại phải có 10-11 chữ số!",
									"error");
							return;
						}
					});
</script>
