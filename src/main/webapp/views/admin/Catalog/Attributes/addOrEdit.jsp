<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-6 offset-3 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/Catalog/Attributes/saveOrUpdate"
			method="POST">
			<div class="card">
				<div class="card-header">
					<h2>
						<c:choose>
							<c:when test="${not empty attribute.id}">Chỉnh Sửa Thông Số Kỹ Thuật</c:when>
							<c:otherwise>Thêm Thông Số Kỹ Thuật</c:otherwise>
						</c:choose>
					</h2>
				</div>
				<div class="card-body">

					<!-- Hidden ID (nếu đang edit) -->
					<input type="hidden" name="id" value="${attribute.id}" />

					<div class="mb-3">
						<label class="form-label">Tên Thông Số:</label> <input type="text"
							name="name" class="form-control" value="${attribute.name}"
							placeholder="Nhập tên duy nhất" required />
					</div>

					<div class="mb-3">
						<label class="form-label">Chọn Kiểu Dữ Liệu:</label> <select
							name="dataType" class="form-control" required>
							<option value="1" ${attribute.dataType == 1 ? 'selected' : ''}>Văn
								bản</option>
							<option value="2" ${attribute.dataType == 2 ? 'selected' : ''}>Số</option>
							<option value="3" ${attribute.dataType == 3 ? 'selected' : ''}>Luận
								lý</option>
						</select>
					</div>

					<div class="mb-3">
						<label class="form-label">Đơn Vị:</label> <input type="text"
							name="unit" class="form-control" value="${attribute.unit}"
							placeholder="Nhập đơn vị" />
					</div>

					<div class="mt-3">
						<button type="submit" class="btn btn-primary">
							<i class="bi bi-save"></i> Lưu
						</button>
						<a
							href="${pageContext.request.contextPath}/admin/Catalog/Attributes/searchpaginated"
							class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>
