<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="pr"  value="${requestScope.result}" />

<div class="page-inner">
  <div class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-3">
    <div><h3 class="fw-bold mb-0">Quản lý kho</h3></div>
  </div>

  <!-- ========== BỘ LỌC + SẮP XẾP ========== -->
  <form class="card card-round mb-3" method="get" action="${ctx}/manager/inventory">
    <div class="card-body">
      <div class="row g-2 align-items-end">
        <div class="col-12 col-md-4">
          <label class="form-label" for="q">Tìm kiếm (tên SP / SKU)</label>
          <input id="q" name="q" value="${fn:escapeXml(q)}" class="form-control" placeholder="Ví dụ: iPhone, SKU-001"/>
        </div>
        <div class="col-12 col-md-3">
          <label class="form-label" for="category">Danh mục</label>
          <select id="category" name="category" class="form-select">
            <option value="">Tất cả</option>
            <c:forEach var="c" items="${categories}">
              <option value="${c.id}" <c:if test="${category==c.id}">selected</c:if>>${c.name}</option>
            </c:forEach>
          </select>
        </div>
        <div class="col-6 col-md-2">
          <label class="form-label" for="size">Hiển thị</label>
          <select id="size" name="size" class="form-select">
            <c:forEach var="s" items="${[10,20,50,100]}">
              <option value="${s}" <c:if test="${size==s}">selected</c:if>>${s}</option>
            </c:forEach>
          </select>
        </div>
        <div class="col-6 col-md-3 d-flex gap-2">
          <button class="btn btn-dark flex-grow-1">Lọc</button>
          <button class="btn btn-success" type="button" data-bs-toggle="modal" data-bs-target="#bulkModal">
            Nhập/Xuất hàng loạt
          </button>
        </div>
      </div>
    </div>
  </form>

  <!-- ===== MODAL BULK ===== -->
  <div class="modal fade" id="bulkModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Nhập/Xuất hàng loạt</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <p class="text-muted mb-2">Mỗi dòng: <code>SKU|số_lượng_±</code></p>
          <!-- ====== NHẬP BẰNG EXCEL ====== -->
          <div class="d-flex flex-wrap gap-2 align-items-center mb-2">
            <input id="bulkFile" type="file"
                   accept=".xlsx,.xls,.csv,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
                   class="form-control" style="max-width:320px;">
            <button type="button" id="btnBulkTemplate" class="btn btn-outline-secondary btn-sm">Tải file mẫu</button>
            <span class="small text-muted">Cột bắt buộc: <code>SKU</code>, <code>Delta</code> (số dương = nhập, âm = xuất)</span>
          </div>
          <textarea id="bulkText" class="form-control" rows="8" placeholder="SKU001|10&#10;SKU002|-5"></textarea>
          <div class="small text-muted mt-2">Chỉ số nguyên. Dòng không hợp lệ sẽ bị bỏ qua.</div>
          <pre id="bulkResult" class="mt-3" style="white-space:pre-wrap;"></pre>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" id="btnBulkClose" data-bs-dismiss="modal">Đóng</button>
          <button id="btnBulkImport" class="btn btn-primary">Lưu</button>
        </div>
      </div>
    </div>
  </div>

  <!-- ===== MODAL NHẬP/XUẤT LẺ (1 BIẾN THỂ) ===== -->
  <div class="modal fade" id="singleModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Nhập/Xuất biến thể</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <div class="mb-2"><strong id="singleProductName">—</strong></div>
          <div class="mb-3">
            <label class="form-label">SKU</label>
            <input id="singleSku" class="form-control" readonly />
          </div>
          <div class="mb-3">
            <label class="form-label">Tồn hiện tại</label>
            <input id="singleCurrent" class="form-control" readonly />
          </div>
          <div class="mb-1">
            <label class="form-label">Số lượng (±):</label>
            <input id="singleDelta" type="number" class="form-control" placeholder="ví dụ: 5 hoặc -3" />
            <div class="form-text">Số dương = nhập | số âm = xuất.</div>
          </div>
          <div id="singleMsg" class="small mt-2"></div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" id="btnSingleClose" data-bs-dismiss="modal">Hủy</button>
          <button id="btnSingleSave" class="btn btn-primary">Lưu</button>
        </div>
      </div>
    </div>
  </div>

  <!-- ========== BẢNG BIẾN THỂ ========== -->
  <div class="card card-round">
    <div class="card-body p-0">
      <div class="table-responsive">
        <table class="table align-items-center mb-0">
          <thead class="thead-light">
          <c:set var="curSort" value="${empty sort ? 'created' : sort}" />
          <c:set var="curDir"  value="${empty dir  ? 'desc'   : dir }" />
          <c:set var="flipDir" value="${curDir=='asc' ? 'desc' : 'asc'}" />
          <tr>
            <th>
              <a class="text-decoration-none"
                 href="?q=${fn:escapeXml(q)}&category=${category}&size=${size}&sort=product&dir=${curSort=='product'?flipDir:'asc'}">
                Sản phẩm
                <c:if test="${curSort=='product'}"><i class="fa fa-sort-${curDir=='asc'?'up':'down'} ms-1"></i></c:if>
              </a>
            </th>
            <th>
              <a class="text-decoration-none"
                 href="?q=${fn:escapeXml(q)}&category=${category}&size=${size}&sort=sku&dir=${curSort=='sku'?flipDir:'asc'}">
                SKU
                <c:if test="${curSort=='sku'}"><i class="fa fa-sort-${curDir=='asc'?'up':'down'} ms-1"></i></c:if>
              </a>
            </th>
            <th class="text-end">
              <a class="text-decoration-none"
                 href="?q=${fn:escapeXml(q)}&category=${category}&size=${size}&sort=price&dir=${curSort=='price'?flipDir:'asc'}">
                Giá
                <c:if test="${curSort=='price'}"><i class="fa fa-sort-${curDir=='asc'?'up':'down'} ms-1"></i></c:if>
              </a>
            </th>
            <th class="text-end">
              <a class="text-decoration-none"
                 href="?q=${fn:escapeXml(q)}&category=${category}&size=${size}&sort=qty&dir=${curSort=='qty'?flipDir:'asc'}">
                Tồn (chi nhánh)
                <c:if test="${curSort=='qty'}"><i class="fa fa-sort-${curDir=='asc'?'up':'down'} ms-1"></i></c:if>
              </a>
            </th>
            <th class="text-end">Hành động</th>
          </tr>
          </thead>
          <tbody>
          <c:choose>
            <c:when test="${empty pr || empty pr.content}">
              <tr><td colspan="5" class="text-center text-muted py-4">Không có sản phẩm</td></tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="row" items="${pr.content}">
                <tr data-sku="${row.sku()}" data-name="${fn:escapeXml(row.productName())}">
                  <td>
                    <div class="fw-semibold">${row.productName()}</div>
                    <small class="text-muted">Id sản phẩm: #${row.productId()}</small>
                  </td>
                  <td><code>${row.sku()}</code></td>
                  <td class="text-end">
                    <fmt:formatNumber value="${row.price()}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                  </td>
                  <td class="text-end">
                    <span class="badge bg-secondary" data-qty>${row.qty()}</span>
                  </td>
                  <td class="text-end">
                    <button type="button" class="btn btn-sm btn-primary btn-single" data-bs-toggle="modal" data-bs-target="#singleModal">
                      Nhập/Xuất
                    </button>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Phân trang -->
      <div class="card-footer">
        <c:if test="${not empty pr && pr.total > pr.size}">
          <%
            com.uteshop.dao.manager.common.PageResult<?> pageRes =
                    (com.uteshop.dao.manager.common.PageResult<?>) request.getAttribute("result");
            int pageNow = pageRes.getPage();
            int sizeNow = pageRes.getSize();
            long totalPages = (pageRes.getTotal() + sizeNow - 1) / sizeNow;
          %>
          <nav>
            <ul class="pagination mb-0">
              <li class="page-item <%= pageNow<=1?"disabled":"" %>">
                <a class="page-link"
                   href="?q=${fn:escapeXml(q)}&category=${category}&size=${size}&sort=${curSort}&dir=${curDir}&page=<%=pageNow-1%>">«</a>
              </li>
              <%
                int tp = (int) totalPages;
                int start = Math.max(1, pageNow-2);
                int end = Math.min(tp, pageNow+2);
                for (int i=start; i<=end; i++) {
              %>
              <li class="page-item <%= i==pageNow?"active":"" %>">
                <a class="page-link"
                   href="?q=${fn:escapeXml(q)}&category=${category}&size=${size}&sort=${curSort}&dir=${curDir}&page=<%=i%>"><%=i%></a>
              </li>
              <% } %>
              <li class="page-item <%= pageNow>=totalPages?"disabled":"" %>">
                <a class="page-link"
                   href="?q=${fn:escapeXml(q)}&category=${category}&size=${size}&sort=${curSort}&dir=${curDir}&page=<%=pageNow+1%>">»</a>
              </li>
            </ul>
            <small class="text-muted ms-2">
              Trang <%=pageNow%>/<%=totalPages%> • Tổng <%=pageRes.getTotal()%> biến thể
            </small>
          </nav>
          <hr>
        </c:if>
        <div class="d-flex align-items-center justify-content-between mt-2">
          <div></div>
          <div>
            <button id="btnExportExcel" type="button" class="btn btn-success">
              Xuất tồn ra Excel
            </button>
          </div>
        </div>
      </div>
  </div>
</div>

<script src="https://cdn.sheetjs.com/xlsx-0.20.3/package/dist/xlsx.full.min.js"></script>
<script>
  // ===== EXPORT EXCEL =====
  async function downloadExcel() {
    const url = "${pageContext.request.contextPath}/api/manager/inventory/export-excel"

    const btn = document.getElementById("btnExportExcel");
    btn.disabled = true; btn.textContent = "Đang xuất dữ liệu...";

    const res = await fetch(url, {
      method: "GET",
      credentials: "same-origin"
    });

    if (!res.ok) {
      const txt = await res.text().catch(() => "");
      alert("Export lỗi: " + res.status + " " + txt);
      return;
    }

    const blob = await res.blob();
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "inventory.xlsx";
    a.click();
    URL.revokeObjectURL(a.href);

    btn.disabled = false; btn.textContent = "Xuất tồn ra Excel";
  }
  document.getElementById("btnExportExcel")?.addEventListener("click", downloadExcel);

  (() => {
    const ctx = "${pageContext.request.contextPath}";
    const API_BULK = ctx + "/api/manager/inventory/bulk-import";

    const $ = (sel, root=document) => root.querySelector(sel);

    // ===== Parse textarea (giống bản trước) =====
    function parseBulkTextarea(text) {
      const items = [];
      const errors = [];
      const lines = (text || "").split(/\r?\n/);
      const SEP = /\s*\|\s*|\s*,\s*|\s+/;

      lines.forEach((raw, idx) => {
        const lineNum = idx + 1;
        if (!raw) return;
        let s = raw.trim();
        if (!s) return;
        if (s.startsWith("#") || s.startsWith("//")) return;
        if (s.charCodeAt(0) === 0xFEFF) s = s.slice(1);

        const parts = s.split(SEP).filter(Boolean);
        if (parts.length !== 2) { errors.push('Dòng ' + lineNum + ': sai định dạng'); return; }

        const sku = String(parts[0]).trim();
        const deltaStr = String(parts[1]).trim();

        if (!/^[A-Za-z0-9._-]{1,80}$/.test(sku)) { errors.push('Dòng ' + lineNum + ': SKU không hợp lệ'); return; }
        if (!/^[+-]?\d+$/.test(deltaStr)) { errors.push('Dòng ' + lineNum + ': số lượng không hợp lệ'); return; }

        const delta = parseInt(deltaStr, 10);
        items.push({ sku, delta, _line: lineNum });
      });

      // Gom SKU trùng (cộng dồn) nhưng vẫn giữ danh sách gửi để hiển thị
      const merged = new Map();
      for (const it of items) merged.set(it.sku, (merged.get(it.sku) || 0) + it.delta);
      const mergedArr = Array.from(merged, ([sku, delta]) => ({ sku, delta }));

      return { items: mergedArr, errors };
    }

    async function postJson(url, data) {
      const res = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json; charset=UTF-8" },
        credentials: "same-origin",
        body: JSON.stringify(data)
      });
      const json = await res.json().catch(() => ({}));
      if (!res.ok || !json.ok) throw new Error(json.error || 'HTTP: ' + res.status);
      return json;
    }

    // ===== Render helper: hiển thị theo bảng văn bản gọn gàng =====
    function renderBulkResult(outEl, sentItems, json, parseErrors) {
      const result = json.result || {};                      // { sku: newQty }
      const notFound = (json.summary && json.summary.not_found) || [];
      const invalid = (json.summary && json.summary.invalid_lines) || [];
      const lines = [];

      // Duyệt theo danh sách đã gửi để đảm bảo thứ tự & luôn có SKU hiển thị
      for (const { sku, delta } of sentItems) {
        const deltaDisp = delta > 0 ? ("+" + delta) : String(delta);
        if (Object.prototype.hasOwnProperty.call(result, sku)) {
          lines.push(sku + "\t" + deltaDisp + "\t✅ tồn mới = " + result[sku]);
        } else if (notFound.indexOf(sku) !== -1) {
          lines.push(sku + "\t" + deltaDisp + "\t❌ Không tìm thấy");
        } else {
          lines.push(sku + "\t" + deltaDisp + "\t⚠️ Không cập nhật");
        }
      }

      // Lỗi định dạng
      const extraErr = [...(parseErrors || []), ...invalid];
      if (extraErr.length) {
        lines.push("", "⛔ Dòng lỗi:");
        for (const e of extraErr) lines.push(" - " + e);
      }

      // Summary
      const sum = json.summary || {};
      lines.push(
              "Tổng: " + (sum.total ?? sentItems.length) +
              " | Hợp lệ: " + (sum.valid ?? "-") +
              " | Cập nhật: " + (sum.updated ?? Object.keys(result).length) +
              " | Không tìm thấy: " + notFound.length
      );

      outEl.textContent = lines.join("\n");
    }

    // ===== BULK =====
    // ===== EXCEL → TEXTAREA =====
    const inputBulkFile = document.getElementById("bulkFile");
    const btnBulkTemplate = document.getElementById("btnBulkTemplate");

    btnBulkTemplate?.addEventListener("click", () => {
      // Tạo file mẫu .xlsx (SKU, Delta)
      const wb = XLSX.utils.book_new();
      const ws = XLSX.utils.aoa_to_sheet([
        ["SKU", "Delta"],
        ["SKU001", 10],
        ["SKU002", -5]
      ]);
      XLSX.utils.book_append_sheet(wb, ws, "Bulk");
      const wbout = XLSX.write(wb, { bookType: "xlsx", type: "array" });
      const blob = new Blob([wbout], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" });
      const a = document.createElement("a");
      a.href = URL.createObjectURL(blob);
      a.download = "bulk-template.xlsx";
      a.click();
      URL.revokeObjectURL(a.href);
    });

    inputBulkFile?.addEventListener("change", async (e) => {
      const file = e.target.files?.[0];
      if (!file) return;
      const ta = document.getElementById("bulkText");
      const out = document.getElementById("bulkResult");
      out.textContent = "";

      try {
        const data = await file.arrayBuffer();
        const wb = XLSX.read(data, { type: "array" });
        const ws = wb.Sheets[wb.SheetNames[0]];
        const rows = XLSX.utils.sheet_to_json(ws, { header: 1, blankrows: false, raw: true });

        if (!rows.length) { out.textContent = "File rỗng."; return; }

        // Tìm cột "SKU" và "Delta" (không phân biệt hoa thường)
        const header = rows[0].map(h => String(h || "").trim().toLowerCase());
        const skuIdx = header.indexOf("sku");
        const deltaIdx = header.indexOf("delta");

        if (skuIdx < 0 || deltaIdx < 0) {
          out.textContent = "Không tìm thấy cột SKU và Delta trong sheet đầu.";
          return;
        }

        const lines = [];
        const errors = [];
        for (let i = 1; i < rows.length; i++) {
          const r = rows[i] || [];
          const sku = String(r[skuIdx] ?? "").trim();
          const deltaStr = String(r[deltaIdx] ?? "").trim();
          const deltaNum = parseInt(deltaStr, 10);

          if (deltaNum === 0) { continue; } // Bỏ qua dòng có delta bằng 0
          if (!sku) { errors.push(`Dòng ${i+1}: thiếu SKU`); continue; }
          if (!/^[A-Za-z0-9._-]{1,80}$/.test(sku)) { errors.push('Dòng ' + i+1 + ': SKU không hợp lệ'); continue; }
          if (!/^[+-]?\d+$/.test(deltaStr)) { errors.push('Dòng ' + i+1 + ': Delta không hợp lệ'); continue; }

          lines.push(sku + `|` + deltaStr);
        }

        // Đổ vào textarea để vẫn có thể chỉnh tay nếu muốn
        ta.value = lines.join("\n");

        // Hiển thị lỗi (nếu có)
        if (errors.length) {
          out.textContent = "Một số dòng bị bỏ qua:\n" + errors.join("\n");
        }
      } catch (err) {
        console.error(err);
        document.getElementById("bulkResult").textContent = "Không thể đọc file: " + err.message;
      } finally {
        // reset để lần sau có thể chọn lại cùng file
        e.target.value = "";
      }
    });


    const btnBulkImport = $("#btnBulkImport");
    const btnBulkClose  = $("#btnBulkClose");

    $("#btnBulkImport")?.addEventListener("click", async function() {
      const ta = $("#bulkText");
      const out = $("#bulkResult");

      const { items, errors } = parseBulkTextarea(ta.value);
      if (!items.length) { out.textContent = errors.length ? errors.join("\n") : "Không có dòng hợp lệ."; return; }

      this.disabled = true; const old = this.textContent; this.textContent = "Đang lưu...";

      try {
        const json = await postJson(API_BULK, { items });

        // Cập nhật badge qty tại bảng chính
        for (const [sku, qty] of Object.entries(json.result || {})) {
          const badge = document.querySelector(`tr[data-sku="${sku}"] [data-qty]`);
          if (badge) badge.textContent = qty;
        }

        // Render kết quả rõ ràng
        renderBulkResult(out, items, json, errors);

        // Đổi nút “Đóng” -> “Hoàn thành” (reload)
        btnBulkClose.textContent = "Hoàn thành";
        btnBulkClose.removeAttribute("data-bs-dismiss");
        btnBulkClose.onclick = () => window.location.reload();

      } catch (e) {
        console.error(e);
        out.textContent = "Lỗi: " + e.message;
      } finally {
        this.disabled = false; this.textContent = old;
      }
    });

    // ===== SINGLE (modal) =====
    const singleModal = document.getElementById("singleModal");
    const fldName  = $("#singleProductName");
    const fldSku   = $("#singleSku");
    const fldCur   = $("#singleCurrent");
    const fldDelta = $("#singleDelta");
    const singleMsg= $("#singleMsg");
    const btnSingleSave = $("#btnSingleSave");
    const btnSingleClose= $("#btnSingleClose");

    singleModal?.addEventListener("show.bs.modal", (ev) => {
      const btn = ev.relatedTarget;
      const tr  = btn?.closest("tr");
      const sku = tr?.dataset.sku || "";
      const name= tr?.dataset.name || "—";
      const qty = tr?.querySelector("[data-qty]")?.textContent?.trim() || "0";

      singleModal.dataset.sku = sku;
      fldName.textContent = name;
      fldSku.value = sku;
      fldCur.value = qty;
      fldDelta.value = "";
      singleMsg.textContent = "";

      // Reset nút về trạng thái mặc định mỗi lần mở
      btnSingleClose.textContent = "Hủy";
      btnSingleClose.setAttribute("data-bs-dismiss", "modal");
      btnSingleClose.onclick = null;
    });

    $("#btnSingleSave")?.addEventListener("click", async function(){
      const sku = (singleModal.dataset.sku || fldSku.value || "").trim();
      const deltaStr = (fldDelta.value || "").trim();
      if (!sku) { singleMsg.textContent = "Thiếu SKU."; return; }
      if (!/^[+-]?\d+$/.test(deltaStr)) { singleMsg.textContent = "Số lượng không hợp lệ."; return; }

      this.disabled = true; const old = this.textContent; this.textContent = "Đang lưu...";
      try {
        const delta = parseInt(deltaStr, 10);
        const json = await postJson(API_BULK, { items: [{ sku, delta }] });

        const newQty = json.result?.[sku];
        if (typeof newQty === "number") {
          fldCur.value = String(newQty);
          fldDelta.value = "";
          singleMsg.innerHTML = "<span class='text-success'>Đã cập nhật.</span>";
          const badge = document.querySelector(`tr[data-sku="${sku}"] [data-qty]`);
          if (badge) badge.textContent = newQty;

          // Đổi nút “Hủy” -> “Hoàn thành” (reload)
          btnSingleClose.textContent = "Hoàn thành";
          btnSingleClose.removeAttribute("data-bs-dismiss");
          btnSingleClose.onclick = () => window.location.reload();
        } else {
          singleMsg.innerHTML = "<span class='text-danger'>Không tìm thấy SKU.</span>";
        }
      } catch (e) {
        console.error(e);
        singleMsg.innerHTML = "<span class='text-danger'>Lỗi: " + e.message + "</span>";
      } finally {
        this.disabled = false; this.textContent = old;
      }
    });
  })();
</script>
