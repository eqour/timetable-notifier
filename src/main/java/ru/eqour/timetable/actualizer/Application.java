package ru.eqour.timetable.actualizer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.eqour.timetable.TimetableParser;
import ru.eqour.timetable.WeekComparer;
import ru.eqour.timetable.api.FileActualizer;
import ru.eqour.timetable.exception.NotifierException;
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

    private static final int PERIOD_IN_MILLISECONDS = 10 * 1000;
    private static final int MAX_PERIOD_AFTER_CHANGE = 5;
    private final Logger LOG = LogManager.getLogger();

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
        int period = -1;
        long timer = System.currentTimeMillis();
        //noinspection InfiniteLoopStatement
        while (true) {
            long current = System.currentTimeMillis();
            if (current > timer + PERIOD_IN_MILLISECONDS) {
                timer = current;
                if (actualizer.actualize()) {
                    period = 0;
                } else if (period >= 0) {
                    period++;
                }
                LOG.log(Level.INFO, "Период: " + period);
                if (period >= MAX_PERIOD_AFTER_CHANGE) {
                    period = -1;
                    actualize();
                }
            } else {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void actualize() {
        try {
            LOG.log(Level.INFO, "Актуализация расписания");
            Week actualWeek = getActualWeek(actualizer);
            if (settings.savedWeek != null) {
                Map<String, List<Day[]>> differences = WeekComparer.findDifferences(settings.savedWeek, actualWeek);
                if (!differences.isEmpty()) {
                    LOG.log(Level.INFO, "Рассылка уведомлений");
                    sendNotifications(differences);
                }
            }
            LOG.log(Level.INFO, "Сохранение настроек");
            settings.savedWeek = actualWeek;
            settingsManager.save(settings);
        } catch (Exception e) {
            LOG.log(Level.ERROR, "При актуализации расписания возникло исключение: " + e.getMessage());
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

    private void sendNotifications(Map<String, List<Day[]>> differences) {
        for (Map.Entry<String, List<Day[]>> entry : differences.entrySet()) {
            for (Subscriber subscriber : subscriberRepository.getSubscribers(entry.getKey())) {
                for (Notifier notifier : NotifierFactory.createNotifiersForSubscriber(subscriber, settings)) {
                    String recipient = NotifierFactory.getRecipientIdForNotifier(notifier, subscriber);
                    try {
                        notifier.sendMessage(recipient, formatChangesString(entry.getValue()));
                    } catch (NotifierException e) {
                        LOG.log(Level.ERROR, "Не удалось отправить сообщение. Получатель: " + recipient + ". Исключение: " + e.getMessage());
                    }
                }
            }
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
