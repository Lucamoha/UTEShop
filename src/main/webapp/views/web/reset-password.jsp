<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đặt Mật Khẩu Mới - UTEShop</title>
    <link href="templates/css/register.css" rel="stylesheet" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"
    />
    <style>
      .password-requirements {
        font-size: 13px;
        color: #666;
        margin-top: 10px;
        padding: 10px;
        background: #f8f9fa;
        border-radius: 5px;
      }

      .requirement {
        margin: 5px 0;
      }

      .requirement.valid {
        color: #28a745;
      }

      .requirement.invalid {
        color: #dc3545;
      }
    </style>
  </head>
  <body>
    <div class="register-container">
      <div class="logo-container">
        <img src="templates/images/icons/logo-01.png" alt="Logo" />
      </div>

      <div class="welcome-text">Đặt mật khẩu mới</div>

      <div class="terms" style="margin-bottom: 30px; text-align: center">
        Nhập mật khẩu mới cho tài khoản của bạn
      </div>

      <form
        action="${pageContext.request.contextPath}/reset-password"
        method="post"
        id="resetForm"
      >
        <div class="form-group">
          <input
            type="password"
            id="newPassword"
            name="newPassword"
            placeholder="Mật khẩu mới"
            required
            minlength="6"
          />
        </div>

        <div class="form-group">
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            placeholder="Xác nhận mật khẩu"
            required
            minlength="6"
          />
        </div>

        <!-- Password Requirements -->
        <div class="password-requirements">
          <div class="requirement" id="length">
            <i class="fas fa-times-circle"></i> Ít nhất 6 ký tự
          </div>
          <div class="requirement" id="match">
            <i class="fas fa-times-circle"></i> Mật khẩu xác nhận khớp
          </div>
        </div>

        <%-- Hiển thị thông báo lỗi (nếu có) --%>
        <c:if test="${not empty error}">
          <div style="color: red; text-align: center; margin: 15px 0">
            ${error}
          </div>
        </c:if>

        <%-- Hiển thị thông báo thành công (nếu có) --%>
        <c:if test="${not empty success}">
          <div style="color: green; text-align: center; margin: 15px 0">
            ${success}
          </div>
        </c:if>

        <button type="submit" class="register-btn">Đặt lại mật khẩu</button>
      </form>

      <div class="divider">
        <span></span>
      </div>

      <div class="login-link">
        <a href="${pageContext.request.contextPath}/login"
          >← Quay lại đăng nhập</a
        >
      </div>
    </div>

    <script>
      // Real-time password validation
      const newPassword = document.getElementById("newPassword");
      const confirmPassword = document.getElementById("confirmPassword");
      const lengthReq = document.getElementById("length");
      const matchReq = document.getElementById("match");

      function validatePassword() {
        // Check length
        if (newPassword.value.length >= 6) {
          lengthReq.classList.remove("invalid");
          lengthReq.classList.add("valid");
          lengthReq.innerHTML =
            '<i class="fas fa-check-circle"></i> Ít nhất 6 ký tự';
        } else {
          lengthReq.classList.remove("valid");
          lengthReq.classList.add("invalid");
          lengthReq.innerHTML =
            '<i class="fas fa-times-circle"></i> Ít nhất 6 ký tự';
        }

        // Check match
        if (
          confirmPassword.value &&
          newPassword.value === confirmPassword.value
        ) {
          matchReq.classList.remove("invalid");
          matchReq.classList.add("valid");
          matchReq.innerHTML =
            '<i class="fas fa-check-circle"></i> Mật khẩu xác nhận khớp';
        } else {
          matchReq.classList.remove("valid");
          matchReq.classList.add("invalid");
          matchReq.innerHTML =
            '<i class="fas fa-times-circle"></i> Mật khẩu xác nhận khớp';
        }
      }

      newPassword.addEventListener("input", validatePassword);
      confirmPassword.addEventListener("input", validatePassword);

      // Form validation on submit
      document
        .getElementById("resetForm")
        .addEventListener("submit", function (e) {
          if (newPassword.value !== confirmPassword.value) {
            e.preventDefault();
            alert("Mật khẩu xác nhận không khớp!");
            confirmPassword.focus();
          } else if (newPassword.value.length < 6) {
            e.preventDefault();
            alert("Mật khẩu phải có ít nhất 6 ký tự!");
            newPassword.focus();
          }
        });
    </script>
  </body>
</html>
