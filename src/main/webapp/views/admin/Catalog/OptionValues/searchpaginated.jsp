<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-4">
	<div class="card">
		<div class="card-header">
			<h4>Danh Sách Giá Trị Tùy Chọn</h4>
		</div>
		<div class="card-body">

			<c:if test="${not empty message}">
				<div class="alert alert-primary" role="alert">
					<i>${message}</i>
				</div>
				<c:remove var="message" scope="session" />
				<!-- xóa message sau khi da thong bao tranh trung lai khi chuyen trang -->
			</c:if>

			<c:if test="${not empty sessionScope.errorMessage}">
				<div class="alert alert-danger">${sessionScope.errorMessage}</div>
				<c:remove var="errorMessage" scope="session" />
			</c:if>

			<div class="row mt-2 mb-3">
				<div class="col-md-6">
					<form
						action="${pageContext.request.contextPath}/admin/Catalog/OptionValues/searchpaginated"
						method="get">
						<div class="input-group">
							<input type="text" class="form-control" name="searchKeyword"
								placeholder="Nhập từ khóa giá trị để tìm"
								value="${searchKeyword}">
							<button class="btn btn-outline-primary">
								<i class="fa fa-search search-icon"></i> Tìm kiếm
							</button>
						</div>
					</form>
				</div>

				<div class="col-md-6">
					<div class="float-end">
						<a class="btn btn-outline-success"
							href="${pageContext.request.contextPath}/admin/Catalog/OptionValues/saveOrUpdate"><i
							class="bi bi-plus-circle"></i> Thêm Giá Trị Tùy Chọn</a>
					</div>
				</div>
			</div>

			<c:if test="${empty optionValueList}">
				<div class="alert alert-danger">Không tìm thấy giá trị tùy chọn</div>
			</c:if>

			<c:if test="${not empty optionValueList}">
				<div class="table-responsive">
					<table class="table table-striped align-middle text-center">
						<thead class="table-dark">
							<tr>
								<th>ID</th>
								<th>Loại Tùy Chọn</th>
								<th>Giá Trị</th>
								<th>Thao tác</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="optionValue" items="${optionValueList}">
								<tr>
									<td>${optionValue.id}</td>
									<td>${optionValue.optionType.code}</td>
									<td>${optionValue.value}</td>
									<td>
										<div class="btn-group" role="group">
											<a
												href="${pageContext.request.contextPath}/admin/Catalog/OptionValues/view?id=${optionValue.id}"
												class="btn btn-outline-info me-1" title="Xem"> <i
												class="bi bi-eye"></i> <!-- me-1 (margin-end) -->
											</a> <a
												href="${pageContext.request.contextPath}/admin/Catalog/OptionValues/saveOrUpdate?id=${optionValue.id}"
												class="btn btn-outline-warning me-1" title="Sửa"> <i
												class="bi bi-pencil-square"></i>
											</a> <a href="javascript:void(0)" class="btn btn-outline-danger"
												data-id="${optionValue.id}" data-name="${optionValue.value}"
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
							href="${pageContext.request.contextPath}/admin/Catalog/OptionValues/searchpaginated?page=1&size=${size}">First</a>
						</li>

						<c:forEach begin="1" end="${totalPages}" var="i">
							<li class="page-item ${i == currentPage ? 'active' : ''}"><a
								class="page-link"
								href="${pageContext.request.contextPath}/admin/Catalog/OptionValues/searchpaginated?page=${i}&size=${size}">
									${i} </a></li>
						</c:forEach>

						<li
							class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
							<a class="page-link"
							href="${pageContext.request.contextPath}/admin/Catalog/OptionValues/searchpaginated?page=${totalPages}&size=${size}">Last</a>
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
				Bạn có chắc muốn xóa <b><span id="optionValueValue"></span></b>?
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
		document.getElementById("optionValueValue").innerText = code;
		document.getElementById("yesOption").setAttribute(
				"href",
				'${pageContext.request.contextPath}/admin/Catalog/OptionValues/delete?id='
						+ id);
		var modal = new bootstrap.Modal(document
				.getElementById('confirmationId'));
		modal.show();
	}
</script>