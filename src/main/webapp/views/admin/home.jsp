<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div
	class="d-flex align-items-center justify-content-between pt-2 pb-4 flex-wrap">
	<h1 class="fw-bold mb-3 mb-md-0">DASHBOARD</h1>

	<div class="col-md-4 col-sm-12">
		<div class="card card-round mb-0">
			<div class="card-body pb-0">
				<h2 class="mb-2">Doanh thu tháng ${currentMonth}</h2>
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
	<div class="col-md-12">
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
</div>
<div class="row">
	<div class="col-md-12">
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
					<!-- Projects table -->
					<table class="table align-items-center mb-0">
						<thead class="thead-light">
							<tr>
								<th scope="col">Tên sản phẩm</th>
								<th scope="col">Số lượng đã bán</th>
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
									<th scope="col">Tên sản phẩm</th>
									<th scope="col">Tồn kho</th>
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

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
	const revenueLabels = ${
		revenueLabels != null ? revenueLabels : '[]'
	};
	const revenueValues = ${
		revenueValues != null ? revenueValues : '[]'
	};
	const dailyLabels = ${
		dailyLabels != null ? dailyLabels : '[]'
	};
	const dailyValues = ${
		dailyValues != null ? dailyValues : '[]'
	};

	document.addEventListener("DOMContentLoaded", function() {
		// BIỂU ĐỒ DOANH THU THEO THÁNG
		const ctxRevenue = document.getElementById('revenueChart');
		new Chart(ctxRevenue, {
			type : 'line',
			data : {
				labels : revenueLabels,
				datasets : [ {
					label : 'Doanh thu (VND)',
					data : revenueValues,
					borderWidth : 2,
					borderColor : '#007bff',
					fill : false,
					tension : 0.3
				} ]
			},
			/* options : {
				responsive : true,
				scales : {
					y : {
						beginAtZero : true
					}
				}
			} */
			
			options: {
			    responsive: true,
			    scales: {
			        y: {
			            beginAtZero: true,
			            ticks: {
			                callback: function(value) {
			                    const num = Number(value);
			                    if (isNaN(num)) return value;
			                    return new Intl.NumberFormat('vi-VN').format(num) + ' ₫';
			                }
			            }
			        }
			    },
			    plugins: {
			        tooltip: {
			            callbacks: {
			                label: function(context) {
			                    const value = Number(context.parsed.y);
			                    if (isNaN(value)) return '';
			                    return 'Doanh thu: ' + new Intl.NumberFormat('vi-VN').format(value) + ' ₫';
			                }
			            }
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
					fill : false,
					tension : 0.3
				} ]
			},
			/* options : {
				responsive : true,
				scales : {
					y : {
						beginAtZero : true
					}
				}
			} */
			options: {
			    responsive: true,
			    scales: {
			        y: {
			            beginAtZero: true,
			            ticks: {
			                callback: function(value) {
			                    const num = Number(value);
			                    if (isNaN(num)) return value;
			                    return new Intl.NumberFormat('vi-VN').format(num) + ' ₫';
			                }
			            }
			        }
			    },
			    plugins: {
			        tooltip: {
			            callbacks: {
			                label: function(context) {
			                    const value = Number(context.parsed.y);
			                    if (isNaN(value)) return '';
			                    return 'Doanh thu: ' + new Intl.NumberFormat('vi-VN').format(value) + ' ₫';
			                }
			            }
			        }
			    }
			}
		});
	});
</script>