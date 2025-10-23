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
			id="productForm" method="POST" enctype="multipart/form-data">
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
						<label class="form-label fw-bold">Tên Sản Phẩm:</label> <input
							type="text" id="name" name="name" class="form-control"
							value="${product.name}" placeholder="Nhập tên sản phẩm" />
					</div>

					<!-- Slug -->
					<div class="mb-3">
						<label class="form-label fw-bold">Slug (tự động tạo):</label> <input
							type="text" id="slug" name="slug" class="form-control"
							value="${product.slug}" readonly />
					</div>

					<!-- Mô tả -->
					<div class="mb-3">
						<label class="form-label fw-bold">Mô Tả:</label>
						<textarea name="description" class="form-control" rows="3">${product.description}</textarea>
					</div>

					<!-- Giá gốc -->
					<div class="mb-3">
						<label class="form-label fw-bold">Giá Gốc:</label> <input
							type="number" name="basePrice" min="1000" max="9999999999"
							class="form-control" value="${product.basePrice.intValue()}"
							required />
					</div>

					<!-- Danh mục -->
					<div class="mb-3">
						<label class="form-label fw-bold">Danh Mục:</label> <select
							name="categoryId" id="categorySelect" class="form-select"
							required>
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
						<label class="form-label fw-bold">Trạng Thái:</label> <select
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
						<label class="form-label fw-bold">Hình Ảnh Sản Phẩm:</label>
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
													width="200" height="150" class="img-thumbnail"></td>
												<td><input type="hidden" name="tempImages"
													value="${img}">
													<button type="button"
														class="btn btn-outline-danger btn-sm remove-temp-img">Xóa</button>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>

							<div class="card-footer">
								<label for="productImageInput" class="form-label">Tải
									lên ảnh mới:</label> <input class="form-control" type="file"
									name="productImages" id="productImageInput" multiple>
							</div>
						</div>
					</div>

					<div class="mb-3">
						<label class="form-label fw-bold">Thông Số Kỹ Thuật:</label>
						<div class="card p-3">
							<div id="attributeContainer">
								<c:choose>
									<c:when test="${not empty categoryAttributes}">
										<!-- Hiển thị attributes với giá trị đã nhập khi có lỗi validation -->
										<c:forEach var="attr" items="${categoryAttributes}">
											<div class="mb-3">
												<label class="form-label fw-bold"> ${attr.name} <c:if
														test="${not empty attr.unit}"> (${attr.unit})</c:if>
												</label> <input type="hidden" name="attributeIds" value="${attr.id}" />

												<c:set var="attrValue" value="" />
												<c:forEach var="pAttr" items="${productAttributes}">
													<c:if test="${pAttr.attributeId == attr.id}">
														<c:set var="attrValue" value="${pAttr.valueText}" />
													</c:if>
												</c:forEach>

												<c:choose>
													<c:when test="${attr.dataType == 2}">
														<input type="number" name="attributeValues"
															class="form-control" placeholder="Nhập giá trị số"
															step="0.01" value="${attrValue}" />
													</c:when>
													<c:when test="${attr.dataType == 3}">
														<select name="attributeValues" class="form-select">
															<option value="">-- Chọn --</option>
															<option value="true"
																${attrValue == 'true' ? 'selected' : ''}>Có</option>
															<option value="false"
																${attrValue == 'false' ? 'selected' : ''}>Không</option>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="attributeValues"
															class="form-control" placeholder="Nhập giá trị"
															value="${attrValue}" />
													</c:otherwise>
												</c:choose>
											</div>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<p class="text-muted mb-0">Vui lòng chọn danh mục để hiển
											thị thuộc tính.</p>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
						<small class="text-muted fst-italic">* Các thuộc tính sẽ
							được tải tự động khi chọn danh mục</small>
					</div>

					<div class="mb-3">
						<label class="form-label fw-bold">Chi Tiết Biến Thể Sản
							Phẩm:</label>
						<div class="card p-3">
							<div class="mb-3">
								<label class="form-label ">Loại tùy chọn</label>
								<div id="optionTypeContainer">
									<c:forEach var="type" items="${optionTypes}">
										<div class="form-check mb-2">
											<input class="form-check-input option-type-checkbox"
												type="checkbox" id="type_${type.id}" value="${type.id}"
												data-name="${type.code}"> <label
												class="form-check-label" for="type_${type.id}">
												${type.code} </label>

											<!-- Nơi hiện checkbox giá trị -->
											<div class="option-values-container ms-4 mt-2"></div>
										</div>
									</c:forEach>
								</div>
							</div>
							<div id="variantTableContainer" class="mt-4">
								<c:choose>
									<c:when test="${not empty variantList}">
										<!-- Hiển thị variants với giá trị đã nhập khi có lỗi validation -->
										<table class="table table-bordered align-middle">
											<thead class="table-dark">
												<tr>
													<!-- Hiển thị cột options động -->
													<c:if test="${not empty variantList[0].options}">
														<c:forEach begin="1" end="${variantList[0].options.size()}" var="idx">
															<th>TÙY CHỌN ${idx}</th>
														</c:forEach>
													</c:if>
													<th>SKU</th>
													<th>GIÁ</th>
													<th>TRẠNG THÁI</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach var="variant" items="${variantList}">
													<tr>
														<!-- Hiển thị giá trị options -->
														<c:forEach var="optionValue" items="${variant.options}">
															<td>${optionValue}</td>
														</c:forEach>
														
														<td><input type="text" name="newVariants.sku"
															class="form-control form-control-sm"
															value="${variant.sku}" required></td>
														<td><input type="number" name="newVariants.price"
															class="form-control form-control-sm"
															value="${variant.price}" min="0" step="0.01" required>
														</td>
														<td>
															<select name="newVariants.status" class="form-select form-select-sm">
																<option value="true" ${variant.status ? 'selected' : ''}>Hoạt động</option>
																<option value="false" ${!variant.status ? 'selected' : ''}>Ngừng</option>
															</select>
															<!-- Hidden inputs để lưu optionValueIds -->
															<c:forEach var="optionId" items="${variant.optionValueIds}">
																<input type="hidden" name="newVariants.optionValueIds[]" value="${optionId}">
															</c:forEach>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</c:when>
									<c:otherwise>
										<p class="text-muted fst-italic">Chọn các tùy chọn để hiển
											thị biến thể...</p>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>

					<!-- Submit -->
					<div class="mt-3">
						<button type="submit" class="btn btn-primary" id="submitBtn">
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

    // ====== Validation form ======
    document.getElementById('productForm').addEventListener('submit', function(e) {
        const name = document.getElementById('name').value.trim();
        const categoryId = document.getElementById('categorySelect').value;
        const basePrice = document.querySelector('input[name="basePrice"]').value;

        if (!name) {
            alert('Vui lòng nhập tên sản phẩm!');
            e.preventDefault();
            return false;
        }

        if (!categoryId) {
            alert('Vui lòng chọn danh mục!');
            e.preventDefault();
            return false;
        }

        if (!basePrice || parseFloat(basePrice) < 1000) {
            alert('Giá gốc phải từ 1,000 VNĐ trở lên!');
            e.preventDefault();
            return false;
        }

        // Kiểm tra biến thể nếu có
        const variantSkus = document.querySelectorAll('input[name="newVariants.sku"]');
        if (variantSkus.length > 0) {
            for (let i = 0; i < variantSkus.length; i++) {
                if (!variantSkus[i].value.trim()) {
                    alert('Vui lòng nhập đầy đủ thông tin cho tất cả các biến thể!');
                    variantSkus[i].focus();
                    e.preventDefault();
                    return false;
                }
            }
        }

        return true;
    });
</script>

<script>
document.getElementById("categorySelect").addEventListener("change", function() {
    const categoryId = this.value;
    const container = document.getElementById("attributeContainer");
    const contextPath = "${pageContext.request.contextPath}";

    if (!categoryId) {
        container.innerHTML = "<p class='text-muted'>Vui lòng chọn danh mục để hiển thị thuộc tính.</p>";
        return;
    }

    fetch(contextPath + "/admin/Catalog/Products/loadAttributes?categoryId=" + categoryId)
        .then(response => {
            if (!response.ok) throw new Error("HTTP error " + response.status);
            return response.text();
        })
        .then(html => {
            container.innerHTML = html;
        })
        .catch(err => {
            console.error(err);
            container.innerHTML = "<p class='text-danger'>Lỗi khi tải thuộc tính!</p>";
        });
});
</script>

<script>
document.addEventListener("DOMContentLoaded", function () {
  const contextPath = "${pageContext.request.contextPath}";

  document.addEventListener("change", function (e) {
    if (e.target.classList.contains("option-type-select")) {
      const optionTypeId = e.target.value;
      const valueSelect = e.target
        .closest(".variant-item")
        .querySelector(".option-value-select");

      if (!optionTypeId) {
        valueSelect.innerHTML =
          "<option value=''>-- Chọn giá trị --</option>";
        return;
      }

      fetch(contextPath + "/admin/Catalog/Products/loadOptionValues?typeId=" + optionTypeId)
        .then((res) => res.json())
        .then((values) => {
          valueSelect.innerHTML = values
            .map((v) => `<option value='${v.id}'>${v.value}</option>`)
            .join("");
        })
        .catch(() => {
          valueSelect.innerHTML =
            "<option value=''>Lỗi tải giá trị!</option>";
        });
    }
  });

  // Thêm biến thể mới
  document.getElementById("addVariantBtn").addEventListener("click", function () {
    const container = document.getElementById("variantContainer");
    const clone = container.firstElementChild.cloneNode(true);
    clone.querySelectorAll("input, select").forEach(el => el.value = "");
    container.appendChild(clone);
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

<!-- Thêm biến thể theo loại tùy chọn -->
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
        let html = `<div class="border rounded p-2 bg-light">
                      <label class="form-label small mb-2 d-block">Chọn giá trị cho ${checkbox.dataset.name}</label>`;
        values.forEach(v => {
          html += `
            <div class="form-check form-check-inline">
              <input class="form-check-input option-value-checkbox" type="checkbox"
                     value="\${v.id}" data-type-id="\${checkbox.value}" data-value="\${v.value}">
              <label class="form-check-label">\${v.value}</label>
            </div>`;
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
    const typeIdMap = {}; // Thêm map để lưu typeId
    const valueIdMap = {}; // Thêm map để lưu valueId của mỗi option

    // Gom dữ liệu từ checkbox
    document.querySelectorAll(".option-type-checkbox:checked").forEach(type => {
      const typeId = type.value;
      const typeName = type.dataset.name;
      
      const valueCheckboxes = document.querySelectorAll(`.option-value-checkbox[data-type-id='\${typeId}']:checked`);
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
    let html ='<table class="table table-bordered align-middle"><thead class="table-dark"><tr>'
    	typeNames.forEach(n => {
            html += `<th>\${n}</th>`;
        });
    html += '<th>SKU</th><th>GIÁ</th><th>TRẠNG THÁI</th></tr></thead><tbody>'

    combinations.forEach((combo, index) => {
      html += "<tr>";
      combo.forEach(val => html += `<td>\${val}</td>`);
      
      // Thêm các option value IDs dưới dạng hidden inputs
      let optionValueIdsHtml = '';
      if (valueCombinations[index]) {
        const valueIds = Array.isArray(valueCombinations[index]) ? valueCombinations[index] : [valueCombinations[index]];
        valueIds.forEach(vid => {
          optionValueIdsHtml += `<input type="hidden" name="newVariants.optionValueIds[]" value="\${vid}">`;
        });
      }
      
      html += `
        <td>
          <input type="text" name="newVariants.sku" class="form-control form-control-sm" required>
        </td>
        <td>
          <input type="number" name="newVariants.price" class="form-control form-control-sm" min="0" step="0.01" required>
        </td>
        <td>
          <select name="newVariants.status" class="form-select form-select-sm">
            <option value="true">Hoạt động</option>
            <option value="false">Ngừng</option>
          </select>
          \${optionValueIdsHtml}
        </td>
      </tr>`;
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
