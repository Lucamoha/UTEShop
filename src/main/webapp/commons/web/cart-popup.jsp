<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>

<!-- Cart Popup -->
<div class="wrap-header-cart js-panel-cart">
    <div class="s-full js-hide-cart"></div>

    <div class="header-cart flex-col-l p-l-65 p-r-25">
        <div class="header-cart-title flex-w flex-sb-m p-b-8">
            <span class="mtext-103 cl2">
                Giỏ hàng
            </span>

            <div class="fs-35 lh-10 cl2 p-lr-5 pointer hov-cl1 trans-04 js-hide-cart" style="position: relative;">
                <span style="font-size: 35px; line-height: 1; font-family: 'Segoe UI', Arial, sans-serif;">×</span>
            </div>
        </div>
        
        <div class="header-cart-content flex-w js-pscroll">
            <div id="cart-popup-items" class="header-cart-wrapitem w-full">
                <!-- Cart items will be loaded here via AJAX -->
                <div class="text-center p-t-50 p-b-50">
                    <div class="spinner-border" role="status">
                        <span class="sr-only">Loading...</span>
                    </div>
                </div>
            </div>
            
            <div class="w-full" id="cart-popup-footer" style="display: none;">
                <div class="header-cart-total w-full p-tb-40">
                    <span id="cart-popup-total">Total: 0đ</span>
                </div>

                <div class="header-cart-buttons flex-w w-full">
                    <a href="${pageContext.request.contextPath}/cart" 
                       class="flex-c-m stext-101 cl0 size-107 bg3 bor2 hov-btn3 p-lr-15 trans-04 m-r-8 m-b-10"
                       style="width: 100%;">
                        Xem giỏ hàng
                    </a>

                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Load cart popup khi click vào icon
document.addEventListener('DOMContentLoaded', function() {
    // Update cart count on page load
    updateCartCount();
    
    // Load cart popup when clicking cart icon
    const cartIcons = document.querySelectorAll('.js-show-cart');
    cartIcons.forEach(icon => {
        icon.addEventListener('click', function() {
            loadCartPopup();
        });
    });
});

function updateCartCount() {
    fetch('${pageContext.request.contextPath}/cart/count')
        .then(response => response.json())
        .then(data => {
            if (data.needLogin) {
                // User not logged in
                document.querySelectorAll('.js-show-cart').forEach(el => {
                    el.setAttribute('data-notify', '0');
                });
            } else {
                // Update cart count
                document.querySelectorAll('.js-show-cart').forEach(el => {
                    el.setAttribute('data-notify', data.count);
                });
            }
        })
        .catch(error => console.error('Error:', error));
}

function loadCartPopup() {
    const itemsContainer = document.getElementById('cart-popup-items');
    const footer = document.getElementById('cart-popup-footer');
    
    // Show loading
    itemsContainer.innerHTML = `
        <div class="text-center p-t-50 p-b-50">
            <div class="spinner-border" role="status">
                <span class="sr-only">Loading...</span>
            </div>
        </div>
    `;
    
    // Gọi trực tiếp /cart/items thay vì /cart/count
    fetch('${pageContext.request.contextPath}/cart/items')
        .then(response => response.json())
        .then(data => {
            console.log('Load cart popup response:', data);
            
            // Check if need login
            if (data.needLogin || (!data.success && data.needLogin === undefined)) {
                // User not logged in - check bằng cách fetch /cart/count
                fetch('${pageContext.request.contextPath}/cart/count')
                    .then(r => r.json())
                    .then(countData => {
                        if (countData.needLogin) {
                            itemsContainer.innerHTML = `
                                <div class="text-center p-t-50 p-b-50 p-l-20 p-r-20">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" fill="currentColor" viewBox="0 0 16 16">
                                        <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"/>
                                    </svg>
                                    <p class="mtext-109 cl2 p-t-20 p-b-10">Bạn chưa đăng nhập</p>
                                    <p class="stext-113 cl6 p-b-30">Vui lòng đăng nhập để sử dụng giỏ hàng</p>
                                    <a href="${pageContext.request.contextPath}/login?redirect=${pageContext.request.contextPath}/cart" 
                                       class="flex-c-m stext-101 cl0 bg1 bor1 hov-btn1 p-lr-30 p-tb-10 trans-04 pointer" 
                                       style="display: inline-block; border-radius: 25px; text-decoration: none;">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" style="margin-right: 8px;" viewBox="0 0 16 16">
                                            <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"/>
                                        </svg>
                                        Đăng nhập ngay
                                    </a>
                                </div>
                            `;
                            footer.style.display = 'none';
                        }
                    });
                return;
            }
            
            // Check if empty cart
            if (!data.items || data.items.length === 0) {
                itemsContainer.innerHTML = `
                    <div class="text-center p-t-50 p-b-50">
                        <i class="zmdi zmdi-shopping-cart" style="font-size: 60px; color: #ccc;"></i>
                        <p class="stext-113 cl6 p-t-20">Giỏ hàng của bạn đang trống</p>
                    </div>
                `;
                footer.style.display = 'none';
                return;
            }
            
            // Has items - render them
            renderCartItems(data);
        })
        .catch(error => {
            console.error('Error loading cart popup:', error);
            itemsContainer.innerHTML = `
                <div class="text-center p-t-50 p-b-50">
                    <p class="stext-113 cl6">Có lỗi xảy ra</p>
                </div>
            `;
            footer.style.display = 'none';
        });
}

function renderCartItems(data) {
    const itemsContainer = document.getElementById('cart-popup-items');
    const footer = document.getElementById('cart-popup-footer');
    
    console.log('Rendering cart items:', data);
    
    // CRITICAL: Show warning if items were removed
    let warningHtml = '';
    if (data.removedItems && data.removedItems.length > 0) {
        console.log('Items were removed due to status=false:', data.removedItems);
        warningHtml = '<div class="alert alert-warning" style="margin: 10px; padding: 10px; background-color: #fff3cd; border-left: 3px solid #ff9800; border-radius: 3px;">' +
            '<p style="margin: 0; color: #856404; font-size: 13px;"><strong>Thông báo:</strong> Một số sản phẩm đã ngừng kinh doanh và đã được xóa khỏi giỏ hàng.</p>' +
            '</div>';
    }
    
    // Build items HTML
    let itemsHtml = warningHtml + '<ul class="header-cart-wrapitem w-full">';
    
    data.items.forEach(item => {
        const contextPath = '${pageContext.request.contextPath}';
        const imageUrl = item.productImage ? 
            contextPath + '/image?fname=' + item.productImage : 
            contextPath + '/templates/images/no-image.png';
        
        const price = new Intl.NumberFormat('vi-VN').format(item.price);
        const itemTotal = new Intl.NumberFormat('vi-VN').format(item.price * item.quantity);
        const variantInfo = item.variantSKU ? '<span class="stext-108 cl6" style="font-size: 11px;">SKU: ' + item.variantSKU + '</span><br>' : '';
        const productDetailUrl = contextPath + '/product-detail?product=' + item.productSlug;
        
        itemsHtml += 
            '<li class="header-cart-item flex-w flex-t m-b-12">' +
                '<div class="header-cart-item-img">' +
                    '<a href="' + productDetailUrl + '">' +
                        '<img src="' + imageUrl + '" alt="' + item.productName + '" style="width: 70px; height: 70px; object-fit: cover;">' +
                    '</a>' +
                '</div>' +
                '<div class="header-cart-item-txt p-t-8 p-l-15">' +
                    '<a href="' + productDetailUrl + '" class="header-cart-item-name m-b-5 hov-cl1 trans-04" style="display: block; max-width: 200px; text-decoration: none;">' +
                        item.productName +
                    '</a>' +
                    variantInfo +
                    '<span class="header-cart-item-info">' +
                        item.quantity + ' x ' + price + 'đ = <strong>' + itemTotal + 'đ</strong>' +
                    '</span>' +
                '</div>' +
            '</li>';
    });
    
    itemsHtml += '</ul>';
    
    itemsContainer.innerHTML = itemsHtml;
    
    // Calculate total from items
    let totalAmount = 0;
    data.items.forEach(item => {
        totalAmount += item.price * item.quantity;
    });
    
    // Update footer with VND format
    const totalFormatted = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(totalAmount);
    
    document.getElementById('cart-popup-total').innerHTML = 'Tổng cộng: <strong>' + totalFormatted + '</strong>';
    footer.style.display = 'block';
    
    // CRITICAL: Update cart count on header icon after rendering
    // This ensures the count is updated when items are auto-removed
    console.log('Updating cart count after popup render...');
    updateCartCount();
}

// Export function for use in other scripts
window.updateCartCount = updateCartCount;
</script>
