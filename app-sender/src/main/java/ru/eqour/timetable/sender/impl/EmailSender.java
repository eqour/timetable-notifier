package ru.eqour.timetable.sender.impl;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.eqour.timetable.sender.MessageSender;
import ru.eqour.timetable.sender.exception.SendMessageException;
import ru.eqour.timetable.sender.model.EmailSenderSettings;
import ru.eqour.timetable.sender.model.Message;

import java.util.Properties;

/**
 * Отправитель сообщений по электронной почте.
 */
public class EmailSender implements MessageSender {

    private final String from;
    private final JavaMailSender mailSender;

    /**
     * Создаёт новый экземпляр класса {@code EmailSender}.
     *
     * @param settings настройки отправителя сообщений.
     */
    public EmailSender(EmailSenderSettings settings) {
        this.mailSender = createJavaMailSender(settings);
        this.from = settings.getUsername();
    }

    private JavaMailSender createJavaMailSender(EmailSenderSettings settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUsername());
        mailSender.setPassword(settings.getPassword());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", settings.getProtocol());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", settings.getDebug());
        return mailSender;
    }

    @Override
    public void sendMessage(String recipient, Message message) throws SendMessageException {
        try {
            sendEmail(recipient, message.getSubject(), message.getText());
        } catch (MailException e) {
            throw new SendMessageException(e);
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
