package ru.eqour.timetable.notifier;

import ru.eqour.timetable.exception.NotifierException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public abstract class SimpleWebRequestNotifier implements Notifier {

    private final String token;

    public SimpleWebRequestNotifier(String token) {
        this.token = token;
    }

    @Override
    public void sendMessage(String recipient, String message) {
        try {
            String encodedMessage = URLEncoder.encode(message, "UTF-8");
            sendWebRequest(buildSendMessageURL(token, recipient, encodedMessage));
        } catch (Exception e) {
            throw new NotifierException(e);
        }
    }

    private void sendWebRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.getInputStream();
        connection.disconnect();
    }

    protected abstract String buildSendMessageURL(String token, String recipient, String message);
}
