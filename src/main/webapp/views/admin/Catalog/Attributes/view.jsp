<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<div class="col-8 offset-2 mt-4">
		<div class="card">
			<div class="card-header">
				<h4>Chi Tiết Thông Số Kỹ Thuật</h4>
			</div>
			<div class="card-body">
				<table class="table table-bordered">
					<tr>
						<th>Id</th>
						<td>${attribute.id}</td>
					</tr>
					<tr>
						<th>Tên Thông Số</th>
						<td>${attribute.name}</td>
					</tr>
					<tr>
						<th>Kiểu Dữ Liệu</th>
						<td>${attribute.dataType == 1 ? 'Văn bản' : attribute.dataType == 2 ? 'Số' : attribute.dataType == 3 ? 'Luận lý' : 'Không xác định'}
						</td>
					</tr>
					<tr>
						<th>Đơn Vị</th>
						<td>${attribute.unit}</td>
					</tr>
				</table>
			</div>
			<div class="card-footer text-right">
				<!-- Back -->
				<a
					href="${pageContext.request.contextPath}/admin/Catalog/Attributes/searchpaginated"
					class="btn btn-secondary"><i class="bi bi-arrow-left-circle"></i>
					Trở lại</a>

				<!-- Edit -->
				<a
					href="${pageContext.request.contextPath}/admin/Catalog/Attributes/saveOrUpdate?id=${attribute.id}"
					class="btn btn-warning"><i class="bi bi-pencil-square"></i>
					Chỉnh sửa</a>

				<!-- Delete -->
				<a href="javascript:void(0)" class="btn btn-danger"
					data-id="${attribute.id}" data-name="${attribute.name}"
					onclick="showConfirmation(this)"> <i class="bi bi-trash"></i>
					Xóa
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
					Bạn có chắc muốn xóa <b><span id="attributeName"></span></b>?
				</div>
				<div class="modal-footer">
					<a id="yesOption" href="#" class="btn btn-danger">Có</a>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Không</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Script xác nhận xóa -->
	<script>
		function showConfirmation(el) {
			var id = el.getAttribute("data-id");
			var code = el.getAttribute("data-name");
			document.getElementById("attributeName").innerText = code;
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
