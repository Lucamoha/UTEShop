<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet"
      href="${pageContext.request.contextPath}/templates/css/home.css">

<!-- breadcrumb -->
<div class="container">
    <div class="bread-crumb flex-w p-l-25 p-r-15 p-t-30 p-lr-0-lg">
        <span class="stext-109 cl4"> </span>
    </div>
</div>

<!-- Product Detail -->
<section class="sec-product-detail bg0 p-t-65 p-b-60">
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
                        <select class="js-select2" name="${opt.optionTypeCode}">
                            <option>Choose an option</option>
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
                                >
                                    Thêm vào giỏ hàng
                                </button>
                            </div>
                        </div>
                    </div>

                    <!--  -->
                    <div class="flex-w flex-m p-l-100 p-t-40 respon7">
                        <div class="flex-m bor9 p-r-10 m-r-11">
                            <a
                                    href="#"
                                    class="fs-14 cl3 hov-cl1 trans-04 lh-10 p-lr-5 p-tb-2 js-addwish-detail tooltip100"
                                    data-tooltip="Add to Wishlist"
                            >
                                <i class="zmdi zmdi-favorite"></i>
                            </a>
                        </div>

                        <a
                                href="#"
                                class="fs-14 cl3 hov-cl1 trans-04 lh-10 p-lr-5 p-tb-2 m-r-8 tooltip100"
                                data-tooltip="Facebook"
                        >
                            <i class="fa fa-facebook"></i>
                        </a>

                        <a
                                href="#"
                                class="fs-14 cl3 hov-cl1 trans-04 lh-10 p-lr-5 p-tb-2 m-r-8 tooltip100"
                                data-tooltip="Twitter"
                        >
                            <i class="fa fa-twitter"></i>
                        </a>

                        <a
                                href="#"
                                class="fs-14 cl3 hov-cl1 trans-04 lh-10 p-lr-5 p-tb-2 m-r-8 tooltip100"
                                data-tooltip="Google Plus"
                        >
                            <i class="fa fa-google-plus"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="bor10 m-t-50 p-t-43 p-b-40">
            <!-- Tab01 -->
            <div class="tab01">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li class="nav-item p-b-10">
                        <a
                                class="nav-link active"
                                data-toggle="tab"
                                href="#description"
                                role="tab"
                        >Mô tả sản phẩm</a
                        >
                    </li>



                    <li class="nav-item p-b-10">
                        <a class="nav-link" data-toggle="tab" href="#reviews" role="tab"
                        >Đánh giá (1)</a
                        >
                    </li>
                </ul>

                <!-- Tab panes -->
                <div class="tab-content p-t-43">
                    <!-- - -->
                    <div
                            class="tab-pane fade show active"
                            id="description"
                            role="tabpanel"
                    >
                        <div class="how-pos2 p-lr-15-md">
                            <p class="stext-102 cl6">
                                ${product.description}
                            </p>
                        </div>
                    </div>


                    <div class="tab-pane fade" id="reviews" role="tabpanel">
                        <div class="row">
                            <div class="col-sm-10 col-md-8 col-lg-6 m-lr-auto">
                                <div class="p-b-30 m-lr-15-sm">
                                    <!-- Review -->
                                    <div class="flex-w flex-t p-b-68">
                                        <div
                                                class="wrap-pic-s size-109 bor0 of-hidden m-r-18 m-t-6"
                                        >
                                            <img src="images/avatar-01.jpg" alt="AVATAR" />
                                        </div>

                                        <div class="size-207">
                                            <div class="flex-w flex-sb-m p-b-17">
                            <span class="mtext-107 cl2 p-r-20">
                              Ariana Grande
                            </span>

                                                <span class="fs-18 cl11">
                              <i class="zmdi zmdi-star"></i>
                              <i class="zmdi zmdi-star"></i>
                              <i class="zmdi zmdi-star"></i>
                              <i class="zmdi zmdi-star"></i>
                              <i class="zmdi zmdi-star-half"></i>
                            </span>
                                            </div>

                                            <p class="stext-102 cl6">
                                                Quod autem in homine praestantissimum atque optimum
                                                est, id deseruit. Apud ceteros autem philosophos
                                            </p>
                                        </div>
                                    </div>

                                    <!-- Add review -->
                                    <form class="w-full">
                                        <h5 class="mtext-108 cl2 p-b-7">Add a review</h5>

                                        <p class="stext-102 cl6">
                                            Your email address will not be published. Required
                                            fields are marked *
                                        </p>

                                        <div class="flex-w flex-m p-t-50 p-b-23">
                          <span class="stext-102 cl3 m-r-16">
                            Your Rating
                          </span>

                                            <span class="wrap-rating fs-18 cl11 pointer">
                            <i
                                    class="item-rating pointer zmdi zmdi-star-outline"
                            ></i>
                            <i
                                    class="item-rating pointer zmdi zmdi-star-outline"
                            ></i>
                            <i
                                    class="item-rating pointer zmdi zmdi-star-outline"
                            ></i>
                            <i
                                    class="item-rating pointer zmdi zmdi-star-outline"
                            ></i>
                            <i
                                    class="item-rating pointer zmdi zmdi-star-outline"
                            ></i>
                            <input
                                    class="dis-none"
                                    type="number"
                                    name="rating"
                            />
                          </span>
                                        </div>

                                        <div class="row p-b-25">
                                            <div class="col-12 p-b-5">
                                                <label class="stext-102 cl3" for="review"
                                                >Your review</label
                                                >
                                                <textarea
                                                        class="size-110 bor8 stext-102 cl2 p-lr-20 p-tb-10"
                                                        id="review"
                                                        name="review"
                                                ></textarea>
                                            </div>

                                            <div class="col-sm-6 p-b-5">
                                                <label class="stext-102 cl3" for="name">Name</label>
                                                <input
                                                        class="size-111 bor8 stext-102 cl2 p-lr-20"
                                                        id="name"
                                                        type="text"
                                                        name="name"
                                                />
                                            </div>

                                            <div class="col-sm-6 p-b-5">
                                                <label class="stext-102 cl3" for="email"
                                                >Email</label
                                                >
                                                <input
                                                        class="size-111 bor8 stext-102 cl2 p-lr-20"
                                                        id="email"
                                                        type="text"
                                                        name="email"
                                                />
                                            </div>
                                        </div>

                                        <button
                                                class="flex-c-m stext-101 cl0 size-112 bg7 bor11 hov-btn3 p-lr-15 trans-04 m-b-10"
                                        >
                                            Submit
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="bg6 flex-c-m flex-w size-302 m-t-73 p-tb-15">
        <span class="stext-107 cl6 p-lr-25"></span>

        <span class="stext-107 cl6 p-lr-25"></span>
    </div>
</section>

<!-- Related Products -->
<section class="sec-relate-product bg0 p-t-45 p-b-105">
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

<!-- Modal1 -->
<div class="wrap-modal1 js-modal1 p-t-60 p-b-20">
    <div class="overlay-modal1 js-hide-modal1"></div>

    <div class="container">
        <div class="bg0 p-t-60 p-b-30 p-lr-15-lg how-pos3-parent">
            <button class="how-pos3 hov3 trans-04 js-hide-modal1">
                <img src="images/icons/icon-close.png" alt="CLOSE" />
            </button>

            <div class="row">
                <div class="col-md-6 col-lg-7 p-b-30">
                    <div class="p-l-25 p-r-30 p-lr-0-lg">
                        <div class="wrap-slick3 flex-sb flex-w">
                            <div class="wrap-slick3-dots"></div>
                            <div class="wrap-slick3-arrows flex-sb-m flex-w"></div>

                            <div class="slick3 gallery-lb">
                                <div
                                        class="item-slick3"
                                        data-thumb="images/product-detail-01.jpg"
                                >
                                    <div class="wrap-pic-w pos-relative">
                                        <img
                                                src="images/product-detail-01.jpg"
                                                alt="IMG-PRODUCT"
                                        />

                                        <a
                                                class="flex-c-m size-108 how-pos1 bor0 fs-16 cl10 bg0 hov-btn3 trans-04"
                                                href="images/product-detail-01.jpg"
                                        >
                                            <i class="fa fa-expand"></i>
                                        </a>
                                    </div>
                                </div>

                                <div
                                        class="item-slick3"
                                        data-thumb="images/product-detail-02.jpg"
                                >
                                    <div class="wrap-pic-w pos-relative">
                                        <img
                                                src="images/product-detail-02.jpg"
                                                alt="IMG-PRODUCT"
                                        />

                                        <a
                                                class="flex-c-m size-108 how-pos1 bor0 fs-16 cl10 bg0 hov-btn3 trans-04"
                                                href="images/product-detail-02.jpg"
                                        >
                                            <i class="fa fa-expand"></i>
                                        </a>
                                    </div>
                                </div>

                                <div
                                        class="item-slick3"
                                        data-thumb="images/product-detail-02.jpg"
                                >
                                    <div class="wrap-pic-w pos-relative">
                                        <img
                                                src="images/product-detail-02.jpg"
                                                alt="IMG-PRODUCT"
                                        />

                                        <a
                                                class="flex-c-m size-108 how-pos1 bor0 fs-16 cl10 bg0 hov-btn3 trans-04"
                                                href="images/product-detail-02.jpg"
                                        >
                                            <i class="fa fa-expand"></i>
                                        </a>
                                    </div>
                                </div>

                                <div
                                        class="item-slick3"
                                        data-thumb="images/product-detail-03.jpg"
                                >
                                    <div class="wrap-pic-w pos-relative">
                                        <img
                                                src="images/product-detail-03.jpg"
                                                alt="IMG-PRODUCT"
                                        />

                                        <a
                                                class="flex-c-m size-108 how-pos1 bor0 fs-16 cl10 bg0 hov-btn3 trans-04"
                                                href="images/product-detail-03.jpg"
                                        >
                                            <i class="fa fa-expand"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 col-lg-5 p-b-30">
                    <div class="p-r-50 p-t-5 p-lr-0-lg">
                        <h4 class="mtext-105 cl2 js-name-detail p-b-14">
                            Lightweight Jacket
                        </h4>

                        <span class="mtext-106 cl2"> $58.79 </span>

                        <p class="stext-102 cl3 p-t-23">
                            Nulla eget sem vitae eros pharetra viverra. Nam vitae luctus
                            ligula. Mauris consequat ornare feugiat.
                        </p>

                        <!--  -->
                        <div class="p-t-33">
                            <div class="flex-w flex-r-m p-b-10">
                                <div class="size-203 flex-c-m respon6">Size</div>

                                <div class="size-204 respon6-next">
                                    <div class="rs1-select2 bor8 bg0">
                                        <select class="js-select2" name="time">
                                            <option>Choose an option</option>
                                            <option>Size S</option>
                                            <option>Size M</option>
                                            <option>Size L</option>
                                            <option>Size XL</option>
                                        </select>
                                        <div class="dropDownSelect2"></div>
                                    </div>
                                </div>
                            </div>

                            <div class="flex-w flex-r-m p-b-10">
                                <div class="size-203 flex-c-m respon6">Color</div>

                                <div class="size-204 respon6-next">
                                    <div class="rs1-select2 bor8 bg0">
                                        <select class="js-select2" name="time">
                                            <option>Choose an option</option>
                                            <option>Red</option>
                                            <option>Blue</option>
                                            <option>White</option>
                                            <option>Grey</option>
                                        </select>
                                        <div class="dropDownSelect2"></div>
                                    </div>
                                </div>
                            </div>

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
                                    >
                                        Add to cart
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!--  -->
                        <div class="flex-w flex-m p-l-100 p-t-40 respon7">
                            <div class="flex-m bor9 p-r-10 m-r-11">
                                <a
                                        href="#"
                                        class="fs-14 cl3 hov-cl1 trans-04 lh-10 p-lr-5 p-tb-2 js-addwish-detail tooltip100"
                                        data-tooltip="Add to Wishlist"
                                >
                                    <i class="zmdi zmdi-favorite"></i>
                                </a>
                            </div>

                            <a
                                    href="#"
                                    class="fs-14 cl3 hov-cl1 trans-04 lh-10 p-lr-5 p-tb-2 m-r-8 tooltip100"
                                    data-tooltip="Facebook"
                            >
                                <i class="fa fa-facebook"></i>
                            </a>

                            <a
                                    href="#"
                                    class="fs-14 cl3 hov-cl1 trans-04 lh-10 p-lr-5 p-tb-2 m-r-8 tooltip100"
                                    data-tooltip="Twitter"
                            >
                                <i class="fa fa-twitter"></i>
                            </a>

                            <a
                                    href="#"
                                    class="fs-14 cl3 hov-cl1 trans-04 lh-10 p-lr-5 p-tb-2 m-r-8 tooltip100"
                                    data-tooltip="Google Plus"
                            >
                                <i class="fa fa-google-plus"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
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
<script>
    $(".js-addwish-b2, .js-addwish-detail").on("click", function (e) {
        e.preventDefault();
    });

    $(".js-addwish-b2").each(function () {
        var nameProduct = $(this).parent().parent().find(".js-name-b2").html();
        $(this).on("click", function () {
            swal(nameProduct, "is added to wishlist !", "success");

            $(this).addClass("js-addedwish-b2");
            $(this).off("click");
        });
    });

    $(".js-addwish-detail").each(function () {
        var nameProduct = $(this)
            .parent()
            .parent()
            .parent()
            .find(".js-name-detail")
            .html();

        $(this).on("click", function () {
            swal(nameProduct, "is added to wishlist !", "success");

            $(this).addClass("js-addedwish-detail");
            $(this).off("click");
        });
    });

    /*---------------------------------------------*/

    $(".js-addcart-detail").each(function () {
        var nameProduct = $(this)
            .parent()
            .parent()
            .parent()
            .parent()
            .find(".js-name-detail")
            .html();
        $(this).on("click", function () {
            swal(nameProduct, "is added to cart !", "success");
        });
    });
</script>
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
<script src="js/main.js"></script>