package ru.eqour.timetable.repository;

import com.google.gson.reflect.TypeToken;
import ru.eqour.timetable.exception.RepositoryException;
import ru.eqour.timetable.model.Subscriber;
import ru.eqour.timetable.util.JsonFileHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Обеспечивает доступ к данным о подписчиках на изменения в расписании.
 * Данные хранятся в файле по указанному пути.
 */
public class SimpleSubscriberRepository implements SubscriberRepository {

    private final String path;

    /**
     * Создаёт новый экземпляр класса {@code SimpleSubscriberRepository}.
     *
     * @param path путь к файлу с данными о подписчиках в json формате.
     */
    public SimpleSubscriberRepository(String path) {
        this.path = path;
    }

    private Map<String, List<Subscriber>> loadSubscriberMap() {
        try {
            return JsonFileHelper.loadFromFile(path, new TypeToken<Map<String, List<Subscriber>>>(){}.getType());
        } catch (Exception e) {
            throw new RepositoryException();
        }
    }

    @Override
    public List<Subscriber> getSubscribers(String groupName) {
        Map<String, List<Subscriber>> subscriberMap = loadSubscriberMap();
        return subscriberMap.getOrDefault(groupName, Collections.emptyList());
    }
}
