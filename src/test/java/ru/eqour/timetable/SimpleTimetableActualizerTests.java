package ru.eqour.timetable;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.actualizer.SimpleTimetableActualizer;
import ru.eqour.timetable.exception.NotifierException;
import ru.eqour.timetable.exception.RepositoryException;
import ru.eqour.timetable.exception.WeekValidationException;
import ru.eqour.timetable.mock.*;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Subscriber;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.settings.Settings;

import java.util.*;

public class SimpleTimetableActualizerTests {

    @Test
    public void whenValidDataAndNotDifferencesThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasDifferencesThenSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(getDifferences())
                .withSubscribers(getSubscribersMap())
                .thenSendNotificationCalls(1)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasDifferencesAndHasNullSavedWeekThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(getDifferences())
                .withSubscribers(getSubscribersMap())
                .withNullTimetableCache()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenCacheManagerWithNullLoadThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(getDifferences())
                .withSubscribers(getSubscribersMap())
                .withCacheLoadError()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenSettingsNullThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withNullSettings()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(0)
                .thenSaveCacheCalls(0)
                .thenThrowsException(IllegalArgumentException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenCacheManagerWithBadSaveThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withCacheSaveError()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .thenThrowsException(RuntimeException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasDifferencesAndBadNotifierSenderThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(getDifferences())
                .withSubscribers(getSubscribersMap())
                .withNotificationSenderError()
                .thenSendNotificationCalls(1)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .thenThrowsException(NotifierException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenInvalidTimetableParsingThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(getDifferences())
                .withSubscribers(getSubscribersMap())
                .withParserError()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(0)
                .thenThrowsException(RuntimeException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenInvalidTimetableThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(getDifferences())
                .withSubscribers(getSubscribersMap())
                .withWeekValidationError()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(0)
                .thenThrowsException(WeekValidationException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenErrorInSubscriberRepositoryThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(getDifferences())
                .withSubscribers(getSubscribersMap())
                .withSubscriberRepositoryError()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .thenThrowsException(RepositoryException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenErrorInWeekComparatorThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(getDifferences())
                .withSubscribers(getSubscribersMap())
                .withComparerError()
                .thenSendNotificationCalls(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .thenThrowsException(RuntimeException.class)
                .build()
                .actualize();
    }

    private Map<String, List<Day[]>> getDifferences() {
        Map<String, List<Day[]>> differences = new HashMap<>();
        Lesson lesson = new Lesson();
        lesson.time = "mock-time";
        lesson.classroom = "mock-classroom";
        lesson.discipline = "mock-discipline";
        lesson.teacher = "mock-teacher";
        Day day = new Day();
        day.date = "mock-date";
        day.lessons = new Lesson[] { lesson };
        differences.put("group-1", Collections.singletonList(new Day[] { day, day }));
        return differences;
    }

    private Map<String, List<Subscriber>> getSubscribersMap() {
        Map<String, List<Subscriber>> map = new HashMap<>();
        Subscriber s1 = new Subscriber();
        s1.vkId = "vk-1";
        Subscriber s2 = new Subscriber();
        s2.vkId = "vk-2";
        s2.telegramId = "tg-1";
        map.put("group-1", Arrays.asList(s1, s2));
        return map;
    }

    private static class TestCaseBuilder {

        private boolean notificationSenderError;
        private boolean cacheLoadError;
        private boolean cacheSaveError;
        private boolean subscriberRepositoryError;
        private boolean weekValidationError;
        private boolean parserError;
        private boolean comparerError;
        private Map<String, List<Subscriber>> subscribers;
        private Map<String, List<Day[]>> differences;
        private Settings settings;
        private List<Week> timetableCache;
        private int sendNotificationsCalls;
        private int loadSettingsCalls;
        private int saveSettingsCalls;
        private Class<? extends Exception> exception;

        public TestCaseBuilder() {
            settings = new Settings();
            timetableCache = Collections.singletonList(new Week());
        }

        public TestCaseBuilder withNotificationSenderError() {
            notificationSenderError = true;
            return this;
        }

        public TestCaseBuilder withCacheLoadError() {
            cacheLoadError = true;
            return this;
        }

        public TestCaseBuilder withCacheSaveError() {
            cacheSaveError = true;
            return this;
        }

        public TestCaseBuilder withSubscriberRepositoryError() {
            subscriberRepositoryError = true;
            return this;
        }

        public TestCaseBuilder withWeekValidationError() {
            weekValidationError = true;
            return this;
        }

        public TestCaseBuilder withParserError() {
            parserError = true;
            return this;
        }

        public TestCaseBuilder withComparerError() {
            comparerError = true;
            return this;
        }

        public TestCaseBuilder withSubscribers(Map<String, List<Subscriber>> subscribers) {
            this.subscribers = subscribers;
            return this;
        }

        public TestCaseBuilder withDifferences(Map<String, List<Day[]>> differences) {
            this.differences = differences;
            return this;
        }

        public TestCaseBuilder withNullTimetableCache() {
            timetableCache = null;
            return this;
        }

        public TestCaseBuilder withNullSettings() {
            settings = null;
            return this;
        }

        public TestCaseBuilder thenSendNotificationCalls(int amount) {
            sendNotificationsCalls = amount;
            return this;
        }

        public TestCaseBuilder thenLoadCacheCalls(int amount) {
            loadSettingsCalls = amount;
            return this;
        }

        public TestCaseBuilder thenSaveCacheCalls(int amount) {
            saveSettingsCalls = amount;
            return this;
        }

        public TestCaseBuilder thenThrowsException(Class<? extends Exception> exception) {
            this.exception = exception;
            return this;
        }

        public ActualizerRunnable build() {
            return () -> {
                NotificationSenderMock senderMock = new NotificationSenderMock(notificationSenderError);
                CacheManagerMock<List<Week>> cacheManagerMock = new CacheManagerMock<>(cacheSaveError, cacheLoadError, timetableCache);
                ActualizerRunnable testRunnable = () -> {
                    SimpleTimetableActualizer actualizer = new SimpleTimetableActualizer(
                            cacheManagerMock,
                            new FileActualizerMock(),
                            new SubscriberRepositoryMock(subscribers == null ? new HashMap<>() : subscribers, subscriberRepositoryError),
                            settings,
                            new WeekValidatorMock(weekValidationError),
                            senderMock::sendNotifications,
                            new TimetableParserMock(!parserError),
                            new WeekComparerMock(differences == null ? new HashMap<>() : differences, comparerError)
                    );
                    actualizer.actualize();
                };
                if (exception == null) {
                    testRunnable.actualize();
                } else {
                    Assert.assertThrows(exception, testRunnable::actualize);
                }
                Assert.assertEquals(senderMock.getSendNotificationsCalls(), sendNotificationsCalls);
                Assert.assertEquals(cacheManagerMock.getLoadCalls(), loadSettingsCalls);
                Assert.assertEquals(cacheManagerMock.getSaveCalls(), saveSettingsCalls);
            };
        }
    }

    @FunctionalInterface
    private interface ActualizerRunnable {
        void actualize() throws WeekValidationException;
    }
}
