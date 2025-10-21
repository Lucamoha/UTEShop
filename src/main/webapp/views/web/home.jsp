<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/templates/css/home.css">
<!-- Slider -->
<section class="section-slide">
	<div class="wrap-slick1">
		<div class="slick1">
			<div class="item-slick1"
				style="background-image: url(uploads/slide/slide-01.jpg);">
				<div class="container h-full">
					<div class="flex-col-l-m h-full p-t-100 p-b-30 respon5">
						<div class="layer-slick1 animated visible-false"
							data-appear="fadeInDown" data-delay="0">
							<span class="ltext-101 cl2 respon2"> Sản phẩm Apple mới nhất
							</span>
						</div>

						<div class="layer-slick1 animated visible-false"
							data-appear="fadeInUp" data-delay="800">
							<h2 class="ltext-201 cl2 p-t-19 p-b-43 respon1">iPhone & MacBook</h2>
						</div>

						<div class="layer-slick1 animated visible-false"
							data-appear="zoomIn" data-delay="1600">
							<a href="product.html"
								class="flex-c-m stext-101 cl0 size-101 bg1 bor1 hov-btn1 p-lr-15 trans-04">
								Mua ngay </a>
						</div>
					</div>
				</div>
			</div>

			<div class="item-slick1"
				style="background-image: url(uploads/slide/slide-02.jpg);">
				<div class="container h-full">
					<div class="flex-col-l-m h-full p-t-100 p-b-30 respon5">
						<div class="layer-slick1 animated visible-false"
							data-appear="rollIn" data-delay="0">
							<span class="ltext-101 cl2 respon2"> Sản phẩm Apple mới nhất </span>
						</div>

						<div class="layer-slick1 animated visible-false"
							data-appear="lightSpeedIn" data-delay="800">
							<h2 class="ltext-201 cl2 p-t-19 p-b-43 respon1">iPhone & MacBook</h2>
						</div>

						<div class="layer-slick1 animated visible-false"
							data-appear="slideInUp" data-delay="1600">
							<a href="product.html"
								class="flex-c-m stext-101 cl0 size-101 bg1 bor1 hov-btn1 p-lr-15 trans-04">
                                Mua Ngay</a>
						</div>
					</div>
				</div>
			</div>

			<div class="item-slick1"
				style="background-image: url(uploads/slide/slide-03.jpg);">
				<div class="container h-full">
					<div class="flex-col-l-m h-full p-t-100 p-b-30 respon5">
						<div class="layer-slick1 animated visible-false"
							data-appear="rotateInDownLeft" data-delay="0">
							<span class="ltext-101 cl2 respon2"> Sản phẩm Apple mới nhất </span>
						</div>

						<div class="layer-slick1 animated visible-false"
							data-appear="rotateInUpRight" data-delay="800">
							<h2 class="ltext-201 cl2 p-t-19 p-b-43 respon1">iPhone & MacBook
							</h2>
						</div>

						<div class="layer-slick1 animated visible-false"
							data-appear="rotateIn" data-delay="1600">
							<a href="product.html"
								class="flex-c-m stext-101 cl0 size-101 bg1 bor1 hov-btn1 p-lr-15 trans-04">
								Mua ngay </a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>

<!-- Banner -->
<div class="sec-banner bg0 p-t-80 p-b-50">
	<div class="container">
		<div class="row">
			<div class="col-md-6 col-xl-4 p-b-30 m-lr-auto">
				<!-- Block1 -->
				<div class="block1 wrap-pic-w">
					<img src="uploads/banner/banner-01.jpg" alt="IMG-BANNER"> <a
						href="${pageContext.request.contextPath}/category/iphone"
						class="block1-txt ab-t-l s-full flex-col-l-sb p-lr-38 p-tb-34 trans-03 respon3">
						<div class="block1-txt-child1 flex-col-l">
							<span class="block1-name ltext-102 trans-04 p-b-8"> iPhone
							</span> <span class="block1-info stext-102 trans-04"> Mẫu mới nhất
							</span>
						</div>

						<div class="block1-txt-child2 p-b-4 trans-05">
							<div class="block1-link stext-101 cl0 trans-09">Khám phá ngay</div>
						</div>
					</a>
				</div>
			</div>

			<div class="col-md-6 col-xl-4 p-b-30 m-lr-auto">
				<!-- Block1 -->
				<div class="block1 wrap-pic-w">
					<img src="uploads/banner/banner-03.jpg" alt="IMG-BANNER"> <a
						href="${pageContext.request.contextPath}/category/macbook"
						class="block1-txt ab-t-l s-full flex-col-l-sb p-lr-38 p-tb-34 trans-03 respon3">
						<div class="block1-txt-child1 flex-col-l">
							<span class="block1-name ltext-102 trans-04 p-b-8">
								Mac </span> <span class="block1-info stext-102 trans-04">
								Xu hướng công nghệ </span>
						</div>

						<div class="block1-txt-child2 p-b-4 trans-05">
							<div class="block1-link stext-101 cl0 trans-09">Khám phá ngay</div>
						</div>
					</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Sản phẩm mới -->
<section class="sec-product bg0 p-t-80 p-b-50">
    <div class="container">
        <div class="p-b-32">
            <h3 class="ltext-105 cl5 txt-center respon1">
                Sản Phẩm Mới Nhất
            </h3>
        </div>
        <div class="row isotope-grid">
            <c:forEach var="product" items="${topLatestProducts}">
                <div class="col-sm-6 col-md-4 col-lg-3 p-b-35 isotope-item">
                    <div class="block2">
                        <div class="block2-pic hov-img0">
                            <c:url value="/image?fname=${product.getImages()[0].getImageUrl()}" var="imgUrl"></c:url>
                            <img class="product-img" src="${imgUrl}" alt="${product.name}">


                            <a href="${pageContext.request.contextPath}/product-detail?product=${product.slug}" class="block2-btn flex-c-m stext-103 cl2 size-102 bg0 bor2 hov-btn1 p-lr-15 trans-04">
                                Xem chi tiết
                            </a>
                        </div>
                        <div class="block2-txt flex-w p-t-14">
                            <div class="block2-txt-child1 flex-col-l">
                                <a href="${pageContext.request.contextPath}/product-detail?product=${product.slug}" class="stext-104 cl4 hov-cl1 trans-04 js-name-b2 p-b-6">
                                        ${product.name}
                                </a>
                                <span class="stext-105 cl3">
                                    <fmt:formatNumber value="${product.basePrice}" type="number" /> VND
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>
