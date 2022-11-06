package ru.eqour.timetable;

import org.apache.poi.ooxml.POIXMLException;
import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.util.Compare;
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
        Compare.compareWeeks(expected, actual);
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
        Compare.compareWeeks(expected, actual);
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
        Compare.compareWeeks(expected, actual);
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
}
