package ru.eqour.timetable.notifier;

/**
 * Класс для отправки сообщений.
 */
public interface Notifier {

    /**
     * Отправляет сообщение получателю.
     *
     * @param recipient строка, идентифицирующая получателя сообщения.
     * @param message сообщение получателю.
     * @throws ru.eqour.timetable.exception.NotifierException в случае, если произошла ошибка при отправке сообщения.
     */
    void sendMessage(String recipient, String message);
}
