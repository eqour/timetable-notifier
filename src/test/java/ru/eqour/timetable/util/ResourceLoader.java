package ru.eqour.timetable.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ru.eqour.timetable.model.Day;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceLoader {
    private static final Gson GSON = new Gson();

    public static <T> T loadFromResources(String path, Class<T> type) {
        try {
            URL resource = ResourceLoader.class.getResource(path);
            if (resource != null) {
                String json = new String(Files.readAllBytes(Paths.get(resource.toURI())));
                return GSON.fromJson(json, type);
            } else {
                throw new RuntimeException();
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, List<Day[]>> loadWeeksDifferencesFromResources(String path) {
        try {
            URL resource = ResourceLoader.class.getResource(path);
            if (resource != null) {
                String json = new String(Files.readAllBytes(Paths.get(resource.toURI())));
                return GSON.fromJson(json, new TypeToken<Map<String, List<Day[]>>>(){}.getType());
            } else {
                throw new RuntimeException();
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFullPathToResource(String path) {
        return Objects.requireNonNull(ResourceLoader.class.getResource(path)).getPath();
    }
}
