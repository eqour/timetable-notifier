package ru.eqour.timetable.actualizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.comparer.WeekComparer;
import ru.eqour.timetable.parser.TimetableParser;
import ru.eqour.timetable.validator.WeekValidator;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.exception.WeekValidationException;
import ru.eqour.timetable.model.*;
import ru.eqour.timetable.notifier.Notifier;
import ru.eqour.timetable.repository.SubscriberRepository;
import ru.eqour.timetable.settings.Settings;
import ru.eqour.timetable.settings.SettingsManager;
import ru.eqour.timetable.util.factory.NotifierFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleTimetableActualizer {

    private final Logger LOG = LogManager.getLogger();
    private final SettingsManager settingsManager;
    private final FileActualizer actualizer;
    private final SubscriberRepository subscriberRepository;
    private final Settings settings;
    private final WeekValidator validator;
    private final Consumer<List<Notification>> sendNotificationsConsumer;
    private final TimetableParser timetableParser;
    private final WeekComparer weekComparer;

    public SimpleTimetableActualizer(SettingsManager settingsManager, FileActualizer actualizer,
                                     SubscriberRepository subscriberRepository, WeekValidator validator,
                                     Consumer<List<Notification>> sendNotificationsConsumer, TimetableParser timetableParser,
                                     WeekComparer weekComparer) {
        this.settingsManager = settingsManager;
        this.actualizer = actualizer;
        this.subscriberRepository = subscriberRepository;
        this.settings = settingsManager.load();
        this.validator = validator;
        this.sendNotificationsConsumer = sendNotificationsConsumer;
        this.timetableParser = timetableParser;
        this.weekComparer = weekComparer;
    }

    public void actualize() throws WeekValidationException {
        LOG.log(Level.INFO, "Actualizing the timetable");
        Week actualWeek = getActualWeek(actualizer);
        validator.validate(actualWeek);
        LOG.log(Level.INFO, "Saving settings");
        Week oldWeek = settings.savedWeek;
        settings.savedWeek = actualWeek;
        settingsManager.save(settings);
        if (settings.savedWeek != null && oldWeek != null) {
            Map<String, List<Day[]>> differences = weekComparer.findDifferences(oldWeek, actualWeek);
            if (!differences.isEmpty()) {
                LOG.log(Level.INFO, "Sending notifications");
                sendNotificationsConsumer.accept(collectNotifications(differences));
            }
        }
    }

    private Week getActualWeek(FileActualizer actualizer) {
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
                    builder.append(lesson.discipline).append("\n");
                    builder.append(lesson.teacher).append(" ").append(lesson.classroom);
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
}
