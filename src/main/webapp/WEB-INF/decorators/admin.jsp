<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html lang="en">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Apple Store Admin Dashboard</title>
<meta content="width=device-width, initial-scale=1.0, shrink-to-fit=no"
	name="viewport" />
<link rel="icon"
	href="${pageContext.request.contextPath}/templates/admin/img/kaiadmin/favicon.ico"
	type="image/x-icon" />

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<!-- Fonts and icons -->
<script
	src="${pageContext.request.contextPath}/templates/admin/js/plugin/webfont/webfont.min.js"></script>
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
            urls: ["${pageContext.request.contextPath}/templates/admin/css/fonts.min.css"]
        },
        active: function () {
            sessionStorage.fonts = true;
        }
    });
</script>


<!-- CSS Files -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/templates/admin/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/templates/admin/css/plugins.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/templates/admin/css/kaiadmin.min.css" />

<!-- CSS Just for demo purpose, don't include it in your project -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/templates/admin/css/demo.css" />
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</head>
<body>
	<div class="wrapper">
		<!-- Sidebar -->
		<%@ include file="/commons/admin/slidebar.jsp"%>
		<!-- End Sidebar -->

		<div class="main-panel">
			<div class="main-header">
				<div class="main-header-logo">
					<!-- Logo Header -->
					<%@ include file="/commons/admin/logoHeader.jsp"%>
					<!-- End Logo Header -->
				</div>

				<!-- Navbar Header -->
				<%@ include file="/commons/admin/navbarheader.jsp"%>
				<!-- End Navbar -->
			</div>
			
			<div class="container">
          		<div class="page-inner">
				<sitemesh:write property="body" />
				</div>
	        </div>
		

			<%@ include file="/commons/admin/footer.jsp"%>
			</div>
		</div>

		<!--   Core JS Files   -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/core/jquery-3.7.1.min.js"></script>
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/core/popper.min.js"></script>
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/core/bootstrap.min.js"></script>

		<!-- jQuery Scrollbar -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"></script>

		<!-- Chart JS -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/chart.js/chart.min.js"></script>

		<!-- jQuery Sparkline -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/jquery.sparkline/jquery.sparkline.min.js"></script>

		<!-- Chart Circle -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/chart-circle/circles.min.js"></script>

		<!-- Datatables -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/datatables/datatables.min.js"></script>

		<!-- Bootstrap Notify -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/bootstrap-notify/bootstrap-notify.min.js"></script>

		<!-- jQuery Vector Maps -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/jsvectormap/jsvectormap.min.js"></script>
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/jsvectormap/world.js"></script>

		<!-- Sweet Alert -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/plugin/sweetalert/sweetalert.min.js"></script>

		<!-- Kaiadmin JS -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/kaiadmin.min.js"></script>

		<!-- Kaiadmin DEMO methods, don't include it in your project! -->
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/setting-demo.js"></script>
		<script
			src="${pageContext.request.contextPath}/templates/admin/js/demo.js"></script>
</body>
</html>