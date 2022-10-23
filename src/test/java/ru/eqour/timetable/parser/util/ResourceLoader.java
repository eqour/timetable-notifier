package ru.eqour.timetable.parser.util;

import com.google.gson.Gson;
import ru.eqour.timetable.model.Week;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceLoader {

    public static Week loadWeekFromResources(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Gson gson = new Gson();
        try {
            URL resource = classLoader.getResource(path);
            if (resource != null) {
                String json = new String(Files.readAllBytes(Paths.get(resource.toURI())));
                return gson.fromJson(json, Week.class);
            } else {
                throw new RuntimeException();
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
