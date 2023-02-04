package ru.eqour.timetable.util.factory;

import ru.eqour.timetable.api.google.GoogleDriveServiceApiImpl;
import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.api.google.GoogleDriveApiImpl;
import ru.eqour.timetable.api.google.GoogleDriveExcelFileActualizer;

/**
 * Фабрика актуализаторов файлов.
 */
public class FileActualizerFactory {

    public enum FileActualizerType {

        /**
         * Актуализатор файлов Google Drive с авторизацией OAuth.
         */
        GOOGLE_DRIVE,

        /**
         * Актуализатор файлов Google Drive с авторизацией серивсного аккаунта.
         */
        GOOGLE_DRIVE_SERVICE
    }

    /**
     * Создаёт новый экземпляр интерфейса {@code FileActualizer}.
     *
     * @param type тип актуализатора.
     * @param settings настройки приложения.
     * @return актуализатор файлов.
     */
    public static FileActualizer create(FileActualizerType type, Settings settings) {
        switch (type) {
            case GOOGLE_DRIVE: return new GoogleDriveExcelFileActualizer(settings.timetableFileId, new GoogleDriveApiImpl());
            case GOOGLE_DRIVE_SERVICE: return new GoogleDriveExcelFileActualizer(settings.timetableFileId, new GoogleDriveServiceApiImpl());
            default: throw new IllegalArgumentException();
        }
    }
}
