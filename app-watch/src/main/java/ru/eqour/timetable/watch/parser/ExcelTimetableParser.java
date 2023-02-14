package ru.eqour.timetable.watch.parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import ru.eqour.timetable.watch.model.Lesson;
import ru.eqour.timetable.watch.parser.impl.SimpleTimetableParser;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

/**
 * Парсер расписания для файлов Excel. Содержит основные методы для получения данных из книги.
 */
public abstract class ExcelTimetableParser implements TimetableParser {

    private final DataFormatter formatter = new DataFormatter();

    /**
     * Создаёт рабочую книгу из входного потока данных.
     *
     * @param inputStream входной поток данных.
     * @return объект {@code Workbook}.
     * @throws IOException в случае ошибки при чтении/записи данных.
     */
    protected final Workbook createWorkbook(InputStream inputStream) throws IOException {
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

    /**
     * Возвращает названия всех листов в книге.
     *
     * @param workbook рабочая книга.
     * @return массив названий листов.
     */
    protected final String[] getWorkbookSheets(Workbook workbook) {
        List<String> ans = new ArrayList<>();
        workbook.forEach(sheet -> ans.add(sheet.getSheetName()));
        return ans.toArray(new String[0]);
    }

    /**
     * Считывает лист в двумерный массив строк. Пустые ячейки записываются как пустые строки.
     *
     * @param sheet лист книги.
     * @return двумерный массив строк.
     */
    protected final String[][] readWorkbookSheet(Sheet sheet) {
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

    /**
     * Лист недели расписания.
     */
    protected final static class TimetableSheet {

        /**
         * Период, за который проходит неделя.
         */
        public final SimpleTimetableParser.Period period;

        /**
         * Двумерный массив строк, содержащий полученные из ячеек листа книги данные.
         */
        public final String[][] table;

        /**
         * Создаёт новый экземпляр класса {@code TimetableSheet}.
         *
         * @param period период, за который проходит неделя.
         * @param table двумерный массив строк, содержащий полученные из ячеек листа книги данные.
         */
        public TimetableSheet(SimpleTimetableParser.Period period, String[][] table) {
            this.period = period;
            this.table = table;
        }
    }

    /**
     * Период времени.
     */
    protected final static class Period {

        /**
         * Строковое представление периода.
         */
        public final String stringValue;

        /**
         * Дата начала периода.
         */
        public final LocalDate startDate;

        /**
         * Дата конца периода.
         */
        public final LocalDate endDate;

        /**
         * Создаёт новый экземпляр класса {@code Period}.
         *
         * @param stringValue строковое представление периода.
         * @param startDate дата начала периода.
         * @param endDate дата конца периода.
         */
        public Period(String stringValue, LocalDate startDate, LocalDate endDate) {
            this.stringValue = stringValue;
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    /**
     * Возвращает значение из двумерного массива строк с дополнительными проверками.
     *
     * @param table двумерный массив строк, содержащий полученные из ячеек листа книги данные.
     * @param row индекс строки.
     * @param column индекс столбца.
     * @return пустую строку, если происходит выход за верхние границы массива. Если полученная строка пустая, то
     * возвращается {@code null}. В противном случае возвращается строка из массива по указанным индексам.
     */
    protected final String getTableValue(String[][] table, int row, int column) {
        if (row < table.length && column < table[row].length) {
            String value = table[row][column];
            return Objects.equals(value, "") ? null : value;
        } else {
            return "";
        }
    }

    /**
     * Выполняет проверку на валидность занятия.
     *
     * @param lesson занятие.
     * @return {@code true}, если занятие валидно, в противном случае {@code false}.
     */
    protected final boolean lessonIsValid(Lesson lesson) {
        return (lesson.teacher != null && !lesson.teacher.isEmpty())
                || (lesson.classroom != null && !lesson.classroom.isEmpty())
                || (lesson.discipline != null && !lesson.discipline.isEmpty());
    }
}
