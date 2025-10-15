<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="sidebar" data-background-color="white">
    <div class="sidebar-logo">
        <!-- Logo Header -->
        <div class="logo-header" data-background-color="white">
            <a href="${pageContext.request.contextPath}/manager/dashboard" class="logo">
                <img src="${pageContext.request.contextPath}/templates/images/icons/logo-01.png" alt="navbar brand" class="navbar-brand" height="60">
            </a>
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
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/manager/dashboard">
                        <i class="fas fa-home"></i>
                        <p>Trang chủ</p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/manager/orders">
                        <i class="fas fa-shopping-cart"></i>
                        <p>Đơn hàng</p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/manager/inventory">
                        <i class="fa fa-archive"></i>
                        <p>Quản lý kho</p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/manager/ordersStats">
                        <i class="far fa-chart-bar"></i>
                        <p>Thống kê bán hàng</p>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>