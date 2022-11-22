package ru.eqour.timetable.repository;

import ru.eqour.timetable.model.Subscriber;

import java.util.List;

public interface SubscriberRepository {

    List<Subscriber> getSubscribers(String groupName);
}
