<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<section class="row">

	<c:if test="${not empty error}">
		<div class="alert alert-danger" role="alert">${error}</div>
	</c:if>

	<div class="col-md-6 offset-md-3 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/Voucher/Vouchers/add"
			method="POST">
			<div class="card">
				<div class="card-header">
					<h4 class="mb-0">Thêm Voucher</h4>
				</div>
				<div class="card-body">
					<div class="mb-3">
						<label for="code" class="form-label">Mã Voucher *</label> <input
							type="text" class="form-control" id="code" name="code"
							value="${voucher.code}" required>
					</div>

					<div class="mb-3">
						<label for="descText" class="form-label">Mô Tả</label> <input
							type="text" class="form-control" id="descText" name="descText"
							value="${voucher.descText}">
					</div>

					<div class="mb-3">
						<label for="type" class="form-label">Loại *</label> <select
							class="form-select" id="type" name="type" required>
							<option value="1" ${voucher.type == 1 ? 'selected' : ''}>Giảm
								Theo Phần Trăm</option>
							<option value="2" ${voucher.type == 2 ? 'selected' : ''}>Giảm
								Theo Số Tiền</option>
						</select>
					</div>

					<div class="mb-3">
						<label for="value" class="form-label">Giá Trị *</label> <input
							type="number" step="1" class="form-control" id="value"
							name="value"
							value="${voucher.value}"
							required>
					</div>

					<div class="mb-3">
						<label for="maxUses" class="form-label">Số Lần Sử Dụng Tối
							Đa *</label> <input type="number" class="form-control" id="maxUses"
							name="maxUses" value="${voucher.maxUses}" required min="1">
					</div>

					<div class="mb-3">
						<label for="startsAt" class="form-label">Ngày Bắt Đầu *</label> <input
							type="datetime-local" class="form-control" id="startsAt"
							name="startsAt" value="${voucher.startsAt}" required>
					</div>

					<div class="mb-3">
						<label for="endsAt" class="form-label">Ngày Kết Thúc *</label> <input
							type="datetime-local" class="form-control" id="endsAt"
							name="endsAt" value="${voucher.endsAt}" required>
					</div>

					<div class="mb-3 form-check">
						<input type="checkbox" class="form-check-input" id="isActive"
							name="isActive" value="true" ${voucher.isActive ? 'checked' : ''}>
						<label class="form-check-label" for="isActive">Hoạt Động</label>
					</div>

					<button type="submit" class="btn btn-primary">
						<i class="bi bi-save"></i> Lưu
					</button>
					<a
						href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/searchpaginated"
						class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy</a>
				</div>
			</div>
		</form>
	</div>
</section>

<script>
	document.addEventListener("DOMContentLoaded", function() {
		const typeSelect = document.getElementById("type");
		const valueInput = document.getElementById("value");

		function updateValueConstraints() {
			if (typeSelect.value === "1") {
				//Giảm theo %
				valueInput.min = 0;
				valueInput.max = 90;
				valueInput.placeholder = "0 - 90 (%)";
			} else {
				//Giảm theo số tiền
				valueInput.min = 1000;
				valueInput.max = 999999999;
				valueInput.placeholder = "Từ 1,000 đến 999,999,999 (VNĐ)";
			}
		}

		//Cập nhật khi load và khi thay đổi loại
		updateValueConstraints();
		typeSelect.addEventListener("change", updateValueConstraints);
	});
</script>