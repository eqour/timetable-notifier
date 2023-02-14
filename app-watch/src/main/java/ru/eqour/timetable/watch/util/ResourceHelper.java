package ru.eqour.timetable.watch.util;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Содержит методы для доступа к ресурсам.
 */
public class ResourceHelper {

    /**
     * Возвращает полный путь к файлу.
     *
     * @param path путь к файлу относительно папки ресурсов.
     * @return путь к файлу.
     */
    public static Path getFullPathToResource(String path) {
        try {
            return Paths.get(Objects.requireNonNull(JsonFileHelper.class.getResource(path)).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
