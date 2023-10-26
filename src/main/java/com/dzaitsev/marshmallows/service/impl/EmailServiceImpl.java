package com.dzaitsev.marshmallows.service.impl;

import com.dzaitsev.marshmallows.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String ENCODING = StandardCharsets.UTF_8.name();
    private static final Locale locale = new Locale("RU", "ru");

    private final JavaMailSender emailSender;

    private final TemplateEngine templateEngine;

    private boolean sendMail(String recipient, String subject, String html) {
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setFrom("marshmallow_1@bk.ru");
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(html, true);
        emailSender.send(mimeMessage);
        return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean sendVerificationCode(String recipient, String code) {
        final Context ctx = new Context(locale);
        ctx.setVariable("code", code);
        final String htmlContent = this.templateEngine.process("code", ctx);
        return sendMail(recipient, "Код верификации", htmlContent);
    }
}