package ru.eqour.timetable.watch.api.google;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Класс, выполняющий взаимодействие с Google Drive API.
 * Использует авторизацию с помощью сервисного аккаунта Google Cloud.
 */
public class GoogleDriveServiceApiImpl extends GoogleDriveApiImpl {

    @Override
    protected HttpRequestInitializer getHttpRequestInitializer() throws IOException {
        InputStream inputStream = Files.newInputStream(Paths.get(CREDENTIALS_FILE_PATH));
        return new HttpCredentialsAdapter(ServiceAccountCredentials.fromStream(inputStream).createScoped(SCOPES));
    }
}
