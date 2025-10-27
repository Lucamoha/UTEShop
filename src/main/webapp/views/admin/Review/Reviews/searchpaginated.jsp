<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
java.time.format.DateTimeFormatter fullFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
pageContext.setAttribute("fullFormatter", fullFormatter);
%>

<div class="container mt-4">
	<div class="card">
		<div class="card-header">
			<h4>Danh Sách Đánh Giá</h4>
		</div>
		<div class="card-body">

			<c:if test="${not empty param.success}">
				<div class="alert alert-success">Thao tác thành công!</div>
			</c:if>

			<div class="row mt-2 mb-3">
				<div class="col-md-6">
					<a
						href="${pageContext.request.contextPath}/admin/Review/Reviews/export"
						class="btn btn-success">Xuất Excel</a>
				</div>
				<div class="col-md-6">
					<div class="float-end">
						<form
							action="${pageContext.request.contextPath}/admin/Review/Reviews/filter"
							method="get" class="d-flex">
							<select name="rating" class="form-select me-2"
								style="width: 150px;">
								<option value="0">Tất cả</option>
								<c:forEach var="i" begin="1" end="5">
									<option value="${i}" ${selectedRating == i ? 'selected' : ''}>${i} 
										Sao</option>
								</c:forEach>
							</select>
							<button type="submit" class="btn btn-primary">Lọc</button>
						</form>
					</div>
				</div>
			</div>

			<c:if test="${not empty listReviews}">
				<div class="table-responsive">
					<table class="table table-striped align-middle text-center">
						<thead class="table-dark">
							<tr>
								<th>Người Dùng</th>
								<th>Sản Phẩm</th>
								<th>Đánh Giá</th>
								<th>Nội Dung</th>
								<th>Trạng Thái</th>
								<th>Ngày Tạo</th>
								<th>Hành Động</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="review" items="${listReviews}">
								<tr>
									<td>${review.user.id}-${review.user.fullName}</td>
									<td>${review.product.name}</td>
									<td>${review.rating} Sao</td>
									<td>${review.content}</td>
									<td>${review.status ? 'Hiển thị' : 'Ẩn'}</td>
									<td>${review.createdAt.format(fullFormatter)}</td>
									<td class="d-flex align-items-center gap-2"><a
										href="${pageContext.request.contextPath}/admin/Review/Reviews/view?id=${review.id}"
										class="btn btn-outline-info" title="Xem"> <i
											class="bi bi-eye"></i></a> <a href="javascript:void(0)"
										class="btn btn-outline-danger" data-id="${review.id}"
										data-name="${review.content}"
										onclick="showConfirmation(this)" title="Xóa"> <i
											class="bi bi-trash"></i></a></td>
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
				<h5 class="modal-title" id="confirmationLabel">Xác nhận xóa</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="Close"></button>
			</div>
			<div class="modal-body">
				Bạn có chắc muốn xóa đánh giá có nội dung "<b><span id="reviewContent"></span></b>" ?
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
		var content = el.getAttribute("data-name");
		document.getElementById("reviewContent").innerText = content;
		document.getElementById("yesOption").setAttribute(
				"href",
				'${pageContext.request.contextPath}/admin/Review/Reviews/delete?id='
						+ id);
		var modal = new bootstrap.Modal(document
				.getElementById('confirmationId'));
		modal.show();
	}
</script>