package ru.eqour.timetable.comparer;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Group;
import ru.eqour.timetable.model.Week;

import java.util.*;

/**
 * Используется для нахождения различий между неделями.
 */
public class SimpleWeekComparer implements WeekComparer {

    /**
     * Находит различия в днях между двумя списками недель для каждой учебной группы.
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
        if (weeks1 == null || weeks2 == null) {
            throw new IllegalArgumentException();
        }
        Map<String, Week> weekMap = new HashMap<>();
        for (Week week : weeks1) {
            if (week != null) {
                weekMap.put(week.period, week);
            }
        }
        Map<String, List<Day[]>> ans = new HashMap<>();
        for (Week week : weeks2) {
            if (weekMap.containsKey(week.period)) {
                ans.putAll(findDifferences(weekMap.get(week.period), week));
            }
        }
        return ans;
    }

    /**
     * Находит различия в днях между двумя неделями для каждой учебной группы.
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
        if (w1 == null || w2 == null || w1.groups == null || w2.groups == null) {
            throw new IllegalArgumentException();
        }
        Map<String, List<Day[]>> ans = new HashMap<>();
        if (!Objects.equals(w1.period, w2.period)) {
            return ans;
        }
        Map<String, Group> groupMap = new HashMap<>();
        for (Group group : w1.groups) {
            groupMap.put(group.name, group);
        }
        for (Group group : w2.groups) {
            if (groupMap.containsKey(group.name)) {
                List<Day[]> differences = findDifferences(groupMap.get(group.name), group);
                if (!differences.isEmpty()) {
                    ans.put(group.name, differences);
                }
            }
        }
        return ans;
    }

    private List<Day[]> findDifferences(Group g1, Group g2) {
        if (g1 == null || g2 == null) {
            throw new IllegalArgumentException();
        }
        return findDifferences(g1.days, g2.days);
    }

    private List<Day[]> findDifferences(Day[] d1, Day[] d2) {
        if (d1 == null || d2 == null || d1.length != d2.length) {
            throw new IllegalArgumentException();
        }
        List<Day[]> ans = new ArrayList<>();
        for (int i = 0; i < d1.length; i++) {
            if (d1[i].date == null || d2[i].date == null) {
                throw new IllegalArgumentException();
            }
            if (!Objects.equals(d1[i].date, d2[i].date)) {
                continue;
            }
            if (!Objects.equals(d1[i], d2[i])) {
                ans.add(new Day[] { new Day(d1[i]), new Day(d2[i]) });
            }
        }
        return ans;
    }
}
