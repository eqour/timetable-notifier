package ru.eqour.timetable.sender;

import ru.eqour.timetable.sender.exception.SendMessageException;
import ru.eqour.timetable.sender.model.Message;

/**
 * Интерфейс, описывающий отправитель сообщений.
 */
public interface MessageSender {

    /**
     * Отправляет сообщение получателю.
     *
     * @param recipient получатель сообщения.
     * @param message сообщение.
     * @throws SendMessageException в случае, если произошла ошибка при отправке сообщения.
     */
    void sendMessage(String recipient, Message message) throws SendMessageException;
}
