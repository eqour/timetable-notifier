package ru.eqour.timetable.watch.exception;

/**
 * Исключение используется, когда происходит ошибка во время отправки уведомлений.
 */
public class NotifierException extends RuntimeException {

    public NotifierException() {
    }

    public NotifierException(Throwable cause) {
        super(cause);
    }
}
