package ru.eqour.timetable.watch;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.watch.converter.WeekConverter;
import ru.eqour.timetable.watch.model.Week;
import ru.eqour.timetable.watch.util.Compare;
import ru.eqour.timetable.watch.util.JsonFileHelper;
import ru.eqour.timetable.watch.util.ResourceHelper;

import java.util.Collections;

public class WeekConverterTests {

    private static final WeekConverter converter = new WeekConverter();

    @Test
    public void whenFirstArgumentNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> converter.convertToTeacherWeek(null));
    }

    @Test
    public void whenFirstCollectionArgumentNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> converter.convertToTeacherWeeks(null));
    }

    @Test
    public void whenValidDataThenReturnValidResult() {
        runConversionTest(0);
    }

    private void runConversionTest(@SuppressWarnings("SameParameterValue") int testIndex) {
        Week w1 = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 0), Week.class);
        Week expected = JsonFileHelper.loadFromFile(getWeekPath(testIndex, 1), Week.class);
        Week actual = converter.convertToTeacherWeeks(Collections.singletonList(w1)).get(0);
        Compare.compareWeeks(actual, expected);
    }

    private String getWeekPath(int index, int subIndex) {
        return ResourceHelper.getFullPathToResource("/week-converter/week-" + index + "-" + subIndex + ".json").toString();
    }
}
