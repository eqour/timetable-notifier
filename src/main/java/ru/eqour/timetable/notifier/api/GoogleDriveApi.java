package ru.eqour.timetable.notifier.api;

import java.io.ByteArrayOutputStream;

public interface GoogleDriveApi {

    FileMetadata getFileMetadata(String fileId);
    ByteArrayOutputStream exportFile(String fileId, String mimeType);
}
