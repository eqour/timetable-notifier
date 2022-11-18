package ru.eqour.timetable.exception;

public class WeekValidationException extends Exception {

    public WeekValidationException(String message) {
        super(message);
    }

    public WeekValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
