<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<section class="row">
	<c:if test="${not empty error}">
		<div class="alert alert-danger">${error}</div>
	</c:if>

	<div class="col-12 mt-4">
		<form id="productForm"
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
							id="categorySelect" name="categoryId" class="form-select"
							required>
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
														class="form-control" value="${attr.valueNumber}" step="0.01" />
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
					</div>

					<!-- Biến thể -->
					<div class="mb-3">
						<label class="form-label">Biến Thể Sản Phẩm:</label>

						<!-- Biến thể đã có -->
						<h6 class="mt-3">Biến thể hiện tại:</h6>
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
									<c:choose>
										<c:when test="${v.id > 0}">
											<!-- Existing variant -->
											<tr data-variant-id="${v.id}">
												<td><input type="hidden" name="existingVariants.id"
													value="${v.id}" /> <input type="text"
													name="existingVariants.sku" value="${v.sku}"
													class="form-control form-control-sm" /></td>

												<td><input type="number" step="1"
													name="existingVariants.price" value="${v.price.intValue()}"
													min="1000" class="form-control form-control-sm" /></td>

												<td><select name="existingVariants.status"
													class="form-select form-select-sm">
														<option value="true" ${v.status ? 'selected' : ''}>Đang bán</option>
														<option value="false" ${!v.status ? 'selected' : ''}>Ngừng bán</option>
												</select></td>

												<td><c:forEach var="opt" items="${v.options}">
														<span class="badge bg-secondary">${opt}</span>
														<br />
													</c:forEach> 
													<c:forEach var="optVId" items="${v.optionValueIds}">
														<input type="hidden"
															name="existingVariants.optionValueIds[]"
															value="${optVId}" />						
													</c:forEach></td>

												<td>
													<button type="button"
														class="btn btn-outline-danger btn-sm remove-variant">
														<i class="bi bi-trash"></i> Xóa
													</button>
												</td>
											</tr>
										</c:when>
										<c:otherwise>
											<!-- New variant (from validation error) -->
											<tr>
												<td><input type="text"
													name="newVariants.sku" value="${v.sku}"
													class="form-control form-control-sm" /></td>

												<td><input type="number" step="1"
													name="newVariants.price" value="${v.price.intValue()}"
													min="1000" class="form-control form-control-sm" /></td>

												<td><select name="newVariants.status"
													class="form-select form-select-sm">
														<option value="true" ${v.status ? 'selected' : ''}>Đang bán</option>
														<option value="false" ${!v.status ? 'selected' : ''}>Ngừng bán</option>
												</select></td>

												<td><c:forEach var="opt" items="${v.options}">
														<span class="badge bg-success">${opt}</span>
														<br />
													</c:forEach> 
													<c:forEach var="optVId" items="${v.optionValueIds}">
														<input type="hidden"
															name="newVariants.optionValueIds[]"
															value="${optVId}" />						
													</c:forEach></td>

												<td>
													<span class="badge bg-info">Mới</span>
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</tbody>
						</table>

						<!-- Thêm biến thể mới -->
						<h6 class="mt-4">Thêm biến thể mới:</h6>
						<div class="card p-3">
							<div class="mb-3">
								<label class="form-label">Loại tùy chọn</label>
								<div id="optionTypeContainer">
									<c:forEach var="type" items="${availableOptionTypes}">
										<div class="form-check mb-2">
											<input class="form-check-input option-type-checkbox"
												type="checkbox" id="update_type_${type.id}"
												value="${type.id}" data-name="${type.code}"> <label
												class="form-check-label" for="update_type_${type.id}">
												${type.code} </label>
											<!-- Nơi hiện checkbox giá trị -->
											<div class="option-values-container ms-4 mt-2"></div>
										</div>
									</c:forEach>
								</div>
							</div>
							<div id="variantTableContainer" class="mt-4">
								<p class="text-muted fst-italic">Chọn các tùy chọn để hiển
									thị biến thể...</p>
							</div>
						</div>
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

<!-- ====== Xử lý thay đổi danh mục - Load lại thông số kỹ thuật ====== -->
<script>
document.addEventListener('DOMContentLoaded', function() {
    const categorySelect = document.getElementById('categorySelect');
    const attributesTbody = document.getElementById('attributes-tbody');
    const contextPath = "${pageContext.request.contextPath}";
    
    // Lưu trữ giá trị hiện tại của các attributes
    function getCurrentAttributeValues() {
        const values = {};
        const rows = attributesTbody.querySelectorAll('tr');
        
        rows.forEach(row => {
            const hiddenInput = row.querySelector('input[name="existingAttributes.attributeId"]');
            const valueInput = row.querySelector('input[name="existingAttributes.value"], select[name="existingAttributes.value"]');
            
            if (hiddenInput && valueInput) {
                const attrId = hiddenInput.value;
                const value = valueInput.value;
                if (value && value !== '') {
                    values[attrId] = value;
                }
            }
        });
        
        return values;
    }
    
    // Load attributes khi thay đổi category
    categorySelect.addEventListener('change', function() {
        const categoryId = this.value;
        
        if (!categoryId) {
            return;
        }
        
        // Lưu giá trị hiện tại trước khi reload
        const currentValues = getCurrentAttributeValues();
        console.log('Current attribute values:', currentValues);
        
        // Gọi API để lấy attributes theo category mới
        fetch(contextPath + '/admin/Catalog/Products/loadAttributes?categoryId=' + categoryId)
            .then(response => response.text())
            .then(html => {
                // Parse HTML response để lấy danh sách attributes
                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = html;
                
                // Xóa attributes cũ
                attributesTbody.innerHTML = '';
                
                // Lấy tất cả các attribute từ response
                const attributeDivs = tempDiv.querySelectorAll('div.mb-3');
                
                if (attributeDivs.length === 0) {
                    // Không có attributes cho category này
                    const tr = document.createElement('tr');
                    tr.innerHTML = '<td colspan="3" class="text-muted">Không có thuộc tính nào cho danh mục này.</td>';
                    attributesTbody.appendChild(tr);
                    return;
                }
                
                // Duyệt qua các attributes và tạo table rows
                attributeDivs.forEach(attrDiv => {
                    const hiddenInput = attrDiv.querySelector('input[type="hidden"][name="attributeIds"]');
                    const label = attrDiv.querySelector('label');
                    const valueInput = attrDiv.querySelector('input[name="attributeValues"], select[name="attributeValues"]');
                    
                    if (hiddenInput && label && valueInput) {
                        const attrId = hiddenInput.value;
                        const attrName = label.textContent.trim();
                        
                        // Tạo row mới
                        const tr = document.createElement('tr');
                        
                        // Column 1: Attribute name
                        const tdName = document.createElement('td');
                        const hiddenAttrInput = document.createElement('input');
                        hiddenAttrInput.type = 'hidden';
                        hiddenAttrInput.name = 'existingAttributes.attributeId';
                        hiddenAttrInput.value = attrId;
                        
                        const selectAttr = document.createElement('select');
                        selectAttr.name = 'existingAttributes.attributeId';
                        selectAttr.className = 'form-select';
                        selectAttr.disabled = true;
                        
                        const option = document.createElement('option');
                        option.value = attrId;
                        option.selected = true;
                        option.textContent = attrName;
                        
                        selectAttr.appendChild(option);
                        tdName.appendChild(hiddenAttrInput);
                        tdName.appendChild(selectAttr);
                        
                        // Column 2: Attribute value
                        const tdValue = document.createElement('td');
                        const clonedInput = valueInput.cloneNode(true);
                        clonedInput.name = 'existingAttributes.value';
                        
                        // Nếu có giá trị cũ, restore nó
                        if (currentValues[attrId]) {
                            clonedInput.value = currentValues[attrId];
                        }
                        
                        tdValue.appendChild(clonedInput);
                        
                        // Column 3: Action
                        const tdAction = document.createElement('td');
                        const deleteBtn = document.createElement('button');
                        deleteBtn.type = 'button';
                        deleteBtn.className = 'btn btn-outline-danger btn-sm remove-attribute';
                        deleteBtn.innerHTML = '<i class="bi bi-trash"></i> Xóa';
                        tdAction.appendChild(deleteBtn);
                        
                        tr.appendChild(tdName);
                        tr.appendChild(tdValue);
                        tr.appendChild(tdAction);
                        attributesTbody.appendChild(tr);
                    }
                });
            })
            .catch(err => {
                console.error('Lỗi khi tải attributes:', err);
                alert('Có lỗi xảy ra khi tải thuộc tính!');
            });
    });
});
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

<!-- ====== Thêm biến thể bằng checkbox ====== -->
<script>
document.addEventListener("DOMContentLoaded", function () {
  const ctx = "${pageContext.request.contextPath}";

  // Khi chọn loại tùy chọn
  document.addEventListener("change", async function (e) {
    if (e.target.classList.contains("option-type-checkbox")) {
      const checkbox = e.target;
      const container = checkbox.closest(".form-check").querySelector(".option-values-container");

      if (checkbox.checked) {
        // Gọi API lấy danh sách OptionValue
        const res = await fetch(ctx + "/admin/Catalog/Products/loadOptionValues?typeId=" + checkbox.value);
        const values = await res.json();
        
        console.log("Option values JSON:", values);

        // Hiển thị các giá trị dưới dạng checkbox
        let html = '<div class="border rounded p-2 bg-light">';
        html += '<label class="form-label small mb-2 d-block">Chọn giá trị cho ' + checkbox.dataset.name + '</label>';
        
        values.forEach(v => {
          html += '<div class="form-check form-check-inline">';
          html += '<input class="form-check-input option-value-checkbox" type="checkbox" ';
          html += 'value="' + v.id + '" data-type-id="' + checkbox.value + '" data-value="' + v.value + '">';
          html += '<label class="form-check-label">' + v.value + '</label>';
          html += '</div>';
        });
        
        html += "</div>";
        container.innerHTML = html;
      } else {
        container.innerHTML = "";
        generateVariantTable(); // cập nhật lại bảng
      }
    }

    // Khi chọn giá trị tùy chọn
    if (e.target.classList.contains("option-value-checkbox")) {
      generateVariantTable();
    }
  });

  // Hàm sinh bảng các biến thể
  function generateVariantTable() {
    const typeMap = {};
    const typeIdMap = {}; // Map để lưu typeId
    const valueIdMap = {}; // Map để lưu valueId của mỗi option

    // Gom dữ liệu từ checkbox
    document.querySelectorAll(".option-type-checkbox:checked").forEach(type => {
      const typeId = type.value;
      const typeName = type.dataset.name;
      
      const valueCheckboxes = document.querySelectorAll('.option-value-checkbox[data-type-id="' + typeId + '"]:checked');
      const values = Array.from(valueCheckboxes).map(v => v.dataset.value);
      const valueIds = Array.from(valueCheckboxes).map(v => v.value);

      if (values.length > 0) {
        typeMap[typeName] = values;
        typeIdMap[typeName] = typeId;
        valueIdMap[typeName] = valueIds;
      }
    });

    const variantContainer = document.getElementById("variantTableContainer");
    if (Object.keys(typeMap).length === 0) {
      variantContainer.innerHTML = "<p class='text-muted fst-italic'>Chưa có biến thể nào được chọn.</p>";
      return;
    }

    const typeNames = Object.keys(typeMap);
    const combinations = cartesian(Object.values(typeMap));
    const valueCombinations = cartesian(Object.values(valueIdMap)); // Tạo tổ hợp của value IDs
    
    console.log("typeMap:", typeMap);
    console.log("valueIdMap:", valueIdMap);
    console.log("combinations:", combinations);
    console.log("valueCombinations:", valueCombinations);

    // Sinh bảng
    let html = '<table class="table table-bordered align-middle"><thead class="table-dark"><tr>';
    typeNames.forEach(n => {
      html += '<th>' + n + '</th>';
    });
    html += '<th>SKU</th><th>GIÁ</th><th>TRẠNG THÁI</th></tr></thead><tbody>';

    combinations.forEach((combo, index) => {
      html += "<tr>";
      combo.forEach(val => html += '<td>' + val + '</td>');
      
      // Thêm các option value IDs dưới dạng hidden inputs
      let optionValueIdsHtml = '';
      if (valueCombinations[index]) {
        const valueIds = Array.isArray(valueCombinations[index]) ? valueCombinations[index] : [valueCombinations[index]];
        valueIds.forEach(vid => {
          optionValueIdsHtml += '<input type="hidden" name="newVariants.optionValueIds[]" value="' + vid + '">';
        });
      }
      
      html += '<td>';
      html += '<input type="text" name="newVariants.sku" class="form-control form-control-sm" required>';
      html += '</td>';
      html += '<td>';
      html += '<input type="number" name="newVariants.price" class="form-control form-control-sm" min="0" step="1" required>';
      html += '</td>';
      html += '<td>';
      html += '<select name="newVariants.status" class="form-select form-select-sm">';
      html += '<option value="true">Hoạt động</option>';
      html += '<option value="false">Ngừng</option>';
      html += '</select>';
      html += optionValueIdsHtml;
      html += '</td>';
      html += '</tr>';
    });
    html += "</tbody></table>";

    variantContainer.innerHTML = html;
  }

  // Cartesian product (tổ hợp tất cả biến thể)
  function cartesian(arr) {
	  if (arr.length === 0) return [];
	  if (arr.length === 1) return arr[0].map(v => [v]); //xử lý trường hợp chỉ có 1 option type
	  return arr.reduce((a, b) =>
	    a.flatMap(d => b.map(e => [d, e].flat()))
	  );
	}
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
