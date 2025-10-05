<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>List Product</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container mt-4">
		<div class="card">
			<div class="card-header">
				<h4>List Product</h4>
			</div>
			<div class="card-body">

				<c:if test="${not empty message}">
					<div class="alert alert-primary" role="alert">
						<i>${message}</i>
					</div>
				</c:if>

				<div class="row mt-2 mb-3">
					<div class="col-md-6">
						<form
							action="${pageContext.request.contextPath}/admin/Product/Products/searchpaginated"
							method="get">
							<div class="input-group">
								<input type="text" class="form-control" name="name"
									placeholder="Nhập từ khóa để tìm" value="${param.name}">
								<button class="btn btn-outline-primary">Search</button>
							</div>
						</form>
					</div>

					<div class="col-md-6">
						<div class="float-end">
							<a class="btn btn-outline-success"
								href="${pageContext.request.contextPath}/admin/Product/Products/add">Add
								New Product</a>
						</div>
					</div>
				</div>

				<c:if test="${empty productList}">
					<div class="alert alert-danger">No Product Found</div>
				</c:if>

				<c:if test="${not empty productList}">
					<table class="table table-striped align-middle text-center">
						<thead class="table-dark">
							<tr>
								<th>ID</th>
								<th>Product Name</th>
								<th>Slug</th>
								<th>Price</th>
								<th>Category</th>
								<th>Status</th>
								<th>Action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="product" items="${productList}">
								<tr>
									<td>${product.id}</td>
									<td>${product.name}</td>
									<td>${product.slug}</td>
									<td><fmt:formatNumber value="${product.basePrice}"
											type="number" pattern="#,##0.000" /> VNĐ</td>
									<td>${product.category.name}</td>
									<td><c:choose>
											<c:when test="${product.status}">
												<span class="badge bg-success">Active</span>
											</c:when>
											<c:otherwise>
												<span class="badge bg-secondary">Inactive</span>
											</c:otherwise>
										</c:choose></td>
									<td><a
										href="${pageContext.request.contextPath}/admin/Product/Products/view?id=${product.id}"
										class="btn btn-outline-info btn-sm">View</a> <a
										href="${pageContext.request.contextPath}/admin/Product/Products/edit?id=${product.id}"
										class="btn btn-outline-warning btn-sm">Edit</a> <a
										href="javascript:void(0)"
										class="btn btn-outline-danger btn-sm" data-id="${product.id}"
										data-name="${product.name}" onclick="showConfirmation(this)">Delete</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>

				<!-- Phân trang -->
				<c:if test="${totalPages > 1}">
					<nav aria-label="Page navigation">
						<ul class="pagination justify-content-center">

							<li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
								<a class="page-link"
								href="${pageContext.request.contextPath}/admin/Product/Products/searchpaginated?page=1&size=${size}">First</a>
							</li>

							<c:forEach begin="1" end="${totalPages}" var="i">
								<li class="page-item ${i == currentPage ? 'active' : ''}">
									<a class="page-link"
									href="${pageContext.request.contextPath}/admin/Product/Products/searchpaginated?page=${i}&size=${size}">
										${i} </a>
								</li>
							</c:forEach>

							<li
								class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
								<a class="page-link"
								href="${pageContext.request.contextPath}/admin/Product/Products/searchpaginated?page=${totalPages}&size=${size}">Last</a>
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
					<h5 class="modal-title" id="confirmationLabel">Confirmation</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					Bạn có chắc muốn xóa <b><span id="productName"></span></b>?
				</div>
				<div class="modal-footer">
					<a id="yesOption" href="#" class="btn btn-danger">Yes</a>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Close</button>
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
			document.getElementById("productName").innerText = name;
			document.getElementById("yesOption").setAttribute(
					"href",
					'${pageContext.request.contextPath}/admin/Product/Products/delete?id='
							+ id);
			var modal = new bootstrap.Modal(document
					.getElementById('confirmationId'));
			modal.show();
		}
	</script>

</body>
</html>
