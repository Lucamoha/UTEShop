<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<section class="row">
	<div class="col-5 mt-4">
		<div class="card-header">
			<a
				href="${pageContext.request.contextPath}/admin/Catalog/Products/searchpaginated"
				class="btn btn-secondary"><i class="bi bi-arrow-left-circle"></i>
				Back</a>
		</div>
	</div>

	<div class="col-12 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Chi Tiết Sản Phẩm</h4>
			</div>
			<div class="card-body">
				<table class="table table-bordered">
					<tr>
						<th>ID</th>
						<td>${product.id}</td>
					</tr>
					<tr>
						<th>Tên</th>
						<td>${product.name}</td>
					</tr>
					<tr>
						<th>Slug</th>
						<td>${product.slug}</td>
					</tr>
					<tr>
						<th>Mô Tả</th>
						<td>${product.description}</td>
					</tr>
					<tr>
						<th>Giá Gốc</th>
						<td>${product.basePrice}</td>
					</tr>
					<tr>
						<th>Trạng Thái</th>
						<td><c:choose>
								<c:when test="${product.status}">Đang Bán</c:when>
								<c:otherwise>Ngừng Bán</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th>Danh Mục</th>
						<td><c:if test="${not empty product.category}">
                                ${product.category.name}
                            </c:if></td>
					</tr>
					<tr>
						<th>Ngày Tạo</th>
						<td>${product.createdAt}</td>
					</tr>
					<tr>
						<th>Ngày Cập Nhật</th>
						<td>${product.updatedAt}</td>
					</tr>
				</table>
			</div>
			<div class="card-footer text-right">

				<!-- Edit -->
				<a
					href="${pageContext.request.contextPath}/admin/Catalog/Products/saveOrUpdate?id=${product.id}"
					class="btn btn-warning"><i class="bi bi-pencil-square"></i>
					Edit</a>

				<!-- Delete -->
				<a href="javascript:void(0)" class="btn btn-danger"
					data-id="${product.id}" data-name="${product.name}"
					onclick="showConfirmation(this)"> <i class="bi bi-trash"></i>
					Delete
				</a>
			</div>
		</div>
	</div>

	<div class="col-12 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Hình Ảnh Sản Phẩm</h4>
			</div>
			<div class="card-body">
				<table class="table table-bordered">
					<thead>
						<tr>
							<th>Ảnh</th>
							<th>Thao Tác</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${productsDetailModel.productImages}" var="img">
							<tr>
								<c:url value='/image' var="imageUrl">
									<c:param name="fname" value="${img.imageUrl}" />
								</c:url>
								<td><img height="150" width="200"
									src="${pageContext.request.contextPath}/image?fname=${img.imageUrl}" /></td>
								<td><a
									href="<c:url value='admin/Catalog/Products/image/saveOrUpdate?imageId=${img.id}'/>">Sửa</a>
									| <a
									href="<c:url value='admin/Catalog/Products/image/delete?imageId=${img.id}'/>">Xóa</a>
								</td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>
			<div class="card-footer text-right">
				<a class="btn btn-outline-success"
					href="${pageContext.request.contextPath}/admin/Catalog/Products/saveOrUpdate"><i
					class="bi bi-plus-circle"></i> Thêm ảnh</a>
			</div>
		</div>
	</div>

	<div class="col-12 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Thông Số Kỹ Thuật</h4>
			</div>
			<div class="card-body">
				<div class="table-responsive">
					<table class="table table-striped align-middle text-center">
						<thead class="table-dark">
							<tr>
								<th>Thuộc tính</th>
								<th>Giá trị</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="attr" items="${productAttributes}">
								<tr>
									<td>${attr.name}</td>
									<td>${attr.displayValue}</td>

									<td>
										<div class="btn-group" role="group">
											<a
												href="${pageContext.request.contextPath}/admin/Catalog/Products/view?id=${v.id}"
												class="btn btn-outline-info me-1" title="View"> <i
												class="bi bi-eye"></i> <!-- me-1 (margin-end) -->
											</a> <a
												href="${pageContext.request.contextPath}/admin/Catalog/Products/saveOrUpdate?id=${v.id}"
												class="btn btn-outline-warning me-1" title="Edit"> <i
												class="bi bi-pencil-square"></i>
											</a> <a href="javascript:void(0)" class="btn btn-outline-danger"
												data-id="${product.id}" data-name="${product.name}"
												onclick="showConfirmation(this)" title="Delete"> <i
												class="bi bi-trash"></i>
											</a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<div class="col-12 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Chi Tiết Biến Thể Sản Phẩm</h4>
			</div>
			<div class="card-body">
				<div class="table-responsive">
					<table class="table table-striped align-middle text-center">
						<thead class="table-dark">
							<tr>
								<th>SKU</th>
								<th>Giá</th>
								<th>Trạng Thái</th>
								<th>Tùy chọn</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="v" items="${variantList}">
								<tr>
									<td>${v.SKU}</td>
									<td><fmt:formatNumber value="${v.price}" type="number"
											maxFractionDigits="0" groupingUsed="true" />VND</td>
									<td><c:choose>
											<c:when test="${v.status}">
												<span class="badge bg-success">Đang bán</span>
											</c:when>
											<c:otherwise>
												<span class="badge bg-secondary">Ngừng bán</span>
											</c:otherwise>
										</c:choose></td>

									<td><c:forEach var="opt" items="${v.options}">
										${opt}<br />
										</c:forEach></td>

									<td>
										<div class="btn-group" role="group">
											<a
												href="${pageContext.request.contextPath}/admin/Catalog/Products/view?id=${v.id}"
												class="btn btn-outline-info me-1" title="View"> <i
												class="bi bi-eye"></i> <!-- me-1 (margin-end) -->
											</a> <a
												href="${pageContext.request.contextPath}/admin/Catalog/Products/saveOrUpdate?id=${v.id}"
												class="btn btn-outline-warning me-1" title="Edit"> <i
												class="bi bi-pencil-square"></i>
											</a> <a href="javascript:void(0)" class="btn btn-outline-danger"
												data-id="${product.id}" data-name="${product.name}"
												onclick="showConfirmation(this)" title="Delete"> <i
												class="bi bi-trash"></i>
											</a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
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
					Bạn có chắc muốn xóa <b><span id="productName"></span></b>?
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
			document.getElementById("productName").innerText = name;
			document.getElementById("yesOption").setAttribute(
					"href",
					'${pageContext.request.contextPath}/admin/Catalog/Products/delete?id='
							+ id);
			var modal = new bootstrap.Modal(document
					.getElementById('confirmationId'));
			modal.show();
		}
	</script>
</section>
