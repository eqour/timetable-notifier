package ru.eqour.timetable.mock;

import ru.eqour.timetable.exception.RepositoryException;
import ru.eqour.timetable.model.Subscriber;
import ru.eqour.timetable.repository.SubscriberRepository;

import java.util.List;
import java.util.Map;

public class SubscriberRepositoryMock implements SubscriberRepository {

    private final Map<String, List<Subscriber>> subscriberMap;
    private final boolean throwException;

    public SubscriberRepositoryMock(Map<String, List<Subscriber>> subscriberMap, boolean throwException) {
        this.subscriberMap = subscriberMap;
        this.throwException = throwException;
    }

    @Override
    public List<Subscriber> getSubscribers(String groupName) {
        if (throwException) {
            throw new RepositoryException();
        } else {
            return subscriberMap.get(groupName);
        }
    }
}
