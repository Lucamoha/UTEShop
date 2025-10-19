<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-6 offset-3 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/Catalog/Categories/saveOrUpdateCategoryAttribute"
			method="POST">
			<div class="card">
				<div class="card-header">
					<h2>
						<c:choose>
							<c:when test="${not empty categoryAttribute.id}">Chỉnh Sửa Thông Số Danh Mục</c:when>
							<c:otherwise>Thêm Thông Số Danh Mục</c:otherwise>
						</c:choose>
					</h2>
				</div>
				<div class="card-body">

					<!-- Hidden ID (nếu đang edit) -->
					<input type="hidden" name="id" value="${categoryAttribute.id}" />
					
					<!-- Khi add có thêm categoryId từ req -->
					<input type="hidden" name="categoryId" value="${categoryId}" />

					<div class="mb-3">
						<label class="form-label">Thông Số:</label> <select
							name="attributeId" class="form-select" required>
							<!-- <option value="">NULL</option> -->
							<c:forEach var="attr" items="${attributeList}">
								<option value="${attr.id}"
									<c:if test="${categoryAttribute.attribute.id == attr.id}">selected</c:if>>
									${attr.name}</option>
							</c:forEach>
						</select>
					</div>

					<div class="mb-3">
						<label class="form-label">Cho Phép Lọc:</label> <select
							name="isFilterable" class="form-control" required>
							<option value="true"
								${categoryAttribute.isFilterable ? 'selected' : ''}>Cho
								Phép</option>
							<option value="false"
								${!categoryAttribute.isFilterable ? 'selected' : ''}>Không
								Cho Phép</option>
						</select>
					</div>
					
					<div class="mb-3">
						<label class="form-label">Cho Phép So Sánh:</label> <select
							name="isComparable" class="form-control" required>
							<option value="true"
								${categoryAttribute.isComparable ? 'selected' : ''}>Cho
								Phép</option>
							<option value="false"
								${!categoryAttribute.isComparable ? 'selected' : ''}>Không
								Cho Phép</option>
						</select>
					</div>

					<div class="mt-3">
						<button type="submit" class="btn btn-primary"><i class="bi bi-save"></i> Lưu
						</button>
						<a
							href="${pageContext.request.contextPath}/admin/Catalog/Categories/view?id=${categoryAttribute.category.id}"
							class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>
