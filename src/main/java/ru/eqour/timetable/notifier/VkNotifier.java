package ru.eqour.timetable.notifier;

public class VkNotifier extends SimpleWebRequestNotifier {

    public VkNotifier(String token) {
        super(token);
    }

    @Override
    protected String buildSendMessageURL(String token, String recipient, String message) {
        return "https://api.vk.com/method/messages.send?v=5.131&user_id=" +
                recipient +
                "&message=" +
                message +
                "&access_token=" +
                token;
    }
}
