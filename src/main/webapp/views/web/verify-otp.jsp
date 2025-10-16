<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ include file="/commons/taglib.jsp"%>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Xác Thực OTP - UTEShop</title>
    <link href="templates/css/register.css" rel="stylesheet" />
    <style>
      .otp-container {
        display: flex;
        justify-content: center;
        gap: 10px;
        margin: 30px 0;
      }

      .otp-input {
        width: 50px;
        height: 50px;
        font-size: 24px;
        text-align: center;
        border: 2px solid #ddd;
        border-radius: 8px;
        transition: border-color 0.3s ease, transform 0.2s ease;
      }

      .otp-input:focus {
        outline: none;
        border-color: #007bff;
        transform: scale(1.05);
        box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
      }

      .timer {
        color: #e74c3c;
        font-weight: bold;
        font-size: 16px;
      }
    </style>
  </head>
  <body>
    <div class="register-container">
      <div class="logo-container">
        <img src="templates/images/icons/logo-01.png" alt="Logo" />
      </div>

      <div class="welcome-text">Xác thực OTP</div>

      <div class="terms" style="margin-bottom: 20px; text-align: center">
        Mã xác thực đã được gửi đến email của bạn
      </div>

      <form
        action="${pageContext.request.contextPath}/verify-otp"
        method="post"
        id="otpForm"
      >
        <!-- 6 ô nhập OTP tách rời -->
        <div class="otp-container">
          <input
            type="text"
            class="otp-input"
            maxlength="1"
            pattern="[0-9]"
            required
            autocomplete="off"
          />
          <input
            type="text"
            class="otp-input"
            maxlength="1"
            pattern="[0-9]"
            required
            autocomplete="off"
          />
          <input
            type="text"
            class="otp-input"
            maxlength="1"
            pattern="[0-9]"
            required
            autocomplete="off"
          />
          <input
            type="text"
            class="otp-input"
            maxlength="1"
            pattern="[0-9]"
            required
            autocomplete="off"
          />
          <input
            type="text"
            class="otp-input"
            maxlength="1"
            pattern="[0-9]"
            required
            autocomplete="off"
          />
          <input
            type="text"
            class="otp-input"
            maxlength="1"
            pattern="[0-9]"
            required
            autocomplete="off"
          />
        </div>

        <!-- Hidden input chứa OTP đầy đủ -->
        <input type="hidden" name="otp" id="otpValue" />

        <!-- Timer -->
        <p style="text-align: center; margin: 20px 0">
          <span class="timer" id="timer">Mã OTP có hiệu lực trong 5 phút</span>
        </p>

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

        <button type="submit" class="register-btn">Xác thực</button>
      </form>

      <div class="terms" style="margin-top: 20px">
        Không nhận được mã?
        <a href="${pageContext.request.contextPath}/forgot-password">Gửi lại</a>
      </div>

      <div class="divider">
        <span></span>
      </div>

      <div class="login-link">
        <a href="${pageContext.request.contextPath}/forgot-password"
          >← Quay lại</a
        >
      </div>
    </div>

    <script>
      // Auto-focus và merge OTP inputs
      const inputs = document.querySelectorAll(".otp-input");
      const otpValue = document.getElementById("otpValue");

      inputs.forEach((input, index) => {
        // Auto-focus next input khi nhập
        input.addEventListener("input", function () {
          if (this.value.length === 1 && index < inputs.length - 1) {
            inputs[index + 1].focus();
          }

          // Merge OTP thành chuỗi đầy đủ
          let otp = "";
          inputs.forEach((inp) => (otp += inp.value));
          otpValue.value = otp;
        });

        // Backspace: focus về ô trước
        input.addEventListener("keydown", function (e) {
          if (e.key === "Backspace" && this.value === "" && index > 0) {
            inputs[index - 1].focus();
          }
        });

        // Chỉ cho phép nhập số
        input.addEventListener("keypress", function (e) {
          if (!/[0-9]/.test(e.key)) {
            e.preventDefault();
          }
        });

        // Paste support: tự động phân tách OTP
        input.addEventListener("paste", function (e) {
          e.preventDefault();
          const pasteData = e.clipboardData.getData("text").replace(/\D/g, "");

          for (let i = 0; i < Math.min(pasteData.length, inputs.length); i++) {
            inputs[i].value = pasteData[i];
          }

          // Merge OTP
          let otp = "";
          inputs.forEach((inp) => (otp += inp.value));
          otpValue.value = otp;

          // Focus ô cuối
          if (pasteData.length >= inputs.length) {
            inputs[inputs.length - 1].focus();
          } else {
            inputs[pasteData.length].focus();
          }
        });
      });

      // Focus ô đầu tiên khi load trang
      inputs[0].focus();
    </script>
  </body>
</html>
