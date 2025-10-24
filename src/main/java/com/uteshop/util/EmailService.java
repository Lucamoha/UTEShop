package com.uteshop.util;

import com.uteshop.entity.order.OrderItems;
import com.uteshop.entity.order.Orders;
import com.uteshop.enums.PaymentEnums;
import com.uteshop.services.impl.manager.OrdersManagerServiceImpl;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailService {

    private static final Properties emailConfig = loadEmailConfig();
    private static final ExecutorService emailExecutor = Executors.newFixedThreadPool(3);

    /**
     * Load cấu hình email từ config.properties
     */
    private static Properties loadEmailConfig() {
        Properties props = new Properties();
        try (InputStream input = EmailService.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Cannot load email config: " + e.getMessage());
        }
        return props;
    }

    private static String vnd(BigDecimal n) {
        if (n == null) n = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return nf.format(n);
    }

    private static String getFromEmail() {
        return emailConfig.getProperty("smtp.from.email", "your-email@gmail.com");
    }

    private static String getPassword() {
        return emailConfig.getProperty("smtp.from.password", "your-app-password");
    }

    private static String getSmtpHost() {
        return emailConfig.getProperty("smtp.host", "smtp.gmail.com");
    }

    private static String getSmtpPort() {
        return emailConfig.getProperty("smtp.port", "587");
    }

    private static String getFromName() {
        return emailConfig.getProperty("smtp.from.name", "UTEShop");
    }

    // Gửi OTP qua email đặt lại mật khẩu
    public static boolean sendOTP(String toEmail, String otp) {
        String subject = "Mã OTP đặt lại mật khẩu - UTEShop";
        String htmlContent = buildOTPEmailTemplate(otp);

        return sendEmail(toEmail, subject, htmlContent);
    }

    // Gửi OTP đăng ký tài khoản
    public static boolean sendRegistrationOTP(String toEmail, String otp) {
        String subject = "Mã OTP xác thực đăng ký - UTEShop";
        String htmlContent = buildRegistrationOTPTemplate(otp);

        return sendEmail(toEmail, subject, htmlContent);
    }

    // Gửi email xác nhận đơn hàng
    public static void sendOrderConfirmation(Integer orderId, Integer branchId) {
        OrdersManagerServiceImpl ordersManagerService = new OrdersManagerServiceImpl();
        Orders order = ordersManagerService.findByIdWithItems(orderId, branchId);

        String toEmail = order.getUser().getEmail();
        String orderCode = order.getId().toString();
        String customerName = order.getReceiverName();
        String paymentMethod = PaymentEnums.Method.label(order.getPayment().getMethod());
        String address = order.getAddressLine() + " " +
                order.getDistrict() + " " +
                order.getWard() + " " +
                order.getCity();
        String orderDate = order.getCreatedAt().toString();
        String subTotal = vnd(order.getSubtotal());
        String discountAmount = "-" + vnd(order.getDiscountAmount());
        String totalAmount = vnd(order.getTotalAmount());
        String orderDetailHtml = "";
        for (OrderItems oi : order.getItems()) {
            orderDetailHtml += "<tr>";
            orderDetailHtml += "<td>" + oi.getVariant().getSKU() + "</td>";
            orderDetailHtml += "<td>" + oi.getQuantity() + "</td>";
            orderDetailHtml += "<td>" + vnd(oi.getPrice()) + "</td>";
            orderDetailHtml += "<td>" + vnd(oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity()))) + "</td>";
            orderDetailHtml += "</tr>";
        }

        String subject = "Xác nhận đơn hàng #" + orderCode + " - UTEShop";

        String htmlContent = buildOrderConfirmationTemplate(
                customerName,
                orderCode,
                paymentMethod,
                address,
                orderDate,
                subTotal,
                discountAmount,
                totalAmount,
                orderDetailHtml
        );

        sendEmailAsync(toEmail, subject, htmlContent);
    }

    // Tạo mã OTP ngẫu nhiên 6 chữ số
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Gửi email
    private static boolean sendEmail(String toEmail, String subject, String htmlContent) {
        try {
            // 1. Cấu hình SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", getSmtpHost());
            props.put("mail.smtp.port", getSmtpPort());
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            // 2. Tạo session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(getFromEmail(), getPassword());
                }
            });

            // 3. Tạo message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(getFromEmail(), getFromName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // 4. Gửi email
            Transport.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void sendEmailAsync(String toEmail, String subject, String htmlContent) {
        emailExecutor.submit(() -> {
            try {
                sendEmail(toEmail, subject, htmlContent);
            } catch (Exception e) {
                System.err.println("[Async Email] Lỗi gửi email tới " + toEmail + ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    // Mẫu email OTP
    private static String buildOTPEmailTemplate(String otp) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; }
                        .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 10px; }
                        .header { text-align: center; color: #333; }
                        .otp-box { background: #f0f0f0; padding: 20px; text-align: center; margin: 20px 0; border-radius: 5px; }
                        .otp-code { font-size: 32px; font-weight: bold; color: #e91e63; letter-spacing: 5px; }
                        .footer { color: #666; font-size: 12px; text-align: center; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h2 class="header">🔐 Đặt lại mật khẩu UTEShop</h2>
                        <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>
                        <p>Vui lòng sử dụng mã xác thực bên dưới:</p>

                        <div class="otp-box">
                            <div class="otp-code">%s</div>
                        </div>

                        <p><strong>Lưu ý:</strong></p>
                        <ul>
                            <li>Mã có hiệu lực trong <strong>5 phút</strong></li>
                            <li>Không chia sẻ mã này với bất kỳ ai</li>
                            <li>Nếu bạn không yêu cầu, vui lòng bỏ qua email này</li>
                        </ul>

                        <div class="footer">
                            <p>Email tự động từ hệ thống UTEShop</p>
                            <p>&copy; 2025 UTEShop. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(otp);
    }

    // Mẫu email OTP đăng ký tài khoản
    private static String buildRegistrationOTPTemplate(String otp) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; }
                        .container { max-width: 600px; margin: 20px auto; background: white; padding: 30px; border-radius: 10px; }
                        .header { text-align: center; color: #333; }
                        .otp-box { background: #f0f0f0; padding: 20px; text-align: center; margin: 20px 0; border-radius: 5px; }
                        .otp-code { font-size: 32px; font-weight: bold; color: #4CAF50; letter-spacing: 5px; }
                        .footer { color: #666; font-size: 12px; text-align: center; margin-top: 20px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h2 class="header">🎉 Chào mừng đến với UTEShop</h2>
                        <p>Cảm ơn bạn đã đăng ký tài khoản tại UTEShop!</p>
                        <p>Vui lòng sử dụng mã xác thực bên dưới để hoàn tất đăng ký:</p>

                        <div class="otp-box">
                            <div class="otp-code">%s</div>
                        </div>

                        <p><strong>Lưu ý:</strong></p>
                        <ul>
                            <li>Mã có hiệu lực trong <strong>5 phút</strong></li>
                            <li>Không chia sẻ mã này với bất kỳ ai</li>
                            <li>Nếu bạn không yêu cầu, vui lòng bỏ qua email này</li>
                        </ul>

                        <div class="footer">
                            <p>Email tự động từ hệ thống UTEShop</p>
                            <p>&copy; 2025 UTEShop. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """
                .formatted(otp);
    }

    private static String buildOrderConfirmationTemplate(String customerName,
                                                         String orderCode,
                                                         String paymentMethod,
                                                         String address,
                                                         String orderDate,
                                                         String subTotal,
                                                         String discountAmount,
                                                         String totalAmount,
                                                         String orderDetailHtml) {

        return """
            <!DOCTYPE html>
            <html lang="vi">
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f6f6f6; margin: 0; padding: 0; }
                    .container { max-width: 650px; margin: 30px auto; background: white; padding: 30px; border-radius: 10px; }
                    h2 { color: #2E7D32; text-align: center; }
                    table { width: 100%%; border-collapse: collapse; margin-top: 20px; }
                    th, td { border-bottom: 1px solid #ddd; padding: 10px; text-align: left; }
                    th { background-color: #f0f0f0; }
                    .lineTotal { font-size: 18px; font-weight: bold; color: #0a3df2; text-align: right; }
                    .total { font-size: 18px; font-weight: bold; color: #2E7D32; text-align: right; }
                    .discount { font-size: 18px; font-weight: bold; color: #E91E63; text-align: right; }
                    .info { margin-top: 20px; line-height: 1.6; }
                    .footer { font-size: 12px; color: #777; text-align: center; margin-top: 30px; }
                    .highlight { color: #2196F3; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Cảm ơn %s đã đặt hàng tại UTEShop!</h2>
                    <p>Xin chào <strong>%s</strong>,</p>
                    <p>Chúng tôi đã nhận được đơn hàng của bạn. Thông tin chi tiết như sau:</p>

                    <div class="info">
                        <p><strong>Mã đơn hàng:</strong> <span class="highlight">%s</span></p>
                        <p><strong>Ngày đặt:</strong> %s</p>
                        <p><strong>Phương thức thanh toán:</strong> %s</p>
                        <p><strong>Địa chỉ giao hàng:</strong> %s</p>
                    </div>

                    <table>
                        <thead>
                            <tr>
                                <th>Sản phẩm</th>
                                <th>Số lượng</th>
                                <th>Đơn giá</th>
                                <th>Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>

                    <p class="lineTotal">Tạm tính: %s</p>
                    <p class="discount">Giảm giá: %s</p>
                    <p class="total">Tổng tiền: %s</p>

                    <p style="margin-top:20px;">Đơn hàng của bạn sẽ được xử lý và giao trong thời gian sớm nhất.</p>
                    <p>Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ bộ phận chăm sóc khách hàng của UTEShop.</p>

                    <div class="footer">
                        <p>Email tự động từ hệ thống UTEShop</p>
                        <p>&copy; 2025 UTEShop. All rights reserved.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(customerName,
                customerName,
                orderCode,
                orderDate,
                paymentMethod,
                address,
                orderDetailHtml,
                subTotal,
                discountAmount,
                totalAmount);
    }

}
