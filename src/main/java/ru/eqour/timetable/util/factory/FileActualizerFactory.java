package ru.eqour.timetable.util.factory;

import ru.eqour.timetable.api.google.GoogleDriveServiceApiImpl;
import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.api.google.GoogleDriveApiImpl;
import ru.eqour.timetable.api.google.GoogleDriveExcelFileActualizer;

public class FileActualizerFactory {

    public enum FileActualizerType {
        GOOGLE_DRIVE, GOOGLE_DRIVE_SERVICE
    }

    public static FileActualizer create(FileActualizerType type, Settings settings) {
        switch (type) {
            case GOOGLE_DRIVE: return new GoogleDriveExcelFileActualizer(settings.timetableFileId, new GoogleDriveApiImpl());
            case GOOGLE_DRIVE_SERVICE: return new GoogleDriveExcelFileActualizer(settings.timetableFileId, new GoogleDriveServiceApiImpl());
            default: throw new IllegalArgumentException();
        }
    }
}
