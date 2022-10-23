package ru.eqour.timetable.parser;

import ru.eqour.timetable.model.Week;
import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.parser.util.ResourceLoader;

public class TimetableParserTests {

    @Test
    public void parseTimetableTest() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Week expected = ResourceLoader.loadWeekFromResources("timetable-parsed-0.json");
        Week actual = TimetableParser.parseTimetable(classLoader.getResourceAsStream("timetable-0.xlsx"));
        Assert.assertEquals(expected, actual);
    }
}
