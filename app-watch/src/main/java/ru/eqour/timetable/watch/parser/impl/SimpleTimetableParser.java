package ru.eqour.timetable.watch.parser.impl;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.eqour.timetable.watch.model.Day;
import ru.eqour.timetable.watch.model.Group;
import ru.eqour.timetable.watch.model.Lesson;
import ru.eqour.timetable.watch.model.Week;
import ru.eqour.timetable.watch.parser.ExcelTimetableParser;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация парсера расписания из книги Excel в соответствии со строгим шаблоном.
 */
public class SimpleTimetableParser extends ExcelTimetableParser {

    private static final int DAYS_IN_WEEK = 6;
    private static final int LESSONS_IN_DAY = 7;
    private static final int LESSON_SIZE = 3;

    @Override
    public List<Week> parseTimetable(InputStream inputStream) throws IOException {
        TimetableSheet timetableSheet = readLastWorkbookSheet(createWorkbook(inputStream));
        return timetableSheet == null ? Collections.emptyList() : Collections.singletonList(parseWeek(timetableSheet));
    }

    private TimetableSheet readLastWorkbookSheet(Workbook workbook) {
        try {
            Period lastPeriod = getLastPeriod(getWorkbookSheets(workbook));
            if (lastPeriod != null) {
                Sheet sheet = workbook.getSheet(lastPeriod.stringValue);
                return new TimetableSheet(lastPeriod, readWorkbookSheet(sheet));
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private Period getLastPeriod(String[] periods) {
        List<Period> sorted = Arrays.stream(periods)
                .map(this::parsePeriod)
                .sorted(Comparator.comparing(p -> p.startDate))
                .collect(Collectors.toList());
        return sorted.isEmpty() ? null : sorted.get(sorted.size() - 1);
    }

    private Period parsePeriod(String periodString) {
        try {
            String startDate = periodString.split("-")[0];
            String endDate = periodString.split("-")[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return new Period(periodString, LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Week parseWeek(TimetableSheet sheet) {
        try {
            int columnOffset = 2;
            int groupsNumber = sheet.table[0].length - columnOffset;
            Week week = new Week();
            week.period = sheet.period.stringValue;
            week.groups = new Group[groupsNumber];
            for (int i = 0; i < groupsNumber; i++) {
                week.groups[i] = parseGroup(sheet, columnOffset + i);
            }
            return week;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private Group parseGroup(TimetableSheet sheet, int column) {
        int rowOffset = 1;
        Group group = new Group();
        group.name = getTableValue(sheet.table, 0, column);
        group.days = new Day[6];
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            group.days[i] = parseDay(sheet.table, rowOffset + LESSONS_IN_DAY * LESSON_SIZE * i, column);
            group.days[i].date = getDayDate(sheet.period.startDate, i);
        }
        return group;
    }

    private String getDayDate(LocalDate startDate, int dayIndex) {
        LocalDate result = startDate.plusDays(dayIndex);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return result.format(formatter);
    }

    private Day parseDay(String[][] table, int startRow, int column) {
        Day day = new Day();
        day.lessons = new Lesson[LESSONS_IN_DAY];
        for (int i = 0; i < LESSONS_IN_DAY; i++) {
            Lesson lesson = new Lesson();
            int row = startRow + LESSON_SIZE * i;
            lesson.time = getTableValue(table, row, 1);
            lesson.discipline = getTableValue(table, row, column);
            lesson.teacher = getTableValue(table, row + 1, column);
            lesson.classroom = getTableValue(table, row + 2, column);
            day.lessons[i] = lessonIsValid(lesson) ? lesson : null;
        }
        return day;
    }
}
