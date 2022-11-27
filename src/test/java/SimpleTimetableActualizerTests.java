import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.actualizer.SimpleTimetableActualizer;
import ru.eqour.timetable.exception.WeekValidationException;
import ru.eqour.timetable.mock.*;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Subscriber;
import ru.eqour.timetable.settings.Settings;

import java.util.*;

public class SimpleTimetableActualizerTests {

    @Test
    public void whenValidDataAndNotDifferencesThenNotSendNotifiersAndNotThrowException() throws WeekValidationException {
        NotificationSenderMock senderMock = new NotificationSenderMock(false);
        SettingsManagerMock settingsManagerMock = new SettingsManagerMock(false, false, new Settings());
        SimpleTimetableActualizer actualizer = new SimpleTimetableActualizer(
                settingsManagerMock,
                new FileActualizerMock(),
                new SubscriberRepositoryMock(new HashMap<>(), false),
                new WeekValidatorMock(false),
                senderMock::sendNotifications,
                new TimetableParserMock(true),
                new WeekComparerMock(new HashMap<>(), false)
        );
        actualizer.actualize();
        Assert.assertEquals(senderMock.getSendNotificationsCalls(), 0);
        Assert.assertEquals(settingsManagerMock.getLoadCalls(), 1);
        Assert.assertEquals(settingsManagerMock.getSaveCalls(), 1);
    }

    @Test
    public void whenValidDataAndHasDifferencesThenSendNotifiersAndNotThrowException() throws WeekValidationException {
        NotificationSenderMock senderMock = new NotificationSenderMock(false);
        SettingsManagerMock settingsManagerMock = new SettingsManagerMock(false, false, new Settings());
        SimpleTimetableActualizer actualizer = new SimpleTimetableActualizer(
                settingsManagerMock,
                new FileActualizerMock(),
                new SubscriberRepositoryMock(getSubscribersMap(), false),
                new WeekValidatorMock(false),
                senderMock::sendNotifications,
                new TimetableParserMock(true),
                new WeekComparerMock(getDifferences(), false)
        );
        actualizer.actualize();
        Assert.assertEquals(senderMock.getSendNotificationsCalls(), 1);
        Assert.assertEquals(settingsManagerMock.getLoadCalls(), 1);
        Assert.assertEquals(settingsManagerMock.getSaveCalls(), 1);
    }

    @Test
    public void whenSettingsManagerWithBadLoadThenThrowException() {
        NotificationSenderMock senderMock = new NotificationSenderMock(false);
        SettingsManagerMock settingsManagerMock = new SettingsManagerMock(false, true, new Settings());
        SimpleTimetableActualizer actualizer = new SimpleTimetableActualizer(
                settingsManagerMock,
                new FileActualizerMock(),
                new SubscriberRepositoryMock(new HashMap<>(), false),
                new WeekValidatorMock(false),
                senderMock::sendNotifications,
                new TimetableParserMock(true),
                new WeekComparerMock(new HashMap<>(), false)
        );
        Assert.assertThrows(RuntimeException.class, actualizer::actualize);
        Assert.assertEquals(settingsManagerMock.getLoadCalls(), 1);
        Assert.assertEquals(settingsManagerMock.getSaveCalls(), 0);
    }

    @Test
    public void whenSettingsManagerWithBadSaveThenThrowException() {
        NotificationSenderMock senderMock = new NotificationSenderMock(false);
        SettingsManagerMock settingsManagerMock = new SettingsManagerMock(true, false, new Settings());
        SimpleTimetableActualizer actualizer = new SimpleTimetableActualizer(
                settingsManagerMock,
                new FileActualizerMock(),
                new SubscriberRepositoryMock(new HashMap<>(), false),
                new WeekValidatorMock(false),
                senderMock::sendNotifications,
                new TimetableParserMock(true),
                new WeekComparerMock(new HashMap<>(), false)
        );
        Assert.assertThrows(RuntimeException.class, actualizer::actualize);
        Assert.assertEquals(settingsManagerMock.getLoadCalls(), 1);
        Assert.assertEquals(settingsManagerMock.getSaveCalls(), 1);
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
}
