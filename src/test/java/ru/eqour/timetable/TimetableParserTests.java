package ru.eqour.timetable;

import org.apache.poi.ooxml.POIXMLException;
import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.parser.impl.SimpleTimetableParser;
import ru.eqour.timetable.util.Compare;
import ru.eqour.timetable.util.JsonFileHelper;
import ru.eqour.timetable.util.ResourceHelper;

import java.io.IOException;
import java.util.List;

public class TimetableParserTests {

    private static final SimpleTimetableParser parser = new SimpleTimetableParser();

    @Test
    public void whenArgumentNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> parser.parseTimetable(null));
    }

    @Test
    public void whenValidExcelFileThenReturnValidWeek() throws IOException {
        Week expected = JsonFileHelper.loadFromFile(getParsedTimetablePath(0), Week.class);
        Week actual = parser.parseTimetable(getClass().getResourceAsStream(getTimetablePath(0))).get(0);
        Compare.compareWeeks(expected, actual);
    }

    @Test
    public void whenNotExcelFileThenThrowIOException() {
        Assert.assertThrows(IOException.class,
                () -> parser.parseTimetable(getClass().getResourceAsStream("/timetable-parser/text-123.txt")));
    }

    @Test
    public void whenExcelFileWithMergedDaysOfWeekCellsThenReturnValidWeek() throws IOException {
        Week expected = JsonFileHelper.loadFromFile(getParsedTimetablePath(0), Week.class);
        Week actual = parser.parseTimetable(getClass().getResourceAsStream(getTimetablePath(1))).get(0);
        Compare.compareWeeks(expected, actual);
    }

    @Test
    public void whenEmptyExcelFileThenReturnEmptyList() throws IOException {
        List<Week> actual = parser.parseTimetable(getClass().getResourceAsStream(getTimetablePath(2)));
        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void whenExcel97_2003FileThenReturnValidWeek() throws IOException {
        Week expected = JsonFileHelper.loadFromFile(getParsedTimetablePath(0), Week.class);
        Week actual = parser.parseTimetable(getClass().getResourceAsStream("/timetable-parser/timetable-3.xls")).get(0);
        Compare.compareWeeks(expected, actual);
    }

    @Test
    public void whenExcelOpenXMLFileThenThrowPOIXMLException() {
        Assert.assertThrows(POIXMLException.class,
                () -> parser.parseTimetable(getClass().getResourceAsStream(getTimetablePath(4))));
    }

    private String getTimetablePath(int index) {
        return "/timetable-parser/timetable-" + index + ".xlsx";
    }

    private String getParsedTimetablePath(@SuppressWarnings("SameParameterValue") int index) {
        return ResourceHelper.getFullPathToResource("/timetable-parser/timetable-parsed-" + index + ".json").toString();
    }
}
