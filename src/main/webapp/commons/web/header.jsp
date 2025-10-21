<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%> <%@ include file="/commons/taglib.jsp"%>
<c:set var="currentUserEmail" value="${authenticatedEmail}" />
<c:set var="currentUserRole" value="${authenticatedRole}" />
<c:set var="isUserLoggedIn" value="${not empty currentUserEmail}" />

<!-- Header -->
<header class="fixed-top">
    <!-- Header desktop -->
    <div class="container-menu-desktop">
        <!-- Topbar -->
        <div class="top-bar">
            <div class="content-topbar flex-sb-m h-full container">
                <div class="left-top-bar">

                </div>

                <div class="right-top-bar flex-w h-full">
                    <c:choose>
                        <c:when test="${isUserLoggedIn}">
                            <!-- User đã đăng nhập -->
                            <div class="user-dropdown" style="position: relative; display: inline-flex; align-items: center;">
                                <span class="flex-c-m trans-04 p-lr-15 user-dropdown-toggle" style="color: #fff; cursor: pointer; display: inline-flex; align-items: center;">
                                    <i class="fa fa-user" style="margin-right: 5px"></i>
                                    <span style="display: inline-block; vertical-align: middle;">${sessionScope.user.fullName}</span>
                                    <i class="fa fa-caret-down" style="margin-left: 5px"></i>
                                </span>
                                <div class="user-dropdown-menu" style="display: none; position: absolute; top: calc(100% + 10px); right: 0; background: #ffffff; min-width: 220px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); z-index: 9999; border-radius: 6px; overflow: hidden; border: 1px solid #e0e0e0;">
                                    <a href="${pageContext.request.contextPath}/profile" class="dropdown-item user-dropdown-item" style="display: flex; align-items: center; padding: 12px 16px; color: #333; text-decoration: none; transition: all 0.2s; border-bottom: 1px solid #f0f0f0; background: #fff;">
                                        <i class="fa fa-user-circle" style="margin-right: 10px; font-size: 16px;"></i> 
                                        <span>Thông tin tài khoản</span>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/orders" class="dropdown-item user-dropdown-item" style="display: flex; align-items: center; padding: 12px 16px; color: #333; text-decoration: none; transition: all 0.2s; background: #fff;">
                                        <i class="fa fa-list-alt" style="margin-right: 10px; font-size: 16px;"></i> 
                                        <span>Danh sách đơn hàng</span>
                                    </a>
                                </div>
                            </div>
                            <c:if test="${currentUserRole == 'ADMIN'}">
                                <a
                                        href="${pageContext.request.contextPath}/admin/dashboard"
                                        class="flex-c-m trans-04 p-lr-15"
                                >
                                    <i class="fa fa-dashboard"></i> Admin
                                </a>
                            </c:if>
                            <c:if test="${currentUserRole == 'MANAGER'}">
                                <a
                                        href="${pageContext.request.contextPath}/manager/dashboard"
                                        class="flex-c-m trans-04 p-lr-15"
                                >
                                    <i class="fa fa-dashboard"></i> Manager
                                </a>
                            </c:if>
                            <a
                                    href="${pageContext.request.contextPath}/logout"
                                    class="flex-c-m trans-04 p-lr-15"
                            >
                                <i class="fa fa-sign-out"></i> Đăng xuất
                            </a>
                        </c:when>
                        <c:otherwise>
                            <!-- User chưa đăng nhập -->
                            <a
                                    href="${pageContext.request.contextPath}/login"
                                    class="flex-c-m trans-04 p-lr-25"
                            >
                                <i class="fa fa-sign-in"></i> Đăng nhập
                            </a>
                            <a
                                    href="${pageContext.request.contextPath}/register"
                                    class="flex-c-m trans-04 p-lr-25"
                            >
                                <i class="fa fa-user-plus"></i> Đăng ký
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="wrap-menu-desktop">
            <nav class="limiter-menu-desktop container">
                <!-- Logo desktop -->
                <a href="#" class="logo">
                    <img
                            src="${pageContext.request.contextPath}/templates/images/icons/logo-01.png"
                            alt="IMG-LOGO"
                    />
                </a>

                <!-- Menu desktop -->
                <div class="menu-desktop">
                    <ul class="main-menu">
                        <!-- Trang chủ -->
                        <li class="${empty selectedParent ? 'active-menu' : ''}">
                            <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                        </li>

                        <!-- Danh mục cha -->
                        <c:forEach var="cate" items="${parentCategories}">
                            <li
                                    class="${not empty selectedParent and selectedParent.id == cate.id ? 'active-menu' : ''}"
                            >
                                <a
                                        href="${pageContext.request.contextPath}/category/${cate.slug}"
                                >
                                        ${cate.name}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <!-- Icon header -->
                <div class="wrap-icon-header flex-w flex-r-m">
                    <div
                            class="icon-header-item cl2 hov-cl1 trans-04 p-l-22 p-r-11 icon-header-noti js-show-cart"
                            data-notify="2"
                    >
                        <i class="zmdi zmdi-shopping-cart"></i>
                    </div>
                </div>
            </nav>
        </div>
    </div>

    <!-- Header Mobile -->
    <div class="wrap-header-mobile">
        <!-- Logo moblie -->
        <div class="logo-mobile">
            <a href="index.html"
            ><img src="images/icons/logo-01.png" alt="IMG-LOGO"
            /></a>
        </div>

        <!-- Icon header -->
        <div class="wrap-icon-header flex-w flex-r-m m-r-15">
            <div
                    class="icon-header-item cl2 hov-cl1 trans-04 p-r-11 icon-header-noti js-show-cart"
                    data-notify="2"
            >
                <i class="zmdi zmdi-shopping-cart"></i>
            </div>
        </div>

        <!-- Button show menu -->
        <div class="btn-show-menu-mobile hamburger hamburger--squeeze">
            <span class="hamburger-box"> <span class="hamburger-inner"></span> </span>
        </div>
    </div>

  <!-- Menu Mobile -->
  <div class="menu-mobile">
    <ul class="topbar-mobile">
      <li>
        <div class="left-top-bar">

        </div>
      </li>

      <li>
        <div class="right-top-bar flex-w h-full">
          <c:choose>
            <c:when test="${isUserLoggedIn}">
              <!-- User đã đăng nhập -->
              <div class="user-dropdown-mobile" style="position: relative; width: 100%;">
                <span class="flex-c-m p-lr-10 trans-04 user-dropdown-toggle-mobile" style="color: #fff; cursor: pointer;">
                  <i class="fa fa-user"></i> ${currentUserEmail}
                  <i class="fa fa-caret-down" style="margin-left: 5px"></i>
                </span>
                <div class="user-dropdown-menu-mobile" style="display: none; background: rgba(255,255,255,0.1); margin-top: 5px; border-radius: 4px;">
                  <a href="${pageContext.request.contextPath}/profile" style="display: block; padding: 8px 15px; color: #fff; text-decoration: none;">
                    <i class="fa fa-user-circle" style="margin-right: 8px;"></i> Thông tin tài khoản
                  </a>
                  <a href="${pageContext.request.contextPath}/orders" style="display: block; padding: 8px 15px; color: #fff; text-decoration: none;">
                    <i class="fa fa-list-alt" style="margin-right: 8px;"></i> Danh sách đơn hàng
                  </a>
                </div>
              </div>
            </c:when>
            <c:otherwise>
              <!-- User chưa đăng nhập -->
              <a href="${pageContext.request.contextPath}/login" class="flex-c-m p-lr-10 trans-04">
                <i class="fa fa-sign-in"></i> Đăng nhập
              </a>
              <a href="${pageContext.request.contextPath}/register" class="flex-c-m p-lr-10 trans-04">
                <i class="fa fa-user-plus"></i> Đăng ký
              </a>
            </c:otherwise>
          </c:choose>
        </div>
      </li>
    </ul>

    <ul class="main-menu-m">
      <!-- Trang chủ -->
      <li>
        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
      </li>

      <!-- Danh mục cha -->
      <c:forEach var="cate" items="${parentCategories}">
        <li>
          <a href="${pageContext.request.contextPath}/category/${cate.slug}">
            ${cate.name}
          </a>
        </li>
      </c:forEach>

      <!-- Đăng nhập/Đăng xuất cho Mobile -->
      <c:choose>
        <c:when test="${isUserLoggedIn}">
          <c:if test="${currentUserRole == 'ADMIN'}">
            <li>
              <a href="${pageContext.request.contextPath}/admin/dashboard">
                <i class="fa fa-dashboard"></i> Admin Dashboard
              </a>
            </li>
          </c:if>
          <c:if test="${currentUserRole == 'MANAGER'}">
            <li>
              <a href="${pageContext.request.contextPath}/manager/dashboard">
                <i class="fa fa-dashboard"></i> Manager Dashboard
              </a>
            </li>
          </c:if>
          <li>
            <a href="${pageContext.request.contextPath}/logout">
              <i class="fa fa-sign-out"></i> Đăng xuất
            </a>
          </li>
        </c:when>
        <c:otherwise>
          <li>
            <a href="${pageContext.request.contextPath}/login">
              <i class="fa fa-sign-in"></i> Đăng nhập
            </a>
          </li>
          <li>
            <a href="${pageContext.request.contextPath}/register">
              <i class="fa fa-user-plus"></i> Đăng ký
            </a>
          </li>
        </c:otherwise>
      </c:choose>
    </ul>
  </div>    <!-- Modal Search -->
    <div class="modal-search-header flex-c-m trans-04 js-hide-modal-search">
        <div class="container-search-header">
            <button
                    class="flex-c-m btn-hide-modal-search trans-04 js-hide-modal-search"
            >
                <img src="images/icons/icon-close2.png" alt="CLOSE" />
            </button>

            <form class="wrap-search-header flex-w p-l-15">
                <button class="flex-c-m trans-04">
                    <i class="zmdi zmdi-search"></i>
                </button>
                <input class="plh3" type="text" name="search" placeholder="Search..." />
            </form>
        </div>
    </div>
</header>

<!-- Dropdown CSS and JavaScript -->
<style>
.user-dropdown .user-dropdown-item:hover {
    background-color: #717fe0 !important;
    color: #fff !important;
}

.user-dropdown .user-dropdown-item:hover i {
    color: #fff !important;
}

.user-dropdown .user-dropdown-item:hover span {
    color: #fff !important;
}

.user-dropdown-toggle:hover {
    opacity: 0.8;
}

.user-dropdown-mobile .user-dropdown-menu-mobile a:hover {
    background-color: rgba(255,255,255,0.2);
}
</style>

<script>
// Dropdown cho desktop
document.addEventListener('DOMContentLoaded', function() {
    const userDropdown = document.querySelector('.user-dropdown');
    if (userDropdown) {
        const toggle = userDropdown.querySelector('.user-dropdown-toggle');
        const menu = userDropdown.querySelector('.user-dropdown-menu');
        
        toggle.addEventListener('click', function(e) {
            e.stopPropagation();
            menu.style.display = menu.style.display === 'none' ? 'block' : 'none';
        });
        
        // Đóng dropdown khi click bên ngoài
        document.addEventListener('click', function(e) {
            if (!userDropdown.contains(e.target)) {
                menu.style.display = 'none';
            }
        });
    }
    
    // Dropdown cho mobile
    const userDropdownMobile = document.querySelector('.user-dropdown-mobile');
    if (userDropdownMobile) {
        const toggleMobile = userDropdownMobile.querySelector('.user-dropdown-toggle-mobile');
        const menuMobile = userDropdownMobile.querySelector('.user-dropdown-menu-mobile');
        
        toggleMobile.addEventListener('click', function(e) {
            e.stopPropagation();
            menuMobile.style.display = menuMobile.style.display === 'none' ? 'block' : 'none';
        });
    }
});
</script>
