package ru.eqour.timetable.api.google;

import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.api.FileMetadata;

import java.util.Objects;

/**
 * Актуализатор файлов электронных таблиц. Позволяет узнать и получить актуальную версию файла таблицы.
 */
public class GoogleDriveExcelFileActualizer implements FileActualizer {

    private final String fileId;
    private final GoogleDriveApi api;

    private long lastActualVersion;
    private byte[] file;

    /**
     * Создаёт новый экземпляр класса {@code GoogleDriveExcelFileActualizer}.
     *
     * @param fileId идентификатор файла таблицы в Google Drive, актуальную версию которого необходимо отслеживать.
     * @param api API для взаимодействия с Google Drive.
     */
    public GoogleDriveExcelFileActualizer(String fileId, GoogleDriveApi api) {
        this(fileId, api, 0);
    }

    /**
     * Создаёт новый экземпляр класса {@code GoogleDriveExcelFileActualizer}
     * с указанием последней актуальной версии файла таблицы.
     *
     * @param fileId идентификатор файла таблицы в Google Drive, актуальную версию которого необходимо отслеживать.
     * @param api API для взаимодействия с Google Drive.
     * @param lastActualVersion последняя актуальная версия файла таблицы.
     */
    public GoogleDriveExcelFileActualizer(String fileId, GoogleDriveApi api, long lastActualVersion) {
        this.fileId = fileId;
        this.api = api;
        this.lastActualVersion = lastActualVersion;
    }

    @Override
    public boolean actualize() {
        FileMetadata metadata = api.getFileMetadata(fileId);
        if (lastActualVersion != metadata.version) {
            lastActualVersion = metadata.version;
            file = Objects.equals(metadata.mimeType, FileMimeType.GOOGLE_SPREADSHEET.value)
                    ? api.exportFile(fileId, FileMimeType.OFFICE_SPREADSHEET.value).toByteArray()
                    : api.getFile(fileId).toByteArray();
            return true;
        }
        return false;
    }

    @Override
    public byte[] getActualFile() {
        return file;
    }
}
