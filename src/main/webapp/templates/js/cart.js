/**
 * Shopping Cart Functions
 */

// Add to cart from product detail page
function addToCart(productId) {
    // Lấy các options đã chọn
    const selectedOptions = [];
    const optionSelects = document.querySelectorAll('[data-option-type]');
    
    optionSelects.forEach(select => {
        const value = select.value;
        if (value) {
            selectedOptions.push(parseInt(value));
        }
    });
    
    // Kiểm tra đã chọn đủ options chưa
    if (selectedOptions.length < optionSelects.length) {
        alert('Vui lòng chọn đầy đủ thông tin sản phẩm (màu sắc, dung lượng, ...)');
        return;
    }
    
    // Lấy số lượng
    const quantityInput = document.querySelector('.num-product');
    const quantity = quantityInput ? parseInt(quantityInput.value) : 1;
    
    if (quantity < 1) {
        alert('Số lượng phải lớn hơn 0');
        return;
    }
    
    // Gửi request
    const formData = new URLSearchParams();
    formData.append('productId', productId);
    formData.append('quantity', quantity);
    formData.append('options', JSON.stringify(selectedOptions));
    
    fetch(contextPath + '/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData.toString()
    })
    .then(response => response.json())
    .then(data => {
        console.log('Add to cart response:', data);
        if (data.success) {
            // Update cart count
            if (typeof updateCartCount === 'function') {
                updateCartCount();
            }
            
            // Show success message
            swal({
                title: "Thành công!",
                text: data.message,
                icon: "success",
                button: "OK",
            });
        } else {
            if (data.needLogin) {
                // Redirect to login
                swal({
                    title: "Cần đăng nhập",
                    text: data.message,
                    icon: "warning",
                    buttons: {
                        cancel: "Hủy",
                        login: {
                            text: "Đăng nhập",
                            value: "login",
                        },
                    },
                }).then((value) => {
                    if (value === "login") {
                        window.location.href = contextPath + '/login?redirect=' + encodeURIComponent(window.location.pathname);
                    }
                });
            } else {
                swal({
                    title: "Lỗi",
                    text: data.message,
                    icon: "error",
                    button: "OK",
                });
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        swal({
            title: "Lỗi",
            text: "Có lỗi xảy ra. Vui lòng thử lại.",
            icon: "error",
            button: "OK",
        });
    });
}

// Quick add to cart (for product listing pages)
function quickAddToCart(productId, variantId) {
    const formData = new URLSearchParams();
    formData.append('productId', productId);
    formData.append('quantity', 1);
    
    if (variantId) {
        // If variantId is provided directly
        formData.append('options', JSON.stringify([variantId]));
    }
    
    fetch(contextPath + '/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData.toString()
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            if (typeof updateCartCount === 'function') {
                updateCartCount();
            }
            
            swal({
                title: "Đã thêm vào giỏ!",
                text: "Sản phẩm đã được thêm vào giỏ hàng",
                icon: "success",
                timer: 2000,
                buttons: false,
            });
        } else {
            if (data.needLogin) {
                window.location.href = contextPath + '/login';
            } else {
                alert(data.message);
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Có lỗi xảy ra');
    });
}
