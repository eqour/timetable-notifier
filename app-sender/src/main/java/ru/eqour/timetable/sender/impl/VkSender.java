package ru.eqour.timetable.sender.impl;

import java.util.Random;

/**
 * Отправляет сообщения от имени сообщества в социальной сети ВКонтакте.
 */
public class VkSender extends SimpleWebRequestSender {

    private final Random random;

    /**
     * Создаёт новый экземпляр класса {@code VkSender}.
     *
     * @param token секретный ключ, использующийся для отправки сообщений.
     */
    public VkSender(String token) {
        super(token);
        random = new Random();
    }

    @Override
    protected String buildSendMessageURL(String token, String recipient, String message) {
        return "https://api.vk.com/method/messages.send?v=5.131&user_id=" +
                recipient +
                "&message=" +
                message +
                "&access_token=" +
                token +
                "&random_id=" +
                random.nextInt();
    }
}
