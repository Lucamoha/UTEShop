<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng Ký - UTEShop</title>
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
        margin: 10px 0;
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

      <div class="welcome-text">Tạo tài khoản mới</div>

      <form action="register" id="registerForm" method="post">
        <div class="form-group">
          <input
            type="text"
            placeholder="Họ và tên"
            name="fullname"
            value="${param.fullname}"
            required
          />
        </div>

        <div class="form-group">
          <input
            type="email"
            placeholder="Email"
            name="email"
            value="${param.email}"
            required
          />
        </div>

        <div class="form-group">
          <input
            type="tel"
            placeholder="Số điện thoại"
            name="phone"
            value="${param.phone}"
            required
          />
        </div>

        <div class="form-group">
          <input
            type="password"
            id="password"
            placeholder="Mật khẩu"
            name="password"
            value="${param.password}"
            required
            minlength="6"
          />
        </div>

        <div class="form-group">
          <input
            type="password"
            id="confirmPassword"
            placeholder="Nhập lại mật khẩu"
            name="confirmPassword"
            value="${param.confirmPassword}"
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
        <span></span>
      </div>

      <div class="login-link">
        <span>Đã có tài khoản? </span
        ><a href="${pageContext.request.contextPath}/login" id="loginLink"
          >Đăng nhập ngay</a
        >
      </div>

      <div class="success-animation" id="successMessage">
        <div class="checkmark"></div>
        Đăng ký thành công!
      </div>
    </div>

    <script>
      // Real-time password validation
      const password = document.getElementById("password");
      const confirmPassword = document.getElementById("confirmPassword");
      const lengthReq = document.getElementById("length");
      const matchReq = document.getElementById("match");

      function validatePassword() {
        // Check length
        if (password.value.length >= 6) {
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
        if (confirmPassword.value && password.value === confirmPassword.value) {
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

      password.addEventListener("input", validatePassword);
      confirmPassword.addEventListener("input", validatePassword);

      // Form validation on submit
      document
        .getElementById("registerForm")
        .addEventListener("submit", function (e) {
          if (password.value !== confirmPassword.value) {
            e.preventDefault();
            alert("Mật khẩu xác nhận không khớp!");
            confirmPassword.focus();
          } else if (password.value.length < 6) {
            e.preventDefault();
            alert("Mật khẩu phải có ít nhất 6 ký tự!");
            password.focus();
          }
        });
    </script>
  </body>
</html>
