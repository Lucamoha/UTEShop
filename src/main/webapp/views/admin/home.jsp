<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div
	class="d-flex align-items-center justify-content-between pt-2 pb-4 flex-wrap">
	<h1 class="fw-bold mb-3 mb-md-0">DASHBOARD</h1>
</div>

<div class="row align-items-center pt-2 pb-4">
	<!-- Cột trái: bộ lọc -->
	<div class="col-md-8 col-sm-12">
		<form action="${pageContext.request.contextPath}/admin/dashboard"
			method="post" class="d-flex align-items-center flex-wrap gap-3">

			<div class="d-flex align-items-center me-2 flex-nowrap"
				style="white-space: nowrap;">
				<label for="branch" class="me-2 mb-0 fw-semibold white-space-nowrap">Chi
					nhánh:</label> <select name="branchId" id="branch"
					class="form-select form-select-sm">
					<option value="0" ${branchId == 0 ? "selected" : ""}>Tất
						cả</option>
					<c:forEach var="b" items="${branchList}">
						<option value="${b.id}" ${branchId == b.id ? "selected" : ""}>${b.name}</option>
					</c:forEach>
				</select>
			</div>

			<div class="d-flex align-items-center">
				<label for="month" class="me-2 mb-0 fw-semibold">Tháng:</label> <select
					name="month" id="month" class="form-select form-select-sm">
					<c:forEach var="m" begin="1" end="12">
						<option value="${m}" ${m == selectedMonth ? 'selected' : ''}>${m}</option>
					</c:forEach>
				</select>
			</div>

			<div class="d-flex align-items-center">
				<label for="year" class="me-2 mb-0 fw-semibold">Năm:</label> <select
					name="year" id="year" class="form-select form-select-sm">
					<c:forEach var="y" items="${years}">
						<option value="${y}" ${y == selectedYear ? 'selected' : ''}>${y}</option>
					</c:forEach>
				</select>
			</div>

			<button type="submit" class="btn btn-primary btn-sm">Xem báo
				cáo</button>
		</form>
	</div>

	<div class="col-md-4 col-sm-12">
		<div class="card card-round mb-0">
			<div class="card-body pb-0">
				<h3 class="mb-2">Doanh thu tháng ${selectedMonth}</h3>
				<p class="text-muted">
					<fmt:formatNumber value="${revenue.intValue()}" type="number"
						groupingUsed="true" />
					VND
				</p>
			</div>
		</div>
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
							<p class="card-category">Tổng khách hàng đến tháng ${selectedMonth}</p>
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
							<p class="card-category">Khách hàng mới tháng
								${selectedMonth}</p>
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
							<p class="card-category">Đơn đã thanh toán tháng
								${selectedMonth}</p>
							<h4 class="card-title">${orderCount}</h4>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<div class="card card-round">
			<div class="card-header">
				<div class="card-head-row">
					<div class="card-title">Doanh thu theo tháng trong năm
						${selectedYear}</div>
				</div>
			</div>
			<div class="card-body">
				<div class="chart-container" style="min-height: 375px">
					<canvas id="revenueChart"></canvas>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<div class="card card-round">
			<div class="card-header">
				<div class="card-head-row">
					<div class="card-title">Doanh thu theo ngày trong tháng
						${selectedMonth}</div>
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

<div class="row">
	<div class="col-md-12">
		<div class="card card-round">
			<div class="card-header">
				<div class="card-head-row card-tools-still-right">
					<div class="card-title">Top 10 sản phẩm bán chạy</div>
				</div>
			</div>
			<div class="card-body p-0">
				<div class="table-responsive">
					<table class="table align-items-center mb-0">
						<thead class="thead-light">
							<tr>
								<th scope="col" style="width: 60%;">Tên sản phẩm</th>
								<th scope="col" style="width: 40%;">Số lượng đã bán</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="sellingProduct" items="${topSellingList}">
								<tr>
									<th>${sellingProduct[0]}</th>
									<th>${sellingProduct[1]}</th>
								</tr>
							</c:forEach>
						</tbody>
					</table>		
				</div>
			</div>
		</div>
	</div>
</div>


<div class="row">
	<div class="col-md-12">
		<div class="card card-round">
			<div class="card-header">
				<div class="card-head-row card-tools-still-right">
					<div class="card-title">Sản phẩm sắp hết hàng</div>
				</div>
			</div>
			<div class="card-body p-0">
				<c:if test="${empty lowStockList}">
					<div class="alert alert-info">Không có sản phẩm sắp hết hàng</div>
				</c:if>

				<c:if test="${not empty lowStockList}">
					<div class="table-responsive">
						<table class="table align-items-center mb-0">
							<thead class="thead-light">
								<tr>
									<th scope="col" style="width: 60%;">Tên sản phẩm</th>
									<th scope="col" style="width: 40%;">Tồn kho</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="item" items="${lowStockList}">
									<tr>
										<th>${item[1]}</th>
										<th>${item[2]}</th>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
			</div>
		</div>
	</div>
</div>

<script>
	const revenueLabels = ${revenueLabels != null ? revenueLabels : "[]"};
	const revenueValues = ${revenueValues != null ? revenueValues : "[]"}; 
	const dailyLabels = ${dailyLabels != null ? dailyLabels : "[]"};
	const dailyValues = ${dailyValues != null ? dailyValues : "[]"};

	console.log(revenueValues);

	document.addEventListener("DOMContentLoaded", function() {
		// BIỂU ĐỒ DOANH THU THEO THÁNG
		const ctxRevenue = document.getElementById('revenueChart');
		Chart.defaults.scale.ticks.callback = undefined;
		new Chart(ctxRevenue, {
			type : 'line',
			data : {
				labels : revenueLabels,
				datasets : [ {
					label : 'Doanh thu (VND)',
					data : revenueValues,
					borderWidth : 2,
					borderColor : '#007bff',
					backgroundColor : 'rgba(0, 123, 255, 0.1)',
					fill : true,
					tension : 0.3
				} ]
			},
			options : {
				responsive : true,
				maintainAspectRatio : false,
				scales : {
					y : {
						beginAtZero : true,
						ticks : {
							callback: function(value) {
								console.log('Tick value:', value);
							    return new Intl.NumberFormat('vi-VN').format(Number(value)) + ' VND';
							}
						}
					}
				},
				plugins : {
					tooltip : {
						callbacks : {
							label : function(context) {
								let label = context.dataset.label || '';
								if (label) {
									label += ': ';
								}
								const value = context.parsed.y;
								label += value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + ' VND';
								return label;
							}
						}
					},
					legend : {
						display : true,
						position : 'top'
					}
				}
			}
		});

		// BIỂU ĐỒ DOANH THU THEO NGÀY
		const ctxDaily = document.getElementById('dayRevenueChart');
		new Chart(ctxDaily, {
			type : 'line',
			data : {
				labels : dailyLabels,
				datasets : [ {
					label : 'Doanh thu (VND)',
					data : dailyValues,
					borderWidth : 2,
					borderColor : '#28a745',
					backgroundColor : 'rgba(40, 167, 69, 0.1)',
					fill : true,
					tension : 0.3
				} ]
			},
			options : {
				responsive : true,
				maintainAspectRatio : false,
				scales : {
					y : {
						beginAtZero : true,
						ticks : {
							callback: function(value) {
							    return new Intl.NumberFormat('vi-VN').format(Number(value)) + ' VND';
							  }
						}
					}
				},
				plugins : {
					tooltip : {
						callbacks : {
							label : function(context) {
								let label = context.dataset.label || '';
								if (label) {
									label += ': ';
								}
								const value = context.parsed.y;
								label += value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + ' VND';
								return label;
							}
						}
					},
					legend : {
						display : true,
						position : 'top'
					}
				}
			}
		});
	});
</script>