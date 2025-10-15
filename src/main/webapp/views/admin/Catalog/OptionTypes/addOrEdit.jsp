<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-6 offset-3 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/Catalog/OptionTypes/saveOrUpdate"
			method="POST">
			<div class="card">
				<div class="card-header">
					<h2>
						<c:choose>
							<c:when test="${not empty optionType.id}">Chỉnh Sửa Loại Tùy Chọn</c:when>
							<c:otherwise>Thêm Loại Tùy Chọn</c:otherwise>
						</c:choose>
					</h2>
				</div>
				<div class="card-body">

					<!-- Hidden ID (nếu đang edit) -->
					<input type="hidden" name="id" value="${optionType.id}" />

					<div class="mb-3">
						<label class="form-label">Mã Code:</label> <input type="text"
							name="code" class="form-control" value="${optionType.code}"
							placeholder="Nhập mã code duy nhất" required/>
					</div>

					<div class="mt-3">
						<button type="submit" class="btn btn-primary"><i class="bi bi-save"></i> Lưu</button>
						<a
							href="${pageContext.request.contextPath}/admin/Catalog/OptionTypes/searchpaginated"
							class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>
