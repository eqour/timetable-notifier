package ru.eqour.timetable.watch.main;

import com.google.common.reflect.TypeToken;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.db.MongoDbClient;
import ru.eqour.timetable.watch.actualizer.SimpleTimetableActualizer;
import ru.eqour.timetable.watch.api.FileActualizer;
import ru.eqour.timetable.watch.comparer.PeriodWeekComparer;
import ru.eqour.timetable.watch.model.Week;
import ru.eqour.timetable.watch.parser.impl.UdSUVoTimetableParser;
import ru.eqour.timetable.watch.repository.MongoSubscriberRepository;
import ru.eqour.timetable.watch.repository.SubscriberRepository;
import ru.eqour.timetable.watch.settings.CacheManager;
import ru.eqour.timetable.watch.settings.Settings;
import ru.eqour.timetable.watch.settings.SimpleCacheManager;
import ru.eqour.timetable.watch.util.AppConstants;
import ru.eqour.timetable.watch.util.JsonFileHelper;
import ru.eqour.timetable.watch.util.LogHelper;
import ru.eqour.timetable.watch.util.NotificationHelper;
import ru.eqour.timetable.watch.util.factory.FileActualizerFactory;
import ru.eqour.timetable.watch.util.time.TimeBasedUpdater;
import ru.eqour.timetable.watch.validator.UdSUWeekValidator;
import ru.eqour.timetable.watch.validator.WeekValidator;

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

    public UdSUApplication() {
        settings = JsonFileHelper.loadFromFile(AppConstants.SETTINGS_PATH.toString(), Settings.class);
        CacheManager<List<Week>> cacheManager = new SimpleCacheManager<>(AppConstants.TIMETABLE_CACHE_PATH.toString(),
                new TypeToken<List<Week>>(){}.getType());
        FileActualizer actualizer = FileActualizerFactory.create(FileActualizerFactory.FileActualizerType.GOOGLE_DRIVE_SERVICE, settings);
        WeekValidator validator = new UdSUWeekValidator();
        UdSUVoTimetableParser timetableParser = new UdSUVoTimetableParser(settings.parsingPeriod, () -> currentDate);
        SubscriberRepository subscriberRepository = new MongoSubscriberRepository(new MongoDbClient(settings.dbConnection));
        timetableActualizer = new SimpleTimetableActualizer(cacheManager, actualizer, subscriberRepository,
                settings, validator, NotificationHelper::sendNotifications, timetableParser,
                new PeriodWeekComparer(settings.parsingPeriod, () -> currentDate));
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
                LogHelper.logStackTrace(e);
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
            LogHelper.logStackTrace(e);
        }
    }
}
