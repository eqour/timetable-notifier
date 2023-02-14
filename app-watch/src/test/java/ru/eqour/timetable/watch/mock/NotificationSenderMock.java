package ru.eqour.timetable.watch.mock;

import ru.eqour.timetable.watch.exception.NotifierException;
import ru.eqour.timetable.watch.model.Notification;

import java.util.List;

public class NotificationSenderMock {

    private final boolean isIncorrectSend;
    private int sendNotificationsCalls;

    public NotificationSenderMock(boolean isIncorrectSend) {
        this.isIncorrectSend = isIncorrectSend;
        sendNotificationsCalls = 0;
    }

    public void sendNotifications(List<Notification> ignoredNotifications) {
        sendNotificationsCalls++;
        if (isIncorrectSend) {
            throw new NotifierException();
        }
    }

    public int getSendNotificationsCalls() {
        return sendNotificationsCalls;
    }
}
