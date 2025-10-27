<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col-8 offset-2 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Chi Tiết Người Dùng</h4>
			</div>
			<div class="card-body">

				<div class="mb-3">
					<label class="form-label fw-bold">Họ Tên</label> <input type="text"
						class="form-control" value="${user.fullName}" readonly />
				</div>

				<div class="mb-3">
					<label class="form-label fw-bold">Email</label> <input type="email"
						class="form-control" value="${user.email}" readonly />
				</div>

				<div class="mb-3">
					<label class="form-label fw-bold">Số Điện Thoại</label> <input
						type="text" class="form-control" value="${user.phone}" readonly />
				</div>

				<div class="mb-3">
					<label class="form-label fw-bold">Vai Trò Người Dùng</label> <input
						type="text" class="form-control" value="${user.userRole}" readonly />
				</div>

				<div class="mb-3">
					<label class="form-label fw-bold">Trạng Thái</label>
					<c:choose>
						<c:when test="${user.isActive}">
							<input type="text" class="form-control" value="Kích Hoạt"
								readonly />
						</c:when>
						<c:otherwise>
							<input type="text" class="form-control" value="Không Kích Hoạt"
								readonly />
						</c:otherwise>
					</c:choose>
				</div>

				<div class="mt-4">
					<a
						href="${pageContext.request.contextPath}/admin/User/Users/searchpaginated"
						class="btn btn-secondary"><i
						class="bi bi-arrow-left-circle"></i> Trở lại</a>
					<a
						href="${pageContext.request.contextPath}/admin/User/Users/edit?id=${user.id}"
						class="btn btn-warning"><i class="bi bi-pencil-square"></i>
						Sửa</a> 
				</div>

			</div>
		</div>
	</div>
</section>