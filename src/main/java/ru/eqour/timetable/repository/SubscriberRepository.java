package ru.eqour.timetable.repository;

import ru.eqour.timetable.model.Subscriber;

import java.util.List;

/**
 * Обеспечивает доступ к данным о подписчиках на изменения в расписании.
 */
public interface SubscriberRepository {

    /**
     * Возвращает список подписчиков по названию группы.
     *
     * @param groupName название группы.
     * @return список подписчиков.
     * @throws ru.eqour.timetable.exception.RepositoryException в случае, если возникла ошибка при чтении данных.
     */
    List<Subscriber> getSubscribers(String groupName);
}
