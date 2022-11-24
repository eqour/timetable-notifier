package ru.eqour.timetable.notifier;

public class TelegramNotifier extends SimpleWebRequestNotifier {

    public TelegramNotifier(String token) {
        super(token);
    }

    @Override
    protected String buildSendMessageURL(String token, String recipient, String message) {
        return "https://api.telegram.org/bot/" +
                token +
                "/sendMessage?chat_id=" +
                recipient +
                "&text=" +
                message;
    }
}
