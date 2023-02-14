package ru.eqour.timetable.watch;

import com.google.common.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.watch.comparer.PeriodWeekComparer;
import ru.eqour.timetable.watch.model.Day;
import ru.eqour.timetable.watch.model.Week;
import ru.eqour.timetable.watch.util.Compare;
import ru.eqour.timetable.watch.util.JsonFileHelper;
import ru.eqour.timetable.watch.util.ResourceHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PeriodWeekComparerTests {

    @Test
    public void whenValidArgumentsAndHasChangesThenReturnValidResult() {
        runFindDifferencesTest(new PeriodWeekComparer(7,
                () -> LocalDate.of(2022, 10, 31)), 0, 2);
    }

    @Test
    public void whenValidArgumentsAndHasChangesAndFirstChangeNotInPeriodThenReturnValidResult() {
        runFindDifferencesTest(new PeriodWeekComparer(7,
                () -> LocalDate.of(2022, 11, 2)), 0, 3);
    }

    @Test
    public void whenValidArgumentsAndHasChangesAndLastChangeNotInPeriodThenReturnValidResult() {
        runFindDifferencesTest(new PeriodWeekComparer(2,
                () -> LocalDate.of(2022, 10, 31)), 0, 4);
    }

    @Test
    public void whenValidArgumentsAndHasChangesAndAllChangesNotInPeriodThenReturnValidResult() {
        runFindDifferencesTest(new PeriodWeekComparer(7,
                () -> LocalDate.of(2022, 10, 4)), 0, 5);
    }

    private void runFindDifferencesTest(PeriodWeekComparer comparer, @SuppressWarnings("SameParameterValue") int testIndex, int differencesIndex) {
        Week w1 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 0), Week.class);
        Week w2 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 1), Week.class);
        Map<String, List<Day[]>> expected = JsonFileHelper.loadFromFile(getWeekPath(testIndex, differencesIndex),
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
        return ResourceHelper.getFullPathToResource("/week-comparer/period/week-" + index + "-" + subIndex + ".json").toString();
    }
}
