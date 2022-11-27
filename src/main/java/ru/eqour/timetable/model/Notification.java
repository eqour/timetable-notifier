package ru.eqour.timetable.model;

import ru.eqour.timetable.notifier.Notifier;

public class Notification {

    public final Notifier notifier;
    public final Subscriber subscriber;
    public final String message;

    public Notification(Notifier notifier, Subscriber subscriber, String message) {
        this.notifier = notifier;
        this.subscriber = subscriber;
        this.message = message;
    }
}
