<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="currentUserEmail" value="${authenticatedEmail}" />

<div class="main-header-logo">
  <!-- Logo Header -->
  <div class="logo-header" data-background-color="white">
    <a href="${pageContext.request.contextPath}/manager/dashboard" class="logo">
      <img src="${pageContext.request.contextPath}/templates/images/icons/logo-01.png" alt="navbar brand" class="navbar-brand" height="40">
    </a>
    <div class="nav-toggle">
      <button class="btn btn-toggle toggle-sidebar toggled"><i class="gg-more-vertical-alt"></i></button>
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
<nav class="navbar navbar-header navbar-header-transparent navbar-expand-lg border-bottom">
  <div class="container-fluid">
    <nav class="navbar navbar-header-left navbar-expand-lg navbar-form nav-search p-0 d-none d-lg-flex">
      <span class="fw-bold">${sessionScope.branchName}</span>
    </nav>
    <ul class="navbar-nav topbar-nav ms-md-auto align-items-center">
      <li class="nav-item topbar-user dropdown hidden-caret submenu">
        <a class="dropdown-toggle profile-pic" data-bs-toggle="dropdown" href="#" aria-expanded="false">
          <div class="avatar-sm">
            <img src="${pageContext.request.contextPath}/templates/manager/img/logoproduct3.svg" alt="..." class="avatar-img rounded-circle">
          </div>
          <span class="profile-username">
                      <span class="op-7">Hi,</span>
                      <span class="fw-bold">${authenticatedEmail}</span>
                    </span>
        </a>
        <ul class="dropdown-menu dropdown-user animated fadeIn">
          <div class="scroll-wrapper dropdown-user-scroll scrollbar-outer" style="position: relative;"><div class="dropdown-user-scroll scrollbar-outer scroll-content" style="height: 167px; margin-bottom: 0px; margin-right: 0px; max-height: none;">
            <li>
              <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
            </li>
          </div><div class="scroll-element scroll-x"><div class="scroll-element_outer"><div class="scroll-element_size"></div><div class="scroll-element_track"></div><div class="scroll-bar" style="width: 0px;"></div></div></div><div class="scroll-element scroll-y"><div class="scroll-element_outer"><div class="scroll-element_size"></div><div class="scroll-element_track"></div><div class="scroll-bar" style="height: 0px;"></div></div></div></div>
        </ul>
      </li>
    </ul>
  </div>
</nav>