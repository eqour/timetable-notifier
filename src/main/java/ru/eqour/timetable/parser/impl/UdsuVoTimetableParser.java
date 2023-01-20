package ru.eqour.timetable.parser.impl;

import org.apache.poi.ss.usermodel.Workbook;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.parser.ExcelTimetableParser;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UdsuVoTimetableParser extends ExcelTimetableParser {

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
        for (int i = 0; i < sheet.table.length; i++) {
            for (int j = 0; j < sheet.table[i].length; j++) {
                System.out.print(sheet.table[i][j] + "\t");
            }
            System.out.println();
        }
        return null;
    }

    public void setCurrentDateTime(OffsetDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}
