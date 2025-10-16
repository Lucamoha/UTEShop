<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>

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
					Lọc
				</div>
				<div
					class="flex-c-m stext-106 cl6 size-105 bor4 pointer hov-btn3 trans-04 m-tb-4 js-show-search">
					<i class="icon-search cl2 m-r-6 fs-15 trans-04 zmdi zmdi-search"></i>
					Tìm kiếm
				</div>
			</div>
		</div>
		
		<!-- Search product -->
          <div class="dis-none panel-search w-full p-t-10 p-b-15">
            <form method="get" action="" class="bor8 dis-flex p-l-15">
              <button type="submit" class="size-113 flex-c-m fs-16 cl2 hov-cl1 trans-04">
                <i class="zmdi zmdi-search"></i>
              </button>

              <input
                class="mtext-107 cl2 size-114 plh2 p-r-15"
                type="text"
                name="keyword"
                placeholder="Tìm kiếm sản phẩm..."
                value="${currentKeyword}"
              />
            </form>
          </div>
          
          <!-- Filter -->
          <div class="dis-none panel-filter w-full p-t-10">
            <form method="get" action="" id="filterForm">
              <div class="wrap-filter flex-w bg6 w-full p-lr-40 p-t-27 p-lr-15-sm">
                <!-- Sort -->
                <div class="filter-col1 p-r-15 p-b-27">
                  <div class="mtext-102 cl2 p-b-15">Sắp xếp</div>
                  <ul>
                    <li class="p-b-6">
                      <a href="javascript:void(0)" onclick="selectSort('')" 
                         class="filter-link stext-106 trans-04 ${empty currentSortBy ? 'filter-link-active' : ''}"
                         data-sort="">
                        Mặc định
                      </a>
                    </li>
                    <li class="p-b-6">
                      <a href="javascript:void(0)" onclick="selectSort('popularity')" 
                         class="filter-link stext-106 trans-04 ${currentSortBy == 'popularity' ? 'filter-link-active' : ''}"
                         data-sort="popularity">
                        Phổ biến
                      </a>
                    </li>
                    <li class="p-b-6">
                      <a href="javascript:void(0)" onclick="selectSort('newness')" 
                         class="filter-link stext-106 trans-04 ${currentSortBy == 'newness' ? 'filter-link-active' : ''}"
                         data-sort="newness">
                        Mới nhất
                      </a>
                    </li>
                    <li class="p-b-6">
                      <a href="javascript:void(0)" onclick="selectSort('price_asc')" 
                         class="filter-link stext-106 trans-04 ${currentSortBy == 'price_asc' ? 'filter-link-active' : ''}"
                         data-sort="price_asc">
                        Giá: Thấp đến Cao
                      </a>
                    </li>
                    <li class="p-b-6">
                      <a href="javascript:void(0)" onclick="selectSort('price_desc')" 
                         class="filter-link stext-106 trans-04 ${currentSortBy == 'price_desc' ? 'filter-link-active' : ''}"
                         data-sort="price_desc">
                        Giá: Cao đến Thấp
                      </a>
                    </li>
                  </ul>
                </div>

                <!-- Price Slider -->
                <div class="filter-col2 p-r-15 p-b-27 price-filter-container">
                  <div class="mtext-102 cl2 p-b-15 price-filter-title">Khoảng giá</div>
                  <div class="price-slider-wrapper">
                    <!-- Slider -->
                    <div id="price-slider"></div>
                    
                    <!-- Price Display -->
                    <div class="price-display-row">
                      <div class="price-display-container">
                        <div class="price-input-wrapper">
                          <input type="text" id="price-min-display" class="price-input" readonly>
                          <span class="price-currency">đ</span>
                        </div>
                        <span class="price-separator">—</span>
                        <div class="price-input-wrapper">
                          <input type="text" id="price-max-display" class="price-input" readonly>
                          <span class="price-currency">đ</span>
                        </div>
                      </div>
                    </div>
                    
                  </div>
                </div>
                
                <!-- Attribute Filters -->
                <c:if test="${not empty filterOptions.attributes}">
                  <c:forEach var="attrEntry" items="${filterOptions.attributes}">
                    <c:set var="attr" value="${attrEntry.value}" />
                    
                    <div class="filter-col3 p-r-15 p-b-27">
                      <div class="mtext-102 cl2 p-b-15">${attr.name}</div>
                      
                      <c:choose>
                        <%-- TEXT type: Pill buttons for multiple selection --%>
                        <c:when test="${attr.dataType == 1}">
                          <div class="filter-pills-container">
                            <c:forEach var="value" items="${attr.possibleValues}">
                              <label class="filter-pill">
                                <input type="checkbox" 
                                       name="attr_${attr.id}" 
                                       value="${value}"
                                       <c:if test="${currentAttributeFilters[attr.id] != null}">
                                         <c:forEach var="selectedVal" items="${currentAttributeFilters[attr.id]}">
                                           ${selectedVal == value ? 'checked' : ''}
                                         </c:forEach>
                                       </c:if>
                                       class="filter-pill-input">
                                <span class="filter-pill-text">${value}</span>
                              </label>
                            </c:forEach>
                          </div>
                        </c:when>
                        
                        <%-- NUMBER type: Pill buttons for multiple selection --%>
                        <c:when test="${attr.dataType == 2}">
                          <div class="filter-pills-container">
                            <c:forEach var="value" items="${attr.possibleNumberValues}">
                              <label class="filter-pill">
                                <input type="checkbox" 
                                       name="attr_${attr.id}" 
                                       value="${value}"
                                       <c:if test="${currentAttributeFilters[attr.id] != null}">
                                         <c:forEach var="selectedVal" items="${currentAttributeFilters[attr.id]}">
                                           ${selectedVal == value.toString() ? 'checked' : ''}
                                         </c:forEach>
                                       </c:if>
                                       class="filter-pill-input">
                                <span class="filter-pill-text">
                                  <c:choose>
                                    <c:when test="${value % 1 == 0}">
                                      ${value.intValue()}${attr.unit != null ? ' ' : ''}${attr.unit != null ? attr.unit : ''}
                                    </c:when>
                                    <c:otherwise>
                                      ${value}${attr.unit != null ? ' ' : ''}${attr.unit != null ? attr.unit : ''}
                                    </c:otherwise>
                                  </c:choose>
                                </span>
                              </label>
                            </c:forEach>
                          </div>
                        </c:when>
                        
                        <%-- BOOLEAN type: Yes/No pill buttons (single selection) --%>
                        <c:when test="${attr.dataType == 3}">
                          <div class="filter-pills-container">
                            <label class="filter-pill">
                              <input type="radio" 
                                     name="attr_${attr.id}" 
                                     value="1"
                                     <c:if test="${currentAttributeFilters[attr.id] != null}">
                                       <c:forEach var="selectedVal" items="${currentAttributeFilters[attr.id]}">
                                         ${selectedVal == '1' ? 'checked' : ''}
                                       </c:forEach>
                                     </c:if>
                                     class="filter-pill-input">
                              <span class="filter-pill-text">Có</span>
                            </label>
                            <label class="filter-pill">
                              <input type="radio" 
                                     name="attr_${attr.id}" 
                                     value="0"
                                     <c:if test="${currentAttributeFilters[attr.id] != null}">
                                       <c:forEach var="selectedVal" items="${currentAttributeFilters[attr.id]}">
                                         ${selectedVal == '0' ? 'checked' : ''}
                                       </c:forEach>
                                     </c:if>
                                     class="filter-pill-input">
                              <span class="filter-pill-text">Không</span>
                            </label>
                          </div>
                        </c:when>
                      </c:choose>
                    </div>
                  </c:forEach>
                </c:if>

                <!-- Hidden inputs for maintaining state -->
                <input type="hidden" name="sortBy" id="sortByInput" value="${currentSortBy}">
                <input type="hidden" name="minPrice" id="minPriceInput" value="${currentMinPrice}">
                <input type="hidden" name="maxPrice" id="maxPriceInput" value="${currentMaxPrice}">
                <c:if test="${not empty currentKeyword}">
                  <input type="hidden" name="keyword" value="${currentKeyword}">
                </c:if>
              </div>
              
              <!-- Apply Button (Chung cho tất cả filters) -->
              <div class="w-full text-center p-t-10 p-b-15">
                <button type="button" onclick="applyFilters()" class="filter-apply-btn">
                  <i class="zmdi zmdi-check m-r-5"></i>
                  Áp dụng bộ lọc
                </button>
              </div>
            </form>
          </div>
          
          <script>
            // Wait for DOM and noUiSlider to load
            document.addEventListener('DOMContentLoaded', function() {
              // Check if noUiSlider is loaded
              if (typeof noUiSlider === 'undefined') {
                console.error('noUiSlider library not loaded!');
                return;
              }
              
              var priceSlider = document.getElementById('price-slider');
              if (!priceSlider) {
                console.error('Price slider element not found!');
                return;
              }
              
              // Get price values from server
              var minPrice = parseFloat('${filterOptions.minPrice}') || 0;
              var maxPrice = parseFloat('${filterOptions.maxPrice}') || 100000000;
              var currentMin = parseFloat('${currentMinPrice}') || minPrice;
              var currentMax = parseFloat('${currentMaxPrice}') || maxPrice;
              
              console.log('Price range:', minPrice, '-', maxPrice);
              console.log('Current:', currentMin, '-', currentMax);
              
              try {
                // Create slider
                noUiSlider.create(priceSlider, {
                  start: [currentMin, currentMax],
                  connect: true,
                  step: 1000000,
                  range: {
                    'min': minPrice,
                    'max': maxPrice
                  }
                });
                
                // Update display when slider changes
                priceSlider.noUiSlider.on('update', function(values, handle) {
                  var minVal = Math.round(parseFloat(values[0]));
                  var maxVal = Math.round(parseFloat(values[1]));
                  document.getElementById('price-min-display').value = minVal.toLocaleString('vi-VN');
                  document.getElementById('price-max-display').value = maxVal.toLocaleString('vi-VN');
                });
                
                console.log('Price slider initialized successfully!');
              } catch (error) {
                console.error('Error initializing slider:', error);
              }
            });
            
            // Chọn sort (không submit)
            function selectSort(sortValue) {
              // Update hidden input
              document.getElementById('sortByInput').value = sortValue;
              
              // Update active class
              var sortLinks = document.querySelectorAll('.filter-col1 .filter-link');
              sortLinks.forEach(function(link) {
                link.classList.remove('filter-link-active');
              });
              event.target.classList.add('filter-link-active');
            }
            
            // Áp dụng tất cả filters (sort + price)
            function applyFilters() {
              var priceSlider = document.getElementById('price-slider');
              if (priceSlider && priceSlider.noUiSlider) {
                var values = priceSlider.noUiSlider.get();
                document.getElementById('minPriceInput').value = Math.round(parseFloat(values[0]));
                document.getElementById('maxPriceInput').value = Math.round(parseFloat(values[1]));
              }
              document.getElementById('filterForm').submit();
            }
          </script>
          

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

									<a href="${pageContext.request.contextPath}/product-detail?product=${product.slug}"
										class="block2-btn flex-c-m stext-103 cl2 size-102 bg0 bor2 hov-btn1 p-lr-15 trans-04">
										Xem chi tiết </a>
								</div>
								<div class="block2-txt flex-w p-t-14">
									<div class="block2-txt-child1 flex-col-l">
										<a href="${pageContext.request.contextPath}/product-detail?product=${product.slug}"
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
