<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/templates/css/home.css">
<c:set var="cartData" value="${cartData}" />
<c:set var="items" value="${cartData.items}" />
<c:set var="subtotal" value="${cartData.subtotal}" />

<!-- Page title / breadcrumb -->
<div class="container p-t-60 p-b-30">
	<div class="row">
		<div class="col-12">
			<h1></h1>
		</div>
	</div>
</div>

<!-- Shopping Cart -->
<form class="bg0 p-t-75 p-b-85">
	<div class="container">
		<div class="row">
			<div class="col-lg-10 col-xl-7 m-lr-auto m-b-50">
				<div class="m-l-25 m-r--38 m-lr-0-xl">
					<div class="wrap-table-shopping-cart">
						<c:choose>
							<c:when test="${empty items}">
								<div class="text-center p-t-50 p-b-50">
									<i class="zmdi zmdi-shopping-cart"
										style="font-size: 80px; color: #ccc;"></i>
									<p class="stext-113 cl6 p-t-20">Giỏ hàng của bạn đang trống</p>
									<a href="${pageContext.request.contextPath}/home"
										class="flex-c-m stext-101 cl0 size-116 bg3 bor14 hov-btn3 p-lr-15 trans-04 pointer m-t-20"
										style="display: inline-block; width: auto;"> Tiếp tục mua
										sắm </a>
								</div>
							</c:when>
							<c:otherwise>
								<table class="table-shopping-cart">
									<tr class="table_head">
										<th class="column-1">Sản phẩm</th>
										<th class="column-2"></th>
										<th class="column-3">Giá</th>
										<th class="column-4">Số lượng</th>
										<th class="column-5">Tồn kho</th>
										<th class="column-6">Tổng tiền</th>
										<th class="column-7"></th>
									</tr>

									<c:forEach var="item" items="${items}">
										<c:set var="price"
											value="${item.variant != null ? item.variant.price : item.product.basePrice}" />
										<c:set var="itemTotal" value="${price * item.quantity}" />

										<tr class="table_row" data-item-id="${item.id}">
											<td class="column-1">
												<div class="how-itemcart1">
													<c:choose>
														<c:when test="${not empty item.product.images}">
															<img
																src="${pageContext.request.contextPath}/image?fname=${item.product.images[0].imageUrl}"
																alt="${item.product.name}">
														</c:when>
														<c:otherwise>
															<img
																src="${pageContext.request.contextPath}/templates/images/product-default.png"
																alt="${item.product.name}">
														</c:otherwise>
													</c:choose>
												</div>
											</td>
											<td class="column-2">
													<a href="${pageContext.request.contextPath}/product-detail?id=${item.product.id}" 
													   class="stext-104 cl4 hov-cl1 trans-04" 
													   style="text-decoration: none;">
														${item.product.name}
													</a>
													<c:if test="${item.variant != null}">
														<br>
														<small class="text-muted">SKU: ${item.variant.SKU}</small>
													</c:if>
												</td>
											<td class="column-3"><fmt:formatNumber value="${price}"
													type="number" groupingUsed="true" />đ</td>
											<td class="column-4">
												<div class="wrap-num-product flex-w m-l-auto m-r-0">
													<div
														class="cart-btn-decrease cl8 hov-btn3 trans-04 flex-c-m"
														onclick="updateQuantity(<c:out value='${item.id}'/>, -1)">
														<i class="fs-16 zmdi zmdi-minus"></i>
													</div>

													<input class="mtext-104 cl3 txt-center num-product"
														type="number" id="qty-<c:out value='${item.id}'/>"
														value="<c:out value='${item.quantity}'/>" min="1"
														readonly>

													<div
														class="cart-btn-increase cl8 hov-btn3 trans-04 flex-c-m"
														onclick="updateQuantity(<c:out value='${item.id}'/>, 1)">
														<i class="fs-16 zmdi zmdi-plus"></i>
													</div>
												</div>
											</td>
											<td class="column-5 stock-cell" 
												data-variant-id="<c:out value='${item.variant != null ? item.variant.id : 0}'/>">
												<span class="stock-display stext-102 cl6">
													--
												</span>
												<small class="stock-warning stext-111" style="display: none; color: #e74c3c;">
													Tạm hết hàng tại chi nhánh này
												</small>
											</td>
											<td class="column-6" id="total-<c:out value='${item.id}'/>">
												<fmt:formatNumber value="${itemTotal}" type="number"
													groupingUsed="true" />đ
											</td>
											<td class="column-7">
												<button type="button" class="btn-remove-item"
													onclick="removeItem(<c:out value='${item.id}'/>)"
													style="border: none; background: none; cursor: pointer; color: #888; padding: 10px; min-width: 40px; min-height: 40px;">
													<i class="zmdi zmdi-close fs-20"></i>
												</button>
											</td>
										</tr>
									</c:forEach>
								</table>

								<div
								class="flex-w flex-sb-m bor15 p-t-18 p-b-15 p-lr-40 p-lr-15-sm">
								<div class="flex-w flex-m m-r-20 m-tb-5" style="width: 100%; max-width: 600px;">
									<!-- Voucher selector -->
									<select id="voucher-selector" 
											class="stext-104 cl2 plh4 size-117 bor13 p-lr-20 m-r-10 m-tb-5"
											style="flex: 1; min-width: 200px;"
											onchange="onVoucherSelect()">
										<option value="">-- Chọn voucher hoặc nhập mã --</option>
									</select>
									
									<!-- Manual input -->
									<input
										class="stext-104 cl2 plh4 size-117 bor13 p-lr-20 m-r-10 m-tb-5"
										style="flex: 1; min-width: 150px;"
										type="text" id="voucher-code" placeholder="Hoặc nhập mã voucher">

									<!-- Apply button -->
									<div
										class="flex-c-m stext-101 cl2 size-118 bg8 bor13 hov-btn3 p-lr-15 trans-04 pointer m-tb-5"
										onclick="applyVoucher()">Áp dụng</div>
								</div>

								<a href="${pageContext.request.contextPath}/home"
									class="flex-c-m stext-101 cl2 size-119 bg8 bor13 hov-btn3 p-lr-15 trans-04 pointer m-tb-10">
									Tiếp tục mua sắm </a>
							</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>

			<c:if test="${not empty items}">
				<div class="col-sm-10 col-lg-7 col-xl-5 m-lr-auto m-b-50">
					<div
						class="bor10 p-lr-40 p-t-30 p-b-40 m-l-63 m-r-40 m-lr-0-xl p-lr-15-sm">
						<h4 class="mtext-109 cl2 p-b-30">Tổng giỏ hàng</h4>

						<!-- Chọn chi nhánh giao hàng -->
						<div class="p-b-20">
							<label class="stext-102 cl3 p-b-10">Giao từ chi nhánh:</label>
							<select id="branch-select" class="stext-104 cl2 size-116 bor13 p-lr-20" style="width: 100%; height: 50px;">
								<option value="">-- Chọn chi nhánh --</option>
							</select>
						</div>

						<!-- Chọn địa chỉ giao đến -->
						<div class="p-b-30">
							<label class="stext-102 cl3 p-b-10">Giao đến địa chỉ:</label>
							<select id="address-select" class="stext-104 cl2 size-116 bor13 p-lr-20" style="width: 100%; height: 50px;">
								<option value="">-- Chọn địa chỉ --</option>
							</select>
							<small class="stext-111 cl6 p-t-5" style="display: block;">
								<a href="${pageContext.request.contextPath}/profile#addresses" class="cl4 hov-cl1">
									<i class="zmdi zmdi-plus"></i> Thêm địa chỉ mới
								</a>
							</small>
						</div>

						<div class="flex-w flex-t bor12 p-b-13">
							<div class="size-208">
								<span class="stext-110 cl2"> Tạm tính: </span>
							</div>

							<div class="size-209">
								<span class="mtext-110 cl2" id="cart-subtotal"> <fmt:formatNumber
										value="${subtotal}" type="number" groupingUsed="true" />đ
								</span>
							</div>
						</div>

						<!-- Voucher discount -->
						<div class="flex-w flex-t bor12 p-t-15 p-b-13" id="voucher-discount-row" style="display: none;">
							<div class="size-208">
								<span class="stext-110 cl2"> Giảm giá: </span>
								<small class="stext-111 cl6" id="voucher-type-display"></small>
							</div>

							<div class="size-209">
								<span class="mtext-110" style="color: #e74c3c;" id="discount-amount">-0đ</span>
								<button type="button" onclick="removeVoucher()" 
										style="border: none; background: none; cursor: pointer; margin-left: 10px; color: #888;">
									<i class="zmdi zmdi-close"></i>
								</button>
							</div>
						</div>

						<div class="flex-w flex-t bor12 p-t-15 p-b-30">
							<div class="size-208 w-full-ssm">
								<span class="stext-110 cl2"> Phí vận chuyển: </span>
							</div>

							<div class="size-209 p-t-1">
								<span class="stext-111 cl6"> Tính khi thanh toán </span>
							</div>
						</div>
						
						<!-- Out of stock notice -->
						<div id="out-of-stock-notice" style="display: none; margin-bottom: 15px; padding: 10px; background-color: #fff3cd; border-left: 3px solid #ffa534; border-radius: 3px;">
							<small class="stext-111" style="color: #856404;">
								<i class="zmdi zmdi-alert-circle"></i>
								<span id="out-of-stock-count">0</span> sản phẩm hết hàng tại chi nhánh này
							</small>
						</div>

						<div class="flex-w flex-t p-t-27 p-b-33">
							<div class="size-208">
								<span class="mtext-101 cl2"> Tổng cộng: </span>
							</div>

							<div class="size-209 p-t-1">
								<span class="mtext-110 cl2" id="cart-total"> <fmt:formatNumber
										value="${subtotal}" type="number" groupingUsed="true" />đ
								</span>
							</div>
						</div>

						<button type="button"
							class="flex-c-m stext-101 cl0 size-116 bg3 bor14 hov-btn3 p-lr-15 trans-04 pointer"
							onclick="proceedToCheckout()">Đi đến thanh toán</button>
					</div>
				</div>
			</c:if>
		</div>
	</div>
</form>

<style>
/* Custom quantity buttons to avoid conflict with template */
.cart-btn-decrease,
.cart-btn-increase {
    cursor: pointer;
}

.cart-btn-decrease:hover,
.cart-btn-increase:hover {
    background-color: #f0f0f0;
}

.btn-remove-item:hover {
    color: #e74c3c !important;
    transform: scale(1.1);
    transition: all 0.2s ease;
}

.btn-remove-item:active {
    transform: scale(0.95);
}

/* Out of stock item styles */
.table_row.out-of-stock {
    opacity: 0.5;
    background-color: #f9f9f9;
    position: relative;
}

.table_row.out-of-stock::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: repeating-linear-gradient(
        45deg,
        transparent,
        transparent 10px,
        rgba(0,0,0,0.02) 10px,
        rgba(0,0,0,0.02) 20px
    );
    pointer-events: none;
}

.table_row.out-of-stock .column-6 {
    text-decoration: line-through;
    color: #999;
}

.out-of-stock-badge {
    display: inline-block;
    background-color: #e74c3c;
    color: white;
    padding: 2px 8px;
    border-radius: 3px;
    font-size: 11px;
    margin-left: 10px;
    font-weight: bold;
}
</style>

<script>
// Prevent concurrent updates
const updateQueue = new Map();

function updateQuantity(itemId, change) {
    const input = document.getElementById('qty-' + itemId);
    const row = document.querySelector('tr[data-item-id="' + itemId + '"]');
    
    // Get current stock from display
    const stockCell = row.querySelector('.stock-cell');
    const stockDisplay = stockCell ? stockCell.querySelector('.stock-display') : null;
    const currentStock = stockDisplay ? parseInt(stockDisplay.textContent) : 999;
    
    let newQty = parseInt(input.value) + change;
    if (newQty < 1) newQty = 1;
    
    // Validate against stock
    if (currentStock !== 999 && !isNaN(currentStock)) {
        if (newQty > currentStock) {
            swal({
                title: "Vượt quá tồn kho",
                text: "Chỉ còn " + currentStock + " sản phẩm trong kho tại chi nhánh này",
                icon: "warning"
            });
            return;
        }
    }
    
    input.value = newQty;
    updateCartItemDebounced(itemId, newQty);
}

// Debounce update to prevent rapid clicks
function updateCartItemDebounced(itemId, quantity) {
    // Clear existing timeout for this item
    if (updateQueue.has(itemId)) {
        clearTimeout(updateQueue.get(itemId));
    }
    
    // Set new timeout
    const timeoutId = setTimeout(() => {
        updateCartItem(itemId, quantity);
        updateQueue.delete(itemId);
    }, 500); // Wait 500ms after last click
    
    updateQueue.set(itemId, timeoutId);
}

function updateCartItem(itemId, quantity) {
    console.log('Updating item:', itemId, 'quantity:', quantity);
    
    // Disable buttons during update
    const row = document.querySelector('tr[data-item-id="' + itemId + '"]');
    const buttons = row.querySelectorAll('.cart-btn-decrease, .cart-btn-increase');
    buttons.forEach(btn => btn.style.pointerEvents = 'none');
    
    fetch('${pageContext.request.contextPath}/cart/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'itemId=' + itemId + '&quantity=' + quantity
    })
    .then(response => response.json())
    .then(data => {
        console.log('Update response:', data);
        if (data.success) {
            // Update item total real-time
            const row = document.querySelector('tr[data-item-id="' + itemId + '"]');
            if (row) {
                const priceCell = row.querySelector('.column-3');
                const priceText = priceCell.textContent.replace(/[^\d]/g, '');
                const price = parseInt(priceText);
                const itemTotal = price * quantity;
                
                // Update item total
                const totalCell = document.getElementById('total-' + itemId);
                totalCell.textContent = new Intl.NumberFormat('vi-VN').format(itemTotal) + 'đ';
            }
            
            // Update cart totals
            updateCartTotals();
            
            // Update cart count in header
            updateCartCount();
            
            // Update button states based on stock
            updateButtonStates(itemId);
        } else {
            swal({
                title: "Lỗi",
                text: data.message || 'Có lỗi xảy ra',
                icon: "error"
            });
        }
        
        // Re-enable buttons
        const row = document.querySelector('tr[data-item-id="' + itemId + '"]');
        const buttons = row.querySelectorAll('.cart-btn-decrease, .cart-btn-increase');
        buttons.forEach(btn => btn.style.pointerEvents = 'auto');
    })
    .catch(error => {
        console.error('Error updating cart:', error);
        swal({
            title: "Lỗi",
            text: "Có lỗi xảy ra khi cập nhật giỏ hàng",
            icon: "error"
        });
        
        // Re-enable buttons
        const row = document.querySelector('tr[data-item-id="' + itemId + '"]');
        if (row) {
            const buttons = row.querySelectorAll('.cart-btn-decrease, .cart-btn-increase');
            buttons.forEach(btn => btn.style.pointerEvents = 'auto');
        }
    });
}

function updateCartTotals() {
    console.log('Updating cart totals...');
    
    // CRITICAL: Restore voucher info from sessionStorage if missing
    if (!appliedVoucherCode) {
        const code = sessionStorage.getItem('voucherCode');
        const type = sessionStorage.getItem('voucherType');
        const value = sessionStorage.getItem('voucherValue');
        
        if (code && type && value) {
            appliedVoucherCode = code;
            appliedVoucherType = parseInt(type);
            appliedVoucherValue = parseFloat(value);
            
            console.log('Restored voucher variables from sessionStorage:', {
                code: appliedVoucherCode,
                type: appliedVoucherType,
                typeValid: !isNaN(appliedVoucherType),
                value: appliedVoucherValue,
                valueValid: !isNaN(appliedVoucherValue)
            });
            
            // Validate
            if (isNaN(appliedVoucherType) || isNaN(appliedVoucherValue)) {
                console.error('❌ Invalid data from sessionStorage, clearing...');
                appliedVoucherCode = null;
                appliedVoucherType = null;
                appliedVoucherValue = null;
            }
        }
    }
    
    // Calculate new totals from all items
    let subtotal = 0;
    let outOfStockCount = 0;
    let inStockCount = 0;
    
    document.querySelectorAll('.table_row').forEach(row => {
        // Count out of stock items
        if (row.classList.contains('out-of-stock')) {
            outOfStockCount++;
            console.log('Skipping out-of-stock item:', row.getAttribute('data-item-id'));
            return; // Don't include in total
        }
        
        const totalCell = row.querySelector('[id^="total-"]');
        if (totalCell) {
            const totalText = totalCell.textContent.replace(/[^\d]/g, '');
            const itemTotal = parseInt(totalText);
            subtotal += itemTotal;
            inStockCount++;
            console.log('Including item:', row.getAttribute('data-item-id'), 'Total:', itemTotal);
        }
    });
    
    console.log('Subtotal calculation:', {
        inStockItems: inStockCount,
        outOfStockItems: outOfStockCount,
        subtotal: subtotal
    });
    
    // Update subtotal display FIRST
    const subtotalFormatted = new Intl.NumberFormat('vi-VN').format(subtotal) + 'đ';
    const subtotalEl = document.getElementById('cart-subtotal');
    if (subtotalEl) {
        subtotalEl.textContent = subtotalFormatted;
        console.log('✅ Updated subtotal display to:', subtotalFormatted, '(raw value:', subtotal, ')');
    }
    
    // CRITICAL: Use setTimeout to ensure DOM has updated before recalculating voucher
    setTimeout(() => {
        // Recalculate voucher if applied
        console.log('Checking voucher:', {
            code: appliedVoucherCode,
            type: appliedVoucherType,
            value: appliedVoucherValue
        });
        
        if (appliedVoucherCode) {
            // Re-read subtotal from DOM to ensure accuracy
            const currentSubtotalText = document.getElementById('cart-subtotal').textContent;
            const currentSubtotal = parseFloat(currentSubtotalText.replace(/[^\d]/g, ''));
            
            console.log('Voucher is applied, recalculating with CURRENT subtotal:', currentSubtotal);
            
            // Ensure discount row is visible
            const discountRow = document.getElementById('voucher-discount-row');
            if (discountRow && discountRow.style.display === 'none') {
                discountRow.style.display = 'flex';
                console.log('Made discount row visible');
            }
            
            recalculateVoucher(currentSubtotal);
        } else {
            console.log('No voucher applied, setting total = subtotal');
            
            // Hide discount row if no voucher
            const discountRow = document.getElementById('voucher-discount-row');
            if (discountRow && discountRow.style.display !== 'none') {
                discountRow.style.display = 'none';
                console.log('Hidden discount row');
            }
            
            // Re-read subtotal for consistency
            const currentSubtotalText = document.getElementById('cart-subtotal').textContent;
            
            const totalEl = document.getElementById('cart-total');
            if (totalEl) {
                totalEl.textContent = currentSubtotalText;
                console.log('Total set to:', currentSubtotalText);
            }
        }
    }, 10); // End setTimeout - small delay to ensure DOM update
    
    // Show/hide out of stock notice (outside setTimeout)
    const notice = document.getElementById('out-of-stock-notice');
    const countSpan = document.getElementById('out-of-stock-count');
    if (outOfStockCount > 0) {
        countSpan.textContent = outOfStockCount;
        notice.style.display = 'block';
    } else {
        notice.style.display = 'none';
    }
}

function updateCartCount() {
    // Update cart count in header
    fetch('${pageContext.request.contextPath}/cart/count')
        .then(response => response.json())
        .then(data => {
            if (data.count !== undefined) {
                document.querySelectorAll('.js-show-cart').forEach(el => {
                    el.setAttribute('data-notify', data.count);
                });
            }
        })
        .catch(error => {
            console.error('Error updating cart count:', error);
        });
}

function updateButtonStates(itemId) {
    // Update increase/decrease button states based on stock
    const row = document.querySelector('tr[data-item-id="' + itemId + '"]');
    if (!row) return;
    
    const stockCell = row.querySelector('.stock-cell');
    const stockDisplay = stockCell ? stockCell.querySelector('.stock-display') : null;
    const currentStock = stockDisplay ? parseInt(stockDisplay.textContent) : 999;
    
    const qtyInput = document.getElementById('qty-' + itemId);
    const currentQty = qtyInput ? parseInt(qtyInput.value) : 0;
    
    const increaseBtn = row.querySelector('.cart-btn-increase');
    
    if (increaseBtn && !isNaN(currentStock) && currentStock !== 999) {
        if (currentQty >= currentStock || currentStock === 0) {
            increaseBtn.style.opacity = '0.3';
            increaseBtn.style.cursor = 'not-allowed';
            increaseBtn.style.pointerEvents = 'none';
        } else {
            increaseBtn.style.opacity = '1';
            increaseBtn.style.cursor = 'pointer';
            increaseBtn.style.pointerEvents = 'auto';
        }
    }
}

function removeItem(itemId) {
    console.log('Removing item:', itemId);
    
    swal({
        title: "Xác nhận xóa",
        text: "Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?",
        icon: "warning",
        buttons: {
            cancel: {
                text: "Hủy",
                visible: true,
                className: "swal-button--cancel"
            },
            confirm: {
                text: "Xóa",
                className: "swal-button--danger"
            }
        },
        dangerMode: true
    }).then((willDelete) => {
        if (willDelete) {
            removeItemConfirmed(itemId);
        }
    });
}

function removeItemConfirmed(itemId) {
    
    fetch('${pageContext.request.contextPath}/cart/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'itemId=' + itemId
    })
    .then(response => response.json())
    .then(data => {
        console.log('Remove response:', data);
        if (data.success) {
            // Remove row with animation
            const row = document.querySelector('tr[data-item-id="' + itemId + '"]');
            if (row) {
                row.style.transition = 'opacity 0.3s';
                row.style.opacity = '0';
                setTimeout(() => {
                    row.remove();
                    
                    // Check if cart is empty
                    const remainingRows = document.querySelectorAll('.table_row');
                    if (remainingRows.length === 0) {
                        location.reload(); // Reload to show empty cart message
                    } else {
                        updateCartTotals();
                        // Update cart count in header
                        updateCartCount();
                    }
                }, 300);
            }
        } else {
            swal({
                title: "Lỗi",
                text: data.message || 'Không thể xóa sản phẩm',
                icon: "error"
            });
        }
    })
    .catch(error => {
        console.error('Error removing item:', error);
        swal({
            title: "Lỗi",
            text: "Có lỗi xảy ra khi xóa sản phẩm",
            icon: "error"
        });
    });
}

// Global variables to store applied voucher info
let appliedVoucherCode = null;
let appliedVoucherType = null; // 1=Percent, 2=Amount
let appliedVoucherValue = null;

function applyVoucher() {
    const code = document.getElementById('voucher-code').value.trim();
    if (!code) {
        swal({
            title: "Thông báo",
            text: "Vui lòng nhập mã voucher",
            icon: "warning"
        });
        return;
    }
    
    // Show loading
    const applyBtn = document.querySelector('.flex-c-m.stext-101.cl2.size-118');
    const originalText = applyBtn.textContent;
    applyBtn.textContent = 'Đang xử lý...';
    applyBtn.style.pointerEvents = 'none';
    
    fetch('${pageContext.request.contextPath}/cart/apply-voucher', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'voucherCode=' + encodeURIComponent(code)
    })
    .then(response => response.json())
    .then(data => {
        console.log('📥 Server response for apply voucher:', data);
        
        applyBtn.textContent = originalText;
        applyBtn.style.pointerEvents = 'auto';
        
        if (data.success) {
            // Store voucher info for recalculation
            appliedVoucherCode = data.voucherCode;
            appliedVoucherType = parseInt(data.voucherType);
            appliedVoucherValue = parseFloat(data.voucherValue);
            
            console.log('✅ Applied voucher variables:', {
                code: appliedVoucherCode,
                type: appliedVoucherType,
                value: appliedVoucherValue,
                typeName: data.typeName
            });
            
            // Validate parsed values
            if (isNaN(appliedVoucherType) || isNaN(appliedVoucherValue)) {
                console.error('❌ Invalid voucher data from server:', data);
                swal({
                    title: "Lỗi",
                    text: "Dữ liệu voucher không hợp lệ",
                    icon: "error"
                });
                return;
            }
            
            // Persist to sessionStorage for page reloads
            sessionStorage.setItem('voucherCode', data.voucherCode);
            sessionStorage.setItem('voucherType', data.voucherType);
            sessionStorage.setItem('voucherValue', data.voucherValue);
            sessionStorage.setItem('voucherTypeName', data.typeName);
            
            // Show success message
            swal({
                title: "Thành công!",
                text: data.message,
                icon: "success"
            });
            
            // Update UI
            updateVoucherDisplay(data);
            
            // Disable both selector and input
            document.getElementById('voucher-selector').disabled = true;
            document.getElementById('voucher-code').value = data.voucherCode;
            document.getElementById('voucher-code').disabled = true;
            applyBtn.style.display = 'none';
        } else {
            swal({
                title: "Lỗi",
                text: data.message || 'Không thể áp dụng voucher',
                icon: "error"
            });
        }
    })
    .catch(error => {
        console.error('Error applying voucher:', error);
        applyBtn.textContent = originalText;
        applyBtn.style.pointerEvents = 'auto';
        swal({
            title: "Lỗi",
            text: "Có lỗi xảy ra khi áp dụng voucher",
            icon: "error"
        });
    });
}

function updateVoucherDisplay(data) {
    // Show voucher discount row
    const discountRow = document.getElementById('voucher-discount-row');
    discountRow.style.display = 'flex';
    
    // Store voucher info in data attributes for persistence
    discountRow.setAttribute('data-voucher-code', data.voucherCode);
    discountRow.setAttribute('data-voucher-type', data.voucherType);
    discountRow.setAttribute('data-voucher-value', data.voucherValue);
    discountRow.setAttribute('data-voucher-typename', data.typeName);
    
    // Update voucher type display
    const typeDisplay = document.getElementById('voucher-type-display');
    typeDisplay.textContent = '(' + data.typeName + ')';
    
    // CRITICAL: Don't use server's discount/total, recalculate with CURRENT subtotal
    // Get current subtotal from DOM (may have changed due to out-of-stock items)
    const subtotalEl = document.getElementById('cart-subtotal');
    const currentSubtotalText = subtotalEl ? subtotalEl.textContent : '';
    const currentSubtotal = parseFloat(currentSubtotalText.replace(/[^\d]/g, ''));
    
    console.log('updateVoucherDisplay:', {
        subtotalText: currentSubtotalText,
        subtotalValue: currentSubtotal,
        isValid: !isNaN(currentSubtotal) && currentSubtotal > 0
    });
    
    // Validate subtotal before recalculating
    if (isNaN(currentSubtotal) || currentSubtotal <= 0) {
        console.error('Invalid subtotal detected! Using server data as fallback.');
        // Fallback: use server data if subtotal is invalid
        const discountAmount = document.getElementById('discount-amount');
        const discountValue = parseFloat(data.discountAmount);
        discountAmount.textContent = '-' + new Intl.NumberFormat('vi-VN').format(discountValue) + 'đ';
        
        const total = parseFloat(data.total);
        document.getElementById('cart-total').textContent = new Intl.NumberFormat('vi-VN').format(total) + 'đ';
        return;
    }
    
    // Use setTimeout to ensure DOM is ready
    setTimeout(() => {
        recalculateVoucher(currentSubtotal);
    }, 10);
}

// Recalculate voucher discount when subtotal changes (CLIENT-SIDE)
function recalculateVoucher(newSubtotal) {
    console.log('🔄 recalculateVoucher called with subtotal:', newSubtotal);
    
    // Validate input
    if (isNaN(newSubtotal) || newSubtotal <= 0) {
        console.error('❌ Invalid subtotal:', newSubtotal);
        return;
    }
    
    // Try to restore from sessionStorage if missing
    if (!appliedVoucherCode || !appliedVoucherType || appliedVoucherValue === null) {
        const code = sessionStorage.getItem('voucherCode');
        const type = sessionStorage.getItem('voucherType');
        const value = sessionStorage.getItem('voucherValue');
        
        console.log('Voucher variables missing, attempting restore from sessionStorage:', {code, type, value});
        
        if (code && type && value) {
            appliedVoucherCode = code;
            appliedVoucherType = parseInt(type);
            appliedVoucherValue = parseFloat(value);
            console.log('✅ Restored voucher in recalculateVoucher:', {
                code: appliedVoucherCode,
                type: appliedVoucherType,
                value: appliedVoucherValue
            });
        } else {
            console.error('❌ Cannot recalculate voucher - missing voucher info and sessionStorage is empty');
            return;
        }
    }
    
    let discountAmount = 0;
    
    // Calculate discount based on voucher type
    if (appliedVoucherType === 1) {
        // Percent discount
        discountAmount = newSubtotal * (appliedVoucherValue / 100);
        console.log(`Calculating PERCENT discount: ${newSubtotal} * ${appliedVoucherValue}% = ${discountAmount}`);
    } else if (appliedVoucherType === 2) {
        // Fixed amount discount
        discountAmount = Math.min(appliedVoucherValue, newSubtotal);
        console.log(`Calculating FIXED discount: min(${appliedVoucherValue}, ${newSubtotal}) = ${discountAmount}`);
    } else {
        console.error('❌ Unknown voucher type:', appliedVoucherType);
    }
    
    // Ensure non-negative
    if (discountAmount < 0) {
        console.warn('Discount was negative, setting to 0');
        discountAmount = 0;
    }
    
    console.log('📊 Voucher calculation result:', {
        code: appliedVoucherCode,
        type: appliedVoucherType === 1 ? 'Percent' : 'Amount',
        value: appliedVoucherValue,
        subtotal: newSubtotal,
        discountBeforeRound: discountAmount,
        discountAfterRound: Math.round(discountAmount)
    });
    
    // Round discount to avoid floating point issues
    discountAmount = Math.round(discountAmount);
    
    // Update discount display
    const discountAmountEl = document.getElementById('discount-amount');
    if (discountAmountEl) {
        discountAmountEl.textContent = '-' + new Intl.NumberFormat('vi-VN').format(discountAmount) + 'đ';
        console.log('Updated discount display:', discountAmountEl.textContent);
    } else {
        console.error('Discount amount element not found!');
    }
    
    // Calculate and update total
    const total = Math.max(0, newSubtotal - discountAmount);
    const totalEl = document.getElementById('cart-total');
    
    if (totalEl) {
        const formattedTotal = new Intl.NumberFormat('vi-VN').format(total) + 'đ';
        totalEl.textContent = formattedTotal;
        
        // Force DOM update
        totalEl.style.display = 'none';
        setTimeout(() => {
            totalEl.style.display = '';
        }, 0);
        
        console.log('Updated total display:', formattedTotal, '(element text:', totalEl.textContent, ')');
    } else {
        console.error('Total element not found!');
    }
    
    console.log('Final calculation:', {
        subtotal: newSubtotal,
        discount: discountAmount,
        total: total
    });
}

// Remove voucher without confirmation
function removeVoucherSilently() {
    // Reset voucher variables
    appliedVoucherCode = null;
    appliedVoucherType = null;
    appliedVoucherValue = null;
    
    // Clear sessionStorage
    sessionStorage.removeItem('voucherCode');
    sessionStorage.removeItem('voucherType');
    sessionStorage.removeItem('voucherValue');
    sessionStorage.removeItem('voucherTypeName');
    
    document.getElementById('voucher-discount-row').style.display = 'none';
    const subtotalText = document.getElementById('cart-subtotal').textContent;
    document.getElementById('cart-total').textContent = subtotalText;
    
    // Reset both selector and input
    document.getElementById('voucher-selector').value = '';
    document.getElementById('voucher-selector').disabled = false;
    document.getElementById('voucher-code').value = '';
    document.getElementById('voucher-code').disabled = false;
    
    const applyBtn = document.querySelector('.flex-c-m.stext-101.cl2.size-118');
    if (applyBtn) applyBtn.style.display = 'block';
}

function removeVoucher() {
    swal({
        title: "Xác nhận",
        text: "Bạn có chắc muốn hủy voucher?",
        icon: "warning",
        buttons: {
            cancel: {
                text: "Không",
                visible: true,
                className: "swal-button--cancel"
            },
            confirm: {
                text: "Có",
                className: "swal-button--danger"
            }
        }
    }).then((confirmed) => {
        if (confirmed) {
            // Reset voucher variables
            appliedVoucherCode = null;
            appliedVoucherType = null;
            appliedVoucherValue = null;
            
            // Clear sessionStorage
            sessionStorage.removeItem('voucherCode');
            sessionStorage.removeItem('voucherType');
            sessionStorage.removeItem('voucherValue');
            sessionStorage.removeItem('voucherTypeName');
            
            // Hide discount row
            document.getElementById('voucher-discount-row').style.display = 'none';
            
            // Reset total to subtotal
            const subtotalText = document.getElementById('cart-subtotal').textContent;
            document.getElementById('cart-total').textContent = subtotalText;
            
            // Reset both selector and input
            document.getElementById('voucher-selector').value = '';
            document.getElementById('voucher-selector').disabled = false;
            document.getElementById('voucher-code').value = '';
            document.getElementById('voucher-code').disabled = false;
            
            // Show apply button
            const applyBtn = document.querySelector('.flex-c-m.stext-101.cl2.size-118');
            applyBtn.style.display = 'block';
            
            swal({
                title: "Đã hủy",
                text: "Voucher đã được gỡ bỏ",
                icon: "info",
                timer: 1500
            });
        }
    });
}

// ===== VOUCHER FUNCTIONS =====

/**
 * Restore voucher info from sessionStorage after page reload
 */
function restoreVoucherInfo() {
    const code = sessionStorage.getItem('voucherCode');
    const type = sessionStorage.getItem('voucherType');
    const value = sessionStorage.getItem('voucherValue');
    const typeName = sessionStorage.getItem('voucherTypeName');
    
    if (code && type && value && typeName) {
        // Restore global variables
        appliedVoucherCode = code;
        appliedVoucherType = parseInt(type);
        appliedVoucherValue = parseFloat(value);
        
        console.log('Restored voucher from sessionStorage:', code, type, value);
        
        // Show discount row
        const discountRow = document.getElementById('voucher-discount-row');
        discountRow.style.display = 'flex';
        
        // Update type display
        const typeDisplay = document.getElementById('voucher-type-display');
        typeDisplay.textContent = '(' + typeName + ')';
        
        // Disable selector and input
        const voucherSelector = document.getElementById('voucher-selector');
        const voucherInput = document.getElementById('voucher-code');
        const applyBtn = document.querySelector('.flex-c-m.stext-101.cl2.size-118');
        
        if (voucherSelector) voucherSelector.disabled = true;
        if (voucherInput) {
            voucherInput.value = code;
            voucherInput.disabled = true;
        }
        if (applyBtn) applyBtn.style.display = 'none';
        
        // Use setTimeout to ensure DOM is fully rendered before recalculating
        setTimeout(() => {
            const subtotalText = document.getElementById('cart-subtotal').textContent;
            const subtotal = parseFloat(subtotalText.replace(/[^\d]/g, ''));
            
            console.log('Attempting to restore voucher calculation, subtotal:', subtotal);
            
            if (subtotal > 0) {
                console.log('Recalculating restored voucher...');
                recalculateVoucher(subtotal);
            } else {
                // If subtotal is 0, try again after a longer delay
                console.warn('Subtotal is 0, retrying after 500ms...');
                setTimeout(() => {
                    const retrySubtotalText = document.getElementById('cart-subtotal').textContent;
                    const retrySubtotal = parseFloat(retrySubtotalText.replace(/[^\d]/g, ''));
                    if (retrySubtotal > 0) {
                        console.log('Retry successful, recalculating voucher with subtotal:', retrySubtotal);
                        recalculateVoucher(retrySubtotal);
                    } else {
                        console.error('Failed to get valid subtotal after retry');
                    }
                }, 500);
            }
        }, 100);
    }
}

/**
 * Load danh sách vouchers vào combobox
 */
function loadVouchers() {
    const voucherSelector = document.getElementById('voucher-selector');
    if (!voucherSelector) {
        console.warn('Voucher selector element not found');
        return;
    }
    
    // Check if already loaded OR currently loading
    if (vouchersLoaded || isLoadingVouchers) {
        console.log('Vouchers already loaded or loading, skipping...');
        return;
    }
    
    // Check DOM - if already has options, mark as loaded
    if (voucherSelector.options.length > 1) {
        console.log('Vouchers already in DOM, skipping...');
        vouchersLoaded = true;
        return;
    }
    
    isLoadingVouchers = true;
    console.log('Loading vouchers...');
    
    fetch('${pageContext.request.contextPath}/cart/vouchers')
        .then(response => {
            if (!response.ok) {
                throw new Error('HTTP error ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (data.success && data.vouchers) {
                // Clear existing options except placeholder
                voucherSelector.innerHTML = '<option value="">-- Chọn voucher hoặc nhập mã --</option>';
                
                data.vouchers.forEach(voucher => {
                    const option = document.createElement('option');
                    option.value = voucher.code;
                    
                    // Format: "SALE10 - Giảm 10% (Còn 95 lượt)"
                    let text = voucher.code + ' - ' + voucher.typeName;
                    
                    if (voucher.description) {
                        text += ' - ' + voucher.description;
                    }
                    
                    if (voucher.remainingUses < 100) {
                        text += ' (Còn ' + voucher.remainingUses + ' lượt)';
                    }
                    
                    option.textContent = text;
                    voucherSelector.appendChild(option);
                });
                
                vouchersLoaded = true;
                console.log('Vouchers loaded successfully:', data.vouchers.length);
            } else {
                console.error('Invalid vouchers data:', data);
            }
        })
        .catch(error => {
            console.error('Error loading vouchers:', error);
            vouchersLoaded = false; // Allow retry
        })
        .finally(() => {
            isLoadingVouchers = false;
        });
}

/**
 * Handle khi user chọn voucher từ combobox
 */
function onVoucherSelect() {
    const selector = document.getElementById('voucher-selector');
    const codeInput = document.getElementById('voucher-code');
    
    if (selector.value) {
        // User chọn voucher từ dropdown
        codeInput.value = selector.value;
        codeInput.disabled = true; // Disable manual input
    } else {
        // User chọn option "-- Chọn voucher --"
        codeInput.value = '';
        codeInput.disabled = false; // Enable manual input
    }
}

function proceedToCheckout() {
    // TODO: Implement checkout
    swal({
        title: "Thông báo",
        text: "Tính năng thanh toán đang phát triển",
        icon: "info"
    });
}

// ===== BRANCH & ADDRESS SELECTION =====

// Cache to prevent duplicate loads
let branchesLoaded = false;
let addressesLoaded = false;
let vouchersLoaded = false;
let isLoadingBranches = false;
let isLoadingAddresses = false;
let isLoadingVouchers = false;
let branchesRetryCount = 0;
let addressesRetryCount = 0;
const MAX_RETRY_COUNT = 3;

// Load branches và addresses khi page load
document.addEventListener('DOMContentLoaded', function() {
    // Load all resources
    loadBranches();
    loadAddresses();
    loadVouchers();
    
    // Restore voucher info if discount row is visible (page reload case)
    restoreVoucherInfo();
    
    // Event listener cho branch select
    const branchSelect = document.getElementById('branch-select');
    if (branchSelect) {
        branchSelect.addEventListener('change', function() {
            const branchId = this.value;
            if (branchId) {
                updateStockDisplay(branchId);
            } else {
                // Reset stock display
                document.querySelectorAll('.stock-cell').forEach(cell => {
                    cell.querySelector('.stock-display').textContent = '--';
                    cell.querySelector('.stock-warning').style.display = 'none';
                });
            }
        });
    }
});

function loadBranches() {
    const branchSelect = document.getElementById('branch-select');
    if (!branchSelect) {
        console.warn('Branch select element not found');
        return;
    }
    
    // Check if already loaded OR currently loading
    if (branchesLoaded || isLoadingBranches) {
        console.log('Branches already loaded or loading, skipping...');
        return;
    }
    
    // Check DOM - if already has options, mark as loaded
    if (branchSelect.options.length > 1) {
        console.log('Branches already in DOM, skipping...');
        branchesLoaded = true;
        return;
    }
    
    isLoadingBranches = true;
    branchSelect.disabled = true;
    branchSelect.innerHTML = '<option value="">Đang tải chi nhánh...</option>';
    console.log('Loading branches...');
    
    fetch('${pageContext.request.contextPath}/cart/branches')
        .then(response => {
            if (!response.ok) {
                throw new Error('HTTP error ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (data.success && data.branches) {
                // Clear existing options except placeholder
                branchSelect.innerHTML = '<option value="">-- Chọn chi nhánh --</option>';
                
                data.branches.forEach(branch => {
                    const option = document.createElement('option');
                    option.value = branch.id;
                    option.textContent = branch.name;
                    if (branch.address) {
                        option.textContent += ' - ' + branch.address;
                    }
                    branchSelect.appendChild(option);
                });
                
                // Auto select first branch if only one
                if (data.branches.length === 1) {
                    branchSelect.value = data.branches[0].id;
                    updateStockDisplay(data.branches[0].id);
                }
                
                branchesLoaded = true;
                branchesRetryCount = 0; // Reset counter on success
                console.log('Branches loaded successfully:', data.branches.length);
            } else {
                console.error('Invalid branches data:', data);
            }
        })
        .catch(error => {
            console.error('Error loading branches:', error);
            branchSelect.innerHTML = '<option value="">Lỗi tải chi nhánh</option>';
            branchesLoaded = false; // Allow retry
            branchesRetryCount++;
            
            // Auto retry after 2 seconds (max 3 times)
            if (branchesRetryCount < MAX_RETRY_COUNT) {
                setTimeout(() => {
                    console.log('Retrying to load branches... (attempt ' + (branchesRetryCount + 1) + '/' + MAX_RETRY_COUNT + ')');
                    isLoadingBranches = false;
                    loadBranches();
                }, 2000);
            } else {
                console.error('Max retry attempts reached for branches');
                branchSelect.innerHTML = '<option value="">Không thể tải chi nhánh. Vui lòng tải lại trang.</option>';
            }
        })
        .finally(() => {
            branchSelect.disabled = false;
            if (branchesLoaded) {
                isLoadingBranches = false;
            }
        });
}

function loadAddresses() {
    const addressSelect = document.getElementById('address-select');
    if (!addressSelect) {
        console.warn('Address select element not found');
        return;
    }
    
    // Check if already loaded OR currently loading
    if (addressesLoaded || isLoadingAddresses) {
        console.log('Addresses already loaded or loading, skipping...');
        return;
    }
    
    // Check DOM - if already has options with value, mark as loaded
    if (addressSelect.options.length > 1 || 
        (addressSelect.options.length === 1 && addressSelect.options[0].value !== '')) {
        console.log('Addresses already in DOM, skipping...');
        addressesLoaded = true;
        return;
    }
    
    isLoadingAddresses = true;
    addressSelect.disabled = true;
    addressSelect.innerHTML = '<option value="">Đang tải địa chỉ...</option>';
    console.log('Loading addresses...');
    
    fetch('${pageContext.request.contextPath}/cart/addresses')
        .then(response => {
            if (!response.ok) {
                throw new Error('HTTP error ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            if (data.success && data.addresses && data.addresses.length > 0) {
                // Clear existing options first
                addressSelect.innerHTML = '<option value="">-- Chọn địa chỉ giao hàng --</option>';
                
                data.addresses.forEach(address => {
                    const option = document.createElement('option');
                    option.value = address.id;
                    
                    // Chỉ hiển thị địa chỉ
                    option.textContent = address.fullAddress;
                    
                    // Mark default address
                    if (address.isDefault) {
                        option.selected = true;
                    }
                    
                    addressSelect.appendChild(option);
                });
                
                addressesLoaded = true;
                addressesRetryCount = 0; // Reset counter on success
                console.log('Addresses loaded successfully:', data.addresses.length);
            } else if (data.success && (!data.addresses || data.addresses.length === 0)) {
                addressSelect.innerHTML = '<option value="">Chưa có địa chỉ nào. <a href="${pageContext.request.contextPath}/profile#addresses" style="color: #007bff;">Thêm địa chỉ</a></option>';
                addressesLoaded = true;
                addressesRetryCount = 0; // Reset counter
                console.log('No addresses found');
            } else {
                console.error('Invalid addresses data:', data);
            }
        })
        .catch(error => {
            console.error('Error loading addresses:', error);
            addressSelect.innerHTML = '<option value="">Lỗi tải địa chỉ</option>';
            addressesLoaded = false; // Allow retry
            addressesRetryCount++;
            
            // Auto retry after 2 seconds (max 3 times)
            if (addressesRetryCount < MAX_RETRY_COUNT) {
                setTimeout(() => {
                    console.log('Retrying to load addresses... (attempt ' + (addressesRetryCount + 1) + '/' + MAX_RETRY_COUNT + ')');
                    isLoadingAddresses = false;
                    loadAddresses();
                }, 2000);
            } else {
                console.error('Max retry attempts reached for addresses');
                addressSelect.innerHTML = '<option value="">Không thể tải địa chỉ. Vui lòng tải lại trang.</option>';
            }
        })
        .finally(() => {
            addressSelect.disabled = false;
            if (addressesLoaded) {
                isLoadingAddresses = false;
            }
        });
}

function updateStockDisplay(branchId) {
    console.log('Updating stock display for branch:', branchId);
    
    // Collect all variant IDs
    const variantIds = [];
    document.querySelectorAll('.stock-cell').forEach(cell => {
        const variantId = cell.getAttribute('data-variant-id');
        if (variantId && variantId !== '0') {
            variantIds.push(variantId);
        }
    });
    
    if (variantIds.length === 0) {
        console.warn('No variants found to update stock');
        return;
    }
    
    // Fetch stock for all variants
    fetch('${pageContext.request.contextPath}/cart/stock?branchId=' + branchId + '&variantIds=' + variantIds.join(','))
        .then(response => response.json())
        .then(data => {
            if (data.success && data.stocks) {
                // Update each cell
                document.querySelectorAll('.stock-cell').forEach(cell => {
                    const variantId = parseInt(cell.getAttribute('data-variant-id'));
                    const stockDisplay = cell.querySelector('.stock-display');
                    const stockWarning = cell.querySelector('.stock-warning');
                    
                    if (variantId && variantId !== 0) {
                        const stock = data.stocks[variantId] || 0;
                        
                        // Get row and current quantity
                        const row = cell.closest('tr');
                        const itemId = row.getAttribute('data-item-id');
                        const qtyInput = document.getElementById('qty-' + itemId);
                        const currentQty = qtyInput ? parseInt(qtyInput.value) : 0;
                        const increaseBtn = row.querySelector('.cart-btn-increase');
                        
                        // Check if current quantity exceeds new stock
                        if (currentQty > stock && stock > 0) {
                            // Auto-adjust quantity to match stock
                            swal({
                                title: "Điều chỉnh số lượng",
                                text: "Một số sản phẩm trong giỏ hàng đã bị giảm do không đủ số lượng tại chi nhánh này",
                                icon: "info"
                            });
                            
                            // Update input value
                            qtyInput.value = stock;
                            
                            // Update backend
                            updateCartItem(itemId, stock);
                        } else if (currentQty > 0 && stock === 0) {
                            // Stock is 0 but user has items in cart
                            swal({
                                title: "Hết hàng",
                                text: "Một số sản phẩm tạm hết hàng tại chi nhánh này",
                                icon: "warning"
                            });
                        }
                        
                        if (stock === 0) {
                            stockDisplay.textContent = '0';
                            stockDisplay.style.color = '#e74c3c';
                            stockWarning.style.display = 'block';
                            
                            // Mark row as out-of-stock (gray out + exclude from total)
                            row.classList.add('out-of-stock');
                            
                            // Add "HẾT HÀNG" badge to product name
                            const productNameCell = row.querySelector('.column-2');
                            if (productNameCell && !productNameCell.querySelector('.out-of-stock-badge')) {
                                const badge = document.createElement('span');
                                badge.className = 'out-of-stock-badge';
                                badge.textContent = 'HẾT HÀNG';
                                productNameCell.appendChild(badge);
                            }
                            
                            // Disable increase button if stock = 0
                            if (increaseBtn) {
                                increaseBtn.style.opacity = '0.3';
                                increaseBtn.style.cursor = 'not-allowed';
                                increaseBtn.style.pointerEvents = 'none';
                            }
                            
                            // Note: updateCartTotals() will be called once after all updates
                        } else {
                            stockDisplay.textContent = stock;
                            stockDisplay.style.color = stock <= 5 ? '#ffa534' : '#31ce36'; // Yellow if low stock
                            stockWarning.style.display = 'none';
                            
                            // Remove out-of-stock class if exists
                            row.classList.remove('out-of-stock');
                            
                            // Remove "HẾT HÀNG" badge if exists
                            const productNameCell = row.querySelector('.column-2');
                            const badge = productNameCell ? productNameCell.querySelector('.out-of-stock-badge') : null;
                            if (badge) {
                                badge.remove();
                            }
                            
                            // Note: updateCartTotals() will be called once after all updates
                            
                            // Disable increase button if quantity >= stock
                            if (increaseBtn) {
                                const updatedQty = parseInt(qtyInput.value); // Re-check after auto-adjustment
                                if (updatedQty >= stock) {
                                    increaseBtn.style.opacity = '0.3';
                                    increaseBtn.style.cursor = 'not-allowed';
                                    increaseBtn.style.pointerEvents = 'none';
                                } else {
                                    increaseBtn.style.opacity = '1';
                                    increaseBtn.style.cursor = 'pointer';
                                    increaseBtn.style.pointerEvents = 'auto';
                                }
                            }
                        }
                    } else {
                        stockDisplay.textContent = 'N/A';
                        stockDisplay.style.color = '#888';
                        stockWarning.style.display = 'none';
                    }
                });
                
                // CRITICAL: Update cart totals ONCE after all stock updates
                // This prevents race conditions from multiple simultaneous calls
                console.log('All stock updates complete, updating cart totals...');
                
                // Small delay to ensure all DOM updates are complete
                setTimeout(() => {
                    updateCartTotals();
                }, 50);
            }
        })
        .catch(error => {
            console.error('Error fetching stock:', error);
            // Still update totals even on error to ensure consistency
            setTimeout(() => {
                updateCartTotals();
            }, 50);
        });
}

// DEBUG FUNCTION - Call this in console to test voucher calculation
function debugVoucherCalculation() {
    console.log('=== VOUCHER DEBUG INFO ===');
    
    // 1. Check voucher variables
    console.log('Voucher variables:', {
        code: appliedVoucherCode,
        type: appliedVoucherType,
        value: appliedVoucherValue
    });
    
    // 2. Get current subtotal from DOM
    const subtotalEl = document.getElementById('cart-subtotal');
    const subtotalText = subtotalEl ? subtotalEl.textContent : 'NOT FOUND';
    const subtotalValue = subtotalText ? parseFloat(subtotalText.replace(/[^\d]/g, '')) : 0;
    console.log('Subtotal:', {
        text: subtotalText,
        value: subtotalValue
    });
    
    // 3. Get current discount from DOM
    const discountEl = document.getElementById('discount-amount');
    const discountText = discountEl ? discountEl.textContent : 'NOT FOUND';
    console.log('Discount display:', discountText);
    
    // 4. Get current total from DOM
    const totalEl = document.getElementById('cart-total');
    const totalText = totalEl ? totalEl.textContent : 'NOT FOUND';
    console.log('Total display:', totalText);
    
    // 5. Manual calculation
    if (appliedVoucherCode && subtotalValue > 0) {
        let expectedDiscount = 0;
        if (appliedVoucherType === 1) {
            expectedDiscount = subtotalValue * (appliedVoucherValue / 100);
        } else if (appliedVoucherType === 2) {
            expectedDiscount = Math.min(appliedVoucherValue, subtotalValue);
        }
        expectedDiscount = Math.round(expectedDiscount);
        const expectedTotal = subtotalValue - expectedDiscount;
        
        console.log('Expected calculation:', {
            subtotal: subtotalValue,
            discount: expectedDiscount,
            total: expectedTotal
        });
        
        // 6. Try to recalculate
        console.log('Attempting to recalculate...');
        recalculateVoucher(subtotalValue);
    } else {
        console.warn('Cannot calculate - missing voucher or subtotal is 0');
    }
    
    console.log('=== END DEBUG ===');
}

// Make function available globally
window.debugVoucherCalculation = debugVoucherCalculation;

// Quick test function
function testVoucherNow() {
    console.log('🧪 === QUICK VOUCHER TEST ===');
    
    // 1. Get subtotal
    const subtotalEl = document.getElementById('cart-subtotal');
    if (!subtotalEl) {
        console.error('❌ Subtotal element not found!');
        return;
    }
    
    const subtotalText = subtotalEl.textContent;
    console.log('1. Subtotal text from DOM:', subtotalText);
    
    const subtotalValue = parseFloat(subtotalText.replace(/[^\d]/g, ''));
    console.log('2. Parsed subtotal value:', subtotalValue);
    
    // 2. Check voucher variables
    console.log('3. Voucher variables:', {
        code: appliedVoucherCode,
        type: appliedVoucherType,
        typeType: typeof appliedVoucherType,
        typeValid: !isNaN(appliedVoucherType),
        value: appliedVoucherValue,
        valueType: typeof appliedVoucherValue,
        valueValid: !isNaN(appliedVoucherValue)
    });
    
    // 3. Check sessionStorage (raw)
    const rawCode = sessionStorage.getItem('voucherCode');
    const rawType = sessionStorage.getItem('voucherType');
    const rawValue = sessionStorage.getItem('voucherValue');
    
    console.log('4. sessionStorage (raw):', {
        code: rawCode,
        type: rawType,
        typeType: typeof rawType,
        value: rawValue,
        valueType: typeof rawValue
    });
    
    console.log('5. sessionStorage (parsed):', {
        type: parseInt(rawType),
        typeValid: !isNaN(parseInt(rawType)),
        value: parseFloat(rawValue),
        valueValid: !isNaN(parseFloat(rawValue))
    });
    
    // 4. Try to recalculate
    if (subtotalValue > 0) {
        if (appliedVoucherCode && !isNaN(appliedVoucherType) && !isNaN(appliedVoucherValue)) {
            console.log('6. ✅ All valid, calling recalculateVoucher...');
            recalculateVoucher(subtotalValue);
        } else {
            console.error('6. ❌ Voucher data invalid, cannot recalculate');
        }
    } else {
        console.error('6. ❌ Cannot test - subtotal is 0 or invalid');
    }
    
    console.log('🧪 === END TEST ===');
}

window.testVoucherNow = testVoucherNow;
</script>
