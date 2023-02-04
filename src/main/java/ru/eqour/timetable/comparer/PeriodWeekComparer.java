package ru.eqour.timetable.comparer;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Week;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Используется для нахождения различий между неделями за определённый промежуток времени.
 */
public class PeriodWeekComparer extends SimpleWeekComparer {

    private final int periodSizeInDays;
    private final Supplier<LocalDate> localDateSupplier;

    /**
     * Создаёт новый экземпляр класса {@code PeriodWeekComparer}.
     *
     * @param periodSizeInDays длительность промежутка времени в днях.
     * @param localDateSupplier функциональный интерфейс, возвращающий дату на момент выполнения сравнения.
     */
    public PeriodWeekComparer(int periodSizeInDays, Supplier<LocalDate> localDateSupplier) {
        this.periodSizeInDays = periodSizeInDays;
        this.localDateSupplier = localDateSupplier;
    }

    /**
     * Находит различия в днях между двумя списками недель для каждой учебной группы за определённый промежуток времени.
     * Отличия находятся только у недель с одинаковым периодом и групп с совпадающими названиями.
     *
     * @param weeks1 первый список недель.
     * @param weeks2 второй список недель.
     * @return различия между неделями в виде {@code Map<String, List<Day[]>>}, где ключ - название учебной группы,
     * а значение - список отличающихся дней, где значение под индексом {@code 0} - день из недели в {@code weeks1},
     * а значение под индексом {@code 1} - день из недели в {@code weeks2}.
     */
    @Override
    public Map<String, List<Day[]>> findDifferences(List<Week> weeks1, List<Week> weeks2) {
        return filterDifferences(super.findDifferences(weeks1, weeks2));
    }

    /**
     * Находит различия в днях между двумя неделями для каждой учебной группы за определённый промежуток времени.
     * Отличия находятся только у групп с совпадающими названиями.
     *
     * @param w1 первая неделя.
     * @param w2 вторая неделя.
     * @return различия между неделями в виде {@code Map<String, List<Day[]>>}, где ключ - название учебной группы,
     * а значение - список отличающихся дней, где значение под индексом {@code 0} - день из недели {@code w1},
     * а значение под индексом {@code 1} - день из недели {@code w2}.
     */
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
