package ru.eqour.timetable.watch.actualizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.sender.MessageSender;
import ru.eqour.timetable.sender.model.Message;
import ru.eqour.timetable.watch.api.FileActualizer;
import ru.eqour.timetable.watch.comparer.WeekComparer;
import ru.eqour.timetable.watch.converter.WeekConverter;
import ru.eqour.timetable.watch.exception.WeekValidationException;
import ru.eqour.timetable.watch.model.*;
import ru.eqour.timetable.watch.parser.TimetableParser;
import ru.eqour.timetable.watch.repository.SubscriberRepository;
import ru.eqour.timetable.watch.settings.CacheManager;
import ru.eqour.timetable.watch.settings.Settings;
import ru.eqour.timetable.watch.util.ChangesFormatter;
import ru.eqour.timetable.watch.util.factory.NotifierFactory;
import ru.eqour.timetable.watch.validator.WeekValidator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Актуализатор расписания.
 */
public class SimpleTimetableActualizer {

    private final Logger LOG = LogManager.getLogger();
    private final CacheManager<List<Week>> cacheManager;
    private final FileActualizer actualizer;
    private final SubscriberRepository subscriberRepository;
    private final Settings settings;
    private final WeekValidator validator;
    private final Consumer<List<Notification>> sendNotificationsConsumer;
    private final TimetableParser timetableParser;
    private final WeekComparer weekComparer;

    private List<Week> timetableCache;

    public SimpleTimetableActualizer(CacheManager<List<Week>> cacheManager, FileActualizer actualizer,
                                     SubscriberRepository subscriberRepository, Settings settings,
                                     WeekValidator validator, Consumer<List<Notification>> sendNotificationsConsumer,
                                     TimetableParser timetableParser, WeekComparer weekComparer) {
        if (settings == null) {
            throw new IllegalArgumentException("settings is null");
        }
        this.cacheManager = cacheManager;
        this.actualizer = actualizer;
        this.subscriberRepository = subscriberRepository;
        this.settings = settings;
        this.validator = validator;
        this.sendNotificationsConsumer = sendNotificationsConsumer;
        this.timetableParser = timetableParser;
        this.weekComparer = weekComparer;
        timetableCache = cacheManager.load();
    }

    /**
     * Выполняет актуализацию расписания.
     *
     * @throws WeekValidationException если новое расписание не проходит валидацию.
     */
    public void actualize() throws WeekValidationException {
        LOG.log(Level.INFO, "Actualizing the timetable");
        List<Week> actualWeeks = getActualWeeks(actualizer);
        validator.validate(actualWeeks);
        LOG.log(Level.INFO, "Saving parsed timetable");
        List<Week> oldWeeks = timetableCache;
        timetableCache = actualWeeks;
        cacheManager.save(timetableCache);
        if (timetableCache != null && oldWeeks != null) {
            WeekConverter converter = new WeekConverter();
            List<Week> oldTeacherWeeks = converter.convertToTeacherWeeks(oldWeeks);
            List<Week> actualTeacherWeeks = converter.convertToTeacherWeeks(actualWeeks);
            List<Notification> notifications = processTimetableChanges(Arrays.asList(
                    new ProcessDifferencesData(oldWeeks, actualWeeks,
                            subscriberRepository::getSubscribersByStudentGroup,
                            ChangesFormatter::formatChangesStringForGroup),
                    new ProcessDifferencesData(oldTeacherWeeks, actualTeacherWeeks,
                            subscriberRepository::getSubscribersByTeacher,
                            ChangesFormatter::formatChangesStringForTeacher)
            ));
            if (!notifications.isEmpty()) {
                LOG.log(Level.INFO, "Sending notifications");
                sendNotificationsConsumer.accept(notifications);
            }
        }
    }

    private List<Week> getActualWeeks(FileActualizer actualizer) {
        try {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(actualizer.getActualFile())) {
                return timetableParser.parseTimetable(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Notification> processTimetableChanges(List<ProcessDifferencesData> processDifferencesData) {
        List<Notification> notifications = new ArrayList<>();
        for (ProcessDifferencesData data : processDifferencesData) {
            Map<String, List<Day[]>> differences = weekComparer.findDifferences(data.oldWeeks, data.actualWeeks);
            notifications.addAll(collectNotifications(differences, data.getSubscribersFunction, data.changesFormatter));
        }
        return notifications;
    }

    private List<Notification> collectNotifications(Map<String, List<Day[]>> differences,
                                                    Function<String, List<Subscriber>> getSubscribersFunction,
                                                    Function<List<Day[]>, String> changesFormatter) {
        List<Notification> notifications = new ArrayList<>();
        for (Map.Entry<String, List<Day[]>> entry : differences.entrySet()) {
            for (Subscriber subscriber : getSubscribersFunction.apply(entry.getKey())) {
                for (MessageSender notifier : NotifierFactory.createNotifiersForSubscriber(subscriber, settings)) {
                    notifications.add(new Notification(notifier, subscriber, new Message("",
                            changesFormatter.apply(entry.getValue()))));
                }
            }
        }
        return notifications;
    }

    private static class ProcessDifferencesData {

        private final List<Week> oldWeeks, actualWeeks;
        private final Function<String, List<Subscriber>> getSubscribersFunction;
        private final Function<List<Day[]>, String> changesFormatter;

        public ProcessDifferencesData(List<Week> oldWeeks, List<Week> actualWeeks,
                                      Function<String, List<Subscriber>> getSubscribersFunction,
                                      Function<List<Day[]>, String> changesFormatter) {
            this.oldWeeks = oldWeeks;
            this.actualWeeks = actualWeeks;
            this.getSubscribersFunction = getSubscribersFunction;
            this.changesFormatter = changesFormatter;
        }
    }
}
