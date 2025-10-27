<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col-6 offset-3 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/User/Users/edit"
			method="POST">
			<div class="card">
				<div class="card-header">
					<h4>Chỉnh Sửa Người Dùng</h4>
				</div>
				<div class="card-body">

					<!-- ID ẩn -->
					<input type="hidden" name="id" value="${user.id}" />

					<div class="mb-3">
						<label class="form-label">Họ Tên</label> <input type="text"
							name="fullName" class="form-control" value="${user.fullName}"
							required />
					</div>

					<div class="mb-3">
						<label class="form-label">Email</label> <input type="email"
							name="email" class="form-control" value="${user.email}" required />
					</div>

					<div class="mb-3">
						<label class="form-label">Số Điện Thoại</label> <input type="text"
							name="phone" class="form-control" value="${user.phone}" />
					</div>

					<div class="mb-3">
						<label class="form-label">Vai Trò Người Dùng</label> <select
							name="userRole" class="form-select" required>
							<option value="USER" ${user.userRole == 'USER' ? 'selected' : ''}>NGƯỜI
								DÙNG</option>
							<option value="ADMIN"
								${user.userRole == 'ADMIN' ? 'selected' : ''}>NGƯỜI
								QUẢN TRỊ</option>
							<option value="MANAGER"
								${user.userRole == 'MANAGER' ? 'selected' : ''}>NGƯỜI
								QUẢN LÝ</option>
						</select>
					</div>

					<div class="mb-3">
						<label class="form-label">Trạng Thái Người Dùng</label> <select
							name="isActive" class="form-select" required>
							<option value="true" <c:if test="${user.isActive}">selected</c:if>>Kích
								Hoạt</option>
							<option value="false" <c:if test="${!user.isActive}">selected</c:if>>Không
								Kích Hoạt</option>
						</select>
					</div>

					<button type="submit" class="btn btn-primary">
						<i class="bi bi-save"></i> Lưu
					</button>
					<a
						href="${pageContext.request.contextPath}/admin/User/Users/searchpaginated"
						class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy </a>

				</div>
			</div>
		</form>
	</div>
</section>
