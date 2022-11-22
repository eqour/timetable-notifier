package ru.eqour.timetable;

import com.google.common.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.util.Compare;
import ru.eqour.timetable.util.JsonFileHelper;
import ru.eqour.timetable.util.ResourceHelper;

import java.util.List;
import java.util.Map;

public class WeekComparerTests {

    @Test
    public void whenFirstArgumentNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparer.findDifferences(null, new Week()));
    }

    @Test
    public void whenSecondArgumentNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparer.findDifferences(new Week(), null));
    }

    @Test
    public void whenAllArgumentsNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparer.findDifferences(null, null));
    }

    @Test
    public void whenWeeksDaysNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> WeekComparer.findDifferences(new Week(), new Week()));
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

    @Test
    public void whenNullDaysOnNextWeekThenThrowInvalidArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> runFindDifferencesTest(4));
    }

    @Test
    public void whenEmptyDaysOnNextWeekThenThrowInvalidArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> runFindDifferencesTest(5));
    }

    @Test
    public void whenValidArgumentsAndDisciplineAddedThenReturnEmptyResult() {
        runFindDifferencesTest(6);
    }

    @Test
    public void whenValidArgumentsAndDisciplineRemovedThenReturnEmptyResult() {
        runFindDifferencesTest(7);
    }

    private void runFindDifferencesTest(int testIndex) {
        Week w1 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 0), Week.class);
        Week w2 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 1), Week.class);
        Map<String, List<Day[]>> expected = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 2),
                new TypeToken<Map<String, List<Day[]>>>(){}.getType());
        Map<String, List<Day[]>> actual = WeekComparer.findDifferences(w1, w2);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.keySet().size(), actual.keySet().size());
        for (String expectedKey : expected.keySet()) {
            Assert.assertTrue(actual.containsKey(expectedKey));
            for (int i = 0; i < expected.get(expectedKey).size(); i++) {
                Day[] exp = expected.get(expectedKey).get(i);
                Day[] act = actual.get(expectedKey).get(i);
                Assert.assertEquals(exp.length, act.length);
                for (int j = 0; j < exp.length; j++) {
                    Compare.compareDays(exp[i], act[i]);
                }
            }
        }
    }

    private String getWeekPath(int index, int subIndex) {
        return ResourceHelper.getFullPathToResource("/week-comparer/week-" + index + "-" + subIndex + ".json").toString();
    }
}
