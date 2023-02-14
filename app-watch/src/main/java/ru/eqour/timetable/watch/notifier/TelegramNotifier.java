package ru.eqour.timetable.watch.notifier;

/**
 * Отправляет сообщения от имени бота в Telegram.
 */
public class TelegramNotifier extends SimpleWebRequestNotifier {

    /**
     * Создаёт новый экземпляр класса {@code TelegramNotifier}.
     *
     * @param token секретный ключ, использующийся для отправки сообщений.
     */
    public TelegramNotifier(String token) {
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
