package ru.eqour.timetable.watch;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.watch.actualizer.SimpleTimetableActualizer;
import ru.eqour.timetable.watch.exception.NotifierException;
import ru.eqour.timetable.watch.exception.RepositoryException;
import ru.eqour.timetable.watch.exception.WeekValidationException;
import ru.eqour.timetable.watch.model.*;
import ru.eqour.timetable.watch.mock.*;
import ru.eqour.timetable.watch.settings.Settings;
import ru.eqour.timetable.watch.util.TestDataFactory;

import java.util.*;

public class SimpleTimetableActualizerTests {

    @Test
    public void whenValidDataAndNotDifferencesThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasDifferencesThenSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .thenSendNotificationCalls(1)
                .thenSendNotificationsAmount(3)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasTeacherDifferencesAndHasGroupSubscribersThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createTeacherDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasGroupDifferencesAndHasTeacherSubscribersThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withTeacherSubscribers(TestDataFactory.createTeacherSubscribersMap())
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasTeacherStudentDifferencesAndHasTeacherStudentSubscribersThenSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupAndTeacherDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .withTeacherSubscribers(TestDataFactory.createTeacherSubscribersMap())
                .thenSendNotificationCalls(1)
                .thenSendNotificationsAmount(6)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasTeacherStudentDifferencesAndHasTeacherSubscribersThenSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupAndTeacherDifferences())
                .withTeacherSubscribers(TestDataFactory.createTeacherSubscribersMap())
                .thenSendNotificationCalls(1)
                .thenSendNotificationsAmount(3)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasDifferencesAndHasNullSavedWeekThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .withNullTimetableCache()
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .build()
                .actualize();
    }

    @Test
    public void whenCacheManagerWithNullLoadThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .withCacheLoadError()
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
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
                .thenSendNotificationsAmount(0)
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
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .thenThrowsException(RuntimeException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenValidDataAndHasDifferencesAndBadNotifierSenderThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .withNotificationSenderError()
                .thenSendNotificationCalls(1)
                .thenSendNotificationsAmount(3)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .thenThrowsException(NotifierException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenInvalidTimetableParsingThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .withParserError()
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(0)
                .thenThrowsException(RuntimeException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenInvalidTimetableThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .withWeekValidationError()
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(0)
                .thenThrowsException(WeekValidationException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenErrorInSubscriberRepositoryThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .withSubscriberRepositoryError()
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .thenThrowsException(RepositoryException.class)
                .build()
                .actualize();
    }

    @Test
    public void whenErrorInWeekComparatorThenThrowException() throws WeekValidationException {
        new TestCaseBuilder()
                .withDifferences(TestDataFactory.createGroupDifferences())
                .withGroupSubscribers(TestDataFactory.createGroupSubscribersMap())
                .withComparerError()
                .thenSendNotificationCalls(0)
                .thenSendNotificationsAmount(0)
                .thenLoadCacheCalls(1)
                .thenSaveCacheCalls(1)
                .thenThrowsException(RuntimeException.class)
                .build()
                .actualize();
    }

    private static class TestCaseBuilder {

        private boolean notificationSenderError;
        private boolean cacheLoadError;
        private boolean cacheSaveError;
        private boolean subscriberRepositoryError;
        private boolean weekValidationError;
        private boolean parserError;
        private boolean comparerError;
        private Map<String, List<Subscriber>> groupSubscribers;
        private Map<String, List<Subscriber>> teacherSubscribers;
        private Map<String, List<Day[]>> differences;
        private Settings settings;
        private List<Week> timetableCache;
        private int sendNotificationsCalls;
        private int sendNotificationsAmount;
        private int loadSettingsCalls;
        private int saveSettingsCalls;
        private Class<? extends Exception> exception;

        public TestCaseBuilder() {
            settings = TestDataFactory.createSettings();
            timetableCache = Collections.singletonList(TestDataFactory.createEmptyWeek());
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

        public TestCaseBuilder withGroupSubscribers(Map<String, List<Subscriber>> groupSubscribers) {
            this.groupSubscribers = groupSubscribers;
            return this;
        }

        public TestCaseBuilder withTeacherSubscribers(Map<String, List<Subscriber>> teacherSubscribers) {
            this.teacherSubscribers = teacherSubscribers;
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

        public TestCaseBuilder thenSendNotificationsAmount(int amount) {
            sendNotificationsAmount = amount;
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
                            new SubscriberRepositoryMock(groupSubscribers == null ? new HashMap<>() : groupSubscribers,
                                    teacherSubscribers == null ? new HashMap<>() : teacherSubscribers, subscriberRepositoryError),
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
                Assert.assertEquals(sendNotificationsCalls, senderMock.getSendNotificationsCalls());
                Assert.assertEquals(sendNotificationsAmount, senderMock.getSendNotificationsAmount());
                Assert.assertEquals(loadSettingsCalls, cacheManagerMock.getLoadCalls());
                Assert.assertEquals(saveSettingsCalls, cacheManagerMock.getSaveCalls());
            };
        }
    }

    @FunctionalInterface
    private interface ActualizerRunnable {
        void actualize() throws WeekValidationException;
    }
}
