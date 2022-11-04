package ru.eqour.timetable;

import org.apache.poi.ooxml.POIXMLException;
import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Group;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.util.ResourceLoader;

import java.io.IOException;

public class TimetableParserTests {

    private final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    @Test
    public void whenArgumentNullThenThrowIllegalArgumentException() {
        //noinspection ConstantConditions
        Assert.assertThrows(IllegalArgumentException.class, () -> TimetableParser.parseTimetable(null));
    }

    @Test
    public void whenValidExcelFileThenReturnValidWeek() throws IOException {
        Week expected = ResourceLoader.loadFromResources(getParsedTimetablePath(0), Week.class);
        Week actual = TimetableParser.parseTimetable(CLASS_LOADER.getResourceAsStream(getTimetablePath(0)));
        compareWeeks(expected, actual);
    }

    @Test
    public void whenNotExcelFileThenThrowIOException() {
        Assert.assertThrows(IOException.class,
                () -> TimetableParser.parseTimetable(CLASS_LOADER.getResourceAsStream("timetable-parser/text-123.txt")));
    }

    @Test
    public void whenExcelFileWithDateCellsThenReturnValidWeek() throws IOException {
        Week expected = ResourceLoader.loadFromResources(getParsedTimetablePath(0), Week.class);
        Week actual = TimetableParser.parseTimetable(CLASS_LOADER.getResourceAsStream(getTimetablePath(1)));
        compareWeeks(expected, actual);
    }

    @Test
    public void whenEmptyExcelFileThenReturnNull() throws IOException {
        Week actual = TimetableParser.parseTimetable(CLASS_LOADER.getResourceAsStream(getTimetablePath(2)));
        Assert.assertNull(actual);
    }

    @Test
    public void whenExcel97_2003FileThenReturnValidWeek() throws IOException {
        Week expected = ResourceLoader.loadFromResources(getParsedTimetablePath(0), Week.class);
        Week actual = TimetableParser.parseTimetable(CLASS_LOADER.getResourceAsStream("timetable-parser/timetable-3.xls"));
        compareWeeks(expected, actual);
    }

    @Test
    public void whenExcelOpenXMLFileThenThrowPOIXMLException() {
        Assert.assertThrows(POIXMLException.class,
                () -> TimetableParser.parseTimetable(CLASS_LOADER.getResourceAsStream(getTimetablePath(4))));
    }

    private String getTimetablePath(int index) {
        return "timetable-parser/timetable-" + index + ".xlsx";
    }

    private String getParsedTimetablePath(int index) {
        return "timetable-parser/timetable-parsed-" + index + ".json";
    }

    private void compareWeeks(Week a, Week b) {
        Assert.assertEquals(a.period, b.period);
        Assert.assertEquals(a.groups.length, b.groups.length);
        for (int i = 0; i < a.groups.length; i++) {
            compareGroups(a.groups[i], b.groups[i]);
        }
    }

    private void compareGroups(Group a, Group b) {
        Assert.assertEquals(a.name, b.name);
        Assert.assertEquals(a.days.length, b.days.length);
        for (int i = 0; i < a.days.length; i++) {
            compareDays(a.days[i], b.days[i]);
        }
    }

    private void compareDays(Day a, Day b) {
        Assert.assertEquals(a.date, b.date);
        Assert.assertEquals(a.lessons.length, b.lessons.length);
        for (int i = 0; i < a.lessons.length; i++) {
            compareLessons(a.lessons[i], b.lessons[i]);
        }
    }

    private void compareLessons(Lesson a, Lesson b) {
        if (a != null || b != null) {
            if (a == null || b == null) {
                Assert.fail();
            }
            Assert.assertEquals(a.time, b.time);
            Assert.assertEquals(a.discipline, b.discipline);
            Assert.assertEquals(a.teacher, b.teacher);
            Assert.assertEquals(a.classroom, b.classroom);
        }
    }
}
