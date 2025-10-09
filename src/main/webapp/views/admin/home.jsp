<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div
	class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-4">
	<div>
		<h3 class="fw-bold mb-3">Dashboard</h3>
	</div>
</div>
<div class="row">
	<div class="col-sm-6 col-md-3">
		<div class="card card-stats card-round">
			<div class="card-body">
				<div class="row align-items-center">
					<div class="col-icon">
						<div class="icon-big text-center icon-primary bubble-shadow-small">
							<i class="fas fa-users"></i>
						</div>
					</div>
					<div class="col col-stats ms-3 ms-sm-0">
						<div class="numbers">
							<p class="card-category">Tổng số khách hàng</p>
							<h4 class="card-title">${totalCustomers}</h4>
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
							<i class="fas fa-user-check"></i>
						</div>
					</div>
					<div class="col col-stats ms-3 ms-sm-0">
						<div class="numbers">
							<p class="card-category">Khách hàng mới tháng này</p>
							<h4 class="card-title">${newCustomers}</h4>
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
							<p class="card-category">Sản phẩm sắp hết hàng</p>
							<h4 class="card-title">${lowStock}</h4>
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
						<div
							class="icon-big text-center icon-secondary bubble-shadow-small">
							<i class="far fa-check-circle"></i>
						</div>
					</div>
					<div class="col col-stats ms-3 ms-sm-0">
						<div class="numbers">
							<p class="card-category">Đơn hàng tháng này</p>
							<h4 class="card-title">${orderCount}</h4>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-8">
		<div class="card card-round">
			<div class="card-header">
				<div class="card-head-row">
					<div class="card-title">Doanh thu theo tháng trong năm</div>
				</div>
			</div>
			<div class="card-body">
				<div class="chart-container" style="min-height: 375px">
					<canvas id="revenueChart"></canvas>
				</div>
			</div>
		</div>
	</div>
	<div class="col-md-4">
		<div class="card card-round">
			<div class="card-body pb-0">
				<!-- <div class="h1 fw-bold float-end text-primary">+5%</div> -->
				<h2 class="mb-2">Danh thu tháng ${currentMonth}</h2>
				<p class="text-muted">${revenue.intValue()}VND</p>
				<div class="pull-in sparkline-fix">
					<div id="lineChart"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-8">
		<div class="card card-round">
			<div class="card-header">
				<div class="card-head-row">
					<div class="card-title">Doanh thu theo ngày trong tháng</div>
				</div>
			</div>
			<div class="card-body">
				<div class="chart-container" style="min-height: 375px">
					<canvas id="dayRevenueChart"></canvas>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
const revenueLabels = ${revenueLabels != null ? revenueLabels : '[]'};
const revenueValues = ${revenueValues != null ? revenueValues : '[]'};
const dailyLabels = ${dailyLabels != null ? dailyLabels : '[]'};
const dailyValues = ${dailyValues != null ? dailyValues : '[]'};

document.addEventListener("DOMContentLoaded", function() {
    // BIỂU ĐỒ DOANH THU THEO THÁNG
    const ctxRevenue = document.getElementById('revenueChart');
    new Chart(ctxRevenue, {
        type: 'line',
        data: {
            labels: revenueLabels,
            datasets: [{
                label: 'Doanh thu (VND)',
                data: revenueValues,
                borderWidth: 2,
                borderColor: '#007bff',
                fill: false,
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });

    // BIỂU ĐỒ DOANH THU THEO NGÀY
    const ctxDaily = document.getElementById('dayRevenueChart');
    new Chart(ctxDaily, {
        type: 'line',
        data: {
            labels: dailyLabels,
            datasets: [{
                label: 'Doanh thu (VND)',
                data: dailyValues,
                borderWidth: 2,
                borderColor: '#28a745',
                fill: false,
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
});
</script>