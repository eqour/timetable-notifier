package ru.eqour.timetable.repository;

import com.google.gson.reflect.TypeToken;
import ru.eqour.timetable.exception.RepositoryException;
import ru.eqour.timetable.model.Subscriber;
import ru.eqour.timetable.util.JsonFileHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SimpleSubscriberRepository implements SubscriberRepository {

    private final String path;

    public SimpleSubscriberRepository(String path) {
        this.path = path;
    }

    public Map<String, List<Subscriber>> loadSubscriberMap() {
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
