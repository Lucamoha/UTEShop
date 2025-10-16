<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-12 mt-4">
		<form
			action="${pageContext.request.contextPath}/admin/Catalog/Products/saveOrUpdate"
			method="POST" enctype="multipart/form-data">
			<div class="card">
				<div class="card-header">
					<h2>
						<c:choose>
							<c:when test="${not empty product.id}">Sửa Sản Phẩm</c:when>
							<c:otherwise>Thêm Sản Phẩm</c:otherwise>
						</c:choose>
					</h2>
				</div>

				<div class="card-body">
					<input type="hidden" name="id" value="${product.id}" />

					<!-- Tên sản phẩm -->
					<div class="mb-3">
						<label class="form-label">Tên Sản Phẩm:</label> <input type="text"
							id="name" name="name" class="form-control"
							value="${product.name}" placeholder="Nhập tên sản phẩm" />
					</div>

					<!-- Slug -->
					<div class="mb-3">
						<label class="form-label">Slug (tự động tạo):</label> <input
							type="text" id="slug" name="slug" class="form-control"
							value="${product.slug}" readonly />
					</div>

					<!-- Mô tả -->
					<div class="mb-3">
						<label class="form-label">Mô Tả:</label>
						<textarea name="description" class="form-control" rows="3">${product.description}</textarea>
					</div>

					<!-- Giá gốc -->
					<div class="mb-3">
						<label class="form-label">Giá Gốc:</label> <input type="number"
							name="basePrice" min="1000" max="9999999999" class="form-control"
							value="${product.basePrice.intValue()}" required />
					</div>

					<!-- Danh mục -->
					<div class="mb-3">
						<label class="form-label">Danh Mục:</label> <select
							name="categoryId" class="form-select" required>
							<option value="">-- Chọn từ danh sách --</option>
							<c:forEach var="cat" items="${categoryList}">
								<option value="${cat.id}"
									<c:if test="${product.category.id == cat.id}">selected</c:if>>
									${cat.name}</option>
							</c:forEach>
						</select>
					</div>

					<!-- Trạng thái -->
					<div class="mb-3">
						<label class="form-label">Trạng Thái:</label> <select
							name="status" class="form-select" required>
							<option value="true"
								<c:if test="${product.status}">selected</c:if>>Đang Bán</option>
							<option value="false"
								<c:if test="${not product.status}">selected</c:if>>Ngừng
								Bán</option>
						</select>
					</div>

					<!-- Ảnh sản phẩm -->
					<div class="mb-3">
						<label class="form-label">Hình Ảnh Sản Phẩm:</label>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-striped align-middle text-center">
									<thead class="table-dark">
										<tr>
											<th>Ảnh</th>
											<th>Thao Tác</th>
										</tr>
									</thead>
									<tbody id="image-preview-body">
										<c:forEach items="${tempImages}" var="img">
											<tr>
												<td><img
													src="${pageContext.request.contextPath}/image?dir=tmp&fname=${img}"
													width="200" height="150"></td>
												<td><input type="hidden" name="tempImages"
													value="${img}">
													<button type="button"
														class="btn btn-outline-danger btn-sm remove-temp-img">Xóa</button>
												</td>
											</tr>
										</c:forEach>
									</tbody>

									<%-- <tbody id="image-preview-body">
										<!-- Ảnh cũ -->
										<c:forEach items="${productsDetailModel.productImages}"
											var="img">
											<tr>
												<td><img height="150" width="200"
													src="${pageContext.request.contextPath}/image?fname=${img.imageUrl}" />
													<input type="hidden" name="existingImageNames"
													value="${img.imageUrl}"></td>
												<td><a
													href="<c:url value='/admin/Catalog/Products/image/delete?imageId=${img.id}'/>"
													class="btn btn-outline-danger btn-sm">Xóa</a></td>
											</tr>
										</c:forEach>
									</tbody> --%>
								</table>
							</div>

							<div class="card-footer">
								<label for="productImageInput" class="form-label">Tải
									lên ảnh mới:</label> <input class="form-control" type="file"
									name="productImages" id="productImageInput" multiple>
							</div>
						</div>
					</div>

					<!-- Thông số kỹ thuật -->
					<div class="mb-3">
						<label class="form-label">Thông Số Kỹ Thuật:</label>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-striped align-middle text-center">
									<thead class="table-dark">
										<tr>
											<th>Thuộc tính</th>
											<th>Giá trị</th>
											<th>Thao tác</th>
										</tr>
									</thead>
									<tbody id="attributes-tbody">
										<c:forEach var="attr" items="${productAttributes}"
											varStatus="loop">
											<tr>
												<td><select
													name="newAttributes[${loop.index}].attributeId"
													class="form-select">
														<c:forEach var="a" items="${availableAttributes}">
															<option value="${a.id}"
																<c:if test="${a.id == attr.attributeId}">selected</c:if>>
																${a.name}</option>
														</c:forEach>
												</select></td>
												<td><input type="text"
													name="newAttributes[${loop.index}].value"
													class="form-control" value="${attr.displayValue}" /></td>
											</tr>
										</c:forEach>
									</tbody>

								</table>
							</div>
						</div>
						<div class="card-footer text-right">
							<button type="button" class="btn btn-outline-success"
								id="add-attribute-btn">
								<i class="bi bi-plus-circle"></i> Thêm thông số
							</button>
						</div>
					</div>

					<!-- Biến thể -->
					<div class="mb-3">
						<label class="form-label">Chi Tiết Biến Thể Sản Phẩm:</label>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-striped align-middle text-center">
									<thead class="table-dark">
										<tr>
											<th>SKU</th>
											<th>Giá</th>
											<th>Trạng Thái</th>
											<th>Tùy chọn</th>
											<th>Thao Tác</th>
										</tr>
									</thead>
									<tbody id="variants-tbody">
										<c:forEach var="v" items="${variantList}"></c:forEach>
									</tbody>
								</table>
							</div>
						</div>
						<div class="card-footer text-right">
							<button type="button" class="btn btn-outline-success"
								id="add-variant-btn">
								<i class="bi bi-plus-circle"></i> Thêm Biến Thể
							</button>
						</div>
					</div>

					<!-- Submit -->
					<div class="mt-3">
						<button type="submit" class="btn btn-primary">
							<i class="bi bi-save"></i> Save
						</button>
						<a
							href="${pageContext.request.contextPath}/admin/Catalog/Products/searchpaginated"
							class="btn btn-secondary"><i class="bi bi-x-circle"></i>
							Cancel</a>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>

<!-- ========== SCRIPT ========= -->

<script>
    // ====== Tạo slug ======
    function toSlug(str) {
        str = str.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
        str = str.replace(/[^a-z0-9\s-]/g, '').trim().replace(/\s+/g, '-');
        return str.replace(/^-+|-+$/g, '');
    }
    document.getElementById('name').addEventListener('input', function () {
        document.getElementById('slug').value = toSlug(this.value);
    });
</script>

<!-- ====== Dữ liệu JS từ server ====== -->
<script>
    const availableAttributes = [
        <c:forEach var="attr" items="${availableAttributes}" varStatus="status">
            { id: ${attr.id}, name: "${attr.name}" }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const availableOptionTypes = [
        <c:forEach var="optionType" items="${availableOptionTypes}" varStatus="typeStatus">
            {
                id: ${optionType.id},
                code: "${optionType.code}",
                values: [
                    <c:forEach var="optionValue" items="${optionType.values}" varStatus="valueStatus">
                        { id: ${optionValue.id}, value: "${optionValue.value}" }<c:if test="${!valueStatus.last}">,</c:if>
                    </c:forEach>
                ]
            }<c:if test="${!typeStatus.last}">,</c:if>
        </c:forEach>
    ];
</script>

<!-- ====== Hiển thị ảnh preview + upload tạm + xóa ảnh ====== -->
<script>
document.addEventListener("DOMContentLoaded", function () {
    const contextPath = "${pageContext.request.contextPath}";
    const imageInput = document.getElementById("productImageInput");
    const previewBody = document.getElementById("image-preview-body");

    if (!imageInput || !previewBody) {
        console.error("[JS] Không tìm thấy input hoặc tbody preview!");
        return;
    }

    imageInput.addEventListener("change", function (e) {
        const files = e.target.files;
        for (const file of files) {
            const formData = new FormData();
            formData.append("file", file);

            fetch(contextPath + "/admin/Catalog/Products/image/tmpUpload", {
                method: "POST",
                body: formData
            })
            .then(res => res.text())
            .then(fileName => {
                fileName = fileName.trim();
                console.log("[JS] fileName sau trim:", fileName);

                if (!fileName || fileName === "no-file") {
                    alert("Không thể upload ảnh.");
                    return;
                }

                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>
                		<img src="\${contextPath}/image?dir=tmp&fname=\${fileName}" 
                    width="200" height="150" class="img-thumbnail">
                        <input type="hidden" name="tempImages" value="${fileName}">
                    </td>
                    <td>
                        <button type="button" class="btn btn-outline-danger btn-sm remove-temp-img">
                            <i class="bi bi-trash"></i> Xóa
                        </button>
                    </td>
                `;
                previewBody.appendChild(tr);
            })
            .catch(err => {
                console.error("[JS] Lỗi upload ảnh:", err);
                alert("Lỗi khi tải ảnh lên server.");
            })
            .finally(() => {
                imageInput.value = "";
            });
        }
    });

    previewBody.addEventListener("click", function (e) {
        const btn = e.target.closest(".remove-temp-img");
        if (btn) btn.closest("tr").remove();
    });
});
</script>


<!-- ====== Thêm thông số kỹ thuật ====== -->
<script>
document.addEventListener('DOMContentLoaded', function () {
    let attributeIndex = 0;
    const addAttributeBtn = document.getElementById('add-attribute-btn');
    const attributesTbody = document.getElementById('attributes-tbody');

    addAttributeBtn.addEventListener('click', function () {
        const tr = document.createElement('tr');

        const tdName = document.createElement('td');
        const select = document.createElement('select');
        select.className = 'form-select';
        select.name = `newAttributes[${attributeIndex}].attributeId`;
        select.required = true;

        const defaultOpt = document.createElement('option');
        defaultOpt.textContent = '-- Chọn thuộc tính --';
        defaultOpt.value = '';
        select.appendChild(defaultOpt);

        availableAttributes.forEach(a => {
            const opt = document.createElement('option');
            opt.value = a.id;
            opt.textContent = a.name;
            select.appendChild(opt);
        });
        tdName.appendChild(select);

        const tdVal = document.createElement('td');
        const input = document.createElement('input');
        input.type = 'text'; input.className = 'form-control';
        input.name = `newAttributes[${attributeIndex}].value`;
        input.required = true;
        tdVal.appendChild(input);

        const tdAction = document.createElement('td');
        const del = document.createElement('button');
        del.type = 'button'; del.className = 'btn btn-outline-danger btn-sm';
        del.innerHTML = '<i class="bi bi-trash"></i>';
        del.onclick = () => tr.remove();
        tdAction.appendChild(del);

        tr.appendChild(tdName);
        tr.appendChild(tdVal);
        tr.appendChild(tdAction);
        attributesTbody.appendChild(tr);
        attributeIndex++;
    });
});
</script>

<!-- ====== Thêm biến thể ====== -->
<script>
document.addEventListener('DOMContentLoaded', function () {
    let variantIndex = document.querySelectorAll('#variants-tbody tr').length;
    const addVariantBtn = document.getElementById('add-variant-btn');
    const variantsTbody = document.getElementById('variants-tbody');

    addVariantBtn.addEventListener('click', function () {
        const tr = document.createElement('tr');

        // SKU
        const skuTd = document.createElement('td');
        skuTd.innerHTML = `<input type="text" name="newVariants.sku" class="form-control form-control-sm" placeholder="SKU">`;

        // Giá
        const priceTd = document.createElement('td');
        priceTd.innerHTML = `<input type="number" name="newVariants.price" class="form-control form-control-sm" min="1000" required>`;

        // Trạng thái
        const statusTd = document.createElement('td');
        statusTd.innerHTML = `<select name="newVariants.status" class="form-select form-select-sm">
            <option value="true" selected>Đang bán</option>
            <option value="false">Ngừng bán</option>
        </select>`;

        // Tùy chọn
        const optionsTd = document.createElement('td');
        availableOptionTypes.forEach(group => {
            const div = document.createElement('div');
            const sel = document.createElement('select');
            sel.className = 'form-select form-select-sm';
            sel.name = `newVariants.optionValueIds[]`;
            const def = document.createElement('option');
            def.textContent = `-- Chọn ${group.code} --`;
            def.value = '';
            sel.appendChild(def);
            group.values.forEach(v => {
                const o = document.createElement('option');
                o.value = v.id;
                o.textContent = v.value;
                sel.appendChild(o);
            });
            div.appendChild(sel);
            optionsTd.appendChild(div);
        });

        // Xóa
        const actionTd = document.createElement('td');
        const del = document.createElement('button');
        del.type = 'button';
        del.className = 'btn btn-outline-danger btn-sm';
        del.innerHTML = '<i class="bi bi-trash"></i>';
        del.onclick = () => tr.remove();
        actionTd.appendChild(del);

        tr.appendChild(skuTd);
        tr.appendChild(priceTd);
        tr.appendChild(statusTd);
        tr.appendChild(optionsTd);
        tr.appendChild(actionTd);
        variantsTbody.appendChild(tr);

        variantIndex++;
    });
});
</script>


