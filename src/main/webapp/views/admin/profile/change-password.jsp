<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglib.jsp"%>

<div class="page-inner">
    <div class="page-header">
        <h3 class="fw-bold mb-3">Đổi Mật Khẩu</h3>
        <ul class="breadcrumbs mb-3">
            <li class="nav-home">
                <a href="${pageContext.request.contextPath}/admin/dashboard">
                    <i class="icon-home"></i>
                </a>
            </li>
            <li class="separator">
                <i class="icon-arrow-right"></i>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/admin/profile">Trang cá nhân</a>
            </li>
            <li class="separator">
                <i class="icon-arrow-right"></i>
            </li>
            <li class="nav-item">
                <a href="#">Đổi mật khẩu</a>
            </li>
        </ul>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${param.success == 'true'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <strong>Thành công!</strong> Đổi mật khẩu thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${param.error == 'wrong_password'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Lỗi!</strong> Mật khẩu cũ không đúng!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <c:if test="${not empty error && param.error != 'wrong_password'}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <strong>Lỗi!</strong> ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row">
        <!-- Change Password Form -->
        <div class="col-md-8">
            <div class="card">
                <div class="card-header">
                    <div class="card-title">Đổi Mật Khẩu</div>
                </div>
                <div class="card-body">
                    <!-- Password Requirements -->
                    <div class="alert alert-info" id="passwordRequirements">
                        <h6><i class="fas fa-info-circle"></i> Yêu cầu mật khẩu:</h6>
                        <ul class="mb-0">
                            <li class="req-minlength">Tối thiểu 6 ký tự</li>
                            <li>Nên sử dụng kết hợp chữ hoa, chữ thường, số và ký tự đặc biệt</li>
                            <li>Không nên sử dụng mật khẩu quá đơn giản</li>
                        </ul>
                    </div>

                    <form method="post" action="${pageContext.request.contextPath}/admin/profile/change-password" 
                          id="changePasswordForm">
                        <div class="row">
                            <div class="col-md-12 mb-3">
                                <label for="oldPassword" class="form-label">
                                    Mật Khẩu Hiện Tại <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="oldPassword" 
                                           name="oldPassword" required placeholder="Nhập mật khẩu hiện tại">
                                    <button class="btn btn-outline-secondary" type="button" 
                                            onclick="togglePassword('oldPassword')">
                                        <i class="fas fa-eye" id="oldPassword-icon"></i>
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12 mb-3">
                                <label for="newPassword" class="form-label">
                                    Mật Khẩu Mới <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="newPassword" 
                                           name="newPassword" required minlength="6" 
                                           placeholder="Nhập mật khẩu mới">
                                    <button class="btn btn-outline-secondary" type="button" 
                                            onclick="togglePassword('newPassword')">
                                        <i class="fas fa-eye" id="newPassword-icon"></i>
                                    </button>
                                </div>
                                <small class="form-text" id="passwordLengthMsg">
                                    Mật khẩu phải có ít nhất 6 ký tự
                                </small>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12 mb-3">
                                <label for="confirmPassword" class="form-label">
                                    Xác Nhận Mật Khẩu Mới <span class="text-danger">*</span>
                                </label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="confirmPassword" 
                                           name="confirmPassword" required minlength="6" 
                                           placeholder="Nhập lại mật khẩu mới">
                                    <button class="btn btn-outline-secondary" type="button" 
                                            onclick="togglePassword('confirmPassword')">
                                        <i class="fas fa-eye" id="confirmPassword-icon"></i>
                                    </button>
                                </div>
                                <small class="form-text" id="passwordMatchMsg"></small>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-key"></i> Đổi Mật Khẩu
                                </button>
                                <button type="button" class="btn btn-secondary" onclick="resetForm()">
                                    <i class="fas fa-undo"></i> Hủy
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/profile" 
                                   class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left"></i> Quay lại
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Help Card -->
        <div class="col-md-4">
            <div class="card bg-light">
                <div class="card-body">
                    <h5 class="card-title">
                        <i class="fas fa-shield-alt text-primary"></i> Bảo Mật Tài Khoản
                    </h5>
                    <p class="card-text">
                        Việc thay đổi mật khẩu định kỳ giúp bảo vệ tài khoản của bạn khỏi 
                        các truy cập trái phép.
                    </p>
                    <hr>
                    <h6>Mẹo tạo mật khẩu mạnh:</h6>
                    <ul class="small">
                        <li>Sử dụng ít nhất 8 ký tự</li>
                        <li>Kết hợp chữ hoa, chữ thường</li>
                        <li>Thêm số và ký tự đặc biệt</li>
                        <li>Không sử dụng thông tin cá nhân</li>
                        <li>Không sử dụng lại mật khẩu cũ</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .req-minlength.error {
        color: #dc3545;
        font-weight: bold;
    }
    #passwordRequirements.error {
        border-left: 4px solid #dc3545;
    }
    #passwordLengthMsg.error {
        color: #dc3545 !important;
    }
    #passwordMatchMsg.error {
        color: #dc3545 !important;
    }
    #passwordMatchMsg.success {
        color: #28a745 !important;
    }
</style>

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
    
    // Password validation
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
        if (newPassword.value.length < 6) {
            e.preventDefault();
            swal("Lỗi!", "Mật khẩu mới phải có ít nhất 6 ký tự!", "error");
            return;
        }
        
        if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            swal("Lỗi!", "Mật khẩu xác nhận không khớp!", "error");
            return;
        }
    });
    
    // Reset form
    function resetForm() {
        document.getElementById('oldPassword').value = '';
        document.getElementById('newPassword').value = '';
        document.getElementById('confirmPassword').value = '';
        
        passwordMatchMsg.textContent = '';
        passwordMatchMsg.classList.remove('error', 'success');
        passwordLengthMsg.classList.remove('error');
        passwordRequirements.classList.remove('error');
        
        // Reset password visibility
        document.getElementById('oldPassword').type = 'password';
        document.getElementById('newPassword').type = 'password';
        document.getElementById('confirmPassword').type = 'password';
        
        document.getElementById('oldPassword-icon').classList.remove('fa-eye-slash');
        document.getElementById('oldPassword-icon').classList.add('fa-eye');
        document.getElementById('newPassword-icon').classList.remove('fa-eye-slash');
        document.getElementById('newPassword-icon').classList.add('fa-eye');
        document.getElementById('confirmPassword-icon').classList.remove('fa-eye-slash');
        document.getElementById('confirmPassword-icon').classList.add('fa-eye');
    }
    
    // Clear form after successful password change
    const successParam = new URLSearchParams(window.location.search).get('success');
    if (successParam === 'true') {
        resetForm();
    }
</script>
