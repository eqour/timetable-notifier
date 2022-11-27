package ru.eqour.timetable.parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Group;
import ru.eqour.timetable.model.Week;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleTimetableParser implements TimetableParser {

    private static final int DAYS_IN_WEEK = 6;
    private static final int LESSONS_IN_DAY = 7;
    private static final int LESSON_SIZE = 3;

    public Week parseTimetable(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException();
        }
        return parseTimetable(readWorkbookSheet(createWorkbook(inputStream)));
    }

    private Workbook createWorkbook(InputStream inputStream) throws IOException {
        try {
            return new XSSFWorkbookFactory().create(inputStream);
        } catch (POIXMLException e) {
            throw e;
        } catch (Exception ignore) {
        }
        try {
            return new XSSFWorkbookFactory().create(inputStream);
        } catch (Exception ignore) {
        }
        try {
            return new HSSFWorkbookFactory().create(inputStream);
        } catch (Exception ignore) {
        }
        throw new IOException();
    }

    private TimetableSheet readWorkbookSheet(Workbook workbook) {
        Sheet sheet = workbook.getSheet(getLastPeriod(getWorkbookSheets(workbook)));
        TimetableSheet timetableSheet = new TimetableSheet();
        timetableSheet.name = sheet.getSheetName();
        timetableSheet.table = readWorkbookSheet(sheet);
        return timetableSheet;
    }

    private String getLastPeriod(String[] periods) {
        List<String> sorted = Arrays.stream(periods)
                .sorted((p1, p2) -> {
                    try {
                        String firstDate1 = p1.split("-")[0];
                        String firstDate2 = p2.split("-")[0];
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        dateFormat.setLenient(false);
                        Date date1 = dateFormat.parse(firstDate1);
                        Date date2 = dateFormat.parse(firstDate2);
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return sorted.isEmpty() ? null : sorted.get(sorted.size() - 1);
    }

    private String[] getWorkbookSheets(Workbook workbook) {
        List<String> ans = new ArrayList<>();
        workbook.forEach(sheet -> ans.add(sheet.getSheetName()));
        return ans.toArray(new String[0]);
    }

    private String[][] readWorkbookSheet(Sheet sheet) {
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

    private String getCellValue(Cell cell) {
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

    private String[][] convertToArray(List<List<String>> table) {
        int width = getMaxTableWidth(table);
        String[][] result = new String[table.size()][width];
        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < width; j++) {
                result[i][j] = j < table.get(i).size() ? table.get(i).get(j) : "";
            }
        }
        return result;
    }

    private int getMaxTableWidth(List<List<String>> table) {
        int maxWidth = 0;
        for (List<String> row : table) {
            maxWidth = Math.max(maxWidth, row.size());
        }
        return  maxWidth;
    }

    private Week parseTimetable(TimetableSheet sheet) {
        try {
            int columnOffset = 2;
            int groupsNumber = sheet.table[0].length - columnOffset;
            Week week = new Week();
            week.period = sheet.name;
            week.groups = new Group[groupsNumber];
            for (int i = 0; i < groupsNumber; i++) {
                week.groups[i] = parseGroup(sheet.table, columnOffset + i);
            }
            return week;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private Group parseGroup(String[][] table,  int column) {
        int rowOffset = 1;
        Group group = new Group();
        group.name = getTableValue(table, 0, column);
        group.days = new Day[6];
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            group.days[i] = parseDay(table, rowOffset + LESSONS_IN_DAY * LESSON_SIZE * i, column);
        }
        return group;
    }

    private Day parseDay(String[][] table, int startRow, int column) {
        Day day = new Day();
        day.date = getTableValue(table, startRow, 0);
        if (day.date == null) {
            return null;
        }
        day.lessons = new Lesson[LESSONS_IN_DAY];
        for (int i = 0; i < LESSONS_IN_DAY; i++) {
            Lesson lesson = new Lesson();
            int row = startRow + LESSON_SIZE * i;
            lesson.time = getTableValue(table, row, 1);
            lesson.discipline = getTableValue(table, row, column);
            lesson.teacher = getTableValue(table, row + 1, column);
            lesson.classroom = getTableValue(table, row + 2, column);
            if ((lesson.teacher == null || lesson.teacher.isEmpty())
                    && (lesson.classroom == null || lesson.classroom.isEmpty())
                    && (lesson.discipline == null || lesson.discipline.isEmpty())) {
                day.lessons[i] = null;
            } else {
                day.lessons[i] = lesson;
            }
        }
        return day;
    }

    private String getTableValue(String[][] table, int row, int column) {
        if (row < table.length && column < table[row].length) {
            String value = table[row][column];
            return Objects.equals(value, "") ? null : value;
        } else {
            return "";
        }
    }

    private static class TimetableSheet {
        public String name;
        public String[][] table;
    }
}
