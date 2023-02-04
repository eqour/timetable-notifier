package ru.eqour.timetable.api.google;

import ru.eqour.timetable.api.FileMetadata;

import java.io.ByteArrayOutputStream;

/**
 * Описывает взаимодействие с Google Drive API.
 */
public interface GoogleDriveApi {

    /**
     * Возвращает метаданные файла по его идентификатору.
     *
     * @param fileId идентификатор файла в Google Drive.
     * @return метаданные файла, представленные классом {@code FileMetadata}.
     */
    FileMetadata getFileMetadata(String fileId) throws GoogleDriveApiException;

    /**
     * Загружает файл электронной таблицы (Excel).
     * Для загрузки не подходят файлы с MIME типом {@link FileMimeType#GOOGLE_SPREADSHEET}.
     *
     * @param fileId идентификатор файла в Google Drive.
     * @return поток байт, представляющий файл в бинарном виде.
     */
    ByteArrayOutputStream getFile(String fileId);

    /**
     * Экспортирует файл {@link FileMimeType#GOOGLE_SPREADSHEET} в указанном MIME типе.
     *
     * @param fileId идентификатор файла в Google Drive.
     * @param mimeType MIME тип экспортируемого файла.
     * @return поток байт, представляющий файл в бинарном виде.
     */
    ByteArrayOutputStream exportFile(String fileId, String mimeType);
}
