package ru.eqour.timetable.watch.exception;

/**
 * Исключение используется, когда неделя не проходит валидацию.
 */
public class WeekValidationException extends Exception {

    public WeekValidationException() {
    }

    public WeekValidationException(String message) {
        super(message);
    }

    public WeekValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
