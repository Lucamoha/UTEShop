<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<section class="row">
	<div class="col-md-8 offset-2 mt-4">
		<div class="card">
			<div class="card-header">
				<h4 class="mb-0">Chi Tiết Voucher</h4>
			</div>
			<div class="card-body">

				<div class="row">
					<div class="col-md-6">
						<div class="mb-3">
							<label class="form-label fw-bold">Mã Số</label> <input
								type="text" class="form-control" value="${voucher.id}" readonly />
						</div>

						<div class="mb-3">
							<label class="form-label fw-bold">Mã</label> <input type="text"
								class="form-control" value="${voucher.code}" readonly />
						</div>

						<div class="mb-3">
							<label class="form-label fw-bold">Mô Tả</label> <input
								type="text" class="form-control" value="${voucher.descText}"
								readonly />
						</div>

						<div class="mb-3">
							<label class="form-label fw-bold">Loại</label> <input type="text"
								class="form-control"
								value="${voucher.type == 1 ? 'Giảm Giá Phần Trăm' : (voucher.type == 2 ? 'Giảm Giá Cố Định' : 'Không xác định')}"
								readonly />
						</div>

						<div class="mb-3">
							<label class="form-label fw-bold">Giá Trị</label> <input
								type="text" class="form-control"
								value="<c:choose><c:when test='${voucher.type == 1}'><fmt:formatNumber value='${voucher.value}' maxFractionDigits='2'/>%</c:when><c:when test='${voucher.type == 2}'><fmt:formatNumber value='${voucher.value}' type='currency' currencySymbol='₫'/></c:when><c:otherwise>${voucher.value} (Loại không xác định)</c:otherwise></c:choose>"
								readonly />
						</div>
					</div>
					<div class="col-md-6">
						<div class="mb-3">
							<label class="form-label fw-bold">Số Lần Sử Dụng Tối Đa</label> <input
								type="text" class="form-control" value="${voucher.maxUses}"
								readonly />
						</div>

						<div class="mb-3">
							<label class="form-label fw-bold">Số Lần Đã Sử Dụng</label> <input
								type="text" class="form-control" value="${voucher.totalUsed}"
								readonly />
						</div>

						<div class="mb-3">
							<label class="form-label fw-bold">Ngày Bắt Đầu</label> <input
								type="text" class="form-control"
								value="${fn:substring(voucher.startsAt, 8,10)}/${fn:substring(voucher.startsAt,5,7)}/${fn:substring(voucher.startsAt,0,4)} ${fn:substring(voucher.startsAt,11,16)}"
								readonly />
						</div>

						<div class="mb-3">
							<label class="form-label fw-bold">Ngày Kết Thúc</label> <input
								type="text" class="form-control"
								value="${fn:substring(voucher.endsAt, 8,10)}/${fn:substring(voucher.endsAt,5,7)}/${fn:substring(voucher.endsAt,0,4)} ${fn:substring(voucher.endsAt,11,16)}"
								readonly />
						</div>

						<div class="mb-3">
							<label class="form-label fw-bold">Hoạt Động</label> <input
								type="text" class="form-control"
								value="${voucher.isActive ? 'Có' : 'Không'}" readonly />
						</div>
					</div>
				</div>

				<div class="mt-4">
					<a
						href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/searchpaginated"
						class="btn btn-secondary"><i class="bi bi-arrow-left-circle"></i>
						Trở lại </a> <a
						href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/edit?id=${voucher.id}"
						class="btn btn-warning"><i class="bi bi-pencil-square"></i>
						Sửa </a> <a href="javascript:void(0)" class="btn btn-danger"
						data-id="${voucher.id}" data-name="${voucher.code}"
						onclick="showConfirmation(this)"> <i class="bi bi-trash"></i>
						Xóa
					</a>
				</div>

			</div>
		</div>
	</div>

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
					Bạn có chắc muốn xóa voucher có mã <b>"<span id="VoucherCode"></span>"
					</b>?
				</div>
				<div class="modal-footer">
					<a id="yesOption" href="#" class="btn btn-danger">Có</a>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Không</button>
				</div>
			</div>
		</div>
	</div>
</section>

<!-- Script xác nhận xóa -->
<script>
	function showConfirmation(el) {
		var id = el.getAttribute("data-id");
		var code = el.getAttribute("data-name");
		document.getElementById("VoucherCode").innerText = code;
		document.getElementById("yesOption").setAttribute(
				"href",
				'${pageContext.request.contextPath}/admin/Voucher/Vouchers/delete?id='
						+ id);
		var modal = new bootstrap.Modal(document
				.getElementById('confirmationId'));
		modal.show();
	}
</script>