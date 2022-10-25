package ru.eqour.timetable;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.util.ResourceLoader;

import java.util.List;

public class WeekComparatorTests {

    @Test
    public void whenFirstArgumentNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparator.findDifferences(null, new Week()));
    }

    @Test
    public void whenSecondArgumentNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparator.findDifferences(new Week(), null));
    }

    @Test
    public void whenAllArgumentsNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparator.findDifferences(null, null));
    }

    @Test
    public void whenWeeksDaysNullNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparator.findDifferences(new Week(), new Week()));
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
        Week w1 = ResourceLoader.loadWeekFromResources(getWeekPath(testIndex, 0));
        Week w2 = ResourceLoader.loadWeekFromResources(getWeekPath(testIndex, 1));
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
