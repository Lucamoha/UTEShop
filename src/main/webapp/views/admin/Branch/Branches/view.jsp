<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="card-footer text-right">
	<a
		href="${pageContext.request.contextPath}/admin/Branch/Branches/searchpaginated"
		class="btn btn-secondary"><i class="bi bi-arrow-left-circle"></i>
		Trở lại</a>

	<a
		href="${pageContext.request.contextPath}/admin/Branch/Branches/saveOrUpdate?id=${detailModel.branch.id}"
		class="btn btn-warning"><i class="bi bi-pencil-square"></i> Chỉnh
		sửa</a>

	<a href="javascript:void(0)" class="btn btn-danger"
		data-id="${detailModel.branch.id}"
		data-name="${detailModel.branch.name}"
		onclick="showConfirmation(this)"> <i class="bi bi-trash"></i> Xóa
	</a>
</div>

<section class="row">
	<div class="col-12 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Chi Tiết Chi Nhánh</h4>
			</div>
			<div class="card-body">
				<table class="table table-bordered">
					<tr>
						<th>Id</th>
						<td>${detailModel.branch.id}</td>
					</tr>
					<tr>
						<th>Tên Chi Nhánh</th>
						<td>${detailModel.branch.name}</td>
					</tr>
					<tr>
						<th>Địa Chỉ</th>
						<td>${detailModel.branch.address}</td>
					</tr>
					<tr>
						<th>Điện Thoại Liên Hệ</th>
						<td>${detailModel.branch.phone}</td>
					</tr>
					<tr>
						<th>Trạng Thái</th>
						<td>${detailModel.branch.isActive ? 'Hoạt động': 'Ngừng hoạt động'}</td>
					</tr>
					<tr>
						<th>Tên Người Quản Lý</th>
						<td>${detailModel.branch.manager.fullName}</td>
					</tr>
				</table>
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
								<th>Tùy Chọn</th>
								<th>Số Lượng Tồn</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="bi" items="${detailModel.branchInventories}">
								<tr>
									<td>${bi.variant.SKU}</td>
									<td><fmt:formatNumber value="${bi.variant.price}"
											type="number" maxFractionDigits="0" groupingUsed="true" />VND</td>
									<td><c:choose>
											<c:when test="${bi.variant.status}">
												<span class="badge bg-success">Đang bán</span>
											</c:when>
											<c:otherwise>
												<span class="badge bg-secondary">Ngừng bán</span>
											</c:otherwise>
										</c:choose></td>

									<td><c:forEach var="opt" items="${bi.variant.options}">
										${opt}<br />
										</c:forEach></td>
									<td>${bi.branchStock}</td>

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
					Bạn có chắc muốn xóa <b><span id="branchName"></span></b>?
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
			var code = el.getAttribute("data-name");
			document.getElementById("branchName").innerText = code;
			document.getElementById("yesOption").setAttribute(
					"href",
					'${pageContext.request.contextPath}/admin/Branch/Branches/delete?id='
							+ id);
			var modal = new bootstrap.Modal(document
					.getElementById('confirmationId'));
			modal.show();
		}
	</script>
</section>
