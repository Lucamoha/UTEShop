<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
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

            <form id="loginForm" action="login" method="post">
                <%-- Hidden field để giữ redirect URL --%>
                <c:if test="${not empty param.redirect}">
                    <input type="hidden" name="redirect" value="${param.redirect}" />
                </c:if>

                <div class="form-group">
                    <input
                            type="email"
                            class="form-input"
                            placeholder="Email"
                            name="email"
                            required
                    />
                </div>

                <div class="form-group">
                    <input
                            type="password"
                            class="form-input"
                            placeholder="Mật khẩu"
                            name="password"
                            required
                    />
                </div>

                <div class="form-group" style="display: flex; align-items: center; margin-bottom: 15px;">
                    <input type="checkbox" name="remember" id="rememberMe" style="margin-right: 8px;">
                    <label for="rememberMe" style="margin: 0; cursor: pointer;">Ghi nhớ đăng nhập (7 ngày)</label>
                </div>

                <%-- Hiển thị thông báo từ session (redirect từ protected page) --%>
                <c:if test="${not empty sessionScope.loginMessage}">
                    <div style="color: #ff9800; text-align: center; margin-bottom: 10px;">
                            ${sessionScope.loginMessage}
                    </div>
                    <%-- Xóa message sau khi hiển thị --%>
                    <c:remove var="loginMessage" scope="session"/>
                </c:if>

                <%-- Hiển thị thông báo lỗi (nếu có) --%>
                <c:if test="${not empty error}">
                    <div style="color: red; text-align: center; margin-bottom: 10px;">
                            ${error}
                    </div>
                </c:if>

                <button type="submit" class="login-button" id="loginBtn">
                    <span class="btn-text">Đăng Nhập</span>
                </button>
            </form>

            <div class="forgot-password">
                <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
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

            <div class="back-to-home">
                <a href="${pageContext.request.contextPath}/home" class="back-home-btn">
                    <i class="fas fa-home"></i>
                    Trở về trang chủ
                </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>