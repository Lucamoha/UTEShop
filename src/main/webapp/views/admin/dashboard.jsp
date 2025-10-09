<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="container mt-4">
	<h3 class="mb-4">Dashboard</h3>

	<div class="row mb-4 text-center">
		<div class="col-md-3">
			<div class="card bg-primary text-white p-3 shadow">
				<h6>Doanh thu tháng</h6>
				<h3><c:out value="${monthlyRevenue}" default="0"/> ₫</h3>
			</div>
		</div>
		<div class="col-md-3">
			<div class="card bg-success text-white p-3 shadow">
				<h6>Đơn hàng</h6>
				<h3><c:out value="${orderCount}" default="0"/></h3>
			</div>
		</div>
		<div class="col-md-3">
			<div class="card bg-warning text-white p-3 shadow">
				<h6>Khách hàng mới</h6>
				<h3><c:out value="${newCustomers}" default="0"/></h3>
			</div>
		</div>
		<div class="col-md-3">
			<div class="card bg-danger text-white p-3 shadow">
				<h6>Sản phẩm sắp hết</h6>
				<h3><c:out value="${lowStock}" default="0"/></h3>
			</div>
		</div>
	</div>

	<!-- BIỂU ĐỒ -->
	<div class="row">
		<div class="col-md-8">
			<div class="card p-3 shadow">
				<h5 class="text-center mb-3">Doanh thu theo tháng trong năm</h5>
				<canvas id="revenueChart" height="120"></canvas>
			</div>
		</div>

		<div class="col-md-4">
			<div class="card p-3 shadow">
				<h5 class="text-center mb-3">Doanh thu theo danh mục</h5>
				<canvas id="categoryChart" height="120"></canvas>
			</div>
		</div>
	</div>
</div>

<!-- DỮ LIỆU CHO BIỂU ĐỒ -->
<script>
const revenueLabels = ${revenueLabels != null ? revenueLabels : '[]'};
const revenueValues = ${revenueValues != null ? revenueValues : '[]'};

const categoryNames = ${categoryNames != null ? categoryNames : '[]'};
const categoryRevenues = ${categoryRevenues != null ? categoryRevenues : '[]'};

document.addEventListener("DOMContentLoaded", function() {
	// BIỂU ĐỒ DOANH THU THEO THÁNG
	const ctx1 = document.getElementById('revenueChart');
	new Chart(ctx1, {
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

	// BIỂU ĐỒ DOANH THU THEO DANH MỤC
	const ctx2 = document.getElementById('categoryChart');
	new Chart(ctx2, {
	  type: 'pie',
	  data: {
	    labels: categoryNames,
	    datasets: [{
	      data: categoryRevenues,
	      backgroundColor: [
	        '#f39c12',
	        '#00a65a',
	        '#3c8dbc',
	        '#dd4b39',
	        '#605ca8',
	        '#39cccc'
	      ]
	    }]
	  },
	  options: {
	    responsive: true,
	    plugins: {
	      legend: { position: 'bottom' }
	    }
	  }
	});
});
</script>
</body>
</html>
