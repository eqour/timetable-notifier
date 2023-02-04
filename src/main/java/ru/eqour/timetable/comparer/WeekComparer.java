package ru.eqour.timetable.comparer;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Week;

import java.util.List;
import java.util.Map;

/**
 * Описывает интерфейс для нахождения различий между неделями.
 */
public interface WeekComparer {

    /**
     * Находит различия в днях между двумя списками недель для каждой учебной группы.
     *
     * @param weeks1 первый список недель.
     * @param weeks2 второй список недель.
     * @return различия между неделями в виде {@code Map<String, List<Day[]>>}, где ключ - название учебной группы,
     * а значение - список отличающихся дней, где значение под индексом {@code 0} - день из недели в {@code weeks1},
     * а значение под индексом {@code 1} - день из недели в {@code weeks2}.
     */
    Map<String, List<Day[]>> findDifferences(List<Week> weeks1, List<Week> weeks2);

    /**
     * Находит различия в днях между двумя неделями для каждой учебной группы.
     *
     * @param w1 первая неделя.
     * @param w2 вторая неделя.
     * @return различия между неделями в виде {@code Map<String, List<Day[]>>}, где ключ - название учебной группы,
     * а значение - список отличающихся дней, где значение под индексом {@code 0} - день из недели {@code w1},
     * а значение под индексом {@code 1} - день из недели {@code w2}.
     */
    Map<String, List<Day[]>> findDifferences(Week w1, Week w2);
}
