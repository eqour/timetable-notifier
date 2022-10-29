package ru.eqour.timetable.notifier.api.google;

import ru.eqour.timetable.notifier.api.FileMetadata;

import java.io.ByteArrayOutputStream;

public interface GoogleDriveApi {

    FileMetadata getFileMetadata(String fileId);
    ByteArrayOutputStream getFile(String fileId);
    ByteArrayOutputStream exportFile(String fileId, String mimeType);
}
