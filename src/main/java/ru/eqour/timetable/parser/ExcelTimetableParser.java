package ru.eqour.timetable.parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.parser.impl.SimpleTimetableParser;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

public abstract class ExcelTimetableParser implements TimetableParser {

    private final DataFormatter formatter = new DataFormatter();

    protected Workbook createWorkbook(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException();
        }
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

    protected String[] getWorkbookSheets(Workbook workbook) {
        List<String> ans = new ArrayList<>();
        workbook.forEach(sheet -> ans.add(sheet.getSheetName()));
        return ans.toArray(new String[0]);
    }

    protected String[][] readWorkbookSheet(Sheet sheet) {
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
            return formatter.formatCellValue(cell);
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

    protected static class TimetableSheet {

        public final SimpleTimetableParser.Period period;
        public final String[][] table;

        public TimetableSheet(SimpleTimetableParser.Period period, String[][] table) {
            this.period = period;
            this.table = table;
        }
    }

    protected static class Period {

        public final String stringValue;
        public final LocalDate startDate, endDate;

        public Period(String stringValue, LocalDate startDate, LocalDate endDate) {
            this.stringValue = stringValue;
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    protected String getTableValue(String[][] table, int row, int column) {
        if (row < table.length && column < table[row].length) {
            String value = table[row][column];
            return Objects.equals(value, "") ? null : value;
        } else {
            return "";
        }
    }

    protected boolean lessonIsValid(Lesson lesson) {
        return (lesson.teacher != null && !lesson.teacher.isEmpty())
                || (lesson.classroom != null && !lesson.classroom.isEmpty())
                || (lesson.discipline != null && !lesson.discipline.isEmpty());
    }
}
