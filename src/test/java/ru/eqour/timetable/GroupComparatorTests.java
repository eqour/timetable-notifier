package ru.eqour.timetable;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Group;
import ru.eqour.timetable.util.ResourceLoader;

import java.util.List;

public class GroupComparatorTests {

    @Test
    public void whenFirstArgumentNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparator.findDifferences(null, new Group()));
    }

    @Test
    public void whenSecondArgumentNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparator.findDifferences(new Group(), null));
    }

    @Test
    public void whenAllArgumentsNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparator.findDifferences((Group)null, null));
    }

    @Test
    public void whenWeeksDaysNullNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparator.findDifferences(new Group(), new Group()));
    }

    @Test
    public void whenValidArgumentsAndLessonChangedThenReturnValidResult() {
        runFindDifferencesTest(0);
    }

    @Test
    public void whenValidArgumentsAndLessonDateChangedThenThrowInvalidArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> runFindDifferencesTest(1));
    }

    @Test
    public void whenValidArgumentsAndLessonClassroomChangedThenReturnValidResult() {
        runFindDifferencesTest(2);
    }

    @Test
    public void whenValidArgumentsAndThreeDaysChangedChangedThenReturnValidResult() {
        runFindDifferencesTest(3);
    }

    private void runFindDifferencesTest(int testIndex) {
        Group w1 = ResourceLoader.loadFromResources(getWeekPath(testIndex, 0), Group.class);
        Group w2 = ResourceLoader.loadFromResources(getWeekPath(testIndex, 1), Group.class);
        List<Day[]> expected = ResourceLoader.loadWeeksDifferencesFromResources(getWeekPath(testIndex, 2));
        List<Day[]> actual = WeekComparator.findDifferences(w1, w2);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertArrayEquals(expected.get(i), actual.get(i));
        }
    }

    private String getWeekPath(int index, int subIndex) {
        return "week-comparator/week-" + index + "-" + subIndex + ".json";
    }
}
