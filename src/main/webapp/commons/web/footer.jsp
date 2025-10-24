<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!-- Footer -->
<footer class="bg3 p-t-75 p-b-32">
	<div class="container">
		<div class="row">
			<div class="col-sm-6 col-lg-3 p-b-50">
				<h4 class="stext-301 cl0 p-b-30">Sản phẩm Apple</h4>

				<ul>
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

			<div class="col-sm-6 col-lg-3 p-b-50">
				<h4 class="stext-301 cl0 p-b-30">Hỗ trợ khách hàng</h4>

				<ul>
					<li class="p-b-10"><a href="#"
						class="stext-107 cl7 hov-cl1 trans-04"> Tra cứu đơn hàng </a></li>

					<li class="p-b-10"><a href="#"
						class="stext-107 cl7 hov-cl1 trans-04"> Chính sách bảo hành </a></li>

					<li class="p-b-10"><a href="#"
						class="stext-107 cl7 hov-cl1 trans-04"> Giao hàng & Thanh toán </a></li>

					<li class="p-b-10"><a href="#"
						class="stext-107 cl7 hov-cl1 trans-04"> Đổi trả sản phẩm </a></li>

					<li class="p-b-10"><a href="#"
						class="stext-107 cl7 hov-cl1 trans-04"> Câu hỏi thường gặp </a></li>
				</ul>
			</div>

			<div class="col-sm-6 col-lg-3 p-b-50">
				<h4 class="stext-301 cl0 p-b-30">Liên hệ</h4>

				<p class="stext-107 cl7 size-201">
					<i class="fa fa-map-marker m-r-10"></i>
					Số 1 Võ Văn Ngân, Thủ Đức, TP. Hồ Chí Minh
				</p>

				<p class="stext-107 cl7 size-201 p-t-10">
					<i class="fa fa-phone m-r-10"></i>
					Hotline: 028 3896 8641
				</p>

				<p class="stext-107 cl7 size-201 p-t-10">
					<i class="fa fa-envelope m-r-10"></i>
					Email: uteshop@gmail.com
				</p>

				<div class="p-t-27">
					<a href="https://facebook.com" target="_blank" class="fs-18 cl7 hov-cl1 trans-04 m-r-16"> 
						<i class="fa fa-facebook"></i>
					</a> 
					<a href="https://instagram.com" target="_blank" class="fs-18 cl7 hov-cl1 trans-04 m-r-16"> 
						<i class="fa fa-instagram"></i>
					</a> 
					<a href="https://youtube.com" target="_blank" class="fs-18 cl7 hov-cl1 trans-04 m-r-16"> 
						<i class="fa fa-youtube"></i>
					</a>
					<a href="https://tiktok.com" target="_blank" class="fs-18 cl7 hov-cl1 trans-04 m-r-16"> 
						<i class="fa fa-music"></i>
					</a>
				</div>
			</div>

			<div class="col-sm-6 col-lg-3 p-b-50">
				<h4 class="stext-301 cl0 p-b-30">Đăng ký nhận tin</h4>

				<p class="stext-107 cl7 p-b-10">
					Nhận thông tin về sản phẩm mới, khuyến mãi đặc biệt và ưu đãi độc quyền
				</p>

				<form>
					<div class="wrap-input1 w-full p-b-4">
						<input class="input1 bg-none plh1 stext-107 cl7" type="email"
							name="email" placeholder="email@example.com" required>
						<div class="focus-input1 trans-04"></div>
					</div>

					<div class="p-t-18">
						<button
							class="flex-c-m stext-101 cl0 size-103 bg1 bor1 hov-btn2 p-lr-15 trans-04">
							Đăng ký</button>
					</div>
				</form>
			</div>
		</div>

		<div class="p-t-40">
			<div class="flex-c-m flex-w p-b-18">
				<a href="#" class="m-all-1"> <img
					src="${pageContext.request.contextPath}/templates/images/icons/icon-pay-01.png" alt="ICON-PAY">
				</a> <a href="#" class="m-all-1"> <img
					src="${pageContext.request.contextPath}/templates/images/icons/icon-pay-02.png" alt="ICON-PAY">
				</a> <a href="#" class="m-all-1"> <img
					src="${pageContext.request.contextPath}/templates/images/icons/icon-pay-03.png" alt="ICON-PAY">
				</a> <a href="#" class="m-all-1"> <img
					src="${pageContext.request.contextPath}/templates/images/icons/icon-pay-04.png" alt="ICON-PAY">
				</a> <a href="#" class="m-all-1"> <img
					src="${pageContext.request.contextPath}/templates/images/icons/icon-pay-05.png" alt="ICON-PAY">
				</a>
			</div>

			<p class="stext-107 cl6 txt-center">
				Copyright &copy;
				<script>
					document.write(new Date().getFullYear());
				</script>
				UTEShop - Hệ thống bán lẻ ủy quyền Apple chính hãng tại Việt Nam | 
				<a href="#" class="cl7 hov-cl1">Điều khoản sử dụng</a> | 
				<a href="#" class="cl7 hov-cl1">Chính sách bảo mật</a>
			</p>
		</div>
	</div>
</footer>