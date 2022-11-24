package ru.eqour.timetable.actualizer;

import ru.eqour.timetable.TimetableParser;
import ru.eqour.timetable.WeekComparer;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.util.factory.FileActualizerFactory;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Subscriber;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.notifier.Notifier;
import ru.eqour.timetable.repository.SubscriberRepository;
import ru.eqour.timetable.util.factory.NotifierFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Application {

    private final SettingsManager settingsManager;
    private final FileActualizer actualizer;
    private final SubscriberRepository subscriberRepository;
    private final Settings settings;

    public Application(SettingsManager settingsManager, SubscriberRepository subscriberRepository) {
        this.settingsManager = settingsManager;
        settings = settingsManager.load();
        this.actualizer = FileActualizerFactory.create(FileActualizerFactory.FileActualizerType.GOOGLE_DRIVE, settings);
        this.subscriberRepository = subscriberRepository;
    }

    public void start() {
        long timer = System.currentTimeMillis();
        //noinspection InfiniteLoopStatement
        while (true) {
            long current = System.currentTimeMillis();
            if (current > timer + 30000) {
                timer = current;
                actualize();
            } else {
                Thread.yield();
            }
        }
    }

    private void actualize() {
        if (actualizer.actualize()) {
            Week actualWeek = getActualWeek(actualizer);
            if (settings.savedWeek != null) {
                Map<String, List<Day[]>> differences = WeekComparer.findDifferences(settings.savedWeek, actualWeek);
                if (!differences.isEmpty()) {
                    for (Map.Entry<String, List<Day[]>> entry : differences.entrySet()) {
                        for (Subscriber subscriber : subscriberRepository.getSubscribers(entry.getKey())) {
                            for (Notifier notifier : NotifierFactory.createNotifierForSubscriber(subscriber, settings)) {
                                notifier.sendMessage(NotifierFactory.createTokenForNotifier(notifier, subscriber),
                                        formatChangesString(entry.getValue()));
                            }
                        }
                    }
                }
            }
            settings.savedWeek = actualWeek;
            settingsManager.save(settings);
        }
    }

    private Week getActualWeek(FileActualizer actualizer) {
        try {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(actualizer.getActualFile())) {
                return TimetableParser.parseTimetable(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String formatChangesString(List<Day[]> days) {
        StringBuilder builder = new StringBuilder();
        builder.append("Изменения в расписании:\n\n");
        for (Day[] pair : days) {
            Day day = pair[1];
            builder.append(day.date).append("\n\n");
            for (int i = 0; i < day.lessons.length; i++) {
                Lesson lesson = day.lessons[i];
                if (lesson != null) {
                    builder.append(i + 1).append(" пара, ").append(lesson.time).append("\n");
                    builder.append(lesson.discipline).append("\n");
                    builder.append(lesson.teacher).append(" ").append(lesson.classroom);
                    if (i != day.lessons.length - 1) {
                        builder.append("\n\n");
                    }
                }
            }
        }
        return builder.toString();
    }
}
