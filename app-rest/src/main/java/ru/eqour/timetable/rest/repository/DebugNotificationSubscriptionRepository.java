package ru.eqour.timetable.rest.repository;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DebugNotificationSubscriptionRepository implements NotificationSubscriptionRepository {

    private static final List<String> groups = Arrays.asList(
            "ПИ-1", "ПИ-2", "ПИ-3", "ПИ-4"
    );

    private static final List<String> teachers = Arrays.asList(
            "Раскин П.Н.", "Мамрыкин О.В.", "Ощепкова О.П.", "Ардашева Г.Н."
    );

    @Override
    public List<String> findAllGroupSubscriptions() {
        return groups;
    }

    @Override
    public List<String> findAllTeacherSubscriptions() {
        return teachers;
    }
}
