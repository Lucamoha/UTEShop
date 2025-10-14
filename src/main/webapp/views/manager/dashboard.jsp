<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglib.jsp"%>

<%
  java.util.Map<Integer,String> STATUS_MAP = new java.util.LinkedHashMap<>();
  STATUS_MAP.put(0,"Mới tạo");
  STATUS_MAP.put(1,"Đã xác nhận");
  STATUS_MAP.put(2,"Đang giao hàng");
  STATUS_MAP.put(3,"Đã nhận");
  STATUS_MAP.put(4,"Hủy");
  STATUS_MAP.put(5,"Trả hàng");
  request.setAttribute("STATUS_MAP", STATUS_MAP);
%>

<div class="page-inner">
  <div class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-4">
    <div>
      <h3 class="fw-bold mb-3">Trang chủ</h3>
    </div>
  </div>
  <!-- Hàng chart -->
  <div class="row">
    <!-- Bộ lọc -->
    <div class="col-12 mb-3">
      <div class="card card-round shadow-sm">
        <div class="card-body">
          <form class="row g-3 align-items-end">
            <div class="col-12 col-md-2">
              <h5 for="bucket" class="form-label">Nhóm thời gian</h5>
              <select id="bucket" class="form-select">
                <option value="day">Theo ngày</option>
                <option value="week">Theo tuần</option>
                <option value="month">Theo tháng</option>
              </select>
            </div>
            <div class="col-12 col-md-3">
              <h6 for="from" class="form-label">Từ ngày</h6>
              <input id="from" type="date" class="form-control"/>
            </div>
            <div class="col-12 col-md-3">
              <h6 for="to" class="form-label">Đến ngày</h6>
              <input id="to" type="date" class="form-control"/>
            </div>
            <div class="col-12 col-md-2">
              <h6 for="limit" class="form-label">Top sản phẩm</h6>
              <input id="limit" type="number" class="form-control" min="3" max="10" value="5"/>
            </div>
            <div class="col-12 col-md-2 d-grid">
              <button id="loadBtn" type="button" class="btn btn-dark">Tải dữ liệu</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Widgets thông tin -->
    <div class="col-sm-6 col-md-3">
      <div class="card card-stats card-round">
        <div class="card-body">
          <div class="row align-items-center">
            <div class="col-icon">
              <div class="icon-big text-center icon-primary bubble-shadow-small">
                <i class="fas fa-dollar-sign"></i>
              </div>
            </div>
            <div class="col col-stats ms-3 ms-sm-0">
              <div class="numbers">
                <p class="card-category">Doanh thu hôm nay</p>
                <h4 class="card-title" id="kpi-revenue-today"></h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-sm-6 col-md-3">
      <div class="card card-stats card-round">
        <div class="card-body">
          <div class="row align-items-center">
            <div class="col-icon">
              <div class="icon-big text-center icon-secondary bubble-shadow-small">
                <i class="fas fa-dollar-sign"></i>
              </div>
            </div>
            <div class="col col-stats ms-3 ms-sm-0">
              <div class="numbers">
                <p class="card-category">Doanh thu tháng này</p>
                <h4 class="card-title" id="kpi-revenue-month"></h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-sm-6 col-md-3">
      <div class="card card-stats card-round">
        <div class="card-body">
          <div class="row align-items-center">
            <div class="col-icon">
              <div class="icon-big text-center icon-info bubble-shadow-small">
                <i class="fas fa-shopping-cart"></i>
              </div>
            </div>
            <div class="col col-stats ms-3 ms-sm-0">
              <div class="numbers">
                <p class="card-category">Đơn hàng hôm nay</p>
                <h4 class="card-title">${empty todayOrders ? 0 : todayOrders}</h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-sm-6 col-md-3">
      <div class="card card-stats card-round">
        <div class="card-body">
          <div class="row align-items-center">
            <div class="col-icon">
              <div class="icon-big text-center icon-success bubble-shadow-small">
                <i class="fas fa-luggage-cart"></i>
              </div>
            </div>
            <div class="col col-stats ms-3 ms-sm-0">
              <div class="numbers">
                <p class="card-category">Số lượng sản phẩm</p>
                <h4 class="card-title">${empty totalProducts ? 0 : totalProducts}</h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Chart Doanh thu -->
    <div class="col-12 col-lg-6 mb-3">
      <div class="card">
        <div class="card-header">
          <div class="card-head-row">
            <div class="card-title">
              <h5 class="mb-0 fw-bold">Doanh thu theo <span id="title-bucket">ngày</span></h5>
              <small class="text-muted">Tổng: <span id="total-revenue">—</span></small>
            </div>
            <div class="card-tools">
              <button id="btn-export-revenue" type="button" class="btn btn-sm btn-outline-secondary">
                Xuất ảnh
              </button>
            </div>
          </div>
        </div>
        <div class="card-body">
          <div style="height: 360px;">
            <canvas id="revenueChart"></canvas>
          </div>
          <small class="text-muted mt-2">Khoảng: <span id="range-revenue">—</span></small>
        </div>
      </div>
    </div>
    <!-- Chart Top sản phẩm -->
    <div class="col-12 col-lg-6 mb-3">
      <div class="card">
        <div class="card-header">
          <div class="card-head-row">
            <div class="card-title">
              <h5 class="mb-0 fw-bold">Top sản phẩm bán chạy</h5>
              <small class="text-muted"><span id="range-top">—</span></small>
            </div>
            <div class="card-tools">
              <button id="btn-export-top" type="button" class="btn btn-sm btn-outline-secondary">
                Xuất ảnh
              </button>
            </div>
          </div>
        </div>
        <div class="card-body">
          <div style="height: 360px;">
            <canvas id="topChart"></canvas>
          </div>
          <small class="text-muted mt-2">Cột: Doanh thu (VND) • Đường: Số lượng</small>
        </div>
      </div>
    </div>
    <script>
      // === Endpoint backend ===
      const REVENUE_API = "${pageContext.request.contextPath}/api/manager/charts/revenue";
      const TOP_PRODUCTS_API = "${pageContext.request.contextPath}/api/manager/top-products";

      // === Helpers ===
      const fmtVND = (n) => new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND", maximumFractionDigits: 0 }).format(Number(n||0));
      const fmtInt = (n) => new Intl.NumberFormat("vi-VN").format(Number(n||0));
      const qs = (id) => document.getElementById(id);

      // Default từ ngày đầu tháng đến hôm nay
      const pad2 = (x) => String(x).padStart(2, "0");
      const today = new Date();
      const todayISO = today.getFullYear() + "-" + pad2(today.getMonth()+1) + "-" + pad2(today.getDate());
      const monthStart = new Date(today.getFullYear(), today.getMonth(), 1);
      const monthStartISO = monthStart.getFullYear() + "-" + pad2(monthStart.getMonth()+1) + "-" + pad2(monthStart.getDate());
      qs("from").value = monthStartISO;
      qs("to").value = todayISO;
      qs("bucket").value = "day";

      // === Chart instances ===
      let revenueChart, topChart;

      function ensureRevenueChart(ctx) {
        if (revenueChart) return revenueChart;
        revenueChart = new Chart(ctx, {
          type: "line",
          data: { labels: [], datasets: [{
              label: "Doanh thu",
              data: [],
              tension: .3,
              borderWidth: 2,
              pointRadius: 0
            }]},
          options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
              y: { ticks: { callback: (v) => fmtInt(v) } }
            },
            plugins: {
              tooltip: { callbacks: { label: (c) => " " + fmtVND(c.parsed.y) } },
              legend: { display: false }
            }
          }
        });
        return revenueChart;
      }

      function ensureTopChart(ctx) {
        if (topChart) return topChart;
        topChart = new Chart(ctx, {
          type: "bar",
          data: {
            labels: [],
            datasets: [
              { type: "bar",  label: "Doanh thu", data: [], yAxisID: "y",  borderWidth: 1 },
              { type: "line", label: "Số lượng",  data: [], yAxisID: "y1", tension: .3, pointRadius: 2, borderWidth: 2 }
            ]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
              y:  { position: "left",  ticks: { callback: (v)=> fmtInt(v) } },
              y1: { position: "right", grid: { drawOnChartArea:false }, ticks: { callback: (v)=> fmtInt(v) } },
              x:  { ticks: { maxRotation: 25, minRotation: 0, autoSkip: true } }
            },
            plugins: {
              tooltip: {
                callbacks: {
                  label: (ctx) => ctx.datasetIndex === 0
                          ? " Doanh thu: " + fmtVND(ctx.parsed.y)
                          : " Số lượng: " + fmtInt(ctx.parsed.y)
                }
              }
            }
          }
        });
        return topChart;
      }

      async function fetchJSON(url) {
        const res = await fetch(url, { headers: { "Accept": "application/json" } });
        if (!res.ok) throw new Error("HTTP " + res.status);
        return await res.json();
      }

      async function loadKpis() {
        const today = new Date().toISOString().slice(0,10);
        const monthStart = today.slice(0,8) + "01";

        try {
          // Doanh thu hôm nay
          const resToday = await fetchJSON(REVENUE_API + "?bucket=day&from=" + today + "&to=" + today);
          qs("kpi-revenue-today").textContent = fmtVND(resToday.total || 0);

          // Doanh thu tháng này
          const resMonth = await fetchJSON(REVENUE_API + "?bucket=day&from=" + monthStart + "&to=" + today);
          qs("kpi-revenue-month").textContent = fmtVND(resMonth.total || 0);
        } catch (e) {
          console.error("KPI error:", e);
        }
      }

      async function loadCharts() {
        const bucket = qs("bucket").value;
        const from = qs("from").value;
        const to = qs("to").value;
        const limit = qs("limit").value;

        // Cập nhật nhãn
        qs("title-bucket").textContent = (bucket === "day" ? "ngày" : (bucket === "week" ? "tuần" : "tháng"));
        qs("range-revenue").textContent = from + " → " + to;
        qs("range-top").textContent = from + " → " + to;

        const btn = qs("loadBtn");
        btn.disabled = true; btn.textContent = "Đang tải...";

        try {
          // Doanh thu
          const revURL = REVENUE_API + "?bucket=" + encodeURIComponent(bucket)
                  + "&from=" + encodeURIComponent(from)
                  + "&to=" + encodeURIComponent(to);
          const revenue = await fetchJSON(revURL);
          const labels = (revenue.points || []).map(p => p.x);
          const values = (revenue.points || []).map(p => Number(p.y || 0));
          qs("total-revenue").textContent = fmtVND(revenue.total || 0);

          const rc = ensureRevenueChart(document.getElementById("revenueChart"));
          rc.data.labels = labels;
          rc.data.datasets[0].data = values;
          rc.update();

          // Top sản phẩm
          const topURL = TOP_PRODUCTS_API + "?from=" + encodeURIComponent(from)
                  + "&to=" + encodeURIComponent(to)
                  + "&limit=" + encodeURIComponent(limit);
          const top = await fetchJSON(topURL);
          const names   = top.map(it => it.name);
          const amounts = top.map(it => Number(it.amount || 0));
          const qtys    = top.map(it => Number(it.qty || 0));

          const tc = ensureTopChart(document.getElementById("topChart"));
          tc.data.labels = names;
          tc.data.datasets[0].data = amounts;
          tc.data.datasets[1].data = qtys;
          tc.update();

        } catch (e) {
          console.error(e);
          alert("Lỗi tải dữ liệu: " + e.message);
        } finally {
          btn.disabled = false; btn.textContent = "Tải dữ liệu";
        }
      }

      // Khởi tạo
      window.addEventListener("DOMContentLoaded", function() {
        loadCharts();
        loadKpis();
        qs("loadBtn").addEventListener("click", loadCharts, loadKpis);
      });
    </script>
    <script>
      // Helper: tải dataURL thành file
      function downloadURI(uri, filename) {
        const link = document.createElement('a');
        link.href = uri;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }

      // Helper: chuyển text -> slug gọn gàng cho tên file
      function slugify(text) {
        return (text || '')
                .toString()
                .normalize('NFD').replace(/[\u0300-\u036f]/g, '')
                .replace(/[^a-zA-Z0-9]+/g, '-')
                .replace(/-+/g, '-')
                .replace(/^-|-$/g, '')
                .toLowerCase();
      }

      // Xuất ảnh từ canvas, giữ nền trắng, tăng độ phân giải (scale)
      function exportChartAsImage(canvasId, filenameBase, scale = 2) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) return;

        const w = canvas.width;
        const h = canvas.height;

        const off = document.createElement('canvas');
        off.width = Math.max(1, Math.round(w * (scale)));
        off.height = Math.max(1, Math.round(h * (scale)));
        const ctx = off.getContext('2d');

        // Nền trắng (tránh nền trong suốt khi dán vào tài liệu)
        ctx.fillStyle = '#ffffff';
        ctx.fillRect(0, 0, off.width, off.height);

        // Vẽ ảnh từ canvas gốc lên (scale lên)
        ctx.imageSmoothingEnabled = true;
        ctx.imageSmoothingQuality = 'high';
        ctx.drawImage(canvas, 0, 0, off.width, off.height);

        const dataURL = off.toDataURL('image/png');
        downloadURI(dataURL, filenameBase + '.png');
      }

      // Gắn sự kiện cho nút doanh thu
      document.getElementById('btn-export-revenue')?.addEventListener('click', () => {
        const bucket = document.getElementById('title-bucket')?.textContent?.trim() || 'bucket';
        const range = document.getElementById('range-revenue')?.textContent?.trim() || '';
        const total = document.getElementById('total-revenue')?.textContent?.trim() || '';
        const name = 'revenue-' + slugify(bucket) + '-' + slugify(range || 'range') + (total ? '_' + slugify(total) : '');
        exportChartAsImage('revenueChart', name);
      });

      // Gắn sự kiện cho nút top sản phẩm
      document.getElementById('btn-export-top')?.addEventListener('click', () => {
        const range = document.getElementById('range-top')?.textContent?.trim() || '';
        const name = 'top-products-' + slugify(range || 'range');
        exportChartAsImage('topChart', name);
      });
    </script>
  </div>
  <!-- Hàng đơn hàng mới -->
  <div class="row">
    <div class="col-md-12">
      <div class="card card-round">
        <div class="card-header">
          <div class="card-head-row card-tools-still-right">
            <div class="card-title">Đơn hàng mới</div>
            <div class="card-tools"></div>
          </div>
        </div>
        <div class="card-body p-0">
          <div class="table-responsive">
            <table class="table align-items-center mb-0">
              <thead class="thead-light">
              <tr>
                <th scope="col">Id đơn hàng</th>
                <th scope="col" class="text-end">Thời gian</th>
                <th scope="col" class="text-end">Tổng tiền</th>
                <th scope="col" class="text-end">Trạng thái</th>
              </tr>
              </thead>
              <tbody>
              <c:choose>
                <c:when test="${empty ordersRecent}">
                  <tr>
                    <td colspan="4" class="text-center text-muted py-4">Không có đơn hàng</td>
                  </tr>
                </c:when>
                <c:otherwise>
                  <c:forEach var="o" items="${ordersRecent}">
                    <tr>
                      <!-- Cột 1: ID (link sang chi tiết) -->
                      <td>
                        <a href="${pageContext.request.contextPath}/manager/orders/detail?id=${o.id}">
                          #${o.id}
                        </a>
                      </td>

                      <!-- Cột 2: Thời gian (text-end) -->
                      <td class="text-end">
                          ${o.createdAt}
                      </td>

                      <!-- Cột 3: Tổng tiền (text-end) -->
                      <td class="text-end">
                        <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                      </td>

                      <!-- Cột 4: Trạng thái (text-end) -->
                      <td class="text-end">
                        <span class="badge
                          <c:choose>
                            <c:when test='${o.orderStatus==0}'>bg-secondary</c:when>
                            <c:when test='${o.orderStatus==1}'>bg-info</c:when>
                            <c:when test='${o.orderStatus==2}'>bg-primary</c:when>
                            <c:when test='${o.orderStatus==3}'>bg-success</c:when>
                            <c:when test='${o.orderStatus==4}'>bg-danger</c:when>
                            <c:when test='${o.orderStatus==5}'>bg-dark</c:when>
                            <c:otherwise>bg-danger</c:otherwise>
                          </c:choose>">${STATUS_MAP.get(o.orderStatus)}
                        </span>
                      </td>
                    </tr>
                  </c:forEach>
                </c:otherwise>
              </c:choose>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>