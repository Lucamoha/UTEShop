<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Home</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Font Roboto hỗ trợ tiếng Việt -->
<link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap&subset=vietnamese" rel="stylesheet">
<!--===============================================================================================-->
<link rel="icon" type="image/png"
	href="${pageContext.request.contextPath}/templates/images/icons/favicon.png" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/fonts/iconic/css/material-design-iconic-font.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/fonts/linearicons-v1.0.0/icon-font.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/animate/animate.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/animsition/css/animsition.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/select2/select2.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/daterangepicker/daterangepicker.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/slick/slick.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/slick/slick-theme.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/MagnificPopup/magnific-popup.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/templates/vendor/perfect-scrollbar/perfect-scrollbar.css">
<!--===============================================================================================-->
<!-- noUiSlider CSS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.css">
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/templates/css/util.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/templates/css/main.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/templates/css/price-slider.css">
<!--===============================================================================================-->

</head>
<body>
	<div>
		<%@ include file="/commons/web/header.jsp"%>
	</div>
	
	<!-- Cart Popup -->
	<%@ include file="/commons/web/cart-popup.jsp"%>
	
	<div>
		<sitemesh:write property="body" />
	</div>
	<div>
		<%@ include file="/commons/web/footer.jsp"%>
	</div>

	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/jquery/jquery-3.2.1.min.js"></script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/animsition/js/animsition.min.js"></script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/bootstrap/js/popper.js"></script>
	<script src="${pageContext.request.contextPath}/templates/vendor/bootstrap/js/bootstrap.min.js"></script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/select2/select2.min.js"></script>
	<script>
		$(".js-select2").each(function() {
			$(this).select2({
				minimumResultsForSearch : 20,
				dropdownParent : $(this).next(".dropDownSelect2"),
			});
		});
	</script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/daterangepicker/moment.min.js"></script>
	<script src="${pageContext.request.contextPath}/templates/vendor/daterangepicker/daterangepicker.js"></script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/slick/slick.min.js"></script>
	<script src="${pageContext.request.contextPath}/templates/js/slick-custom.js"></script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/parallax100/parallax100.js"></script>
	<script>
		$(".parallax100").parallax100();
	</script>
	<!--===============================================================================================-->
	<script
		src="${pageContext.request.contextPath}/templates/vendor/MagnificPopup/jquery.magnific-popup.min.js"></script>
	<script>
		$(".gallery-lb").each(function() {
			$(this).magnificPopup({
				delegate : "a",
				type : "image",
				gallery : {
					enabled : true,
				},
				mainClass : "mfp-fade",
			});
		});
	</script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/isotope/isotope.pkgd.min.js"></script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/vendor/sweetalert/sweetalert.min.js"></script>
	<script>
		$(".js-addwish-b2").on("click", function(e) {
			e.preventDefault();
		});

		$(".js-addwish-b2").each(
				function() {
					var nameProduct = $(this).parent().parent().find(
							".js-name-b2").html();
					$(this).on("click", function() {
						swal(nameProduct, "is added to wishlist !", "success");

						$(this).addClass("js-addedwish-b2");
						$(this).off("click");
					});
				});

		$(".js-addwish-detail").each(
				function() {
					var nameProduct = $(this).parent().parent().parent().find(
							".js-name-detail").html();

					$(this).on("click", function() {
						swal(nameProduct, "is added to wishlist !", "success");

						$(this).addClass("js-addedwish-detail");
						$(this).off("click");
					});
				});

		/*---------------------------------------------*/
		// Add to cart đã được xử lý bởi cart.js
		// Không cần bind swal ở đây nữa để tránh conflict
	</script>
	<!--===============================================================================================-->
	<script
		src="${pageContext.request.contextPath}/templates/vendor/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	<script>
		$(".js-pscroll").each(function() {
			$(this).css("position", "relative");
			$(this).css("overflow", "hidden");
			var ps = new PerfectScrollbar(this, {
				wheelSpeed : 1,
				scrollingThreshold : 1000,
				wheelPropagation : false,
			});

			$(window).on("resize", function() {
				ps.update();
			});
		});
	</script>
	<!--===============================================================================================-->
	<!-- noUiSlider JS -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/noUiSlider/15.7.1/nouislider.min.js"></script>
	<!--===============================================================================================-->
	<script src="${pageContext.request.contextPath}/templates/js/main.js"></script>
	<!--===============================================================================================-->
	<!-- Cart JS -->
	<script>
		var contextPath = '${pageContext.request.contextPath}';
	</script>
	<script src="${pageContext.request.contextPath}/templates/js/cart.js"></script>

</body>
</html>