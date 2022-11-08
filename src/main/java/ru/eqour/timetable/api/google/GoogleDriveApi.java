package ru.eqour.timetable.api.google;

import ru.eqour.timetable.api.FileMetadata;

import java.io.ByteArrayOutputStream;

public interface GoogleDriveApi {

    FileMetadata getFileMetadata(String fileId);
    ByteArrayOutputStream getFile(String fileId);
    ByteArrayOutputStream exportFile(String fileId, String mimeType);
}
