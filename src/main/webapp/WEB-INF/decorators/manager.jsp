<%@ page language="java" contentType="text/html;charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <title>Manager</title>
  <meta content="width=device-width, initial-scale=1.0, shrink-to-fit=no"
        name="viewport" />
  <link rel="icon"
        href="${pageContext.request.contextPath}/templates/images/icons/logo-01.png"
        type="image/x-icon" />

  <!-- Fonts and icons -->
  <script
          src="${pageContext.request.contextPath}/templates/manager/js/plugin/webfont/webfont.min.js"></script>
  <script>
    WebFont.load({
      google: {
        families: ["Public Sans:300,400,500,600,700"]
      },
      custom: {
        families: [
          "Font Awesome 5 Solid",
          "Font Awesome 5 Regular",
          "Font Awesome 5 Brands",
          "simple-line-icons"
        ],
        urls: ["${pageContext.request.contextPath}/templates/manager/css/fonts.min.css"]
      },
      active: function () {
        sessionStorage.fonts = true;
      }
    });
  </script>


  <!-- CSS Files -->
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/templates/manager/css/bootstrap.min.css" />
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/templates/manager/css/plugins.min.css" />
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/templates/manager/css/kaiadmin.min.css" />

  <!-- Charts -->
  <script src="https://cdn.jsdelivr.net/npm/chart.js@4.5.0/dist/chart.umd.min.js"></script>
</head>
<body>
<div class="wrapper">
  <!-- Sidebar -->
  <%@ include file="/commons/manager/sidebar.jsp"%>
  <!-- End Sidebar -->

  <div class="main-panel">
    <div class="main-header">
      <%@ include file="/commons/manager/header.jsp"%>
    </div>
    <div class="container">
      <sitemesh:write property="body" />
    </div>
  </div>
</div>

<!--   Core JS Files   -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/core/jquery-3.7.1.min.js"></script>
<script
        src="${pageContext.request.contextPath}/templates/manager/js/core/popper.min.js"></script>
<script
        src="${pageContext.request.contextPath}/templates/manager/js/core/bootstrap.min.js"></script>

<!-- jQuery Scrollbar -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"></script>

<!-- jQuery Sparkline -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/plugin/jquery.sparkline/jquery.sparkline.min.js"></script>

<!-- Chart Circle -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/plugin/chart-circle/circles.min.js"></script>

<!-- Datatables -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/plugin/datatables/datatables.min.js"></script>

<!-- Bootstrap Notify -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/plugin/bootstrap-notify/bootstrap-notify.min.js"></script>

<!-- jQuery Vector Maps -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/plugin/jsvectormap/jsvectormap.min.js"></script>
<script
        src="${pageContext.request.contextPath}/templates/manager/js/plugin/jsvectormap/world.js"></script>

<!-- Sweet Alert -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/plugin/sweetalert/sweetalert.min.js"></script>

<!-- Kaiadmin JS -->
<script
        src="${pageContext.request.contextPath}/templates/manager/js/kaiadmin.min.js"></script>

<script>
  $("#lineChart").sparkline([ 102, 109, 120, 99, 110, 105, 115 ], {
    type : "line",
    height : "70",
    width : "100%",
    lineWidth : "2",
    lineColor : "#177dff",
    fillColor : "rgba(23, 125, 255, 0.14)",
  });

  $("#lineChart2").sparkline([ 99, 125, 122, 105, 110, 124, 115 ], {
    type : "line",
    height : "70",
    width : "100%",
    lineWidth : "2",
    lineColor : "#f3545d",
    fillColor : "rgba(243, 84, 93, .14)",
  });

  $("#lineChart3").sparkline([ 105, 103, 123, 100, 95, 105, 115 ], {
    type : "line",
    height : "70",
    width : "100%",
    lineWidth : "2",
    lineColor : "#ffa534",
    fillColor : "rgba(255, 165, 52, .14)",
  });
</script>
</body>
</html>