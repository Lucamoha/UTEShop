<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký - UTEShop</title>
    <link href="templates/css/register.css" rel="stylesheet">
</head>
<body>
<div class="register-container">
    <div class="logo-container">
        <img src="templates/images/icons/logo-01.png" alt="Logo" />
    </div>

    <div class="welcome-text">
        Tạo tài khoản mới
    </div>

    <form id="registerForm">
        <div class="form-group">
            <input type="text" placeholder="Họ và tên" id="fullname" required>
        </div>

        <div class="form-group">
            <input type="email" placeholder="Email hoặc số điện thoại" id="email" required>
        </div>

        <div class="form-group">
            <input type="tel" placeholder="Số điện thoại" id="phone" required>
        </div>

        <div class="form-group">
            <input type="password" placeholder="Mật khẩu" id="password" required>
            <div class="password-strength" id="passwordStrength">
                <div class="strength-bar">
                    <div class="strength-fill" id="strengthFill"></div>
                </div>
                <span id="strengthText">Nhập mật khẩu để kiểm tra độ mạnh</span>
            </div>
        </div>

        <div class="form-group">
            <input type="password" placeholder="Nhập lại mật khẩu" id="confirmPassword" required>
        </div>

        <button type="submit" class="register-btn" id="registerBtn">
            Đăng Ký
        </button>
    </form>

    <div class="terms">
        Bằng cách đăng ký, bạn đồng ý với
        <a href="#">Điều khoản sử dụng</a> và
        <a href="#">Chính sách bảo mật</a> của chúng tôi.
    </div>

    <div class="divider">
        <span>hoặc</span>
    </div>

    <div class="login-link">
        <span>Đã có tài khoản? </span><a href="#" id="loginLink">Đăng nhập ngay</a>
    </div>

    <div class="success-animation" id="successMessage">
        <div class="checkmark"></div>
        Đăng ký thành công!
    </div>
</div>

<script>
    // Password strength checker
    const passwordInput = document.getElementById('password');
    const strengthIndicator = document.getElementById('passwordStrength');
    const strengthFill = document.getElementById('strengthFill');
    const strengthText = document.getElementById('strengthText');

    passwordInput.addEventListener('input', function() {
        const password = this.value;
        if (password.length > 0) {
            strengthIndicator.classList.add('show');
            checkPasswordStrength(password);
        } else {
            strengthIndicator.classList.remove('show');
        }
    });

    function checkPasswordStrength(password) {
        let strength = 0;
        let text = '';

        if (password.length >= 8) strength++;
        if (password.match(/[a-z]/)) strength++;
        if (password.match(/[A-Z]/)) strength++;
        if (password.match(/[0-9]/)) strength++;
        if (password.match(/[^a-zA-Z0-9]/)) strength++;

        strengthFill.className = 'strength-fill';

        if (strength <= 2) {
            strengthFill.classList.add('strength-weak');
            text = 'Mật khẩu yếu';
        } else if (strength <= 4) {
            strengthFill.classList.add('strength-medium');
            text = 'Mật khẩu trung bình';
        } else {
            strengthFill.classList.add('strength-strong');
            text = 'Mật khẩu mạnh';
        }

        strengthText.textContent = text;
    }

    // Form validation
    const form = document.getElementById('registerForm');
    const registerBtn = document.getElementById('registerBtn');
    const successMessage = document.getElementById('successMessage');

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const fullname = document.getElementById('fullname').value;
        const email = document.getElementById('email').value;
        const phone = document.getElementById('phone').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        // Validate fields
        if (!fullname || !email || !phone || !password || !confirmPassword) {
            alert('Vui lòng điền đầy đủ thông tin!');
            return;
        }

        if (password !== confirmPassword) {
            alert('Mật khẩu xác nhận không khớp!');
            return;
        }

        if (password.length < 6) {
            alert('Mật khẩu phải có ít nhất 6 ký tự!');
            return;
        }

        // Validate email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            alert('Email không hợp lệ!');
            return;
        }

        // Validate phone
        const phoneRegex = /^[0-9]{10,11}$/;
        if (!phoneRegex.test(phone.replace(/\D/g, ''))) {
            alert('Số điện thoại không hợp lệ!');
            return;
        }

        // Simulate registration
        registerBtn.textContent = 'Đang xử lý...';
        registerBtn.disabled = true;

        setTimeout(() => {
            form.style.display = 'none';
            successMessage.style.display = 'block';

            setTimeout(() => {
                alert('Đăng ký thành công! Chuyển đến trang đăng nhập...');
                // Here you would redirect to login page
                // window.location.href = '/login';
            }, 2000);
        }, 1500);
    });

    // Phone number formatting
    document.getElementById('phone').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 0) {
            if (value.length <= 3) {
                value = value;
            } else if (value.length <= 6) {
                value = value.slice(0, 3) + ' ' + value.slice(3);
            } else if (value.length <= 10) {
                value = value.slice(0, 3) + ' ' + value.slice(3, 6) + ' ' + value.slice(6);
            } else {
                value = value.slice(0, 3) + ' ' + value.slice(3, 6) + ' ' + value.slice(6, 10);
            }
        }
        e.target.value = value;
    });

    // Input animations
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentNode.classList.add('focused');
        });

        input.addEventListener('blur', function() {
            this.parentNode.classList.remove('focused');
        });
    });
</script>
</body>
</html>