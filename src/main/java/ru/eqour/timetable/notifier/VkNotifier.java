package ru.eqour.timetable.notifier;

import java.util.Random;

public class VkNotifier extends SimpleWebRequestNotifier {

    private final Random random;

    public VkNotifier(String token) {
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
