package ru.eqour.timetable.main;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.TimeBasedUpdater;
import ru.eqour.timetable.validator.SimpleWeekValidator;
import ru.eqour.timetable.validator.WeekValidator;
import ru.eqour.timetable.actualizer.SimpleTimetableActualizer;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.comparer.SimpleWeekComparer;
import ru.eqour.timetable.exception.NotifierException;
import ru.eqour.timetable.model.Notification;
import ru.eqour.timetable.parser.impl.SimpleTimetableParser;
import ru.eqour.timetable.repository.SubscriberRepository;
import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.settings.SettingsManager;
import ru.eqour.timetable.util.factory.FileActualizerFactory;
import ru.eqour.timetable.util.factory.NotifierFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class Application {

    private final Logger LOG = LogManager.getLogger();
    private final TimeBasedUpdater updater;
    private final SimpleTimetableActualizer timetableActualizer;

    public Application(SettingsManager settingsManager, SubscriberRepository subscriberRepository) {
        Settings settings = settingsManager.load();
        FileActualizer actualizer = FileActualizerFactory.create(FileActualizerFactory.FileActualizerType.GOOGLE_DRIVE_SERVICE, settings);
        WeekValidator validator = new SimpleWeekValidator();
        timetableActualizer = new SimpleTimetableActualizer(settingsManager, actualizer, subscriberRepository,
                validator, this::sendNotifications, new SimpleTimetableParser(), new SimpleWeekComparer());
        updater = new TimeBasedUpdater(actualizer::actualize, this::actualize, settings.maxDelayAfterChange);
    }

    public void start() throws InterruptedException {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                updater.update(System.currentTimeMillis());
            } catch (Exception e) {
                LOG.log(Level.ERROR, "An exception occurred while the application was running: " + e.getMessage());
                logStackTrace(e);
            }
            //noinspection BusyWait
            Thread.sleep(1000);
        }
    }

    private void actualize() {
        try {
            timetableActualizer.actualize();
        } catch (Exception e) {
            LOG.log(Level.ERROR, "An exception occurred when actualizing the timetable: " + e.getMessage());
            logStackTrace(e);
        }
    }

    private void sendNotifications(List<Notification> notifications) {
        for (Notification notification : notifications) {
            String recipient = NotifierFactory.getRecipientIdForNotifier(notification.notifier, notification.subscriber);
            try {
                notification.notifier.sendMessage(recipient, (notification.message));
            } catch (NotifierException e) {
                LOG.log(Level.ERROR, "The message could not be sent. Recipient: " + recipient + ". Exception: " + e.getMessage());
                logStackTrace(e);
            }
        }
    }

    private void logStackTrace(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        LOG.log(Level.ERROR, errors);
    }
}
