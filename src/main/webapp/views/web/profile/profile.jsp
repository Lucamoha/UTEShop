<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/templates/css/profile.css">

<!-- Page title / breadcrumb -->
<div class="container p-t-60 p-b-30">
	<div class="row">
		<div class="col-12">
			<h1></h1>
		</div>
	</div>
</div>

<div class="profile-container bg0 p-t-60 p-b-60">
        <div class="profile-layout">
            <!-- Sidebar -->
            <aside class="profile-sidebar">
                <div class="profile-user-info">
                    <div class="profile-avatar">
                        ${user.fullName.substring(0, 1).toUpperCase()}
                    </div>
                    <div class="profile-user-name">${user.fullName}</div>
                </div>
                
                <ul class="profile-menu">
                    <li class="profile-menu-item">
                        <a href="${pageContext.request.contextPath}/profile" class="profile-menu-link active">
                            <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"/>
                            </svg>
                            <span>Thông Tin Cá Nhân</span>
                        </a>
                    </li>
                    <li class="profile-menu-item">
                        <a href="${pageContext.request.contextPath}/profile/addresses" class="profile-menu-link">
                            <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M10.707 2.293a1 1 0 00-1.414 0l-7 7a1 1 0 001.414 1.414L4 10.414V17a1 1 0 001 1h2a1 1 0 001-1v-2a1 1 0 011-1h2a1 1 0 011 1v2a1 1 0 001 1h2a1 1 0 001-1v-6.586l.293.293a1 1 0 001.414-1.414l-7-7z"/>
                            </svg>
                            <span>Địa Chỉ Của Tôi</span>
                        </a>
                    </li>
                    <li class="profile-menu-item">
                        <a href="${pageContext.request.contextPath}/profile/change-password" class="profile-menu-link">
                            <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M18 8a6 6 0 01-7.743 5.743L10 14l-1 1-1 1H6v2H2v-4l4.257-4.257A6 6 0 1118 8zm-6-4a1 1 0 100 2 2 2 0 012 2 1 1 0 102 0 4 4 0 00-4-4z" clip-rule="evenodd"/>
                            </svg>
                            <span>Đổi Mật Khẩu</span>
                        </a>
                    </li>
                    <li class="profile-menu-item">
                        <a href="${pageContext.request.contextPath}/orders" class="profile-menu-link">
                            <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3zM16 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM6.5 18a1.5 1.5 0 100-3 1.5 1.5 0 000 3z"/>
                            </svg>
                            <span>Đơn Hàng Của Tôi</span>
                        </a>
                    </li>
                    <li class="profile-menu-item">
                        <a href="${pageContext.request.contextPath}/logout" class="profile-menu-link logout-link">
                            <svg class="profile-menu-icon" fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd" d="M3 3a1 1 0 00-1 1v12a1 1 0 001 1h12a1 1 0 001-1V4a1 1 0 00-1-1H3zm11 4.414l-4.293 4.293a1 1 0 01-1.414 0L4 7.414 5.414 6l3.293 3.293L13.586 6 15 7.414z" clip-rule="evenodd"/>
                            </svg>
                            <span>Đăng Xuất</span>
                        </a>
                    </li>
                </ul>
            </aside>
            
            <!-- Main Content -->
            <main class="profile-content">
                <div class="profile-header">
                    <h1 class="profile-title">Thông Tin Cá Nhân</h1>
                </div>
                
                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>
                
                <form method="post" action="${pageContext.request.contextPath}/profile" class="profile-form" id="profileForm" novalidate>
                    <div class="form-group">
                        <label class="form-label form-label-required">Email</label>
                        <input type="email" name="email" id="emailInput" class="form-input" 
                               value="${user.email}" required
                               placeholder="Nhập email của bạn">
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label form-label-required">Họ và Tên</label>
                        <input type="text" name="fullName" id="fullNameInput" class="form-input" 
                               value="${user.fullName}" required 
                               placeholder="Nhập họ và tên của bạn">
                    </div>
                    
                    <div class="form-group">
                        <label class="form-label">Số Điện Thoại</label>
                        <input type="tel" name="phone" id="phoneInput" class="form-input" 
                               value="${user.phone}" 
                               placeholder="Nhập số điện thoại">
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" id="saveBtn" class="btn btn-primary">Lưu Thay Đổi</button>
                        <button type="button" id="cancelBtn" class="btn btn-secondary" style="display: none;">Hủy</button>
                    </div>
                </form>
            </main>
        </div>
    </div>

<script>
    // Track original values
    const originalEmail = document.getElementById('emailInput').value;
    const originalFullName = document.getElementById('fullNameInput').value;
    const originalPhone = document.getElementById('phoneInput').value;
    
    const saveBtn = document.getElementById('saveBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const form = document.getElementById('profileForm');
    
    // Check if form has changes
    function checkFormChanges() {
        const currentEmail = document.getElementById('emailInput').value;
        const currentFullName = document.getElementById('fullNameInput').value;
        const currentPhone = document.getElementById('phoneInput').value;
        
        const hasChanges = currentEmail !== originalEmail || 
                          currentFullName !== originalFullName || 
                          currentPhone !== originalPhone;
        
        // Không disable nút save nữa, chỉ show/hide nút cancel
        cancelBtn.style.display = hasChanges ? 'inline-block' : 'none';
    }
    
    // Listen for input changes
    document.getElementById('emailInput').addEventListener('input', checkFormChanges);
    document.getElementById('fullNameInput').addEventListener('input', checkFormChanges);
    document.getElementById('phoneInput').addEventListener('input', checkFormChanges);
    
    // Cancel button - reset form
    cancelBtn.addEventListener('click', function() {
        document.getElementById('emailInput').value = originalEmail;
        document.getElementById('fullNameInput').value = originalFullName;
        document.getElementById('phoneInput').value = originalPhone;
        checkFormChanges();
    });
    
    // Validate form before submit
    form.addEventListener('submit', function(e) {
        const emailInput = document.getElementById('emailInput');
        const phoneInput = document.getElementById('phoneInput');
        const fullNameInput = document.getElementById('fullNameInput');
        
        // Reset all borders
        emailInput.style.borderColor = '';
        phoneInput.style.borderColor = '';
        fullNameInput.style.borderColor = '';
        
        let hasError = false;
        
        // Validate email format
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(emailInput.value.trim())) {
            emailInput.style.borderColor = '#dc3545';
            hasError = true;
            swal({
                title: "Email không hợp lệ",
                text: "Vui lòng nhập đúng định dạng email (ví dụ: name@example.com)",
                icon: "error",
                button: "OK"
            });
        }
        
        // Validate phone format (10-11 digits)
        if (phoneInput.value.trim()) {
            const phonePattern = /^[0-9]{10,11}$/;
            if (!phonePattern.test(phoneInput.value.trim())) {
                phoneInput.style.borderColor = '#dc3545';
                hasError = true;
                if (!hasError || emailInput.style.borderColor !== 'rgb(220, 53, 69)') {
                    swal({
                        title: "Số điện thoại không hợp lệ",
                        text: "Số điện thoại phải có 10-11 chữ số",
                        icon: "error",
                        button: "OK"
                    });
                }
            }
        }
        
        // Validate full name
        if (!fullNameInput.value.trim()) {
            fullNameInput.style.borderColor = '#dc3545';
            hasError = true;
        }
        
        if (hasError) {
            e.preventDefault();
        }
    });
    
    // Logout confirmation using SweetAlert
    document.querySelector('.logout-link').addEventListener('click', function(e) {
        e.preventDefault();
        const logoutUrl = this.href;
        
        swal({
            title: "Xác nhận đăng xuất",
            text: "Bạn có chắc muốn đăng xuất?",
            icon: "warning",
            buttons: {
                cancel: {
                    text: "Hủy",
                    visible: true,
                    className: "swal-button--cancel"
                },
                confirm: {
                    text: "Đăng xuất",
                    className: "swal-button--danger"
                }
            },
            dangerMode: true
        }).then((confirmed) => {
            if (confirmed) {
                window.location.href = logoutUrl;
            }
        });
    });
</script>
