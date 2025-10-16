<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Quên Mật Khẩu - UTEShop</title>
    <link href="templates/css/register.css" rel="stylesheet" />
  </head>
  <body>
    <div class="register-container">
      <div class="logo-container">
        <img src="templates/images/icons/logo-01.png" alt="Logo" />
      </div>

      <div class="welcome-text">Khôi phục mật khẩu</div>

      <div class="terms" style="margin-bottom: 20px; text-align: center">
        Nhập email đã đăng ký để nhận OTP đặt lại mật khẩu
      </div>

      <form action="forgot-password" id="forgotPasswordForm" method="post">
        <div class="form-group">
          <input
            type="email"
            placeholder="Email"
            name="email"
            value="${param.email}"
            required
          />
        </div>

        <%-- Hiển thị thông báo lỗi (nếu có) --%>
        <c:if test="${not empty error}">
          <div style="color: red; text-align: center; margin-bottom: 10px">
            ${error}
          </div>
        </c:if>

        <%-- Hiển thị thông báo thành công (nếu có) --%>
        <c:if test="${not empty success}">
          <div style="color: green; text-align: center; margin-bottom: 10px">
            ${success}
          </div>
        </c:if>

        <button type="submit" class="register-btn">Gửi yêu cầu</button>
      </form>

      <div class="divider">
        <span></span>
      </div>

      <div class="login-link">
        <span>Nhớ mật khẩu? </span
        ><a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a>
      </div>

      <div class="login-link" style="margin-top: 10px">
        <span>Chưa có tài khoản? </span
        ><a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
      </div>
    </div>
  </body>
</html>
