<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglib.jsp"%>

<div class="page-inner">
  <div class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-4">
    <div>
      <h3 class="fw-bold mb-3">Thống kê bán hàng</h3>
    </div>
  </div>

  <div class="row">
    <div class="col-12 mb-3">
      <!-- Bộ lọc -->
      <div class="card card-round shadow-sm">
        <div class="card-body">
          <form class="row g-3 align-items-end" onsubmit="return false;">
            <div class="col-12 col-md-3">
              <h6 for="from" class="form-label">Từ ngày</h6>
              <input id="from" type="date" class="form-control"/>
            </div>
            <div class="col-12 col-md-3">
              <h6 for="to" class="form-label">Đến ngày</h6>
              <input id="to" type="date" class="form-control"/>
            </div>
            <div class="col-12 col-md-2 d-grid">
              <button id="loadBtn" type="button" class="btn btn-dark">Tải dữ liệu</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- KPI -->
    <div class="col-sm-6 col-md-3">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <div class="text-muted">Tổng đơn</div>
          <div id="kpiTotal" class="fs-3 fw-bold">0</div>
        </div>
      </div>
    </div>
    <div class="col-sm-6 col-md-3">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <div class="text-muted">Hoàn thành</div>
          <div class="d-flex align-items-baseline gap-2">
            <div id="kpiDelivered" class="fs-3 fw-bold">0</div>
            <div id="kpiDeliveredPct" class="text-success fw-semibold">0%</div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-sm-6 col-md-3">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <div class="text-muted">Hủy</div>
          <div class="d-flex align-items-baseline gap-2">
            <div id="kpiCanceled" class="fs-3 fw-bold">0</div>
            <div id="kpiCanceledPct" class="text-danger fw-semibold">0%</div>
          </div>
        </div>
      </div>
    </div>
    <div class="col-sm-6 col-md-3">
      <div class="card shadow-sm border-0">
        <div class="card-body">
          <div class="text-muted">Trả hàng</div>
          <div class="d-flex align-items-baseline gap-2">
            <div id="kpiReturned" class="fs-3 fw-bold">0</div>
            <div id="kpiReturnedPct" class="text-warning fw-semibold">0%</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Chart: Trạng thái đơn -->
    <div class="col-12 col-lg-6 mb-3">
      <div class="card">
        <div class="card-header">
          <div class="card-head-row">
            <div class="card-title">
              <h5 class="mb-0 fw-bold">Trạng thái đơn trong kỳ</h5>
            </div>
            <div class="card-tools">
              <button id="exportStatusBtn" type="button" class="btn btn-sm btn-outline-secondary">
                Xuất PNG
              </button>
            </div>
          </div>
        </div>
        <div class="card-body">
          <div style="height: 360px;">
            <canvas id="statusChart"></canvas>
          </div>
          <small class="text-muted mt-2">Khoảng: <span id="statusChart-range">—</span></small>
        </div>
      </div>
    </div>

    <!-- Chart: Phân bổ phương thức thanh toán -->
    <div class="col-12 col-lg-6 mb-3">
      <div class="card">
        <div class="card-header">
          <div class="card-head-row">
            <div class="card-title">
              <h5 class="mb-0 fw-bold">Phân bổ hình thức thanh toán</h5>
            </div>
            <div class="card-tools">
              <button id="exportPaymentBtn" type="button" class="btn btn-sm btn-outline-secondary">
                Xuất PNG
              </button>
            </div>
          </div>
        </div>
        <div class="card-body">
          <div style="height: 360px;">
            <canvas id="paymentChart"></canvas>
          </div>
          <small class="text-muted mt-2">Khoảng: <span id="paymentChart-range">—</span></small>
        </div>
      </div>
    </div>

    <script>
      // Helper ngắn gọn
      const $ = (sel) => document.querySelector(sel);

      // Ngày mặc định = tháng hiện tại
      (function setDefaultDates() {
        const now = new Date();
        const y = now.getFullYear();
        const m = now.getMonth(); // 0-11
        const first = new Date(y, m, 1);
        const last  = new Date(y, m + 1, 0);
        $("#from").value = first.toISOString().slice(0,10);
        $("#to").value   = last.toISOString().slice(0,10);
      })();

      // Biến chart dùng lại
      let statusChart = null;
      let paymentChart = null;

      // Format %
      function fmtPct(x) {
        return (x * 100).toFixed(1).replace(/\.0$/, '') + '%';
      }

      // Xuất PNG
      function downloadCanvasPNG(canvas, filename) {
        const tmpCanvas = document.createElement('canvas');
        tmpCanvas.width = canvas.width;
        tmpCanvas.height = canvas.height;
        const ctx = tmpCanvas.getContext('2d');

        // Nền trắng
        ctx.fillStyle = '#ffffff';
        ctx.fillRect(0, 0, tmpCanvas.width, tmpCanvas.height);

        // Vẽ chart gốc lên trên
        ctx.drawImage(canvas, 0, 0);

        // Tạo link tải
        const link = document.createElement('a');
        link.download = filename;
        link.href = tmpCanvas.toDataURL('image/png', 1.0);
        link.click();
      }

      // Render chart trạng thái
      function renderStatusChart(values) {
        const ctx = $("#statusChart").getContext('2d');
        if (statusChart) statusChart.destroy();

        statusChart = new Chart(ctx, {
          type: 'bar',
          data: {
            labels: ['Hoàn thành', 'Hủy', 'Trả hàng'],
            datasets: [{ label: 'Số đơn', data: values }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            resizeDelay: 200, // tránh rung
            animation: { duration: 400, easing: 'easeOutQuart' },
            plugins: {
              legend: { display: false },
              tooltip: {
                callbacks: {
                  label: function (ctx) { return ' ' + (ctx.raw ?? 0) + ' đơn'; }
                }
              }
            },
            scales: {
              y: { beginAtZero: true, ticks: { precision: 0 } }
            }
          }
        });
      }

      // Render chart phương thức thanh toán
      function renderPaymentChart(labels, values) {
        const ctx = $("#paymentChart").getContext('2d');
        if (paymentChart) paymentChart.destroy();

        paymentChart = new Chart(ctx, {
          type: 'doughnut',
          data: { labels: labels, datasets: [{ data: values }] },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            resizeDelay: 200,
            animation: { duration: 400, easing: 'easeOutQuart' },
            plugins: {
              legend: { position: 'bottom' },
              tooltip: {
                callbacks: {
                  label: function (ctx) {
                    var total = values.reduce(function(a,b){return a+b;}, 0) || 1;
                    var count = ctx.raw || 0;
                    var share = count / total;
                    return ' ' + ctx.label + ': ' + count + ' (' + fmtPct(share) + ')';
                  }
                }
              }
            },
            cutout: '60%'
          }
        });
      }

      // Tải dữ liệu + bind vào UI
      async function loadStats() {
        var from = $("#from").value;
        var to   = $("#to").value;
        if (!from || !to) {
          alert("Vui lòng chọn Từ ngày và Đến ngày");
          return;
        }

        document.getElementById("statusChart-range").textContent = from + " → " + to;
        document.getElementById("paymentChart-range").textContent = from + " → " + to;

        var params = new URLSearchParams({ from: from, to: to });
        // dùng c:url để chắc contextPath đúng, KHÔNG dùng template literal
        var apiBase = '<c:url value="/api/manager/orders-stats"/>';
        var url = apiBase + '?' + params.toString();

        var btn = $("#loadBtn");
        btn.disabled = true; btn.textContent = "Đang tải...";

        try {
          var res = await fetch(url, { headers: { 'Accept': 'application/json' } });
          if (!res.ok) throw new Error('HTTP ' + res.status);
          var data = await res.json();

          // KPI
          $("#kpiTotal").textContent     = data.totalOrders ?? 0;
          $("#kpiDelivered").textContent = data.delivered ?? 0;
          $("#kpiCanceled").textContent  = data.canceled ?? 0;
          $("#kpiReturned").textContent  = data.returned ?? 0;

          $("#kpiDeliveredPct").textContent = fmtPct(data.deliveredRatio ?? 0);
          $("#kpiCanceledPct").textContent  = fmtPct(data.canceledRatio  ?? 0);
          $("#kpiReturnedPct").textContent  = fmtPct(data.returnedRatio  ?? 0);

          // Chart trạng thái
          renderStatusChart([
            data.delivered ?? 0,
            data.canceled ?? 0,
            data.returned ?? 0
          ]);

          // Chart thanh toán
          var payment = Array.isArray(data.paymentBreakdown) ? data.paymentBreakdown : [];
          var pLabels = payment.map(function(p){ return p.method; });
          var pValues = payment.map(function(p){ return p.orders; });
          renderPaymentChart(pLabels, pValues);

        } catch (e) {
          console.error(e);
          alert("Không tải được dữ liệu. Vui lòng thử lại.");
        } finally {
          btn.disabled = false; btn.textContent = "Tải dữ liệu";
        }
      }

      // Export PNG (không dùng template literal)
      $("#exportStatusBtn").addEventListener('click', function () {
        if (statusChart) {
          var fname = 'orders-status-' + $("#from").value + '_to_' + $("#to").value + '.png';
          downloadCanvasPNG($("#statusChart"), fname);
        }
      });
      $("#exportPaymentBtn").addEventListener('click', function () {
        if (paymentChart) {
          var fname = 'payment-methods-' + $("#from").value + '_to_' + $("#to").value + '.png';
          downloadCanvasPNG($("#paymentChart"), fname);
        }
      });

      // Nút tải dữ liệu + auto load
      $("#loadBtn").addEventListener('click', loadStats);
      window.addEventListener('DOMContentLoaded', loadStats);
    </script>
  </div>
</div>
