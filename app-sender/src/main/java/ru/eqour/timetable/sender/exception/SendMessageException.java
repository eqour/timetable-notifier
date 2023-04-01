package ru.eqour.timetable.sender.exception;

/**
 * Исключение используется, когда происходит ошибка во время отправки сообщения.
 */
public class SendMessageException extends Exception {

    public SendMessageException(Throwable cause) {
        super(cause);
    }
}
