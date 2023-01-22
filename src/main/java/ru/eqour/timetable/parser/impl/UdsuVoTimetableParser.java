package ru.eqour.timetable.parser.impl;

import org.apache.poi.ss.usermodel.Workbook;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Group;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.parser.ExcelTimetableParser;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UdsuVoTimetableParser extends ExcelTimetableParser {

    private final int GROUP_START_ROW_INDEX = 4;

    private final int periodSizeInDays;
    private OffsetDateTime currentDateTime;

    public UdsuVoTimetableParser(int periodSizeInDays) {
        this.periodSizeInDays = periodSizeInDays;
    }

    @Override
    public List<Week> parseTimetable(InputStream inputStream) throws IOException {
        if (currentDateTime == null) {
            throw new RuntimeException("currentDateTime is null");
        }
        Workbook workbook = createWorkbook(inputStream);
        List<Period> periodsForParsing = getTimetableWeeksInCurrentPeriod(workbook, currentDateTime.toLocalDate(),
                currentDateTime.toLocalDate().plusDays(periodSizeInDays));
        return periodsForParsing.stream()
                .map(p -> parseWeek(new TimetableSheet(p, readWorkbookSheet(workbook.getSheet(p.stringValue)))))
                .collect(Collectors.toList());
    }

    private List<Period> getTimetableWeeksInCurrentPeriod(Workbook workbook, LocalDate start, LocalDate end) {
        return Arrays.stream(getWorkbookSheets(workbook))
                .map(this::parsePeriod)
                .sorted(Comparator.comparing(p -> p.startDate))
                .filter(p -> (start.isAfter(p.startDate) && start.isBefore(p.endDate)
                        || end.isAfter(p.startDate) && end.isBefore(p.endDate)))
                .collect(Collectors.toList());
    }

    private Period parsePeriod(String periodString) {
        try {
            String startDate = periodString.split("-")[0];
            String endDate = periodString.split("-")[1];
            return new Period(periodString, parsePeriodDate(startDate), parsePeriodDate(endDate));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDate parsePeriodDate(String value) {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("dd.MM.yy"));
    }

    private Week parseWeek(TimetableSheet sheet) {
        Week week = new Week();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        week.period = sheet.period.startDate.format(formatter) + "-" + sheet.period.endDate.format(formatter);
        List<Group> groups = new ArrayList<>();
        int GROUP_START_COLUMN_INDEX = 3;
        for (int col = GROUP_START_COLUMN_INDEX; col < sheet.table[GROUP_START_ROW_INDEX].length; col++) {
            if (sheet.table[GROUP_START_ROW_INDEX][col].isEmpty()) break;
            groups.add(parseGroup(sheet, col));
        }
        week.groups = groups.toArray(new Group[0]);
        return week;
    }

    private Group parseGroup(TimetableSheet sheet, int groupColumnIndex) {
        int DAY_SPACING = 15;
        Group group = new Group();
        group.name = getTableValue(sheet.table, GROUP_START_ROW_INDEX, groupColumnIndex);
        List<Day> days = new ArrayList<>();
        int TIMETABLE_START_ROW_INDEX = 6;
        for (int row = TIMETABLE_START_ROW_INDEX, i = 0; row < sheet.table.length; row += DAY_SPACING, i++) {
            if (sheet.table[row][0].isEmpty()) break;
            days.add(parseDay(sheet, groupColumnIndex, row, i));
        }
        group.days = days.toArray(new Day[0]);
        return group;
    }

    private Day parseDay(TimetableSheet sheet, int groupColumnIndex, int dayRowIndex, int lessonIndex) {
        int LESSON_SPACING = 2;
        int LESSONS_IN_DAY = 7;
        Day day = new Day();
        LocalDate dayDate = sheet.period.startDate.plusDays(lessonIndex);
        day.date = dayDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        List<Lesson> lessons = new ArrayList<>();
        int maxDayRowIndex = dayRowIndex + LESSON_SPACING * LESSONS_IN_DAY;
        for (int row = dayRowIndex; row < maxDayRowIndex && row < sheet.table.length; row += LESSON_SPACING) {
            lessons.add(parseLesson(sheet, groupColumnIndex, row));
        }
        day.lessons = lessons.toArray(new Lesson[0]);
        return day;
    }

    private Lesson parseLesson(TimetableSheet sheet, int groupColumnIndex, int lessonRowIndex) {
        int TIME_COLUMN_INDEX = 2;
        Lesson lesson = new Lesson();
        lesson.time = getTableValue(sheet.table, lessonRowIndex, TIME_COLUMN_INDEX);
        lesson.discipline = getTableValue(sheet.table, lessonRowIndex, groupColumnIndex);
        String[] parsedSecondRow = parseSecondLessonRow(getTableValue(sheet.table, lessonRowIndex + 1, groupColumnIndex));
        if (parsedSecondRow == null) {
            lesson.teacher = lesson.classroom = null;
        } else {
            lesson.teacher = parsedSecondRow[0];
            lesson.classroom = parsedSecondRow[1];
        }
        return lessonIsValid(lesson) ? lesson : null;
    }

    private String[] parseSecondLessonRow(String text) {
        if (text == null) return null;
        if (text.matches("[А-яЕё]+ [А-ЯЁ]\\.[А-ЯЁ]\\. .+")) {
            return text.split("(?<=\\.) ");
        } else if (text.matches("[А-яЕё]+ [А-ЯЁ]\\.[А-ЯЁ]\\.")) {
            return new String[] { text, null };
        }
        return new String[] { text, text };
    }

    public void setCurrentDateTime(OffsetDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}
