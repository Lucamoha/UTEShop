<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-12 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/Branch/Branches/saveOrUpdate"
			method="POST">
			<div class="card">
				<div class="card-header">
					<h2>
						<c:choose>
							<c:when test="${not empty branch.id}">Chỉnh Sửa Thông Tin Chi Nhánh</c:when>
							<c:otherwise>Thêm Chi Nhánh</c:otherwise>
						</c:choose>
					</h2>
				</div>
				<div class="card-body">

					<!-- Hidden ID (nếu đang edit) -->
					<input type="hidden" name="id" value="${branch.id}" />

					<div class="mb-3">
						<label class="form-label">Tên Chi Nhánh:</label> <input
							type="text" name="name" class="form-control"
							value="${branch.name}" placeholder="Nhập tên duy nhất" required />
					</div>

					<div class="mb-3">
						<label class="form-label">Địa Chỉ:</label> <input type="text"
							name="address" class="form-control" value="${branch.address}"
							placeholder="Nhập địa chỉ chi nhánh" />
					</div>

					<div class="mb-3">
						<label class="form-label">Điện Thoại Liên Hệ:</label> <input
							type="text" name="phone" class="form-control"
							value="${branch.phone}" placeholder="Nhập số điện thoại" />
					</div>

					<div class="mb-3">
						<label class="form-label">Trạng Thái:</label> <select
							name="isActive" class="form-control" required>
							<option value="true" ${branch.isActive ? 'selected' : ''}>Hoạt
								động</option>
							<option value="false" ${!branch.isActive ? 'selected' : ''}>Ngừng
								Hoạt động</option>
						</select>
					</div>

					<div class="mb-3">
						<label class="form-label">ID Người Quản Lý:</label>
						<div class="input-group">
							<input type="number" id="managerId" name="managerId"
								class="form-control" placeholder="Nhập ID người quản lý"
								value="${branch.manager != null ? branch.manager.id : ''}">
							<button type="button" class="btn btn-outline-secondary"
								id="checkManager">Kiểm tra</button>
						</div>
					</div>

					<div class="mb-3" id="managerInfo" style="display: none;">
						<label class="form-label">Thông tin Người Quản Lý:</label>
						<div class="border p-2 rounded bg-light">
							<p>
								<strong>Tên:</strong> <span id="managerName"></span>
							</p>
							<p>
								<strong>Điện thoại:</strong> <span id="managerPhone"></span>
							</p>
						</div>
					</div>

					<div id="managerError" class="text-danger" style="display: none;"></div>

					<div class="mb-3">
						<h4 class="mt-4">Thông Tin Tồn Kho Theo Biến Thể</h4>
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>SKU</th>
									<th>Giá</th>
									<th>Tùy chọn</th>
									<th>Tồn kho</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="bi" items="${inventories}">
									<tr>
										<td>${bi.variant.SKU}</td>
										<td>${bi.variant.price}</td>
										<td><c:forEach var="opt" items="${bi.variant.options}">
                        ${opt.optionType.code}: ${opt.optionValue.value}<br />
											</c:forEach></td>
										<td><input type="number" name="stock_${bi.variant.id}"
											value="${bi.branchStock}" class="form-control" min="0" required/></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

					</div>

					<div class="mt-3">
						<button type="submit" class="btn btn-primary">
							<i class="bi bi-save"></i> Lưu
						</button>
						<a
							href="${pageContext.request.contextPath}/admin/Branch/Branches/searchpaginated"
							class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>

<script>
document.getElementById('checkManager').addEventListener('click', function() {
    const id = document.getElementById('managerId').value.trim();
    if (!id) {
        alert('Vui lòng nhập ID người quản lý');
        return;
    }

    fetch('${pageContext.request.contextPath}/admin/Users/find?id=' + id)
        .then(res => res.json())
        .then(data => {
            if (data && data.id) {
                document.getElementById('managerInfo').style.display = 'block';
                document.getElementById('managerError').style.display = 'none';
                document.getElementById('managerName').textContent = data.fullName;
                document.getElementById('managerPhone').textContent = data.phone;
            } else {
                document.getElementById('managerInfo').style.display = 'none';
                document.getElementById('managerError').style.display = 'block';
                document.getElementById('managerError').textContent = 'Không tìm thấy người quản lý với ID này hoặc người quản lý này đã quản lý một chi nhánh khác';
            }
        })
        .catch(() => {
            document.getElementById('managerInfo').style.display = 'none';
            document.getElementById('managerError').style.display = 'block';
            document.getElementById('managerError').textContent = 'Lỗi khi tìm người quản lý';
        });
});
</script>