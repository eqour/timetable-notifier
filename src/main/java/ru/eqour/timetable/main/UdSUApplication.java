package ru.eqour.timetable.main;

import com.google.common.reflect.TypeToken;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.comparer.PeriodWeekComparer;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.settings.CacheManager;
import ru.eqour.timetable.settings.SimpleCacheManager;
import ru.eqour.timetable.util.AppConstants;
import ru.eqour.timetable.util.JsonFileHelper;
import ru.eqour.timetable.util.time.TimeBasedUpdater;
import ru.eqour.timetable.parser.impl.UdSUVoTimetableParser;
import ru.eqour.timetable.validator.UdSUWeekValidator;
import ru.eqour.timetable.validator.WeekValidator;
import ru.eqour.timetable.actualizer.SimpleTimetableActualizer;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.exception.NotifierException;
import ru.eqour.timetable.model.Notification;
import ru.eqour.timetable.repository.SubscriberRepository;
import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.util.factory.FileActualizerFactory;
import ru.eqour.timetable.util.factory.NotifierFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class UdSUApplication {

    private final Logger LOG = LogManager.getLogger();
    private final TimeBasedUpdater updater;
    private final SimpleTimetableActualizer timetableActualizer;
    private final Settings settings;

    private LocalDate currentDate;

    public UdSUApplication(SubscriberRepository subscriberRepository) {
        settings = JsonFileHelper.loadFromFile(AppConstants.SETTINGS_PATH.toString(), Settings.class);
        CacheManager<List<Week>> cacheManager = new SimpleCacheManager<>(AppConstants.TIMETABLE_CACHE_PATH.toString(),
                new TypeToken<List<Week>>(){}.getType());
        FileActualizer actualizer = FileActualizerFactory.create(FileActualizerFactory.FileActualizerType.GOOGLE_DRIVE_SERVICE, settings);
        WeekValidator validator = new UdSUWeekValidator();
        UdSUVoTimetableParser timetableParser = new UdSUVoTimetableParser(settings.parsingPeriod, () -> currentDate);
        timetableActualizer = new SimpleTimetableActualizer(cacheManager, actualizer, subscriberRepository,
                settings, validator, this::sendNotifications, timetableParser, new PeriodWeekComparer(settings.parsingPeriod,
                () -> currentDate));
        updater = new TimeBasedUpdater(actualizer::actualize, this::actualize, settings.maxDelayAfterChange);
    }

    public void start() throws InterruptedException {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                currentDate = OffsetDateTime.now(ZoneOffset.ofHours(settings.zoneOffset)).toLocalDate();
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
