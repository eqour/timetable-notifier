package ru.eqour.timetable.util.factory;

import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.api.google.GoogleDriveApiImpl;
import ru.eqour.timetable.api.google.GoogleDriveExcelFileActualizer;

public class FileActualizerFactory {

    public enum FileActualizerType {
        GOOGLE_DRIVE
    }

    public static FileActualizer create(FileActualizerType type, Settings settings) {
        if (type == FileActualizerType.GOOGLE_DRIVE) {
            return new GoogleDriveExcelFileActualizer(settings.timetableFileId, new GoogleDriveApiImpl());
        }
        throw new IllegalArgumentException();
    }
}
