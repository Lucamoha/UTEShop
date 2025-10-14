<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col-8 offset-2 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Chi Tiết Loại Biến Thể</h4>
			</div>
			<div class="card-body">
				<table class="table table-bordered">
					<tr>
						<th>Id</th>
						<td>${optionType.id}</td>
					</tr>
					<tr>
						<th>Mã Code</th>
						<td>${optionType.code}</td>
					</tr>
				</table>
			</div>
			<div class="card-footer text-right">
				<!-- Back -->
				<a
					href="${pageContext.request.contextPath}/admin/Catalog/OptionTypes/searchpaginated"
					class="btn btn-secondary"><i class="bi bi-arrow-left-circle"></i> Trở lại</a>

				<!-- Edit -->
				<a
					href="${pageContext.request.contextPath}/admin/Catalog/OptionTypes/saveOrUpdate?id=${optionType.id}"
					class="btn btn-warning"><i class="bi bi-pencil-square"></i> Chỉnh sửa</a>

				<!-- Delete -->
				<a href="javascript:void(0)" class="btn btn-danger"
					data-id="${optionType.id}" data-name="${optionType.code}"
					onclick="showConfirmation(this)"> <i class="bi bi-trash"></i> Xóa
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
					<h5 class="modal-title" id="confirmationLabel">Xác Nhận Xóa</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					Bạn có chắc muốn xóa <b><span id="optionTypeCode"></span></b>?
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
			document.getElementById("optionTypeCode").innerText = code;
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
