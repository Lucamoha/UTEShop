<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col-8 offset-2 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Product Detail</h4>
			</div>
			<div class="card-body">
				<table class="table table-bordered">
					<tr>
						<th>ID</th>
						<td>${product.id}</td>
					</tr>
					<tr>
						<th>Name</th>
						<td>${product.name}</td>
					</tr>
					<tr>
						<th>Slug</th>
						<td>${product.slug}</td>
					</tr>
					<tr>
						<th>Description</th>
						<td>${product.description}</td>
					</tr>
					<tr>
						<th>Base Price</th>
						<td>${product.basePrice}</td>
					</tr>
					<tr>
						<th>Status</th>
						<td><c:choose>
								<c:when test="${product.status}">Active</c:when>
								<c:otherwise>Inactive</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th>Category</th>
						<td><c:if test="${not empty product.category}">
                                ${product.category.name}
                            </c:if></td>
					</tr>
					<tr>
						<th>Created At</th>
						<td>${product.createdAt}</td>
					</tr>
					<tr>
						<th>Updated At</th>
						<td>${product.updatedAt}</td>
					</tr>
				</table>
			</div>
			<div class="card-footer text-right">
				<!-- Back -->
				<a
					href="${pageContext.request.contextPath}/admin/Product/Products/searchpaginated"
					class="btn btn-secondary"><i class="bi bi-arrow-left-circle"></i> Back</a>

				<!-- Edit -->
				<a
					href="${pageContext.request.contextPath}/admin/Product/Products/saveOrUpdate?id=${product.id}"
					class="btn btn-warning"><i class="bi bi-pencil-square"></i> Edit</a>

				<!-- Delete -->
				<a href="javascript:void(0)" class="btn btn-danger"
					data-id="${product.id}" data-name="${product.name}"
					onclick="showConfirmation(this)"> <i class="bi bi-trash"></i> Delete
				</a>
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
</section>
