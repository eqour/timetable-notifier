package ru.eqour.timetable.api.google;

/**
 * Возникает, если произошла ошибка при работе с Google Drive API.
 */
public class GoogleDriveApiException extends RuntimeException {

    public GoogleDriveApiException() {
    }

    public GoogleDriveApiException(Throwable cause) {
        super(cause);
    }
}
