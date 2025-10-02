<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!-- Page title / breadcrumb -->
<div class="container p-t-60 p-b-30">
	<div class="row">
		<div class="col-12">
			<h1>
				<c:choose>
					<c:when test="${not empty selectedParent}">
						<c:out value="${selectedParent.name}" />
					</c:when>
					<c:otherwise>All Products</c:otherwise>
				</c:choose>
			</h1>
		</div>
	</div>
</div>

<!-- Category filters (dynamic from childCategories) -->
<section class="bg0 p-t-23 p-b-30">
	<div class="container">
		<div class="flex-w flex-sb-m p-b-20">
			<div class="flex-w flex-l-m filter-tope-group m-tb-10">
				<c:if test="${not empty childCategories}">
					<ul class="flex-w">
						<li class="m-r-10"><a
							href="${pageContext.request.contextPath}/category/${selectedParent.slug}">Tất
								cả</a></li>
						<c:forEach var="child" items="${childCategories}">
							<li class="m-r-10"><a
								href="${pageContext.request.contextPath}/category/${selectedParent.slug}/${child.slug}">
							<c:out value="${child.name}" /></li>
							</a>
							
						</c:forEach>
					</ul>
				</c:if>

				<c:if test="${empty childCategories}">
					<div>Không có danh mục con.</div>
				</c:if>
			</div>

			<div class="flex-w flex-c-m m-tb-10">
				<div
					class="flex-c-m stext-106 cl6 size-104 bor4 pointer hov-btn3 trans-04 m-r-8 m-tb-4 js-show-filter">
					<i
						class="icon-filter cl2 m-r-6 fs-15 trans-04 zmdi zmdi-filter-list"></i>
					Filter
				</div>
				<div
					class="flex-c-m stext-106 cl6 size-105 bor4 pointer hov-btn3 trans-04 m-tb-4 js-show-search">
					<i class="icon-search cl2 m-r-6 fs-15 trans-04 zmdi zmdi-search"></i>
					Search
				</div>
			</div>
		</div>

		<!-- Products grid -->
		<div class="row isotope-grid">
			<c:choose>
				<c:when test="${not empty products}">
					<c:forEach var="product" items="${products}">
						<div class="col-sm-6 col-md-4 col-lg-3 p-b-35 isotope-item">
							<div class="block2">
								<div class="block2-pic hov-img0">
									<c:choose>
										<c:when
											test="${not empty product.images && product.images.size() > 0}">
											<img src="/uploads/${product.images[0].fileName}"
												alt="${product.name}">
										</c:when>
										<c:otherwise>
											<img src="/templates/images/no-image.png"
												alt="${product.name}">
										</c:otherwise>
									</c:choose>

									<a
										href="${pageContext.request.contextPath}/product/${product.slug}"
										class="block2-btn flex-c-m stext-103 cl2 size-102 bg0 bor2 hov-btn1 p-lr-15 trans-04 js-show-modal1">
										Quick View </a>
								</div>

								<div class="block2-txt flex-w flex-t p-t-14">
									<div class="block2-txt-child1 flex-col-l ">
										<a
											href="${pageContext.request.contextPath}/product/${product.slug}"
											class="stext-104 cl4 hov-cl1 trans-04 js-name-b2 p-b-6">
											<c:out value="${product.name}" />
										</a> <span class="stext-105 cl3"> <c:out
												value="${product.basePrice}" />
										</span>
									</div>
									<div class="block2-txt-child2 flex-r p-t-3">
										<a
											href="${pageContext.request.contextPath}/wishlist/add?productId=${product.id}"
											class="btn-addwish-b2 dis-block pos-relative js-addwish-b2">
											<img class="icon-heart1 dis-block trans-04"
											src="/views/templates/images/icons/icon-heart-01.png"
											alt="ICON"> <img
											class="icon-heart2 dis-block trans-04 ab-t-l"
											src="/views/templates/images/icons/icon-heart-02.png"
											alt="ICON">
										</a>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="col-12">
						<p>Chưa có sản phẩm trong danh mục này.</p>
					</div>
				</c:otherwise>
			</c:choose>
		</div>

		<!-- optional: pagination or load more -->
		<div class="flex-c-m flex-w w-full p-t-45">
			<a href="#"
				class="flex-c-m stext-101 cl5 size-103 bg2 bor1 hov-btn1 p-lr-15 trans-04">Load
				More</a>
		</div>
	</div>
</section>
