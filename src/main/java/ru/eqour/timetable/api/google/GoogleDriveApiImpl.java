package ru.eqour.timetable.api.google;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import ru.eqour.timetable.api.FileMetadata;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Класс, выполняющий взаимодействие с Google Drive API. Использует авторизацию с помощью OAuth.
 */
public class GoogleDriveApiImpl implements GoogleDriveApi {

    private static final String APPLICATION_NAME = "TimetableNotifier";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    protected static final String CREDENTIALS_FILE_PATH = "credentials.json";
    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    protected final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE_READONLY);
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private Drive drive;

    private Drive driveInstance() throws IOException {
        if (drive == null) {
            drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getHttpRequestInitializer())
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return drive;
    }

    protected HttpRequestInitializer getHttpRequestInitializer() throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(Files.newInputStream(Paths.get(CREDENTIALS_FILE_PATH))));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @Override
    public FileMetadata getFileMetadata(String fileId) {
        try {
            File file = driveInstance().files().get(fileId)
                    .setFields("version,mimeType")
                    .execute();
            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.version = file.getVersion();
            fileMetadata.mimeType = file.getMimeType();
            return fileMetadata;
        } catch (IOException e) {
            throw new GoogleDriveApiException(e);
        }
    }

    @Override
    public ByteArrayOutputStream getFile(String fileId) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            driveInstance().files().get(fileId)
                    .executeMediaAndDownloadTo(outputStream);
            return outputStream;
        } catch (IOException e) {
            throw new GoogleDriveApiException(e);
        }
    }

    @Override
    public ByteArrayOutputStream exportFile(String fileId, String mimeType) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            driveInstance().files().export(fileId, mimeType)
                    .executeMediaAndDownloadTo(outputStream);
            return outputStream;
        } catch (IOException e) {
            throw new GoogleDriveApiException(e);
        }
    }
}
