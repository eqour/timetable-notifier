package ru.eqour.timetable.rest.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.exception.SendMessageException;
import ru.eqour.timetable.rest.utils.sender.MessageSender;

@Component
public class EmailService implements MessageSender {

    private final JavaMailSender mailSender;

    @Value("${email.username}")
    private String username;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMessage(String recipient, String title, String message) throws SendMessageException {
        try {
            sendEmail(recipient, title, message);
        } catch (MailException e) {
            throw new SendMessageException(e);
        }
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
