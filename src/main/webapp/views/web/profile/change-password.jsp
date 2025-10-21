<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/templates/css/profile.css">
<style>
    .password-input-wrapper {
        position: relative;
    }
    .password-input-wrapper .form-input {
        padding-right: 45px;
    }
    .password-toggle {
        position: absolute;
        right: 12px;
        top: 50%;
        transform: translateY(-50%);
        cursor: pointer;
        color: #999;
        user-select: none;
        font-size: 16px;
        width: 20px;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .password-toggle:hover {
        color: #717fe0;
    }
    .password-requirements.error {
        border-left-color: #dc3545;
    }
    .password-requirements.error h4 {
        color: #dc3545;
    }
    .password-requirements.error .req-minlength {
        color: #dc3545;
    }
    .form-help-text.error {
        color: #dc3545 !important;
        font-weight: 500;
    }
    .form-help-text.success {
        color: #28a745 !important;
        font-weight: 500;
    }
</style>

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
                    ${sessionScope.user.fullName.substring(0, 1).toUpperCase()}
                </div>
                <div class="profile-user-name">${sessionScope.user.fullName}</div>
            </div>
            
            <ul class="profile-menu">
                <li class="profile-menu-item">
                    <a href="${pageContext.request.contextPath}/profile" class="profile-menu-link">
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
                    <a href="${pageContext.request.contextPath}/profile/change-password" class="profile-menu-link active">
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
                <div>
                    <h1 class="profile-title">Đổi Mật Khẩu</h1>
                </div>
            </div>
            
            <c:if test="${param.success == 'true'}">
                <div class="alert alert-success">Đổi mật khẩu thành công!</div>
            </c:if>
            
            <c:if test="${param.error == 'wrong_password'}">
                <div class="alert alert-error">Mật khẩu cũ không đúng!</div>
            </c:if>
            
            <c:if test="${not empty error && param.error != 'wrong_password'}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <!-- Thông báo cho user đăng nhập bằng Google/Facebook -->
            <c:if test="${isSocialLogin}">
                <div class="alert" style="background-color: #e3f2fd; border-left: 4px solid #2196F3; padding: 15px; margin-bottom: 20px; border-radius: 4px;">
                    <h4 style="margin: 0 0 10px 0; color: #1976D2; font-size: 16px;">
                        <i class="fa fa-info-circle" style="margin-right: 8px;"></i>
                        Tài khoản đăng nhập qua Google/Facebook
                    </h4>
                    <p style="margin: 0 0 10px 0; color: #555; font-size: 14px; line-height: 1.6;">
                        Bạn hiện đang đăng nhập bằng tài khoản Google hoặc Facebook. Nếu bạn muốn thiết lập mật khẩu riêng cho tài khoản này, 
                        vui lòng sử dụng tính năng <strong>Quên mật khẩu</strong> để tạo mật khẩu mới.
                    </p>
                    <button type="button" onclick="redirectToForgotPassword()" 
                            class="btn btn-primary" 
                            style="display: inline-flex; align-items: center; gap: 8px; background-color: #2196F3; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; font-size: 14px; font-weight: 500; transition: background-color 0.3s;">
                        <i class="fa fa-key"></i>
                        Thiết lập mật khẩu mới
                    </button>
                </div>
            </c:if>
            
            <div class="password-requirements" id="passwordRequirements">
                <h4>Yêu cầu mật khẩu:</h4>
                <ul>
                    <li class="req-minlength">Tối thiểu 6 ký tự</li>
                    <li>Nên sử dụng kết hợp chữ hoa, chữ thường, số và ký tự đặc biệt</li>
                    <li>Không nên sử dụng mật khẩu quá đơn giản</li>
                </ul>
            </div>
            
            <form method="post" action="${pageContext.request.contextPath}/profile/change-password" class="profile-form" id="changePasswordForm" novalidate>
                <div class="form-group">
                    <label class="form-label form-label-required">Mật Khẩu Hiện Tại</label>
                    <div class="password-input-wrapper">
                        <input type="password" name="oldPassword" id="oldPassword" class="form-input" 
                               required placeholder="Nhập mật khẩu hiện tại">
                        <span class="password-toggle" onclick="togglePassword('oldPassword')">
                            <i class="fa fa-eye" id="oldPassword-icon"></i>
                        </span>
                    </div>
                </div>
                
                <div class="form-group">
                    <label class="form-label form-label-required">Mật Khẩu Mới</label>
                    <div class="password-input-wrapper">
                        <input type="password" name="newPassword" id="newPassword" class="form-input" 
                               required minlength="6" placeholder="Nhập mật khẩu mới">
                        <span class="password-toggle" onclick="togglePassword('newPassword')">
                            <i class="fa fa-eye" id="newPassword-icon"></i>
                        </span>
                    </div>
                    <p class="form-help-text" id="passwordLengthMsg">Mật khẩu phải có ít nhất 6 ký tự</p>
                </div>
                
                <div class="form-group">
                    <label class="form-label form-label-required">Xác Nhận Mật Khẩu Mới</label>
                    <div class="password-input-wrapper">
                        <input type="password" name="confirmPassword" id="confirmPassword" class="form-input" 
                               required minlength="6" placeholder="Nhập lại mật khẩu mới">
                        <span class="password-toggle" onclick="togglePassword('confirmPassword')">
                            <i class="fa fa-eye" id="confirmPassword-icon"></i>
                        </span>
                    </div>
                    <p class="form-help-text" id="passwordMatchMsg"></p>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Đổi Mật Khẩu</button>
                    <button type="reset" class="btn btn-secondary" id="resetBtn">Hủy</button>
                </div>
            </form>
        </main>
    </div>
</div>

<script>
    // Set success flag from URL parameter
    var changePasswordSuccess = '<c:out value="${param.success}"/>' === 'true';
</script>

<script>
    // Toggle password visibility
    function togglePassword(inputId) {
        const input = document.getElementById(inputId);
        const icon = document.getElementById(inputId + '-icon');
        
        if (input.type === 'password') {
            input.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
        } else {
            input.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
        }
    }
    
    // Validate password
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const passwordMatchMsg = document.getElementById('passwordMatchMsg');
    const passwordLengthMsg = document.getElementById('passwordLengthMsg');
    const passwordRequirements = document.getElementById('passwordRequirements');
    const form = document.getElementById('changePasswordForm');
    
    // Check password length
    function checkPasswordLength() {
        if (newPassword.value.length === 0) {
            passwordRequirements.classList.remove('error');
            passwordLengthMsg.classList.remove('error');
            return;
        }
        
        if (newPassword.value.length < 6) {
            passwordRequirements.classList.add('error');
            passwordLengthMsg.classList.add('error');
        } else {
            passwordRequirements.classList.remove('error');
            passwordLengthMsg.classList.remove('error');
        }
    }
    
    // Check password match
    function checkPasswordMatch() {
        if (confirmPassword.value === '') {
            passwordMatchMsg.textContent = '';
            passwordMatchMsg.classList.remove('error');
            passwordMatchMsg.classList.remove('success');
            return;
        }
        
        if (newPassword.value === confirmPassword.value) {
            passwordMatchMsg.textContent = '✓ Mật khẩu khớp';
            passwordMatchMsg.classList.remove('error');
            passwordMatchMsg.classList.add('success');
        } else {
            passwordMatchMsg.textContent = '✗ Mật khẩu không khớp';
            passwordMatchMsg.classList.remove('success');
            passwordMatchMsg.classList.add('error');
        }
    }
    
    newPassword.addEventListener('input', function() {
        checkPasswordLength();
        checkPasswordMatch();
    });
    confirmPassword.addEventListener('input', checkPasswordMatch);
    
    // Validate on submit
    form.addEventListener('submit', function(e) {
        if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            passwordMatchMsg.textContent = '✗ Mật khẩu xác nhận không khớp!';
            passwordMatchMsg.classList.add('error');
            confirmPassword.focus();
        }
    });
    
    // Reset button - clear all fields and messages
    document.getElementById('resetBtn').addEventListener('click', function() {
        // Clear all messages
        passwordMatchMsg.textContent = '';
        passwordMatchMsg.classList.remove('error');
        passwordMatchMsg.classList.remove('success');
        passwordLengthMsg.classList.remove('error');
        passwordRequirements.classList.remove('error');
        
        // Clear all input values
        document.getElementById('oldPassword').value = '';
        document.getElementById('newPassword').value = '';
        document.getElementById('confirmPassword').value = '';
        
        // Reset all password fields to password type
        document.getElementById('oldPassword').type = 'password';
        document.getElementById('newPassword').type = 'password';
        document.getElementById('confirmPassword').type = 'password';
        
        // Reset all icons
        document.getElementById('oldPassword-icon').classList.remove('fa-eye-slash');
        document.getElementById('oldPassword-icon').classList.add('fa-eye');
        document.getElementById('newPassword-icon').classList.remove('fa-eye-slash');
        document.getElementById('newPassword-icon').classList.add('fa-eye');
        document.getElementById('confirmPassword-icon').classList.remove('fa-eye-slash');
        document.getElementById('confirmPassword-icon').classList.add('fa-eye');
    });
    
    // Reset form after successful password change
    if (changePasswordSuccess) {
        // Clear all input values
        document.getElementById('oldPassword').value = '';
        document.getElementById('newPassword').value = '';
        document.getElementById('confirmPassword').value = '';
        
        // Clear all messages
        passwordMatchMsg.textContent = '';
        passwordMatchMsg.classList.remove('error');
        passwordMatchMsg.classList.remove('success');
        passwordLengthMsg.classList.remove('error');
        passwordRequirements.classList.remove('error');
    }
    
    // Function to redirect to forgot password page with logout
    function redirectToForgotPassword() {
        swal({
            title: "Xác nhận thiết lập mật khẩu",
            text: "Tài khoản của bạn sẽ được đăng xuất để thiết lập lại mật khẩu. Bạn có muốn tiếp tục?",
            icon: "warning",
            buttons: {
                cancel: {
                    text: "Hủy",
                    visible: true,
                    className: "swal-button--cancel"
                },
                confirm: {
                    text: "Tiếp tục",
                    className: "swal-button--confirm"
                }
            }
        }).then((confirmed) => {
            if (confirmed) {
                // Chuyển đến trang quên mật khẩu, trang đó sẽ tự động đăng xuất
                window.location.href = '${pageContext.request.contextPath}/forgot-password';
            }
        });
    }
    
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
