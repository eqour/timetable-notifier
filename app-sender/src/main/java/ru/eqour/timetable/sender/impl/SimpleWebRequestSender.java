package ru.eqour.timetable.sender.impl;

import ru.eqour.timetable.sender.MessageSender;
import ru.eqour.timetable.sender.exception.SendMessageException;
import ru.eqour.timetable.sender.model.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Абстрактный класс, выполняющий отправку сообщений при помощи отправки web запроса на определённый ресурс.
 */
public abstract class SimpleWebRequestSender implements MessageSender {

    private final String token;

    /**
     * Создаёт новый экземпляр класса {@code SimpleWebRequestSender}.
     *
     * @param token секретный ключ, использующийся для отправки сообщений.
     */
    public SimpleWebRequestSender(String token) {
        this.token = token;
    }

    @Override
    public void sendMessage(String recipient, Message message) throws SendMessageException {
        try {
            String encodedMessage = URLEncoder.encode(message.getText(), "UTF-8");
            sendWebRequest(buildSendMessageURL(token, recipient, encodedMessage));
        } catch (Exception e) {
            throw new SendMessageException(e);
        }
    }

    private void sendWebRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.getInputStream();
        connection.disconnect();
    }

    /**
     * Выполняет построение url для отправки запроса.
     *
     * @param token секретный ключ, использующийся для отправки сообщения.
     * @param recipient строка, идентифицирующая получателя сообщения.
     * @param message сообщение получателю.
     * @return url адрес.
     */
    protected abstract String buildSendMessageURL(String token, String recipient, String message);
}
