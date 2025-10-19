<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-6 offset-3 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/Catalog/OptionValues/saveOrUpdate"
			method="POST">
			<div class="card">
				<div class="card-header">
					<h2>
						<c:choose>
							<c:when test="${not empty optionValue.id}">Chỉnh Sửa Giá Trị Tùy Chọn</c:when>
							<c:otherwise>Thêm Giá Trị Tùy Chọn</c:otherwise>
						</c:choose>
					</h2>
				</div>
				<div class="card-body">

					<!-- Hidden ID (nếu đang edit) -->
					<input type="hidden" name="id" value="${optionValue.id}" />

					<div class="mb-3">
						<label class="form-label">Loại Tùy Chọn:</label> <select
							name="optionTypeId" class="form-select">
							<c:forEach var="ot" items="${optionTypeList}">
								<option value="${ot.id}"
									<c:if test="${optionValue.optionType.id == ot.id}">selected</c:if>>
									${ot.code}</option>
							</c:forEach>
						</select>
					</div>
					
					<div class="mb-3">
						<label class="form-label">Giá Trị:</label> <input type="text"
							name="value" class="form-control" value="${optionValue.value}"
							placeholder="Nhập giá trị duy nhất theo loại tùy chọn"/>
					</div>

					<div class="mt-3">
						<button type="submit" class="btn btn-primary"><i class="bi bi-save"></i> Lưu</button>
						<a
							href="${pageContext.request.contextPath}/admin/Catalog/OptionValues/searchpaginated"
							class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>
