package ru.eqour.timetable.sender.impl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import ru.eqour.timetable.sender.MessageSender;
import ru.eqour.timetable.sender.exception.SendMessageException;
import ru.eqour.timetable.sender.model.EmailSenderSettings;
import ru.eqour.timetable.sender.model.Message;

import java.util.Properties;

/**
 * Отправитель сообщений по электронной почте.
 */
public class EmailSender implements MessageSender {

    private final EmailSenderSettings settings;

    /**
     * Создаёт новый экземпляр класса {@code EmailSender}.
     *
     * @param settings настройки отправителя сообщений.
     */
    public EmailSender(EmailSenderSettings settings) {
        this.settings = settings;
    }

    @Override
    public void sendMessage(String recipient, Message message) throws SendMessageException {
        try {
            sendEmail(recipient, message.getSubject(), message.getText());
        } catch (Exception e) {
            throw new SendMessageException(e);
        }
    }

    private void sendEmail(String to, String subject, String text) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", settings.getProtocol());
        props.put("mail.smtp.host", settings.getHost());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", settings.getDebug());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(settings.getUsername(), settings.getPassword());
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(settings.getUsername()));
        message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
    }
}
