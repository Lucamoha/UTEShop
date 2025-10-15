<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/templates/css/home.css">


<!-- Product Detail -->
<section class="sec-product-detail bg0 p-b-60" style="padding-top: 150px;">
    <div class="container">
        <div class="row">
            <div class="col-md-6 col-lg-7 p-b-30">
                <div class="p-l-25 p-r-30 p-lr-0-lg">
                    <div class="wrap-slick3 flex-sb flex-w">
                        <div class="wrap-slick3-dots"></div>
                        <div class="wrap-slick3-arrows flex-sb-m flex-w"></div>

                        <div class="slick3 gallery-lb">
                            <c:forEach var="img" items="${product.getImages()}">
                                <c:url value="/image" var="imgUrl">
                                    <c:param name="fname" value="${img.imageUrl}" />
                                </c:url>

                                <div class="item-slick3" data-thumb="${imgUrl}">
                                    <div class="wrap-pic-w pos-relative">
                                        <img src="${imgUrl}" alt="${product.name}" />

                                        <a class="flex-c-m size-108 how-pos1 bor0 fs-16 cl10 bg0 hov-btn3 trans-04"
                                           href="${imgUrl}">
                                            <i class="fa fa-expand"></i>
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6 col-lg-5 p-b-30">
                <div class="p-r-50 p-t-5 p-lr-0-lg">
                    <h4 class="mtext-105 cl2 js-name-detail p-b-14">
                        ${product.name}
                    </h4>

                    <span class="mtext-106 cl2">
                         <fmt:formatNumber value="${product.basePrice}" type="number" /> VND
                    </span>

                    <p class="stext-102 cl3 p-t-23">
                        ${product.description}
                    </p>

                    <!--  -->
                    <div class="p-t-33">
                        <!-- Duyệt qua danh sách OptionDto -->
                        <c:set var="prevType" value="" />

                        <c:forEach var="opt" items="${options}">
                        <!-- Khi gặp optionTypeCode mới, mở select mới -->
                        <c:if test="${opt.optionTypeCode ne prevType}">
                        <!-- Đóng select cũ nếu có -->
                        <c:if test="${not empty prevType}">
                        </select>
                        <div class="dropDownSelect2"></div>
                    </div>
                </div>
            </div>
            </c:if>

            <!-- Hiển thị tiêu đề của nhóm -->
            <div class="flex-w flex-r-m p-b-10">
                <div class="size-203 flex-c-m respon6">${opt.optionTypeCode}</div>
                <div class="size-204 respon6-next">
                    <div class="rs1-select2 bor8 bg0">
                        <select class="js-select2" name="${opt.optionTypeCode}" data-option-type="${opt.optionTypeId}">
                            <option value="">Chọn ${opt.optionTypeCode}</option>
                            </c:if>

                            <!-- Thêm từng optionValue -->
                            <option value="${opt.optionValueId}">${opt.optionValue}</option>

                            <!-- Cập nhật prevType -->
                            <c:set var="prevType" value="${opt.optionTypeCode}" />
                            </c:forEach>

                            <!-- Đóng thẻ cuối cùng -->
                            <c:if test="${not empty prevType}">
                        </select>
                        <div class="dropDownSelect2"></div>
                    </div>
                </div>
            </div>
            </c:if>
            <!----->



                        <div class="flex-w flex-r-m p-b-10">
                            <div class="size-204 flex-w flex-m respon6-next">
                                <div class="wrap-num-product flex-w m-r-20 m-tb-10">
                                    <div
                                            class="btn-num-product-down cl8 hov-btn3 trans-04 flex-c-m"
                                    >
                                        <i class="fs-16 zmdi zmdi-minus"></i>
                                    </div>

                                    <input
                                            class="mtext-104 cl3 txt-center num-product"
                                            type="number"
                                            name="num-product"
                                            value="1"
                                    />

                                    <div
                                            class="btn-num-product-up cl8 hov-btn3 trans-04 flex-c-m"
                                    >
                                        <i class="fs-16 zmdi zmdi-plus"></i>
                                    </div>
                                </div>

                                <button
                                        class="flex-c-m stext-101 cl0 size-101 bg1 bor1 hov-btn1 p-lr-15 trans-04 js-addcart-detail"
                                        type="button"
                                        onclick="addToCart(<c:out value='${product.id}'/>)"
                                >
                                    Thêm vào giỏ hàng
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabs Section -->
        <div class="bor10 m-t-80 p-t-43 p-b-40">
            <!-- Tab01 -->
            <div class="tab01">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li class="nav-item p-b-10">
                        <a class="nav-link active" data-toggle="tab" href="#description" role="tab">
                            Mô tả sản phẩm
                        </a>
                    </li>
                    <li class="nav-item p-b-10">
                        <a class="nav-link" data-toggle="tab" href="#reviews" role="tab">
                            Đánh giá (${reviewStats.totalReviews})
                        </a>
                    </li>
                </ul>

                <!-- Tab panes -->
                <div class="tab-content p-t-43">
                    <!-- Mô tả sản phẩm -->
                    <div class="tab-pane fade show active" id="description" role="tabpanel">
                        <div class="how-pos2 p-lr-15-md">
                            <p class="stext-102 cl6">
                                ${product.description}
                            </p>
                        </div>
                    </div>

                    <!-- Review Tab -->
                    <div class="tab-pane fade" id="reviews" role="tabpanel">
                        <div class="row">
                            <div class="col-md-10 m-lr-auto">

                                <%-- Review Statistics --%>
                                <c:if test="${reviewStats.totalReviews > 0}">
                                    <div class="p-b-30">
                                        <div class="flex-w flex-sb-m">
                                            <div class="flex-w flex-m">
                                                <span class="mtext-106 cl2 p-r-10">${reviewStats.averageRating}</span>
                                                <span class="fs-18 cl11">
                                                    <c:forEach begin="1" end="5" var="i">
                                                        <c:choose>
                                                            <c:when test="${i <= reviewStats.averageRating}">
                                                                <i class="zmdi zmdi-star"></i>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <i class="zmdi zmdi-star-outline"></i>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:forEach>
                                                </span>
                                            </div>
                                            <span class="stext-102 cl6">${reviewStats.totalReviews} đánh giá</span>
                                        </div>

                                            <%-- Rating Distribution --%>
                                        <div class="p-t-20">
                                            <c:forEach begin="1" end="5" var="i">
                                                <c:set var="star" value="${6 - i}" />
                                                <c:set var="count" value="0" />
                                                <c:set var="percent" value="0" />

                            
                                                <c:forEach var="e" items="${reviewStats.ratingDistribution}">
                                                    <c:if test="${e.key == star}">
                                                        <c:set var="count" value="${e.value}" />
                                                    </c:if>
                                                </c:forEach>


                                                <c:forEach var="e" items="${reviewStats.ratingPercentages}">
                                                    <c:if test="${e.key == star}">
                                                        <c:set var="percent" value="${e.value}" />
                                                    </c:if>
                                                </c:forEach>

                                                <div class="flex-w flex-m p-b-10">
                                                    <span class="stext-102 cl6 p-r-10" style="width: 80px;">${star} sao</span>
                                                    <div class="progress" style="width: 200px; height: 8px; background-color: #e0e0e0; border-radius: 4px; overflow: hidden;">
                                                        <div class="progress-bar" 
                                                             style="width: ${percent}%; background-color: #ffc107; height: 100%;"></div>
                                                    </div>
                                                    <span class="stext-102 cl6 p-l-10">(${count})</span>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <hr/>
                                </c:if>

                                <%-- List Reviews --%>
                                <div class="p-t-30">
                                    <c:choose>
                                        <c:when test="${empty reviews}">
                                            <p class="stext-102 cl6 text-center p-t-20 p-b-20">
                                                Chưa có đánh giá nào cho sản phẩm này
                                            </p>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="review" items="${reviews}">
                                                <div class="flex-w flex-t p-b-40">
                                                        <%-- Avatar --%>
                                                    <div class="wrap-pic-s size-109 bor0 of-hidden m-r-18 m-t-6">
                                                        <div class="avatar-placeholder"
                                                             style="width:100%; height:100%; background:#e91e63;
                                                                    display:flex; align-items:center; justify-content:center;
                                                                    color:#fff; font-size:24px; font-weight:bold;">
                                                                ${fn:substring(review.user.fullName, 0, 1)}
                                                        </div>
                                                    </div>

                                                    <div class="size-207">
                                                            <%-- Name & Rating --%>
                                                        <div class="flex-w flex-sb-m p-b-10">
                                                            <span class="mtext-107 cl2">
                                                                ${review.user.fullName}
                                                                <c:if test="${review.purchaseVerified}">
                                                                    <span class="badge bg-success text-white ml-2"
                                                                          style="font-size:10px; padding:2px 6px;">
                                                                        ✓ Đã mua hàng
                                                                    </span>
                                                                </c:if>
                                                            </span>

                                                            <span class="fs-16 cl11">
                                                                <c:forEach begin="1" end="5" var="i">
                                                                    <c:choose>
                                                                        <c:when test="${i <= review.rating}">
                                                                            <i class="zmdi zmdi-star"></i>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <i class="zmdi zmdi-star-outline"></i>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </span>
                                                        </div>

                                                            <%-- Review Content --%>
                                                        <p class="stext-102 cl6">${review.content}</p>

                                                            <%-- Media (Images/Video) --%>
                                                        <c:if test="${review.hasMedia && not empty review.media}">
                                                            <div class="flex-w p-t-10">
                                                                <c:forEach var="media" items="${review.media}">
                                                                    <c:choose>
                                                                        <c:when test="${media.mediaType == 1}">
                                                                            <%-- Image --%>
                                                                            <a href="${pageContext.request.contextPath}/${media.mediaUrl}"
                                                                               class="m-r-10 m-b-10"
                                                                               data-lightbox="review-${review.id}">
                                                                                <img src="${pageContext.request.contextPath}/${media.mediaUrl}"
                                                                                     alt="Review"
                                                                                     style="width:100px; height:100px; object-fit:cover; border-radius:8px;"/>
                                                                            </a>
                                                                        </c:when>
                                                                        <c:when test="${media.mediaType == 2}">
                                                                            <%-- Video --%>
                                                                            <video width="200" height="150" controls class="m-r-10 m-b-10" style="border-radius:8px;">
                                                                                <source src="${pageContext.request.contextPath}/${media.mediaUrl}" type="video/mp4">
                                                                                Trình duyệt không hỗ trợ video.
                                                                            </video>
                                                                        </c:when>
                                                                    </c:choose>
                                                                </c:forEach>
                                                            </div>
                                                        </c:if>

                                                            <%-- Review Date --%>
                                                        <p class="stext-101 cl6 p-t-5" style="font-size:12px;">
                                                                ${review.createdAt}
                                                        </p>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <hr class="m-t-30 m-b-30"/>

                                <%-- Add Review Form --%>
                                <div class="p-b-30">
                                    <h5 class="mtext-108 cl2 p-b-7">Viết đánh giá</h5>

                                    <c:choose>
                                        <%-- Case 1: Chưa đăng nhập --%>
                                        <c:when test="${empty authenticatedEmail}">
                                            <div class="alert alert-info">
                                                <i class="fa fa-info-circle"></i>
                                                Vui lòng <a href="${pageContext.request.contextPath}/login?redirect=${pageContext.request.requestURI}">đăng nhập</a>
                                                để viết đánh giá về sản phẩm này.
                                            </div>
                                        </c:when>

                                        <%-- Case 2: Đã review rồi --%>
                                        <c:when test="${hasReviewed}">
                                            <div class="alert alert-success">
                                                <i class="fa fa-check-circle"></i>
                                                Bạn đã đánh giá sản phẩm này rồi. Cảm ơn bạn!
                                            </div>
                                        </c:when>

                                        <%-- Case 3: Đã đăng nhập nhưng chưa mua --%>
                                        <c:when test="${!hasPurchased}">
                                            <div class="alert alert-warning">
                                                <i class="fa fa-exclamation-triangle"></i>
                                                Bạn cần mua sản phẩm này trước khi có thể đánh giá.
                                            </div>
                                        </c:when>

                                        <%-- Case 4: Có thể review --%>
                                        <c:when test="${canReview}">
                                            <form id="reviewForm" enctype="multipart/form-data">
                                                <input type="hidden" name="productId" value="${product.id}"/>

                                                    <%-- Rating Stars --%>
                                                <div class="flex-w flex-m p-t-20 p-b-23">
                                                    <span class="stext-102 cl3 m-r-16">Đánh giá của bạn <span class="text-danger">*</span></span>
                                                    <span class="wrap-rating fs-18 cl11 pointer">
                                                        <i class="item-rating pointer zmdi zmdi-star-outline" data-value="1"></i>
                                                        <i class="item-rating pointer zmdi zmdi-star-outline" data-value="2"></i>
                                                        <i class="item-rating pointer zmdi zmdi-star-outline" data-value="3"></i>
                                                        <i class="item-rating pointer zmdi zmdi-star-outline" data-value="4"></i>
                                                        <i class="item-rating pointer zmdi zmdi-star-outline" data-value="5"></i>
                                                        <input class="dis-none" type="number" name="rating" id="ratingValue" required/>
                                                    </span>
                                                    <span id="ratingError" class="text-danger ml-2" style="display:none;">Vui lòng chọn số sao</span>
                                                </div>

                                                    <%-- Review Content --%>
                                                <div class="row p-b-25">
                                                    <div class="col-12 p-b-15">
                                                        <label class="stext-102 cl3" for="content">
                                                            Nhận xét của bạn <span class="text-danger">*</span>
                                                        </label>
                                                        <textarea class="size-110 bor8 stext-102 cl2 p-lr-20 p-tb-10"
                                                                  id="content"
                                                                  name="content"
                                                                  rows="5"
                                                                  placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm này..."
                                                                  required></textarea>
                                                    </div>

                                                        <%-- Upload Images (max 3) --%>
                                                    <div class="col-12 p-b-15">
                                                        <label class="stext-102 cl3">
                                                            Hình ảnh (Tối đa 3 ảnh)
                                                        </label>
                                                        <input type="file"
                                                               class="form-control"
                                                               id="images"
                                                               name="images"
                                                               accept="image/*"
                                                               multiple
                                                               onchange="handleImageUpload(this)"/>
                                                        <small class="text-muted">Chấp nhận: JPG, PNG, GIF, WEBP</small>
                                                        <div id="imagePreview" class="flex-w p-t-10"></div>
                                                    </div>

                                                        <%-- Upload Video (max 1) --%>
                                                    <div class="col-12 p-b-15">
                                                        <label class="stext-102 cl3">
                                                            Video (Tối đa 1 video)
                                                        </label>
                                                        <input type="file"
                                                               class="form-control"
                                                               id="video"
                                                               name="video"
                                                               accept="video/*"
                                                               onchange="handleVideoUpload(this)"/>
                                                        <small class="text-muted">Chấp nhận: MP4, AVI, MOV, WMV. Lưu ý: Chỉ upload ảnh HOẶC video</small>
                                                        <div id="videoPreview" class="p-t-10"></div>
                                                    </div>
                                                </div>

                                                <div id="reviewMessage" style="display:none;" class="alert"></div>

                                                <button type="submit"
                                                        id="submitReviewBtn"
                                                        class="flex-c-m stext-101 cl0 size-112 bg7 bor11 hov-btn3 p-lr-15 trans-04 m-b-10">
                                                    <i class="fa fa-paper-plane m-r-5"></i> Gửi đánh giá
                                                </button>
                                            </form>
                                        </c:when>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                                <!---->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Related Products -->
<section class="sec-relate-product bg0 p-t-70 p-b-105">
    <div class="container">
        <div class="p-b-45">
            <h3 class="ltext-106 cl5 txt-center">Sản phẩm liên quan</h3>
        </div>

        <!-- Slide2 -->
        <div class="wrap-slick2">
            <div class="slick2">
                <c:forEach var="product" items="${relevantProducts}">
                    <div class="item-slick2 p-l-15 p-r-15 p-t-15 p-b-15">
                        <div class="block2">
                            <div class="block2-pic hov-img0">
                                <c:url value="/image?fname=${product.getImages()[0].getImageUrl()}" var="imgUrl"></c:url>
                                <img src="${imgUrl}" alt="${product.name}">

                                <!-- Nút Yêu thích và Giỏ hàng -->
                                <div class="block2-icons">
                                    <a href="#" class="block2-icon js-addwish-b2 dis-block icon-heart cl2 trans-04"
                                       data-product-id="${product.id}"
                                       title="Thêm vào yêu thích">
                                        <i class="zmdi zmdi-favorite-outline"></i>
                                    </a>
                                    <a href="#" class="block2-icon js-addcart-detail dis-block icon-cart cl2 trans-04 m-l-10"
                                       data-product-id="${product.id}"
                                       title="Thêm vào giỏ hàng">
                                        <i class="zmdi zmdi-shopping-cart"></i>
                                    </a>
                                </div>

                                <a href="product-detail.html?id=${product.id}"
                                   class="block2-btn flex-c-m stext-103 cl2 size-102 bg0 bor2 hov-btn1 p-lr-15 trans-04">
                                    Xem chi tiết
                                </a>
                            </div>

                            <div class="block2-txt flex-w p-t-14">
                                <div class="block2-txt-child1 flex-col-l">
                                    <a href="${pageContext.request.contextPath}/product-detail?id=${product.id}"
                                       class="stext-104 cl4 hov-cl1 trans-04 js-name-b2 p-b-6">
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
    </div>
</section>

<!-- Back to top -->
<div class="btn-back-to-top" id="myBtn">
    <span class="symbol-btn-back-to-top">
        <i class="zmdi zmdi-chevron-up"></i>
    </span>
</div>

<!--===============================================================================================-->
<script src="vendor/jquery/jquery-3.2.1.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/animsition/js/animsition.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/bootstrap/js/popper.js"></script>
<script src="vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/select2/select2.min.js"></script>
<script>
    $(".js-select2").each(function () {
        $(this).select2({
            minimumResultsForSearch: 20,
            dropdownParent: $(this).next(".dropDownSelect2"),
        });
    });
</script>
<!--===============================================================================================-->
<script src="vendor/daterangepicker/moment.min.js"></script>
<script src="vendor/daterangepicker/daterangepicker.js"></script>
<!--===============================================================================================-->
<script src="vendor/slick/slick.min.js"></script>
<script src="js/slick-custom.js"></script>
<!--===============================================================================================-->
<script src="vendor/parallax100/parallax100.js"></script>
<script>
    $(".parallax100").parallax100();
</script>
<!--===============================================================================================-->
<script src="vendor/MagnificPopup/jquery.magnific-popup.min.js"></script>
<script>
    $(".gallery-lb").each(function () {
        // the containers for all your galleries
        $(this).magnificPopup({
            delegate: "a", // the selector for gallery item
            type: "image",
            gallery: {
                enabled: true,
            },
            mainClass: "mfp-fade",
        });
    });
</script>
<!--===============================================================================================-->
<script src="vendor/isotope/isotope.pkgd.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/sweetalert/sweetalert.min.js"></script>
<!--===============================================================================================-->
<script src="vendor/perfect-scrollbar/perfect-scrollbar.min.js"></script>
<script>
    $(".js-pscroll").each(function () {
        $(this).css("position", "relative");
        $(this).css("overflow", "hidden");
        var ps = new PerfectScrollbar(this, {
            wheelSpeed: 1,
            scrollingThreshold: 1000,
            wheelPropagation: false,
        });

        $(window).on("resize", function () {
            ps.update();
        });
    });
</script>
<!--===============================================================================================-->
<script>
    // XỬ LÝ ĐÁNH GIÁ SAO (RATING STARS)
    document.querySelectorAll('.item-rating').forEach(star => {
        star.addEventListener('click', function() {
            const rating = this.getAttribute('data-value'); // Lấy giá trị sao (1-5)
            document.getElementById('ratingValue').value = rating; // Lưu vào input hidden
            document.getElementById('ratingError').style.display = 'none'; // Ẩn lỗi nếu có

            // Cập nhật hiển thị sao: đổi từ outline → filled
            document.querySelectorAll('.item-rating').forEach((s, idx) => {
                if (idx < rating) {
                    s.classList.remove('zmdi-star-outline');
                    s.classList.add('zmdi-star');
                } else {
                    s.classList.remove('zmdi-star');
                    s.classList.add('zmdi-star-outline');
                }
            });
        });
    });

    // XỬ LÝ UPLOAD & PREVIEW HÌNH ẢNH
    let selectedFiles = []; // Mảng lưu các file đã chọn

    function handleImageUpload(input) {
        const preview = document.getElementById('imagePreview');
        const files = Array.from(input.files);

        // Kiểm tra số lượng (tối đa 3 ảnh)
        if (files.length > 3) {
            alert('Chỉ được upload tối đa 3 ảnh');
            input.value = '';
            selectedFiles = [];
            preview.innerHTML = '';
            return;
        }

        // Lưu files vào biến global
        selectedFiles = files;

        // Disable video nếu có ảnh
        const videoInput = document.getElementById('video');
        if (files.length > 0) {
            videoInput.disabled = true;
            videoInput.value = '';
            document.getElementById('videoPreview').innerHTML = '';
        } else {
            videoInput.disabled = false;
        }

        // Clear preview cũ
        preview.innerHTML = '';

        // Tạo preview cho từng ảnh
        files.forEach((file, index) => {
            const reader = new FileReader();
            reader.onload = function(e) {
                // Tạo wrapper cho mỗi ảnh (có nút xóa)
                const wrapper = document.createElement('div');
                wrapper.className = 'image-preview-wrapper';
                
                // Tạo img element
                const img = document.createElement('img');
                img.src = e.target.result;
                
                // Tạo nút xóa (×)
                const removeBtn = document.createElement('span');
                removeBtn.className = 'remove-preview-btn';
                removeBtn.innerHTML = '×';
                removeBtn.onclick = function() {
                    removeImage(index);
                };
                
                wrapper.appendChild(img);
                wrapper.appendChild(removeBtn);
                preview.appendChild(wrapper);
            };
            reader.readAsDataURL(file);
        });
    }

    // Hàm xóa một ảnh khỏi preview
    function removeImage(index) {
        // Xóa file khỏi mảng
        selectedFiles.splice(index, 1);
        
        // Tạo DataTransfer mới để update input.files
        const dt = new DataTransfer();
        selectedFiles.forEach(file => dt.items.add(file));
        document.getElementById('images').files = dt.files;
        
        // Render lại preview
        const preview = document.getElementById('imagePreview');
        preview.innerHTML = '';
        
        if (selectedFiles.length === 0) {
            document.getElementById('video').disabled = false;
        } else {
            selectedFiles.forEach((file, idx) => {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const wrapper = document.createElement('div');
                    wrapper.className = 'image-preview-wrapper';
                    
                    const img = document.createElement('img');
                    img.src = e.target.result;
                    
                    const removeBtn = document.createElement('span');
                    removeBtn.className = 'remove-preview-btn';
                    removeBtn.innerHTML = '×';
                    removeBtn.onclick = function() {
                        removeImage(idx);
                    };
                    
                    wrapper.appendChild(img);
                    wrapper.appendChild(removeBtn);
                    preview.appendChild(wrapper);
                };
                reader.readAsDataURL(file);
            });
        }
    }

    // XỬ LÝ UPLOAD & PREVIEW VIDEO
    function handleVideoUpload(input) {
        const preview = document.getElementById('videoPreview');
        preview.innerHTML = '';

        const imagesInput = document.getElementById('images');
        if (input.files.length > 0) {
            // Disable images nếu chọn video
            imagesInput.disabled = true;
            imagesInput.value = '';
            selectedFiles = [];
            document.getElementById('imagePreview').innerHTML = '';

            // Tạo wrapper cho video
            const wrapper = document.createElement('div');
            wrapper.className = 'video-preview-wrapper';
            
            const video = document.createElement('video');
            video.src = URL.createObjectURL(input.files[0]);
            video.controls = true;
            video.style.cssText = 'width:200px; height:150px;';
            
            // Nút xóa video
            const removeBtn = document.createElement('span');
            removeBtn.className = 'remove-preview-btn';
            removeBtn.innerHTML = '×';
            removeBtn.style.top = '-8px';
            removeBtn.style.right = '-8px';
            removeBtn.onclick = function() {
                input.value = '';
                preview.innerHTML = '';
                imagesInput.disabled = false;
            };
            
            wrapper.appendChild(video);
            wrapper.appendChild(removeBtn);
            preview.appendChild(wrapper);
        } else {
            imagesInput.disabled = false;
        }
    }

    // GỬI FORM ĐÁNH GIÁ (AJAX)
    // ==========================================
    // Mục đích: Gửi form qua AJAX, nhận JSON response từ server
    document.getElementById('reviewForm').addEventListener('submit', function(e) {
        e.preventDefault(); // Ngăn form submit bình thường (không reload trang)

        // Kiểm tra rating có được chọn chưa
        const rating = document.getElementById('ratingValue').value;
        if (!rating) {
            document.getElementById('ratingError').style.display = 'inline';
            return;
        }

        // Chuẩn bị dữ liệu gửi lên server (bao gồm text + files)
        const formData = new FormData(this);
        const submitBtn = document.getElementById('submitReviewBtn');
        const messageDiv = document.getElementById('reviewMessage');

        // Disable nút submit, hiển thị loading
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fa fa-spinner fa-spin m-r-5"></i> Đang gửi...';

        // GỬI REQUEST LÊN SERVER bằng Fetch API
        fetch('${pageContext.request.contextPath}/review/submit', {
            method: 'POST',
            body: formData // FormData tự động set Content-Type: multipart/form-data
        })
        // Chuyển response thành JSON
        .then(response => response.json())
        
        // Xử lý JSON data nhận được
        .then(data => {
            // DATA LÀ OBJECT JSON TRẢ VỀ TỪ ReviewController
            // Ví dụ: {success: true, message: "Đánh giá thành công"}
            
            messageDiv.style.display = 'block';
            
            if (data.success) {
                // SUCCESS: Hiển thị thông báo xanh
                messageDiv.className = 'alert alert-success';
                messageDiv.innerHTML = '<i class="fa fa-check-circle"></i> ' + data.message;

                // Reset form về trạng thái ban đầu
                document.getElementById('reviewForm').reset();
                document.querySelectorAll('.item-rating').forEach(s => {
                    s.classList.remove('zmdi-star');
                    s.classList.add('zmdi-star-outline');
                });
                document.getElementById('imagePreview').innerHTML = '';
                document.getElementById('videoPreview').innerHTML = '';
                selectedFiles = [];

                // Reload trang sau 2 giây để hiển thị review mới
                setTimeout(() => {
                    location.reload();
                }, 2000);
            } else {
                // ERROR: Hiển thị thông báo đỏ
                messageDiv.className = 'alert alert-danger';
                messageDiv.innerHTML = '<i class="fa fa-exclamation-circle"></i> ' + data.message;
            }
        })
        // Xử lý lỗi network hoặc server error
        .catch(error => {
            messageDiv.style.display = 'block';
            messageDiv.className = 'alert alert-danger';
            messageDiv.innerHTML = '<i class="fa fa-exclamation-circle"></i> Có lỗi xảy ra. Vui lòng thử lại!';
        })
        // Luôn chạy dù thành công hay thất bại
        .finally(() => {
            submitBtn.disabled = false;
            submitBtn.innerHTML = '<i class="fa fa-paper-plane m-r-5"></i> Gửi đánh giá';
        });
    });
</script>
<!--===============================================================================================-->
