package ru.eqour.timetable.watch.model;

import ru.eqour.timetable.sender.MessageSender;
import ru.eqour.timetable.sender.model.Message;

/**
 * Уведомление.
 */
public class Notification {

    /**
     * Способ отправки уведмоления.
     */
    public final MessageSender notifier;

    /**
     * Подписчик на уведомления.
     */
    public final Subscriber subscriber;

    /**
     * Сообщение подписчику.
     */
    public final Message message;

    public Notification(MessageSender notifier, Subscriber subscriber, Message message) {
        this.notifier = notifier;
        this.subscriber = subscriber;
        this.message = message;
    }
}
