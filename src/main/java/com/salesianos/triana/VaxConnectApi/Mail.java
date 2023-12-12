package com.salesianos.triana.VaxConnectApi;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class Mail {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String email,String otp) throws MessagingException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Hello your otp is"+otp);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new  MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("verify otp");
        mimeMessageHelper.setText("""
                <div>
                <a href="http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">clic to verify</a>
                </div>
                """.formatted(email,otp),true);
        javaMailSender.send(mimeMessage);
    }
}
