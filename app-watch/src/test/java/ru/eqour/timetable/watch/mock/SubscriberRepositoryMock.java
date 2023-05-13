package ru.eqour.timetable.watch.mock;

import ru.eqour.timetable.watch.exception.RepositoryException;
import ru.eqour.timetable.watch.model.Subscriber;
import ru.eqour.timetable.watch.repository.SubscriberRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SubscriberRepositoryMock implements SubscriberRepository {

    private final Map<String, List<Subscriber>> groupSubscriberMap, teacherSubscriberMap;
    private final boolean throwException;

    public SubscriberRepositoryMock(Map<String, List<Subscriber>> groupSubscriberMap,
                                    Map<String, List<Subscriber>> teacherSubscriberMap,
                                    boolean throwException) {
        this.groupSubscriberMap = groupSubscriberMap;
        this.teacherSubscriberMap = teacherSubscriberMap;
        this.throwException = throwException;
    }

    @Override
    public List<Subscriber> getSubscribersByStudentGroup(String groupName) {
        return getSubscriberMap(groupSubscriberMap, groupName);
    }

    @Override
    public List<Subscriber> getSubscribersByTeacher(String teacherName) {
        return getSubscriberMap(teacherSubscriberMap, teacherName);
    }

    private List<Subscriber> getSubscriberMap(Map<String, List<Subscriber>> map, String key) {
        if (throwException) {
            throw new RepositoryException();
        } else {
            return map.getOrDefault(key, Collections.emptyList());
        }
    }
}
