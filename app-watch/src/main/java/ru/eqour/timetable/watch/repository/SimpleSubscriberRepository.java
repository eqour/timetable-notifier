package ru.eqour.timetable.watch.repository;

import com.google.gson.reflect.TypeToken;
import ru.eqour.timetable.watch.exception.RepositoryException;
import ru.eqour.timetable.watch.model.Subscriber;
import ru.eqour.timetable.watch.util.JsonFileHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Обеспечивает доступ к данным о подписчиках на изменения в расписании.
 * Данные хранятся в файле по указанному пути.
 */
public class SimpleSubscriberRepository implements SubscriberRepository {

    private static final String GROUP_KEY = "group";
    private static final String TEACHER_KEY = "teacher";

    private final String path;

    /**
     * Создаёт новый экземпляр класса {@code SimpleSubscriberRepository}.
     *
     * @param path путь к файлу с данными о подписчиках в json формате.
     */
    public SimpleSubscriberRepository(String path) {
        this.path = path;
    }

    private Map<String, Map<String, List<Subscriber>>> loadSubscriberMap() {
        try {
            return JsonFileHelper.loadFromFile(path, new TypeToken<Map<String, Map<String, List<Subscriber>>>>(){}.getType());
        } catch (Exception e) {
            throw new RepositoryException();
        }
    }

    @Override
    public List<Subscriber> getSubscribersByStudentGroup(String groupName) {
        Map<String, Map<String, List<Subscriber>>> subscriberMap = loadSubscriberMap();
        if (!subscriberMap.containsKey(GROUP_KEY)) return Collections.emptyList();
        return subscriberMap.get(GROUP_KEY).getOrDefault(groupName, Collections.emptyList());
    }

    @Override
    public List<Subscriber> getSubscribersByTeacher(String teacherName) {
        Map<String, Map<String, List<Subscriber>>> subscriberMap = loadSubscriberMap();
        if (!subscriberMap.containsKey(TEACHER_KEY)) return Collections.emptyList();
        return subscriberMap.get(TEACHER_KEY).getOrDefault(teacherName, Collections.emptyList());
    }
}
