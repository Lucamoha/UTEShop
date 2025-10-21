<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- Navbar Header -->
<nav
	class="navbar navbar-header navbar-header-transparent navbar-expand-lg border-bottom">
	<div class="container-fluid">

		<ul class="navbar-nav align-items-center me-auto">
			<li class="nav-item topbar-user dropdown hidden-caret"><a
				href="#" class="logo"> <img
					src="${pageContext.request.contextPath}/templates/images/icons/logo-01.png"
					alt="IMG-LOGO" height="90" />
			</a></li>
		</ul>

		<ul class="navbar-nav align-items-center ms-auto">
			<li class="nav-item topbar-user dropdown hidden-caret"><a
				class="dropdown-toggle profile-pic" data-bs-toggle="dropdown"
				href="#" aria-expanded="false">
					<div
						class="avatar-sm d-flex align-items-center justify-content-center"
						style="width: 40px; height: 40px; background: #f0f0f0; border-radius: 50%;">
						<i class="bi bi-person-circle"
							style="font-size: 24px; color: #6c757d;"></i>
					</div> <span class="profile-username ms-2"> <span class="op-7">Xin
							chào,</span> <span class="fw-bold">${sessionScope.user.fullName}</span>
				</span>

			</a>
				<ul class="dropdown-menu dropdown-user animated fadeIn">
					<li>
						<div class="dropdown-user-scroll scrollbar-outer">
							<div class="user-box d-flex align-items-center">
								<div
									class="avatar-lg d-flex align-items-center justify-content-center rounded-circle bg-light">
									<i class="bi bi-person-circle"
										style="font-size: 48px; color: #6c757d;"></i>
								</div>
								<div class="u-text ms-3">
									<h4 class="mb-0">${sessionScope.user.fullName}</h4>
									<p class="text-muted mb-2">${sessionScope.user.email}</p>
									<a href="${pageContext.request.contextPath}/admin/profile"
										class="btn btn-xs btn-secondary btn-sm">Xem trang cá nhân</a>
								</div>
							</div>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item"
								href="${pageContext.request.contextPath}/admin/profile"> <i
								class="fas fa-user"></i> Trang cá nhân
							</a>
							<div class="dropdown-divider"></div>
							<a class="dropdown-item"
								href="${pageContext.request.contextPath}/logout"> <i
								class="fas fa-sign-out-alt"></i> Đăng xuất
							</a>
						</div>
					</li>
				</ul></li>
		</ul>
	</div>
</nav>
<!-- End Navbar -->