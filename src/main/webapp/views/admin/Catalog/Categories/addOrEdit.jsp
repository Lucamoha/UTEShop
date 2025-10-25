<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-6 offset-3 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/Catalog/Categories/saveOrUpdate"
			method="POST">
			<div class="card">
				<div class="card-header">
					<h2>
						<c:choose>
							<c:when test="${not empty category.id}">Chỉnh Sửa Danh Mục</c:when>
							<c:otherwise>Thêm Danh Mục</c:otherwise>
						</c:choose>
					</h2>
				</div>
				<div class="card-body">

					<!-- Hidden ID (nếu đang edit) -->
					<input type="hidden" name="id" value="${category.id}" />

					<div class="mb-3">
						<label class="form-label">Tên Danh Mục:</label> <input type="text" id="name"
							name="name" class="form-control" value="${category.name}"
							placeholder="Nhập tên danh mục" />
					</div>
					
					<div class="mb-3">
						<label class="form-label">Slug:</label> <input type="text"
							id="slug" name="slug" class="form-control"
							value="${category.slug}" readonly/>
					</div>

					<div class="mb-3">
						<label class="form-label">Danh Mục Cha:</label> <select
							name="parentId" class="form-select">
							<option value="">NULL</option>
							<c:forEach var="parent" items="${categoryList}">
								<option value="${parent.id}"
									<c:if test="${category.parent.id == parent.id}">selected</c:if>>
									${parent.name}</option>
							</c:forEach>
						</select>
					</div>

					<div class="mt-3">
						<button type="submit" class="btn btn-primary">
							<i class="bi bi-save"></i> Lưu
						</button>
						<a
							href="${pageContext.request.contextPath}/admin/Catalog/Categories/searchpaginated"
							class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>

<script>
    // ====== Tạo slug ======
    function toSlug(str) {
        str = str.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
        str = str.replace(/[^a-z0-9\s-]/g, '').trim().replace(/\s+/g, '-');
        return str.replace(/^-+|-+$/g, '');
    }
    document.getElementById('name').addEventListener('input', function () {
        document.getElementById('slug').value = toSlug(this.value);
    });
</script>
