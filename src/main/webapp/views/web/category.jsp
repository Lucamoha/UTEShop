<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/templates/css/home.css">

<!-- Page title / breadcrumb -->
<div class="container p-t-60 p-b-30">
	<div class="row">
		<div class="col-12">
			<h1></h1>
		</div>
	</div>
</div>

<!-- Category filters -->
<section class="bg0 p-t-23 p-b-30">
	<div class="container">
		<div class="flex-w flex-sb-m p-b-20">
			<div
				class="category-tabs flex-w flex-l-m filter-tope-group m-tb-20 justify-content-center">
				<c:if test="${not empty childCategories}">
					<ul class="nav nav-pills flex-wrap justify-content-center">
						<!-- Nút "Tất cả" -->
						<li class="nav-item m-2"><a
							class="nav-link ${empty selectedCategory or selectedCategory.id == selectedParent.id ? 'active' : ''}"
							href="${pageContext.request.contextPath}/category/${selectedParent.slug}">
								Tất cả </a></li>

						<!-- Danh mục con -->
						<c:forEach var="child" items="${childCategories}">
							<li class="nav-item m-2"><a
								class="nav-link ${not empty selectedCategory and selectedCategory.id == child.id ? 'active' : ''}"
								href="${pageContext.request.contextPath}/category/${selectedParent.slug}/${child.slug}">
									<c:out value="${child.name}" />
							</a></li>
						</c:forEach>
					</ul>
				</c:if>

				<c:if test="${empty childCategories}">
					<div class="text-center text-muted">Không có danh mục con.</div>
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
									<c:url
										value="/image?fname=${product.getImages()[0].getImageUrl()}"
										var="imgUrl"></c:url>
									<img class="product-img" src="${imgUrl}" alt="${product.name}">

									<!-- Nút Yêu thích và Giỏ hàng -->
									<div class="block2-icons">
										<a href="#"
											class="block2-icon js-addwish-b2 dis-block icon-heart cl2 trans-04"
											data-product-id="${product.id}" title="Thêm vào yêu thích">
											<i class="zmdi zmdi-favorite-outline"></i>
										</a> <a href="#"
											class="block2-icon js-addcart-detail dis-block icon-cart cl2 trans-04 m-l-10"
											data-product-id="${product.id}" title="Thêm vào giỏ hàng">
											<i class="zmdi zmdi-shopping-cart"></i>
										</a>
									</div>

									<a href="product-detail.html?id=${product.id}"
										class="block2-btn flex-c-m stext-103 cl2 size-102 bg0 bor2 hov-btn1 p-lr-15 trans-04">
										Xem chi tiết </a>
								</div>
								<div class="block2-txt flex-w p-t-14">
									<div class="block2-txt-child1 flex-col-l">
										<a href="product-detail.html?id=${product.id}"
											class="stext-104 cl4 hov-cl1 trans-04 js-name-b2 p-b-6">
											${product.name} </a> <span class="stext-105 cl3"> <fmt:formatNumber
												value="${product.basePrice}" type="number" /> VND
										</span>
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

		<!-- Pagination -->
		<div class="flex-c-m flex-w w-full p-t-45">
			<nav aria-label="Page navigation">
				<ul class="pagination justify-content-center">
					<c:if test="${currentPage > 1}">
						<li class="page-item"><a class="page-link"
							href="${pageContext.request.contextPath}/category/${selectedParent.slug}${not empty selectedCategory and selectedCategory.id != selectedParent.id ? '/' + selectedCategory.slug : ''}?page=${currentPage - 1}">
								Trước </a></li>
					</c:if>
					<c:forEach begin="1" end="${totalPages}" var="i">
						<c:choose>
							<c:when
								test="${not empty selectedCategory and selectedCategory.id != selectedParent.id}">
								<li class="page-item ${i == currentPage ? 'active' : ''}">
									<a class="page-link"
									href="${pageContext.request.contextPath}/category/${selectedParent.slug}/${selectedCategory.slug}?page=${i}">
										${i} </a>
								</li>
							</c:when>
							<c:otherwise>
								<li class="page-item ${i == currentPage ? 'active' : ''}">
									<a class="page-link"
									href="${pageContext.request.contextPath}/category/${selectedParent.slug}?page=${i}">
										${i} </a>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:if test="${currentPage < totalPages}">
						<li class="page-item"><a class="page-link"
							href="${pageContext.request.contextPath}/category/${selectedParent.slug}${not empty selectedCategory and selectedCategory.id != selectedParent.id ? '/' + selectedCategory.slug : ''}?page=${currentPage + 1}">
								Sau </a></li>
					</c:if>
				</ul>
			</nav>
		</div>
	</div>
</section>