package com.uteshop.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public class EmailService {

    private static final Properties emailConfig = loadEmailConfig();

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

    // Gửi OTP qua email
    public static boolean sendOTP(String toEmail, String otp) {
        String subject = "Mã OTP đặt lại mật khẩu - UTEShop";
        String htmlContent = buildOTPEmailTemplate(otp);

        return sendEmail(toEmail, subject, htmlContent);
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
}
