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
      (() => {
        // Helper cục bộ
        const qs  = (sel) => document.querySelector(sel);

        // Ngày mặc định = tháng hiện tại
        (function setDefaultDates() {
          const now = new Date();
          const y = now.getFullYear(), m = now.getMonth();
          const first = new Date(y, m, 1);
          const last  = new Date(y, m + 1, 0);
          const fromEl = qs("#from");
          const toEl   = qs("#to");
          if (fromEl) fromEl.value = first.toISOString().slice(0,10);
          if (toEl)   toEl.value   = last.toISOString().slice(0,10);
        })();

        let statusChart = null;
        let paymentChart = null;

        function fmtPct(x) {
          return (x * 100).toFixed(1).replace(/\.0$/, '') + '%';
        }

        function downloadCanvasPNG(canvas, filename) {
          if (!canvas) return;
          const tmpCanvas = document.createElement('canvas');
          tmpCanvas.width = canvas.width;
          tmpCanvas.height = canvas.height;
          const ctx = tmpCanvas.getContext('2d');
          ctx.fillStyle = '#ffffff';
          ctx.fillRect(0, 0, tmpCanvas.width, tmpCanvas.height);
          ctx.drawImage(canvas, 0, 0);
          const link = document.createElement('a');
          link.download = filename;
          link.href = tmpCanvas.toDataURL('image/png', 1.0);
          link.click();
        }

        function renderStatusChart(values) {
          const c = qs("#statusChart");
          if (!c) return;
          const ctx = c.getContext('2d');
          if (statusChart) statusChart.destroy();
          statusChart = new Chart(ctx, {
            type: 'bar',
            data: {
              labels: ['Hoàn thành', 'Hủy', 'Trả hàng'],
              datasets: [{ label: 'Số đơn', data: values }]
            },
            options: {
              responsive: true, maintainAspectRatio: false, resizeDelay: 200,
              animation: { duration: 400, easing: 'easeOutQuart' },
              plugins: {
                legend: { display: false },
                tooltip: { callbacks: { label: (ctx) => ' ' + (ctx.raw ?? 0) + ' đơn' } }
              },
              scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
            }
          });
        }

        function renderPaymentChart(labels, values) {
          const c = qs("#paymentChart");
          if (!c) return;
          const ctx = c.getContext('2d');
          if (paymentChart) paymentChart.destroy();
          paymentChart = new Chart(ctx, {
            type: 'doughnut',
            data: { labels, datasets: [{ data: values }] },
            options: {
              responsive: true, maintainAspectRatio: false, resizeDelay: 200,
              animation: { duration: 400, easing: 'easeOutQuart' },
              plugins: {
                legend: { position: 'bottom' },
                tooltip: {
                  callbacks: {
                    label: (ctx) => {
                      const total = values.reduce((a,b)=>a+b, 0) || 1;
                      const count = ctx.raw || 0;
                      return ' ' + ctx.label + ': ' + count + ' (' + fmtPct(count/total) + ')';
                    }
                  }
                }
              },
              cutout: '60%'
            }
          });
        }

        async function loadStats() {
          const fromEl = qs("#from");
          const toEl   = qs("#to");
          if (!fromEl || !toEl) return;

          const from = fromEl.value, to = toEl.value;
          if (!from || !to) { alert("Vui lòng chọn Từ ngày và Đến ngày"); return; }

          const r1 = document.getElementById("statusChart-range");
          const r2 = document.getElementById("paymentChart-range");
          if (r1) r1.textContent = from + " → " + to;
          if (r2) r2.textContent = from + " → " + to;

          const params = new URLSearchParams({ from, to });
          const apiBase = '<c:url value="/api/manager/orders-stats"/>';
          const url = apiBase + '?' + params.toString();

          const btn = qs("#loadBtn");
          if (btn) { btn.disabled = true; btn.textContent = "Đang tải..."; }

          try {
            const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
            if (!res.ok) throw new Error('HTTP ' + res.status);
            const data = await res.json();

            const setText = (sel, v) => { const el = qs(sel); if (el) el.textContent = v; };
            setText("#kpiTotal",      data.totalOrders ?? 0);
            setText("#kpiDelivered",  data.delivered ?? 0);
            setText("#kpiCanceled",   data.canceled ?? 0);
            setText("#kpiReturned",   data.returned ?? 0);
            setText("#kpiDeliveredPct", fmtPct(data.deliveredRatio ?? 0));
            setText("#kpiCanceledPct",  fmtPct(data.canceledRatio  ?? 0));
            setText("#kpiReturnedPct",  fmtPct(data.returnedRatio  ?? 0));

            renderStatusChart([data.delivered ?? 0, data.canceled ?? 0, data.returned ?? 0]);

            const payment = Array.isArray(data.paymentBreakdown) ? data.paymentBreakdown : [];
            const pLabels = payment.map(p => p.method);
            const pValues = payment.map(p => p.orders);
            renderPaymentChart(pLabels, pValues);

          } catch (e) {
            console.error(e);
            alert("Không tải được dữ liệu. Vui lòng thử lại.");
          } finally {
            if (btn) { btn.disabled = false; btn.textContent = "Tải dữ liệu"; }
          }
        }

        // Bind sự kiện nếu phần tử tồn tại
        const exportStatusBtn  = qs("#exportStatusBtn");
        if (exportStatusBtn) {
          exportStatusBtn.addEventListener('click', () => {
            if (statusChart) {
              const fname = 'orders-status-' + (qs("#from")?.value || '') + '_to_' + (qs("#to")?.value || '') + '.png';
              downloadCanvasPNG(qs("#statusChart"), fname);
            }
          });
        }

        const exportPaymentBtn = qs("#exportPaymentBtn");
        if (exportPaymentBtn) {
          exportPaymentBtn.addEventListener('click', () => {
            if (paymentChart) {
              const fname = 'payment-methods-' + (qs("#from")?.value || '') + '_to_' + (qs("#to")?.value || '') + '.png';
              downloadCanvasPNG(qs("#paymentChart"), fname);
            }
          });
        }

        const loadBtn = qs("#loadBtn");
        if (loadBtn) loadBtn.addEventListener('click', loadStats);

        // Tự load khi DOM sẵn sàng
        window.addEventListener('DOMContentLoaded', loadStats);
      })();
    </script>
  </div>
</div>
