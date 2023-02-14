package ru.eqour.timetable.watch.mock;

import ru.eqour.timetable.watch.exception.RepositoryException;
import ru.eqour.timetable.watch.model.Subscriber;
import ru.eqour.timetable.watch.repository.SubscriberRepository;

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
