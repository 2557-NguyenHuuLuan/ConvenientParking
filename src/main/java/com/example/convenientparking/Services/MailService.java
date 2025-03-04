package com.example.convenientparking.Services;

import com.example.convenientparking.Entities.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private JavaMailSender mailSender;
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }


    public void sendResetPasswordPin(String to, String pin) {
        String subject = "Your Password Reset PIN";
        String text = "Your PIN for resetting your password is: " + pin;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendVerificationEmail(String toEmail, Long userId) {
        String verificationLink = "http://localhost:8080/mail/verify-account?userId=" + userId;
        String subject = "Xác thực tài khoản của bạn";
        String message = "Xin chào,\n\n"
                + "Vui lòng nhấn vào liên kết sau để xác thực tài khoản của bạn: " + verificationLink
                + "\n\nCảm ơn bạn.";

        sendMail(toEmail, subject, message);
    }

}
