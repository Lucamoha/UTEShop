<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng Nhập - UTEShop</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="templates/css/login.css" rel="stylesheet">
</head>
<body>
<div class="login-container">
    <div class="login-left">
        <div class="logo-container">
            <img src="templates/images/icons/logo-01.png" alt="" />
        </div>

        <div class="welcome-text">
            Chào mừng bạn trở lại
        </div>

        <div class="brand-description">
            Khám phá thế giới công nghệ Apple với những sản phẩm chính hãng và dịch vụ tuyệt vời nhất.
        </div>

        <ul class="features-list">
            <li>Sản phẩm Apple chính hãng 100%</li>
            <li>Bảo hành toàn cầu</li>
            <li>Giao hàng miễn phí toàn quốc</li>
            <li>Hỗ trợ kỹ thuật 24/7</li>
            <li>Đổi trả trong 30 ngày</li>
        </ul>
    </div>

    <div class="login-right">
        <div class="form-section">
            <h3>Đăng nhập tài khoản</h3>

            <form id="loginForm">
                <div class="form-group">
                    <input
                            type="email"
                            class="form-input"
                            placeholder="Email hoặc số điện thoại"
                            required
                    />
                </div>

                <div class="form-group">
                    <input
                            type="password"
                            class="form-input"
                            placeholder="Mật khẩu"
                            required
                    />
                </div>

                <button type="submit" class="login-button" id="loginBtn">
                    <span class="btn-text">Đăng Nhập</span>
                </button>
            </form>

            <div class="forgot-password">
                <a href="#" onclick="showAlert('Chức năng quên mật khẩu')">Quên mật khẩu?</a>
            </div>

            <div class="divider">
                <span>hoặc đăng nhập với</span>
            </div>

            <div class="social-login">
                <button
                        class="social-button google"
                        onclick="showAlert('Đăng nhập với Google')"
                        title="Google"
                ></button>
                <button
                        class="social-button facebook"
                        onclick="showAlert('Đăng nhập với Facebook')"
                        title="Facebook"
                ></button>
            </div>

            <div class="register-link">
                Chưa có tài khoản?
                <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
            </div>
        </div>
    </div>
</div>

<script>
    // Login form handling
    document.getElementById("loginForm").addEventListener("submit", function (e) {
        e.preventDefault();

        const loginBtn = document.getElementById("loginBtn");
        const btnText = loginBtn.querySelector(".btn-text");

        // Show loading state
        btnText.innerHTML = '<div class="loading"></div>';
        loginBtn.disabled = true;

        // Simulate login process
        setTimeout(() => {
            btnText.textContent = "Đăng Nhập Thành Công!";
            loginBtn.style.background = "linear-gradient(135deg, #28a745 0%, #20c997 100%)";

            setTimeout(() => {
                alert("Đăng nhập thành công! Chào mừng bạn đến với Apple Store.");
                // Reset button
                btnText.textContent = "Đăng Nhập";
                loginBtn.style.background = "linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%)";
                loginBtn.disabled = false;
            }, 1500);
        }, 2000);
    });

    // Input focus effects
    document.querySelectorAll(".form-input").forEach((input) => {
        input.addEventListener("focus", function () {
            this.parentElement.style.transform = "scale(1.02)";
        });

        input.addEventListener("blur", function () {
            this.parentElement.style.transform = "scale(1)";
        });
    });

    // Social button animations
    document.querySelectorAll(".social-button").forEach((btn) => {
        btn.addEventListener("mouseenter", function () {
            this.style.transform = "translateY(-2px) scale(1.05)";
        });

        btn.addEventListener("mouseleave", function () {
            this.style.transform = "translateY(0) scale(1)";
        });
    });

    // Utility function for alerts
    function showAlert(message) {
        alert(message);
    }

    // Add some interactive particles
    function createParticle() {
        const particle = document.createElement("div");
        particle.style.cssText = `
                position: absolute;
                width: 4px;
                height: 4px;
                background: rgba(26, 26, 26, 0.1);
                border-radius: 50%;
                pointer-events: none;
                animation: particleFloat 8s linear infinite;
            `;

        particle.style.left = Math.random() * 100 + "%";
        particle.style.top = "110%";

        document.body.appendChild(particle);

        setTimeout(() => {
            particle.remove();
        }, 8000);
    }

    // Create particles periodically
    setInterval(createParticle, 3000);
</script>
</body>
</html>