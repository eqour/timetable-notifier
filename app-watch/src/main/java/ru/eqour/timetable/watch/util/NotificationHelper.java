package ru.eqour.timetable.watch.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.sender.exception.SendMessageException;
import ru.eqour.timetable.watch.model.Notification;
import ru.eqour.timetable.watch.util.factory.NotifierFactory;

import java.util.List;

public class NotificationHelper {

    private static final Logger LOG = LogManager.getLogger();

    public static void sendNotifications(List<Notification> notifications) {
        for (Notification notification : notifications) {
            String recipient = NotifierFactory.getRecipientIdForNotifier(notification.notifier, notification.subscriber);
            try {
                notification.notifier.sendMessage(recipient, notification.message);
            } catch (SendMessageException e) {
                LOG.log(Level.ERROR, "The message could not be sent. Recipient: " + recipient + ". Exception: " + e.getMessage());
                LogHelper.logStackTrace(e);
            }
        }
    }
}
