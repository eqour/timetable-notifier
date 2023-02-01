package ru.eqour.timetable.comparer;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Week;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PeriodWeekComparer extends SimpleWeekComparer {

    private final int periodSizeInDays;
    private final Supplier<LocalDate> localDateSupplier;

    public PeriodWeekComparer(int periodSizeInDays, Supplier<LocalDate> localDateSupplier) {
        this.periodSizeInDays = periodSizeInDays;
        this.localDateSupplier = localDateSupplier;
    }

    @Override
    public Map<String, List<Day[]>> findDifferences(List<Week> weeks1, List<Week> weeks2) {
        return filterDifferences(super.findDifferences(weeks1, weeks2));
    }

    @Override
    public Map<String, List<Day[]>> findDifferences(Week w1, Week w2) {
        return filterDifferences(super.findDifferences(w1, w2));
    }

    private Map<String, List<Day[]>> filterDifferences(Map<String, List<Day[]>> differences) {
        Map<String, List<Day[]>> result = new HashMap<>();
        differences.forEach((key, value) -> {
            value.removeIf(difference -> !dateInPeriod(parseDate(difference[0].date)));
            if (!value.isEmpty()) {
                result.put(key, value);
            }
        });
        return result;
    }

    private boolean dateInPeriod(LocalDate date) {
        LocalDate min = localDateSupplier.get();
        LocalDate max = min.plusDays(periodSizeInDays);
        return date.isEqual(min) || date.isEqual(max) || date.isAfter(min) && date.isBefore(max);
    }

    private LocalDate parseDate(String stringDate) {
        return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
