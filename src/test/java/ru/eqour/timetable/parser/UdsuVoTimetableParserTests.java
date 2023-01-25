package ru.eqour.timetable.parser;

import com.google.common.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.parser.impl.UdsuVoTimetableParser;
import ru.eqour.timetable.util.Compare;
import ru.eqour.timetable.util.JsonFileHelper;
import ru.eqour.timetable.util.ResourceHelper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class UdsuVoTimetableParserTests {

    @Test
    public void whenArgumentNullThenThrowIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class,
                () -> createSimpleUdsuParser().parseTimetable(null));
    }

    @Test
    public void whenNotExcelFileThenThrowIOException() {
        Assert.assertThrows(IOException.class,
                () -> createSimpleUdsuParser().parseTimetable(
                        getClass().getResourceAsStream("/timetable-parser/simple/text-123.txt")));
    }

    private UdsuVoTimetableParser createSimpleUdsuParser() {
        int defaultPeriodSizeInDays = 7;
        UdsuVoTimetableParser parser = new UdsuVoTimetableParser(defaultPeriodSizeInDays);
        parser.setCurrentDate(LocalDate.of(2023, 1, 1));
        return parser;
    }

    @Test
    public void whenOnePeriodAndNoWeeksInPeriodThenReturnEmptyList() throws IOException {
        TimetableParser parser = createParser(LocalDate.parse("2022-11-04"), 5);
        List<Week> weeks = parser.parseTimetable(getClass().getResourceAsStream(getTimetablePath(0)));
        Assert.assertNotNull(weeks);
        Assert.assertTrue(weeks.isEmpty());
    }

    @Test
    public void whenValidExcelFileAndCheck0WeekThenReturnValidWeek() throws IOException {
        List<Week> expected = JsonFileHelper.loadFromFile(getParsedTimetablePath(0, 0),
                new TypeToken<List<Week>>(){}.getType());
        Week actual = createParser(LocalDate.parse("2022-09-03"), 7)
                .parseTimetable(getClass().getResourceAsStream(getTimetablePath(0))).get(0);
        Assert.assertEquals(1, expected.size());
        Compare.compareWeeks(expected.get(0), actual);
    }

    @Test
    public void whenValidExcelFileAndCheck2WeekThenReturnValidWeek() throws IOException {
        List<Week> expected = JsonFileHelper.loadFromFile(getParsedTimetablePath(0, 2),
                new TypeToken<List<Week>>(){}.getType());
        Week actual = createParser(LocalDate.parse("2023-01-09"), 7)
                .parseTimetable(getClass().getResourceAsStream(getTimetablePath(0))).get(0);
        Assert.assertEquals(1, expected.size());
        Compare.compareWeeks(expected.get(0), actual);
    }

    private static UdsuVoTimetableParser createParser(LocalDate date, int days) {
        UdsuVoTimetableParser parser = new UdsuVoTimetableParser(days);
        parser.setCurrentDate(date);
        return parser;
    }

    private static String getTimetablePath(@SuppressWarnings("SameParameterValue") int index) {
        return "/timetable-parser/udsu/udsu-" + index + ".xlsx";
    }

    private static String getParsedTimetablePath(@SuppressWarnings("SameParameterValue") int index, int subIndex) {
        return ResourceHelper.getFullPathToResource("/timetable-parser/udsu/timetable-parsed-" +
                index + "-" + subIndex + ".json").toString();
    }
}
