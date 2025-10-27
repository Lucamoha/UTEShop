<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="col mt-4">
	<div class="card">
		<div class="card-header">
			<h4>Danh Sách Voucher</h4>
		</div>
		<div class="card-body">

			<!-- Hiển thị thông báo -->
			<c:if test="${not empty param.success}">
				<div class="alert alert-success" role="alert">Thao tác thành
					công!</div>
			</c:if>

			<div class="row mt-2 mb-3">
				<div class="col-md-6"></div>
				<div class="col-md-6">
					<div class="float-end">
						<a class="btn btn-outline-success"
							href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/add"><i
							class="bi bi-plus-circle"></i> Thêm Voucher</a>
					</div>
				</div>
			</div>

			<c:if test="${not empty listVouchers}">
				<div class="table-responsive">
					<table class="table table-striped align-middle text-center">
						<thead class="table-dark">
							<tr>
								<th>Mã</th>
								<th>Giá Trị</th>
								<th>Lượt Dùng Tối Đa</th>
								<th>Lượt Đã Dùng</th>
								<th>Trạng Thái</th>
								<th>Hành Động</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="voucher" items="${listVouchers}">
								<tr>
									<td>${voucher.code}</td>
									<td><c:choose>
											<c:when test="${voucher.type == 1}">
												<fmt:formatNumber value="${voucher.value}"
													maxFractionDigits="0" />%
                                        </c:when>
											<c:when test="${voucher.type == 2}">
												<fmt:formatNumber value="${voucher.value}" type="currency"
													maxFractionDigits="0" currencySymbol="₫" />
											</c:when>
											<c:otherwise>
                                            ${voucher.value} (Loại không xác định)
                                        </c:otherwise>
										</c:choose></td>
									<td>${voucher.maxUses}</td>
									<td>${voucher.totalUsed}</td>
									<td>${voucher.isActive ? 'Hoạt Động' : 'Không Hoạt Động'}</td>
									<td><a
										href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/view?id=${voucher.id}"
										class="btn btn-outline-info btn-sm" title="Xem"> <i
											class="bi bi-eye"></i>
									</a><a
										href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/edit?id=${voucher.id}"
										class="btn btn-outline-warning btn-sm" title="Sửa"> <i
											class="bi bi-pencil-square"></i></a> <a href="javascript:void(0)"
										class="btn btn-outline-danger btn-sm" data-id="${voucher.id}"
										data-name="${voucher.code}" onclick="showConfirmation(this)"
										title="Xóa"> <i class="bi bi-trash"></i>
									</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
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