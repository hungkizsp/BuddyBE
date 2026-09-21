package com.exe.buddy_english_be.modules.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@Slf4j
public class AuthMailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public void sendVerificationOtp(String toEmail, String otp) {
        String subject = "[Buddy English] Confirm Your Email Verification Code";
        String htmlContent = """
                <div style="font-family: Arial, sans-serif; padding: 20px; color: #333;">
                    <h2 style="color: #4F46E5;">Welcome to Buddy English! 🎉</h2>
                    <p>Your verification code for registration is:</p>
                    <div style="background-color: #F3F4F6; padding: 15px; border-radius: 8px; font-size: 24px; font-weight: bold; letter-spacing: 4px; text-align: center; color: #1F2937; margin: 20px 0;">
                        %s
                    </div>
                    <p>This code will expire in 10 minutes. If you did not request this, please ignore this email.</p>
                </div>
                """.formatted(otp);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    public void sendForgotPasswordOtp(String toEmail, String otp) {
        String subject = "[Buddy English] Password Reset Verification Code";
        String htmlContent = """
                <div style="font-family: Arial, sans-serif; padding: 20px; color: #333;">
                    <h2 style="color: #EF4444;">Reset Your Buddy English Password 🔐</h2>
                    <p>Your password reset verification code is:</p>
                    <div style="background-color: #FEE2E2; padding: 15px; border-radius: 8px; font-size: 24px; font-weight: bold; letter-spacing: 4px; text-align: center; color: #991B1B; margin: 20px 0;">
                        %s
                    </div>
                    <p>This code will expire in 10 minutes. Enter this code to set a new password.</p>
                </div>
                """.formatted(otp);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) {
        if (mailSender == null || fromEmail == null || fromEmail.isBlank()) {
            log.info("[MOCK MAIL] Target: {}, Subject: {}, HTML: \n{}", toEmail, subject, htmlContent);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail, "Buddy English");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Successfully sent email to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}. Logging OTP fallback instead.", toEmail, e.getMessage());
            log.info("[FALLBACK OTP MAIL] Target: {}, Subject: {}, Content:\n{}", toEmail, subject, htmlContent);
        }
    }
}
