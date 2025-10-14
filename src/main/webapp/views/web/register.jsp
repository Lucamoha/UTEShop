<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/commons/taglib.jsp"%>
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

    <form action="register" id="registerForm" method="post">
        <div class="form-group">
            <input type="text" placeholder="Họ và tên" name="fullname" value="${param.fullname}" required>
        </div>

        <div class="form-group">
            <input type="email" placeholder="Email" name="email" value="${param.email}" required>
        </div>

        <div class="form-group">
            <input type="tel" placeholder="Số điện thoại" name="phone" value="${param.phone}" required>
        </div>

        <div class="form-group">
            <input type="password" placeholder="Mật khẩu" name="password" value="${param.password}" required>
            <div class="password-strength" id="passwordStrength">
                <div class="strength-bar">
                    <div class="strength-fill" id="strengthFill"></div>
                </div>
                <span id="strengthText">Nhập mật khẩu để kiểm tra độ mạnh</span>
            </div>
        </div>

        <div class="form-group">
            <input type="password" placeholder="Nhập lại mật khẩu" name="confirmPassword" value="${param.confirmPassword}" required>
        </div>

        <%-- Hiển thị thông báo lỗi (nếu có) --%>
        <c:if test="${not empty error}">
            <div style="color: red; text-align: center; margin-bottom: 10px;">
                    ${error}
            </div>
        </c:if>

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
        <span>Đã có tài khoản? </span><a href=${pageContext.request.contextPath}/login id="loginLink">Đăng nhập ngay</a>
    </div>

    <div class="success-animation" id="successMessage">
        <div class="checkmark"></div>
        Đăng ký thành công!
    </div>
</div>
</body>
</html>