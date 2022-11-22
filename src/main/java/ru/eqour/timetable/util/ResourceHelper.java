package ru.eqour.timetable.util;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ResourceHelper {

    public static Path getFullPathToResource(String path) {
        try {
            return Paths.get(Objects.requireNonNull(JsonFileHelper.class.getResource(path)).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
