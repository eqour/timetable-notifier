package ru.eqour.timetable.watch.notifier;

import ru.eqour.timetable.watch.exception.NotifierException;

/**
 * Класс для отправки сообщений.
 */
public interface Notifier {

    /**
     * Отправляет сообщение получателю.
     *
     * @param recipient строка, идентифицирующая получателя сообщения.
     * @param message сообщение получателю.
     * @throws NotifierException в случае, если произошла ошибка при отправке сообщения.
     */
    void sendMessage(String recipient, String message);
}
