<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- <!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>List Product</title>
</head>
<body> -->
	<div class="container mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Danh mục sản phẩm</h4>
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
					<div class="col-md-6">
						<form
							action="${pageContext.request.contextPath}/admin/Catalog/Categories/searchpaginated"
							method="get">
							<div class="input-group">
								<input type="text" class="form-control" name="searchKeyword"
									placeholder="Nhập từ khóa tên để tìm" value="${searchKeyword}">
								<button class="btn btn-outline-primary"><i class="fa fa-search search-icon"></i>  Search</button>
							</div>
						</form>
					</div>

					<div class="col-md-6">
						<div class="float-end">
							<a class="btn btn-outline-success"
								href="${pageContext.request.contextPath}/admin/Catalog/Categories/saveOrUpdate"><i
								class="bi bi-plus-circle"></i> Thêm danh mục </a>
						</div>
					</div>
				</div>

				<c:if test="${empty categoryList}">
					<div class="alert alert-danger">Không tìm thấy danh mục</div>
				</c:if>

				<c:if test="${not empty categoryList}">
					<div class="table-responsive">
						<table class="table table-striped align-middle text-center">
							<thead class="table-dark">
								<tr>
									<th>Tên Danh Mục</th>
									<th>Slug</th>
									<th>Danh Mục Cha</th>
									<th>Thao Tác</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="category" items="${categoryList}">
									<tr>
										<td>${category.name}</td>
										<td>${category.slug}</td>
										<td><c:choose>
												<c:when test="${category.parent != null}">${category.parent.name}</c:when>
												<c:otherwise>
													<span class="text-muted">—</span>
												</c:otherwise>
											</c:choose></td>

										<td>
											<div class="btn-group" role="group">
												<a
													href="${pageContext.request.contextPath}/admin/Catalog/Categories/view?id=${category.id}"
													class="btn btn-outline-info me-1" title="Xem"> <i
													class="bi bi-eye"></i> <!-- me-1 (margin-end) -->
												</a> <a
													href="${pageContext.request.contextPath}/admin/Catalog/Categoried/saveOrUpdate?id=${category.id}"
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
								<li class="page-item ${i == currentPage ? 'active' : ''}">
									<a class="page-link"
									href="${pageContext.request.contextPath}/admin/Catalog/Categories/searchpaginated?page=${i}&size=${size}">
										${i} </a>
								</li>
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
					<h5 class="modal-title" id="confirmationLabel">Xác nhận xóa</h5>
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

<!-- </body>
</html>
 -->