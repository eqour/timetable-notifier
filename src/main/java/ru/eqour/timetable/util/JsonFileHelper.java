package ru.eqour.timetable.util;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

/**
 * Содержит методы для обработки json файлов.
 */
public class JsonFileHelper {

    private static final Gson GSON = new Gson().newBuilder().setPrettyPrinting().create();

    /**
     * Десериализует объект из json файла.
     *
     * @param path путь к json файлу.
     * @param type класс десериализуемого объекта.
     * @return десериализуемый объект.
     * @param <T> объект десериализации.
     */
    public static <T> T loadFromFile(String path, Class<T> type) {
        return loadFromFile(path, json -> GSON.fromJson(json, type));
    }

    /**
     * Десериализует объект из json файла.
     *
     * @param path путь к json файлу.
     * @param type тип десериализуемого объекта.
     * @return десериализуемый объект.
     * @param <T> тип объекта десериализации.
     */
    public static <T> T loadFromFile(String path, Type type) {
        return loadFromFile(path, json -> GSON.fromJson(json, type));
    }

    private static <T> T loadFromFile(String path, Function<String, T> function) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(path)));
            return function.apply(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Сериализует объект в json файл.
     *
     * @param path путь к json файлу.
     * @param value сериализуемый объект.
     * @param <T> тип объекта сериализации.
     */
    public static <T> void saveToFile(String path, T value) {
        String json = GSON.toJson(value);
        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            writer.write(json);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
