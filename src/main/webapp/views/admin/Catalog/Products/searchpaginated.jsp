<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="container mt-4">
	<div class="card">
		<div class="card-header">
			<h4>Danh Sách Sản Phẩm</h4>
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
				<div class="col-md-8">
					<form
						action="${pageContext.request.contextPath}/admin/Catalog/Products/searchpaginated"
						method="get">
						<div class="input-group">
							<input type="text" id="searchBox" class="form-control"
								name="searchKeyword"
								placeholder="Nhập từ khóa tên hoặc nói để tìm"
								value="${searchKeyword}">
							<button id="startBtn" type="button"
								class="btn btn-outline-secondary">🎤 Nói</button>
							<button class="btn btn-outline-primary" type="submit">Tìm
								kiếm</button>
						</div>
					</form>
				</div>

				<div class="col-md-4">
					<div class="float-end">
						<a class="btn btn-outline-success"
							href="${pageContext.request.contextPath}/admin/Catalog/Products/saveOrUpdate"><i
							class="bi bi-plus-circle"></i> Thêm Sản Phẩm</a>
					</div>
				</div>
			</div>

			<c:if test="${empty productsDetailModels}">
				<div class="alert alert-danger">Không tìm thấy sản phẩm</div>
			</c:if>

			<c:if test="${not empty productsDetailModels}">
				<div class="table-responsive">
					<table class="table table-striped align-middle text-center">
						<thead class="table-dark">
							<tr>
								<th>Tên Sản Phẩm</th>
								<th>Slug</th>
								<th>Giá</th>
								<th>Danh Mục</th>
								<th>Trạng thái</th>
								<th>Biến Thể</th>
								<th>Thao Tác</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="productsDetailModel"
								items="${productsDetailModels}">
								<tr>
									<td>${productsDetailModel.product.name}</td>
									<td>${productsDetailModel.product.slug}</td>
									<td><fmt:formatNumber
											value="${productsDetailModel.product.basePrice}"
											type="number" maxFractionDigits="0" groupingUsed="true" />VND</td>
									<!-- maxFractionDigits="0": khong lay phan thap phan
										groupingUsed="true": dung dau phan cach hang nghin
									-->
									<td>${productsDetailModel.product.category.name}</td>
									<td><c:choose>
											<c:when test="${productsDetailModel.product.status}">
												<span class="badge bg-success">Đang bán</span>
											</c:when>
											<c:otherwise>
												<span class="badge bg-secondary">Ngừng bán</span>
											</c:otherwise>
										</c:choose></td>

									<td>${productsDetailModel.totalVariants}</td>

									<td>
										<div class="btn-group" role="group">
											<a
												href="${pageContext.request.contextPath}/admin/Catalog/Products/view?id=${productsDetailModel.product.id}"
												class="btn btn-outline-info me-1" title="Xem"> <i
												class="bi bi-eye"></i> <!-- me-1 (margin-end) -->
											</a> <a
												href="${pageContext.request.contextPath}/admin/Catalog/Products/saveOrUpdate?id=${productsDetailModel.product.id}"
												class="btn btn-outline-warning me-1" title="Chỉnh sửa">
												<i class="bi bi-pencil-square"></i>
											</a> <a href="javascript:void(0)" class="btn btn-outline-danger"
												data-id="${productsDetailModel.product.id}"
												data-name="${productsDetailModel.product.name}"
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
							href="${pageContext.request.contextPath}/admin/Catalog/Products/searchpaginated?page=1&size=${size}">First</a>
						</li>

						<c:forEach begin="1" end="${totalPages}" var="i">
							<li class="page-item ${i == currentPage ? 'active' : ''}"><a
								class="page-link"
								href="${pageContext.request.contextPath}/admin/Catalog/Products/searchpaginated?page=${i}&size=${size}">
									${i} </a></li>
						</c:forEach>

						<li
							class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
							<a class="page-link"
							href="${pageContext.request.contextPath}/admin/Catalog/Products/searchpaginated?page=${totalPages}&size=${size}">Last</a>
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
				<h5 class="modal-title" id="confirmationLabel">Xác Nhận</h5>
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

<script>
document.addEventListener("DOMContentLoaded", () => {
  if (!('webkitSpeechRecognition' in window)) {
    console.warn("Trình duyệt không hỗ trợ Web Speech API");
    return;
  }

  const recognition = new webkitSpeechRecognition();
  recognition.lang = 'vi-VN';
  recognition.continuous = false;
  recognition.interimResults = false;

  const searchBox = document.getElementById('searchBox');
  const startBtn = document.getElementById('startBtn');

  startBtn.addEventListener('click', (e) => {
    e.preventDefault();
    recognition.start();
    startBtn.textContent = "🎧 Đang nghe...";
  });

  recognition.onresult = (event) => {
	let text = event.results[0][0].transcript;
    
 	// Xóa dấu câu ở cuối nếu có (?, ., !) do lên giọng
    text = text.trim().replace(/[?.!]+$/g, "");
    
    searchBox.value = text;
  };

  recognition.onerror = (e) => {
    console.error("Speech recognition error:", e);
    alert("Không thể nhận diện giọng nói (" + e.error + ")");
  };

  recognition.onend = () => {
    startBtn.textContent = "🎤 Nói";
  };
});
</script>

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
