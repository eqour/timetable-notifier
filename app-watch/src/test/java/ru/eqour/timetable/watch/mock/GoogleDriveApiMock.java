package ru.eqour.timetable.watch.mock;

import ru.eqour.timetable.watch.api.FileMetadata;
import ru.eqour.timetable.watch.api.google.FileMimeType;
import ru.eqour.timetable.watch.api.google.GoogleDriveApi;
import ru.eqour.timetable.watch.api.google.GoogleDriveApiException;

import java.io.*;
import java.util.Objects;

public class GoogleDriveApiMock implements GoogleDriveApi {

    public static final String WORKSPACE_EXCEL_FILE_ID = "google-excel";
    public static final String MICROSOFT_EXCEL_FILE_ID = "microsoft-excel";

    private long version;

    public GoogleDriveApiMock withVersion(long version) {
        this.version = version;
        return this;
    }

    @Override
    public FileMetadata getFileMetadata(String fileId) {
        FileMetadata metadata = new FileMetadata();
        metadata.version = version;
        metadata.mimeType = getMimeType(fileId);
        return metadata;
    }

    @Override
    public ByteArrayOutputStream getFile(String fileId) {
        if (isGoogleWorkspaceDocument(fileId)) {
            throw new GoogleDriveApiException();
        } else {
            return new ByteArrayOutputStream();
        }
    }

    @Override
    public ByteArrayOutputStream exportFile(String fileId, String mimeType) {
        if (isGoogleWorkspaceDocument(fileId)) {
            return new ByteArrayOutputStream();
        } else {
            throw new GoogleDriveApiException();
        }
    }

    private boolean isGoogleWorkspaceDocument(String fileID) {
        return Objects.equals(fileID, WORKSPACE_EXCEL_FILE_ID);
    }

    private String getMimeType(String fileId) {
        switch (fileId) {
            case WORKSPACE_EXCEL_FILE_ID: return FileMimeType.GOOGLE_SPREADSHEET.value;
            case MICROSOFT_EXCEL_FILE_ID: return FileMimeType.OFFICE_SPREADSHEET.value;
            default: throw new IllegalArgumentException();
        }
    }
}
