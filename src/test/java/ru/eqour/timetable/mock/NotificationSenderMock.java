package ru.eqour.timetable.mock;

import ru.eqour.timetable.model.Notification;

import java.util.List;

public class NotificationSenderMock {

    private final boolean isIncorrectSend;
    private int sendNotificationsCalls;
    private List<Notification> notifications;

    public NotificationSenderMock(boolean isIncorrectSend) {
        this.isIncorrectSend = isIncorrectSend;
        sendNotificationsCalls = 0;
    }

    public void sendNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        sendNotificationsCalls++;
        if (isIncorrectSend) {
            throw new RuntimeException();
        }
    }

    public int getSendNotificationsCalls() {
        return sendNotificationsCalls;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
