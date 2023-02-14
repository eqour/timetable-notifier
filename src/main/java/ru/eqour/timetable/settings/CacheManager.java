package ru.eqour.timetable.settings;

/**
 * Описывает интерфейс кэширования объекта.
 *
 * @param <T> кэшируемый объект.
 */
public interface CacheManager<T> {

    /**
     * Сохраняет объект в кэш.
     *
     * @param value объект, который необходимо сохранить.
     */
    void save(T value);

    /**
     * Загружает объект из кэша.
     *
     * @return объект, который необходимо загрузить.
     */
    T load();
}
