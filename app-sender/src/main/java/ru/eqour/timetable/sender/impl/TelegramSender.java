package ru.eqour.timetable.sender.impl;

/**
 * Отправляет сообщения от имени бота в Telegram.
 */
public class TelegramSender extends SimpleWebRequestSender {

    /**
     * Создаёт новый экземпляр класса {@code TelegramSender}.
     *
     * @param token секретный ключ, использующийся для отправки сообщений.
     */
    public TelegramSender(String token) {
        super(token);
    }

    @Override
    protected String buildSendMessageURL(String token, String recipient, String message) {
        return "https://api.telegram.org/bot" +
                token +
                "/sendMessage?chat_id=" +
                recipient +
                "&text=" +
                message;
    }
}
