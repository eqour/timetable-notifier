package ru.eqour.timetable.watch.model;

import ru.eqour.timetable.watch.notifier.Notifier;

/**
 * Уведомление.
 */
public class Notification {

    /**
     * Способ отправки уведмоления.
     */
    public final Notifier notifier;

    /**
     * Подписчик на уведомления.
     */
    public final Subscriber subscriber;

    /**
     * Сообщение подписчику.
     */
    public final String message;

    public Notification(Notifier notifier, Subscriber subscriber, String message) {
        this.notifier = notifier;
        this.subscriber = subscriber;
        this.message = message;
    }
}
