package ru.eqour.timetable.watch.main;

import com.google.common.reflect.TypeToken;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.watch.actualizer.SimpleTimetableActualizer;
import ru.eqour.timetable.watch.api.FileActualizer;
import ru.eqour.timetable.watch.comparer.SimpleWeekComparer;
import ru.eqour.timetable.watch.model.Week;
import ru.eqour.timetable.watch.parser.impl.SimpleTimetableParser;
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
import ru.eqour.timetable.watch.validator.SimpleWeekValidator;
import ru.eqour.timetable.watch.validator.WeekValidator;

import java.util.List;

public class SimpleApplication {

    private final Logger LOG = LogManager.getLogger();
    private final TimeBasedUpdater updater;
    private final SimpleTimetableActualizer timetableActualizer;

    public SimpleApplication(SubscriberRepository subscriberRepository) {
        Settings settings = JsonFileHelper.loadFromFile(AppConstants.SETTINGS_PATH.toString(), Settings.class);
        CacheManager<List<Week>> cacheManager = new SimpleCacheManager<>(AppConstants.TIMETABLE_CACHE_PATH.toString(),
                new TypeToken<List<Week>>(){}.getType());
        FileActualizer actualizer = FileActualizerFactory.create(FileActualizerFactory.FileActualizerType.GOOGLE_DRIVE_SERVICE, settings);
        WeekValidator validator = new SimpleWeekValidator();
        timetableActualizer = new SimpleTimetableActualizer(cacheManager, actualizer, subscriberRepository, settings,
                validator, NotificationHelper::sendNotifications, new SimpleTimetableParser(), new SimpleWeekComparer());
        updater = new TimeBasedUpdater(actualizer::actualize, this::actualize, settings.maxDelayAfterChange);
    }

    public void start() throws InterruptedException {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
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