package ru.eqour.timetable;

import org.apache.poi.ss.usermodel.*;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimetableParser {

    private static final int DAYS_IN_WEEK = 6;
    private static final int LESSONS_IN_DAY = 7;
    private static final int LESSON_SIZE = 3;

    public static Week parseTimetable(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException();
        }
        return parseTimetable(readFirstWorkbookSheet(createWorkbook(inputStream)));
    }

    private static Workbook createWorkbook(InputStream inputStream) throws IOException {
        return WorkbookFactory.create(inputStream);
    }

    private static String[][] readFirstWorkbookSheet(Workbook workbook) {
        return readWorkbookSheet(workbook.getSheetAt(0));
    }

    private static String[][] readWorkbookSheet(Sheet sheet) {
        List<List<String>> table = new ArrayList<>();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                List<String> line = new ArrayList<>();
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    line.add(getCellValue(row.getCell(j)));
                }
                table.add(line);
            } else {
                table.add(Collections.emptyList());
            }
        }
        return convertToArray(table);
    }

    private static String getCellValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING: return cell.getStringCellValue();
                case NUMERIC: return Double.toString(cell.getNumericCellValue());
                default: return "";
            }
        } else {
            return "";
        }
    }

    private static String[][] convertToArray(List<List<String>> table) {
        int width = getMaxTableWidth(table);
        String[][] result = new String[table.size()][width];
        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < width; j++) {
                result[i][j] = j < table.get(i).size() ? table.get(i).get(j) : "";
            }
        }
        return result;
    }

    private static int getMaxTableWidth(List<List<String>> table) {
        int maxWidth = 0;
        for (List<String> row : table) {
            maxWidth = Math.max(maxWidth, row.size());
        }
        return  maxWidth;
    }

    private static Week parseTimetable(String[][] table) {
        Week week = new Week();
        week.days = new Day[6];
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            week.days[i] = parseDay(table, LESSONS_IN_DAY * LESSON_SIZE * i, 0);
        }
        return week;
    }

    private static Day parseDay(String[][] table, int startRow, int startColumn) {
        Day day = new Day();
        day.date = getTableValue(table, startRow, startColumn);
        if (day.date.isEmpty()) {
            return null;
        }
        day.lessons = new Lesson[LESSONS_IN_DAY];
        for (int i = 0; i < LESSONS_IN_DAY; i++) {
            Lesson lesson = new Lesson();
            int row = startRow + LESSON_SIZE * i;
            lesson.time = getTableValue(table, row, startColumn + 1);
            lesson.discipline = getTableValue(table, row, startColumn + 2);
            lesson.teacher = getTableValue(table, row + 1, startColumn + 2);
            lesson.classroom = getTableValue(table, row + 2, startColumn + 2);
            if (lesson.teacher.isEmpty() && lesson.discipline.isEmpty() && lesson.classroom.isEmpty()) {
                day.lessons[i] = null;
            } else {
                day.lessons[i] = lesson;
            }
        }
        return day;
    }

    private static String getTableValue(String[][] table, int row, int column) {
        if (row < table.length && column < table[row].length) {
            return table[row][column];
        } else {
            return "";
        }
    }
}
