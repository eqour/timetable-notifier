package ru.eqour.timetable.rest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.eqour.timetable.sender.impl.EmailSender;
import ru.eqour.timetable.sender.impl.TelegramSender;
import ru.eqour.timetable.sender.impl.VkSender;
import ru.eqour.timetable.sender.model.EmailSenderSettings;

@Configuration
public class SenderConfig {

    @Value("${email.host}")
    private String host;
    @Value("${email.port}")
    private int port;
    @Value("${email.username}")
    private String username;
    @Value("${email.password}")
    private String password;
    @Value("${email.protocol}")
    private String protocol;
    @Value("${email.debug}")
    private String debug;
    @Value("${vk.token}")
    private String vkToken;
    @Value("${telegram.token}")
    private String telegramToken;

    @Bean
    public EmailSender emailSender() {
        return new EmailSender(new EmailSenderSettings(host, port, username, password, protocol, debug));
    }

    @Bean
    public VkSender vkSender() {
        return new VkSender(vkToken);
    }

    @Bean
    public TelegramSender telegramSender() {
        return new TelegramSender(telegramToken);
    }
}
