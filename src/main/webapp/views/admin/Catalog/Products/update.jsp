<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-12 mt-4">
		<form id ="productForm"
			action="${pageContext.request.contextPath}/admin/Catalog/Products/saveOrUpdate"
			method="POST" enctype="multipart/form-data">

			<div class="card">
				<div class="card-header">
					<h2>Sửa Sản Phẩm</h2>
				</div>

				<div class="card-body">
					<input type="hidden" name="id" value="${product.id}" />

					<!-- Tên sản phẩm -->
					<div class="mb-3">
						<label class="form-label">Tên Sản Phẩm:</label> <input type="text"
							id="name" name="name" class="form-control"
							value="${product.name}" required />
					</div>

					<!-- Slug -->
					<div class="mb-3">
						<label class="form-label">Slug:</label> <input type="text"
							id="slug" name="slug" class="form-control"
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
							name="basePrice" class="form-control" min="1000"
							value="${product.basePrice.intValue()}" required />
					</div>

					<!-- Danh mục -->
					<div class="mb-3">
						<label class="form-label">Danh Mục:</label> <select
							name="categoryId" class="form-select" required>
							<option value="">-- Chọn danh mục --</option>
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
							name="status" class="form-select">
							<option value="true"
								<c:if test="${product.status}">selected</c:if>>Đang Bán</option>
							<option value="false"
								<c:if test="${!product.status}">selected</c:if>>Ngừng
								Bán</option>
						</select>
					</div>

					<!-- Ảnh sản phẩm -->
					<div class="mb-3">
						<label class="form-label">Hình Ảnh Sản Phẩm:</label>
						<div class="table-responsive">
							<table class="table table-striped align-middle text-center">
								<thead class="table-dark">
									<tr>
										<th>Ảnh</th>
										<th>Thao Tác</th>
									</tr>
								</thead>
								<tbody id="image-preview-body">
									<c:forEach var="img"
										items="${productsDetailModel.productImages}">
										<tr>
											<td><img
												src="${pageContext.request.contextPath}/image?fname=${img.imageUrl}"
												width="200" height="150" class="img-thumbnail"> <input
												type="hidden" name="existingImages" value="${img.imageUrl}">
											</td>
											<td>
												<button type="button"
													class="btn btn-outline-danger btn-sm remove-old-img">
													Xóa</button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

						<div class="mt-2">
							<label class="form-label">Thêm ảnh mới:</label> <input
								class="form-control" type="file" name="productImages"
								id="productImageInput" multiple>
						</div>
					</div>

					<!-- Thông số kỹ thuật -->
					<div class="mb-3">
						<label class="form-label">Thông Số Kỹ Thuật:</label>
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
										<td><input type="hidden"
											name="existingAttributes.attributeId"
											value="${attr.attributeId}" /> <select
											name="existingAttributes.attributeId" class="form-select"
											disabled>
												<c:forEach var="a" items="${availableAttributes}">
													<option value="${a.id}"
														<c:if test="${a.id == attr.attributeId}">selected</c:if>>
														${a.name}</option>
												</c:forEach>
										</select></td>
										<td><c:choose>
												<c:when test="${attr.dataType == 3}">
													<!-- Boolean: Combobox -->
													<select name="existingAttributes.value" class="form-select">
														<option value="">-- Chọn --</option>
														<option value="true"
															<c:if test="${attr.valueText == 'true' || attr.valueText == 'Có'}">selected</c:if>>Có</option>
														<option value="false"
															<c:if test="${attr.valueText == 'false' || attr.valueText == 'Không'}">selected</c:if>>Không</option>
													</select>
												</c:when>
												<c:when test="${attr.dataType == 2}">
													<!-- Number -->
													<input type="number" name="existingAttributes.value"
														class="form-control" value="${attr.valueText}" step="0.01" />
												</c:when>
												<c:otherwise>
													<!-- Text (default) -->
													<input type="text" name="existingAttributes.value"
														class="form-control" value="${attr.valueText}" />
												</c:otherwise>
											</c:choose></td>
										<td>
											<button type="button"
												class="btn btn-outline-danger btn-sm remove-attribute">
												<i class="bi bi-trash"></i> Xóa
											</button>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<button type="button" class="btn btn-outline-success"
							id="add-attribute-btn">
							<i class="bi bi-plus-circle"></i> Thêm thông số
						</button>
					</div>

					<!-- Biến thể -->
					<div class="mb-3">
						<label class="form-label">Biến Thể Sản Phẩm:</label>
						<table class="table table-striped align-middle text-center">
							<thead class="table-dark">
								<tr>
									<th>SKU</th>
									<th>Giá</th>
									<th>Trạng thái</th>
									<th>Tùy chọn</th>
									<th>Thao tác</th>
								</tr>
							</thead>
							<tbody id="variants-tbody">
								<c:forEach var="v" items="${variantList}" varStatus="loop">
									<tr data-variant-id="${v.id}">
										<!-- SKU -->
										<td><input type="hidden" name="existingVariants.id"
											value="${v.id}" /> <input type="text"
											name="existingVariants.sku" value="${v.SKU}"
											class="form-control form-control-sm" /></td>

										<!-- Giá -->
										<td><input type="number" name="existingVariants.price"
											value="${v.price}" min="1000"
											class="form-control form-control-sm" /></td>

										<!-- Trạng thái -->
										<td><select name="existingVariants.status"
											class="form-select form-select-sm">
												<option value="true" ${v.status ? 'selected' : ''}>Đang
													bán</option>
												<option value="false" ${!v.status ? 'selected' : ''}>Ngừng
													bán</option>
										</select></td>

										<!-- Tùy chọn -->
										<td><c:forEach var="opt" items="${v.options}">
												<span class="badge bg-secondary">${opt}</span>
												<br />
											</c:forEach></td>

										<!-- Thao tác -->
										<td>
											<button type="button"
												class="btn btn-outline-danger btn-sm remove-variant">
												<i class="bi bi-trash"></i> Xóa
											</button>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<button type="button" class="btn btn-outline-success"
							id="add-variant-btn">
							<i class="bi bi-plus-circle"></i> Thêm Biến Thể
						</button>
					</div>

					<!-- Submit -->
					<div class="mt-3">
						<button type="submit" class="btn btn-primary">
							<i class="bi bi-save"></i> Lưu
						</button>
						<a
							href="${pageContext.request.contextPath}/admin/Catalog/Products/searchpaginated"
							class="btn btn-secondary"><i class="bi bi-x-circle"></i> Hủy</a>
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
            { id: ${attr.id}, name: "${attr.name}", dataType: ${attr.dataType} }<c:if test="${!status.last}">,</c:if>
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
                        <input type="hidden" name="tempImages" value="\${fileName}"
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

<!-- xóa ảnh cũ khỏi giao diện -->
<script>
document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#image-preview-body").addEventListener("click", (e) => {
        const btn = e.target.closest(".remove-old-img");
        if (btn) {
            btn.closest("tr").remove();
        }
    });
});
</script>

<!-- Khởi tạo biến toàn cục để lưu các variant đã xóa -->
<script>
let deletedVariantIds = [];
</script>

<!-- xóa biến thể cũ khỏi giao diện -->
<script>
document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#variants-tbody").addEventListener("click", (e) => {
        const btn = e.target.closest(".remove-variant");
        if (btn) {
            const tr = btn.closest("tr");
            const variantId = tr.getAttribute("data-variant-id");
            
            // Nếu là biến thể đã có trong DB, lưu lại để xóa sau
            if (variantId) {
                deletedVariantIds.push(variantId);
                console.log("Đã đánh dấu xóa variant ID:", variantId);
            }
            
            tr.remove();
        }
    });
});
</script>

<!-- xóa thông số cũ khỏi giao diện -->
<script>
document.addEventListener("DOMContentLoaded", () => {
    document.querySelector("#attributes-tbody").addEventListener("click", (e) => {
        const btn = e.target.closest(".remove-attribute");
        if (btn) {
            btn.closest("tr").remove();
        }
    });
});
</script>


<!-- ====== Thêm thông số kỹ thuật ====== -->
<script>
document.addEventListener('DOMContentLoaded', function () {
    const addAttributeBtn = document.getElementById('add-attribute-btn');
    const attributesTbody = document.getElementById('attributes-tbody');

    addAttributeBtn.addEventListener('click', function () {
        const tr = document.createElement('tr');

        const tdName = document.createElement('td');
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';
        hiddenInput.name = 'attributeIds';
        
        const select = document.createElement('select');
        select.className = 'form-select';
        select.required = true;

        const defaultOpt = document.createElement('option');
        defaultOpt.textContent = '-- Chọn thuộc tính --';
        defaultOpt.value = '';
        select.appendChild(defaultOpt);

        availableAttributes.forEach(a => {
            const opt = document.createElement('option');
            opt.value = a.id;
            opt.textContent = a.name;
            opt.dataset.dataType = a.dataType; // Lưu dataType vào option
            select.appendChild(opt);
        });
        
        const tdVal = document.createElement('td');
        
        // Function để render input dựa trên dataType
        function renderValueInput(dataType) {
            tdVal.innerHTML = ''; // Clear old input
            
            if (dataType == 3) {
                // Boolean: Combobox
                const selectBool = document.createElement('select');
                selectBool.name = 'attributeValues';
                selectBool.className = 'form-select';
                selectBool.innerHTML = `
                    <option value="">-- Chọn --</option>
                    <option value="true">Có</option>
                    <option value="false">Không</option>
                `;
                tdVal.appendChild(selectBool);
            } else if (dataType == 2) {
                // Number
                const inputNum = document.createElement('input');
                inputNum.type = 'number';
                inputNum.name = 'attributeValues';
                inputNum.className = 'form-control';
                inputNum.placeholder = 'Nhập giá trị số';
                inputNum.step = '0.01';
                tdVal.appendChild(inputNum);
            } else {
                // Text (default)
                const inputText = document.createElement('input');
                inputText.type = 'text';
                inputText.name = 'attributeValues';
                inputText.className = 'form-control';
                inputText.placeholder = 'Nhập giá trị';
                tdVal.appendChild(inputText);
            }
        }
        
        // Sync hidden input và render input khi chọn attribute
        select.addEventListener('change', function() {
            hiddenInput.value = this.value;
            const selectedOption = this.options[this.selectedIndex];
            const dataType = selectedOption.dataset.dataType;
            renderValueInput(dataType);
        });
        
        tdName.appendChild(hiddenInput);
        tdName.appendChild(select);

        // Khởi tạo với text input mặc định
        renderValueInput(1);

        const tdAction = document.createElement('td');
        const del = document.createElement('button');
        del.type = 'button'; 
        del.className = 'btn btn-outline-danger btn-sm';
        del.innerHTML = '<i class="bi bi-trash"></i>';
        del.onclick = () => tr.remove();
        tdAction.appendChild(del);

        tr.appendChild(tdName);
        tr.appendChild(tdVal);
        tr.appendChild(tdAction);
        attributesTbody.appendChild(tr);
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

        const skuTd = document.createElement('td');
        skuTd.innerHTML = `<input type="text" name="newVariants.sku" class="form-control form-control-sm" placeholder="SKU">`;

        const priceTd = document.createElement('td');
        priceTd.innerHTML = `<input type="number" name="newVariants.price" class="form-control form-control-sm" min="1000" required>`;

        const statusTd = document.createElement('td');
        statusTd.innerHTML = `<select name="newVariants.status" class="form-select form-select-sm">
            <option value="true" selected>Đang bán</option>
            <option value="false">Ngừng bán</option>
        </select>`;

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

        //Xóa biến thể
        const actionTd = document.createElement('td');
        const del = document.createElement('button');
        del.type = 'button';
        del.className = 'btn btn-outline-danger btn-sm';
        del.innerHTML = '<i class="bi bi-trash"></i>';
        del.onclick = () => {
            // Biến thể mới được tạo trong form không cần track xóa
            tr.remove();
        };
        
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

<script>
document.getElementById("productForm").addEventListener("submit", function (e) {
    if (deletedVariantIds.length > 0) {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "deletedVariantIds";
        input.value = deletedVariantIds.join(","); // ví dụ: "3,5,7"
        this.appendChild(input);
    }
});
</script>
