package ru.eqour.timetable.watch;

import com.google.common.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.watch.comparer.SimpleWeekComparer;
import ru.eqour.timetable.watch.model.Day;
import ru.eqour.timetable.watch.model.Week;
import ru.eqour.timetable.watch.util.JsonFileHelper;
import ru.eqour.timetable.watch.util.ResourceHelper;
import ru.eqour.timetable.watch.util.Compare;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SimpleWeekComparerTests {

    private static final SimpleWeekComparer comparer = new SimpleWeekComparer();

    @Test
    public void whenFirstArgumentNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> comparer.findDifferences(null, new Week()));
    }

    @Test
    public void whenSecondArgumentNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> comparer.findDifferences(new Week(), null));
    }

    @Test
    public void whenAllArgumentsNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> comparer.findDifferences((Week) null, null));
    }

    @Test
    public void whenFirstArgumentAsCollectionNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> comparer.findDifferences(null, Collections.emptyList()));
    }

    @Test
    public void whenSecondArgumentAsCollectionNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> comparer.findDifferences(Collections.emptyList(), null));
    }

    @Test
    public void whenAllArgumentsAsCollectionsNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> comparer.findDifferences((List<Week>) null, null));
    }

    @Test
    public void whenWeeksDaysNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> comparer.findDifferences(new Week(), new Week()));
    }

    @Test
    public void whenValidArgumentsAndLessonChangedThenReturnValidResult() {
        runFindDifferencesTest(0);
    }

    @Test
    public void whenValidArgumentsAndLessonDateChangedToNullThenThrowInvalidArgumentException() {
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
    public void whenValidArgumentsAndGroupRemovedThenReturnEmptyResult() {
        runFindDifferencesTest(7);
    }

    @Test
    public void whenValidArgumentsAndLessonDateChangedThenReturnEmptyResult() {
        runFindDifferencesTest(8);
    }

    @Test
    public void whenValidArgumentsAndPeriodChangedThenReturnEmptyResult() {
        runFindDifferencesTest(9);
    }

    @Test
    public void whenValidCollectionArgumentsAndLessonChangedInFirstWeekThenReturnValidResult() {
        runFindDifferencesCollectionTest(10);
    }

    private void runFindDifferencesTest(int testIndex) {
        Week w1 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 0), Week.class);
        Week w2 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 1), Week.class);
        Map<String, List<Day[]>> expected = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 2),
                new TypeToken<Map<String, List<Day[]>>>(){}.getType());
        Map<String, List<Day[]>> actual = comparer.findDifferences(w1, w2);
        assertDifferences(expected, actual);
    }

    private void runFindDifferencesCollectionTest(@SuppressWarnings("SameParameterValue") int testIndex) {
        List<Week> w1 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 0),
                new TypeToken<List<Week>>(){}.getType());
        List<Week> w2 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 1),
                new TypeToken<List<Week>>(){}.getType());
        Map<String, List<Day[]>> expected = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 2),
                new TypeToken<Map<String, List<Day[]>>>(){}.getType());
        Map<String, List<Day[]>> actual = comparer.findDifferences(w1, w2);
        assertDifferences(expected, actual);
    }

    private void assertDifferences(Map<String, List<Day[]>> expected, Map<String, List<Day[]>> actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.keySet().size(), actual.keySet().size());
        for (String expectedKey : expected.keySet()) {
            Assert.assertTrue(actual.containsKey(expectedKey));
            for (int i = 0; i < expected.get(expectedKey).size(); i++) {
                Day[] exp = expected.get(expectedKey).get(i);
                Day[] act = actual.get(expectedKey).get(i);
                Assert.assertEquals(exp.length, act.length);
                for (int j = 0; j < exp.length; j++) {
                    Compare.compareDays(exp[j], act[j]);
                }
            }
        }
    }

    private String getWeekPath(int index, int subIndex) {
        return ResourceHelper.getFullPathToResource("/week-comparer/week-" + index + "-" + subIndex + ".json").toString();
    }
}
