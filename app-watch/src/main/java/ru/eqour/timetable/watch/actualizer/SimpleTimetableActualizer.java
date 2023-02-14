package ru.eqour.timetable.watch.actualizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.watch.api.FileActualizer;
import ru.eqour.timetable.watch.comparer.WeekComparer;
import ru.eqour.timetable.watch.exception.WeekValidationException;
import ru.eqour.timetable.watch.model.*;
import ru.eqour.timetable.watch.notifier.Notifier;
import ru.eqour.timetable.watch.parser.TimetableParser;
import ru.eqour.timetable.watch.repository.SubscriberRepository;
import ru.eqour.timetable.watch.settings.CacheManager;
import ru.eqour.timetable.watch.settings.Settings;
import ru.eqour.timetable.watch.util.factory.NotifierFactory;
import ru.eqour.timetable.watch.validator.WeekValidator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

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
            Map<String, List<Day[]>> differences = weekComparer.findDifferences(oldWeeks, actualWeeks);
            if (!differences.isEmpty()) {
                LOG.log(Level.INFO, "Sending notifications");
                sendNotificationsConsumer.accept(collectNotifications(differences));
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

    private List<Notification> collectNotifications(Map<String, List<Day[]>> differences) {
        List<Notification> notifications = new ArrayList<>();
        for (Map.Entry<String, List<Day[]>> entry : differences.entrySet()) {
            for (Subscriber subscriber : subscriberRepository.getSubscribers(entry.getKey())) {
                for (Notifier notifier : NotifierFactory.createNotifiersForSubscriber(subscriber, settings)) {
                    notifications.add(new Notification(notifier, subscriber, formatChangesString(entry.getValue())));
                }
            }
        }
        return notifications;
    }

    private String formatChangesString(List<Day[]> days) {
        StringBuilder builder = new StringBuilder();
        builder.append("Изменения в расписании:\n\n");
        for (Day[] pair : days) {
            Day day = pair[1];
            builder.append(day.date).append("\n\n");
            boolean hasLessons = false;
            for (int i = 0; i < day.lessons.length; i++) {
                Lesson lesson = day.lessons[i];
                if (lesson != null) {
                    hasLessons = true;
                    builder.append(i + 1).append(" пара, ").append(lesson.time).append("\n");
                    builder.append(getStringOrEmptyString(lesson.discipline)).append("\n");
                    if (Objects.equals(lesson.teacher, lesson.classroom)) {
                        builder.append(getStringOrEmptyString(lesson.teacher));
                    } else {
                        builder.append(getStringOrEmptyString(lesson.teacher)).append(" ")
                                .append(getStringOrEmptyString(lesson.classroom));
                    }
                    if (i != day.lessons.length - 1) {
                        builder.append("\n\n");
                    }
                }
                if (i == day.lessons.length - 1) {
                    if (!hasLessons) {
                        builder.append("Занятий нет\n\n");
                    }
                }
            }
        }
        return builder.toString();
    }

    private String getStringOrEmptyString(String value) {
        return value == null ? "" : value;
    }
}
