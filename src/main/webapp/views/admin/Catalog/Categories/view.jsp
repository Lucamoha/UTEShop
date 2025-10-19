<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="card-footer text-right">
		<a
			href="${pageContext.request.contextPath}/admin/Catalog/Categories/searchpaginated"
			class="btn btn-secondary"><i class="bi bi-arrow-left-circle"></i>
			Trở lại</a>

		<a
			href="${pageContext.request.contextPath}/admin/Catalog/Categories/saveOrUpdate?id=${category.id}"
			class="btn btn-warning"><i class="bi bi-pencil-square"></i> Chỉnh
			sửa</a>

		<a href="javascript:void(0)" class="btn btn-danger"
			data-id="${category.id}" data-name="${category.name}"
			onclick="showConfirmation(this)"> <i class="bi bi-trash"></i> Xóa
		</a>
	</div>
	<div class="col-12 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Chi Tiết Danh Mục</h4>
			</div>
			<div class="card-body">
				<table class="table table-bordered">
					<tr>
						<th>Tên Danh Mục</th>
						<td>${category.name}</td>
					</tr>
					<tr>
						<th>Slug</th>
						<td>${category.slug}</td>
					</tr>
					<tr>
						<th>Danh Mục Cha</th>
						<td><c:if test="${not empty category.parent}">
                                ${category.parent.name}
                            </c:if></td>
					</tr>
				</table>
			</div>
		</div>

		<div class="card">
			<div class="card-header">
				<h4>Danh Sách Thông Số Theo Danh Mục</h4>
			</div>
			<div class="card-body">

				<c:if test="${not empty message}">
					<div class="alert alert-primary" role="alert">
						<i>${message}</i>
					</div>
					<c:remove var="message" scope="session" />
					<!-- xóa message sau khi da thong bao tranh trung lai khi chuyen trang -->
				</c:if>

				<div class="row mt-2 mb-3">
					<div class="col-12 text-end">
						<a class="btn btn-outline-success"
							href="${pageContext.request.contextPath}/admin/Catalog/Categories/saveOrUpdateCategoryAttribute?categoryId=${category.id}">
							<i class="bi bi-plus-circle"></i> Thêm thông số
						</a>
					</div>
				</div>


				<c:if test="${empty category}">
					<div class="alert alert-danger">Không tìm thấy thông số</div>
				</c:if>

				<c:if test="${not empty category}">
					<div class="table-responsive">
						<table class="table table-striped align-middle text-center">
							<thead class="table-dark">
								<tr>
									<th>Thông Số</th>
									<th>Cho Phép Lọc</th>
									<th>Cho Phép So Sánh</th>
									<th>Thao Tác</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="caAttr" items="${category.categoryAttributes}">
									<tr>
										<td>${caAttr.attribute.name}</td>
										<td>${caAttr.isFilterable? 'Cho Phép' : 'Không Cho Phép'}</td>
										<td>${caAttr.isComparable? 'Cho Phép' : 'Không Cho Phép'}</td>
										<td>
											<div class="btn-group" role="group">
												<a
													href="${pageContext.request.contextPath}/admin/Catalog/Categories/saveOrUpdateCategoryAttribute?categoryId=${category.id}&attributeId=${caAttr.attribute.id}"
													class="btn btn-outline-warning me-1" title="Sửa"> <i
													class="bi bi-pencil-square"></i>
												</a> <a href="javascript:void(0)" class="btn btn-outline-danger"
													data-id="${category.id}" data-name="${category.name}"
													onclick="showConfirmation(this)" title="Xóa"> <i
													class="bi bi-trash"></i>
												</a>
											</div>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>

				<!-- Phân trang -->
				<c:if test="${totalPages > 1}">
					<nav aria-label="Page navigation">
						<ul class="pagination justify-content-center">

							<li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
								<a class="page-link"
								href="${pageContext.request.contextPath}/admin/Catalog/Categories/searchpaginated?page=1&size=${size}">First</a>
							</li>

							<c:forEach begin="1" end="${totalPages}" var="i">
								<li class="page-item ${i == currentPage ? 'active' : ''}"><a
									class="page-link"
									href="${pageContext.request.contextPath}/admin/Catalog/Categories/searchpaginated?page=${i}&size=${size}">
										${i} </a></li>
							</c:forEach>

							<li
								class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
								<a class="page-link"
								href="${pageContext.request.contextPath}/admin/Catalog/Categories/searchpaginated?page=${totalPages}&size=${size}">Last</a>
							</li>
						</ul>
					</nav>
				</c:if>
			</div>
		</div>
	</div>

	<!-- Modal xác nhận xóa -->
	<div class="modal fade" id="confirmationId" tabindex="-1"
		aria-labelledby="confirmationLabel" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="confirmationLabel">Xác Nhận Xóa</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					Bạn có chắc muốn xóa <b><span id="categoryName"></span></b>?
				</div>
				<div class="modal-footer">
					<a id="yesOption" href="#" class="btn btn-danger">Có</a>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Không</button>
				</div>
			</div>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

	<!-- Script xác nhận xóa -->
	<script>
		function showConfirmation(el) {
			var id = el.getAttribute("data-id");
			var name = el.getAttribute("data-name");
			document.getElementById("categoryName").innerText = name;
			document.getElementById("yesOption").setAttribute(
					"href",
					'${pageContext.request.contextPath}/admin/Catalog/Categories/delete?id='
							+ id);
			var modal = new bootstrap.Modal(document
					.getElementById('confirmationId'));
			modal.show();
		}
	</script>
</section>
