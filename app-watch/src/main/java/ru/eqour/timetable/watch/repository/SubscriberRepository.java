package ru.eqour.timetable.watch.repository;

import ru.eqour.timetable.watch.model.Subscriber;
import ru.eqour.timetable.watch.exception.RepositoryException;

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
     * @throws RepositoryException в случае, если возникла ошибка при чтении данных.
     */
    List<Subscriber> getSubscribersByStudentGroup(String groupName);

    /**
     * Возвращает список подписчиков по преподавателю.
     *
     * @param teacherName преподаватель.
     * @return список подписчиков.
     * @throws RepositoryException в случае, если возникла ошибка при чтении данных.
     */
    List<Subscriber> getSubscribersByTeacher(String teacherName);
}
