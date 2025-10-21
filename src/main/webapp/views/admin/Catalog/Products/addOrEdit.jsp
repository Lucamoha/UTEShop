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
													src="${pageContext.request.contextPath}/image?fname=${img}"
													width="200" height="150"></td>
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
								<p class="text-muted mb-0">Vui lòng chọn danh mục để hiển
									thị thuộc tính.</p>
							</div>
						</div>
					</div>

					<%-- <div class="mb-3">
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
					</div> --%>

					<!-- Biến thể -->
					<%-- <div class="mb-3">
						<label class="form-label fw-bold">Chi Tiết Biến Thể Sản
							Phẩm</label>
						<div id="variantContainer">
							<div class="variant-item mb-3 border rounded p-3">
								<div class="row mb-2">
									<div class="col-md-4">
										<label class="form-label">Loại tùy chọn</label> <select
											class="form-select option-type-select">
											<option value="">-- Chọn loại tùy chọn --</option>
											<c:forEach var="type" items="${optionTypes}">
												<option value="${type.id}">${type.code}</option>
											</c:forEach>
										</select>
									</div>

									<div class="col-md-4">
										<label class="form-label">Giá trị tùy chọn</label> <select
											class="form-select option-value-select">
											<option value="">-- Chọn giá trị --</option>
										</select>
									</div>
								</div>

								<div class="row">
									<div class="col-md-4">
										<label class="form-label">SKU</label> <input type="text"
											name="sku" class="form-control" />
									</div>
									<div class="col-md-4">
										<label class="form-label">Giá</label> <input type="number"
											name="price" class="form-control" />
									</div>
									<div class="col-md-4">
										<label class="form-label">Trạng thái</label> <select
											name="status" class="form-select">
											<option value="true">Hoạt động</option>
											<option value="false">Ngừng</option>
										</select>
									</div>
								</div>
							</div>
						</div>

						<button type="button" id="addVariantBtn"
							class="btn btn-outline-primary mt-2">+ Thêm biến thể</button>
					</div> --%>


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
								<p class="text-muted fst-italic">Chọn các tùy chọn để hiển
									thị biến thể...</p>
							</div>
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

    // Gom dữ liệu từ checkbox
    document.querySelectorAll(".option-type-checkbox:checked").forEach(type => {
      const typeId = type.value;
      const typeName = type.dataset.name;
      const values = Array.from(
        document.querySelectorAll(`.option-value-checkbox[data-type-id='\${typeId}']:checked`)
      ).map(v => v.dataset.value);

      if (values.length > 0) {
        typeMap[typeName] = values;
      }
    });

    const variantContainer = document.getElementById("variantTableContainer");
    if (Object.keys(typeMap).length === 0) {
      variantContainer.innerHTML = "<p class='text-muted fst-italic'>Chưa có biến thể nào được chọn.</p>";
      return;
    }

    const combinations = cartesian(Object.values(typeMap));
    const typeNames = Object.keys(typeMap);
    
    console.log("typeMap:", typeMap);
    console.log("typeNames:", Object.keys(typeMap));
    console.log("combinations:", cartesian(Object.values(typeMap)));


    // Sinh bảng
    let html ='<table class="table table-bordered align-middle"><thead class="table-dark"><tr>'
    	typeNames.forEach(n => {
            html += `
    	      <th>\${n}</th>
    	  `;
        });
    html += '<th>SKU</th><th>GIÁ</th><th>TRẠNG THÁI</th></tr></thead><tbody>'
        
       /*  let html ='<table class="table table-bordered align-middle"><thead class="table-dark"><tr>'
        	typeNames.forEach(n => {
                html += `
        	    <c:forEach var="n" items="\${typeNames}">
        	      <th>"\${n}"</th>
        	    </c:forEach>
        	  </tr>
        	</thead>
            <tbody>'; */

    combinations.forEach(combo => {
      html += "<tr>";
      combo.forEach(val => html += `<td>\${val}</td>`);
      html += `
        <td><input type="text" name="sku[]" class="form-control form-control-sm"></td>
        <td><input type="number" name="price[]" class="form-control form-control-sm"></td>
        <td>
          <select name="status[]" class="form-select form-select-sm">
            <option value="true">Hoạt động</option>
            <option value="false">Ngừng</option>
          </select>
        </td>
      </tr>`;
    });
    html += "</tbody></table>";

    variantContainer.innerHTML = html;
  }

  // Cartesian product (tổ hợp tất cả biến thể)
  function cartesian(arr) {
    return arr.reduce((a, b) =>
      a.flatMap(d => b.map(e => [d, e].flat()))
    );
  }
});
</script>
