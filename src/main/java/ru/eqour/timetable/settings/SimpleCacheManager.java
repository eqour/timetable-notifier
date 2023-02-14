package ru.eqour.timetable.settings;

import ru.eqour.timetable.util.JsonFileHelper;

import java.lang.reflect.Type;

/**
 * Позволяет сохранять объект в файл и загружать его из файла.
 *
 * @param <T> кэшируемый объект.
 */
public class SimpleCacheManager<T> implements CacheManager<T> {

    private final String settingsPath;
    private final Class<T> classType;
    private final Type type;

    /**
     * Создаёт новый экземпляр класса {@code SimpleCacheManager}.
     *
     * @param path путь к файлу для кэширования данных.
     * @param type тип класса для кэширования.
     */
    public SimpleCacheManager(String path, Class<T> type) {
        this.settingsPath = path;
        this.classType = type;
        this.type = null;
    }

    /**
     * Создаёт новый экземпляр класса {@code SimpleCacheManager}.
     *
     * @param path путь к файлу для кэширования данных.
     * @param type тип класса для кэширования.
     */
    public SimpleCacheManager(String path, Type type) {
        this.settingsPath = path;
        this.type = type;
        this.classType = null;
    }

    @Override
    public void save(T value) {
        JsonFileHelper.saveToFile(settingsPath, value);
    }

    @Override
    public T load() {
        try {
            if (type == null) {
                return JsonFileHelper.loadFromFile(settingsPath, classType);
            } else {
                return JsonFileHelper.loadFromFile(settingsPath, type);
            }
        } catch (Exception e) {
            return null;
        }
    }
}
