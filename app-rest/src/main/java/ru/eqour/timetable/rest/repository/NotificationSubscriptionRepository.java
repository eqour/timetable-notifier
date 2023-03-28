package ru.eqour.timetable.rest.repository;

import java.util.List;

public interface NotificationSubscriptionRepository {

    List<String> findAllGroupSubscriptions();
    List<String> findAllTeacherSubscriptions();
}
