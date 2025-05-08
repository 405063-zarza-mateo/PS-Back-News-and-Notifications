package org.utn.tup.psbacknewsandnotifications.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Service
public class EmailHandler {
    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String to, String subject, String body) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(new InternetAddress(from, "El Galponcito" ));
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        sender.send(message);
    }
}
