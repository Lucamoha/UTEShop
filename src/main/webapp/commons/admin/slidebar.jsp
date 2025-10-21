<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Sidebar -->
<div class="sidebar" data-background-color="dark">
	<div class="sidebar-logo">
		<!-- Logo Header -->
		<div class="logo-header" data-background-color="dark">
			<div class="nav-toggle">
				<button class="btn btn-toggle toggle-sidebar">
					<i class="gg-menu-right"></i>
				</button>
				<button class="btn btn-toggle sidenav-toggler">
					<i class="gg-menu-left"></i>
				</button>
			</div>
			<button class="topbar-toggler more">
				<i class="gg-more-vertical-alt"></i>
			</button>
		</div>
		<!-- End Logo Header -->
	</div>
	<div class="sidebar-wrapper scrollbar scrollbar-inner">
		<div class="sidebar-content">
			<ul class="nav nav-secondary">
				<li class="nav-item active"><a
					href="${pageContext.request.contextPath}/admin/dashboard"> <i
						class="fas fa-home"></i>
						<p>Dashboard</p>
				</a></li>
				<li class="nav-section"><span class="sidebar-mini-icon">
						<i class="fa fa-ellipsis-h"></i>
				</span>
					<h4 class="text-section">Thành phần</h4></li>
				<li class="nav-item"><a data-bs-toggle="collapse" href="#base">
						<i class="bi bi-people"></i>
						<p>Người dùng</p> <span class="caret"></span>
				</a>
					<div class="collapse" id="base">
						<ul class="nav nav-collapse">
							<li><a
								href="${pageContext.request.contextPath}/admin/User/Users/list">
									<span class="sub-item">Quản Lý Người Dùng</span>
							</a></li>
							<li><a
								href="${pageContext.request.contextPath}/admin/Address/Addresses/list">
									<span class="sub-item">Quản Lý Địa Chỉ</span>
							</a></li>
						</ul>
					</div></li>
				<li class="nav-item"><a data-bs-toggle="collapse"
					href="#reviewsSection"> <i class="fas fa-star"></i>
						<p>Đánh Giá</p> <span class="caret"></span>
				</a>
					<div class="collapse" id="reviewsSection">
						<ul class="nav nav-collapse">
							<li><a
								href="${pageContext.request.contextPath}/admin/Review/Reviews/list">
									<span class="sub-item">Quản Lý Đánh Giá</span>
							</a></li>
						</ul>
					</div></li>
				<li class="nav-item"><a data-bs-toggle="collapse"
					href="#vouchersSection"> <i class="fas fa-tags"></i>
						<p>Voucher</p> <span class="caret"></span>
				</a>
					<div class="collapse" id="vouchersSection">
						<ul class="nav nav-collapse">
							<li><a
								href="${pageContext.request.contextPath}/admin/Voucher/Vouchers/list">
									<span class="sub-item">Quản Lý Voucher</span>
							</a></li>
						</ul>
					</div></li>
				<li class="nav-item"><a data-bs-toggle="collapse"
					href="#catalogSection"> <i class="bi bi-box-seam"></i>

						<p>Sản phẩm</p> <span class="caret"></span>
				</a>
					<div class="collapse" id="catalogSection">
						<ul class="nav nav-collapse">
							<li><a
								href="${pageContext.request.contextPath}/admin/Catalog/Categories/searchpaginated">
									<span class="sub-item">Quản Lý Danh Mục</span>
							</a></li>
							<li><a
								href="${pageContext.request.contextPath}/admin/Catalog/Products/searchpaginated">
									<span class="sub-item">Quản Lý Sản Phẩm</span>
							</a></li>
							<li><a
								href="${pageContext.request.contextPath}/admin/Catalog/OptionTypes/searchpaginated">
									<span class="sub-item">Quản Lý Loại Tùy Chọn</span>
							</a></li>
							<li><a
								href="${pageContext.request.contextPath}/admin/Catalog/OptionValues/searchpaginated">
									<span class="sub-item">Quản Lý Giá Trị Tùy Chọn</span>
							</a></li>
							<li><a
								href="${pageContext.request.contextPath}/admin/Catalog/Attributes/searchpaginated">
									<span class="sub-item">Quản Lý Thông Số Kỹ Thuật</span>
							</a></li>
						</ul>
					</div></li>
				<li class="nav-item"><a data-bs-toggle="collapse"
					href="#branchSection"> <i class="fas fa-store"></i>
						<p>Chi nhánh</p> <span class="caret"></span>
				</a>
					<div class="collapse" id="branchSection">
						<ul class="nav nav-collapse">
							<li><a
								href="${pageContext.request.contextPath}/admin/Branch/Branches/searchpaginated">
									<span class="sub-item">Quản Lý Chi Nhánh</span>
							</a></li>
						</ul>
					</div></li>
			</ul>
		</div>
	</div>
</div>
<!-- End Sidebar -->