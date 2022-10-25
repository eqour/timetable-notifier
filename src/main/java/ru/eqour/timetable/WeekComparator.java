package ru.eqour.timetable;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Week;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeekComparator {

    public static List<Day[]> findDifferences(Week w1, Week w2) {
        if (w1 == null || w2 == null) {
            throw new IllegalArgumentException();
        }
        return findDifferences(w1.days, w2.days);
    }

    private static List<Day[]> findDifferences(Day[] d1, Day[] d2) {
        if (d1 == null || d2 == null || d1.length != d2.length) {
            throw new IllegalArgumentException();
        }
        List<Day[]> ans = new ArrayList<>();
        for (int i = 0; i < d1.length; i++) {
            if (!Objects.equals(d1[i].date, d2[i].date) || d1[i].date == null || d2[i].date == null) {
                throw new IllegalArgumentException();
            }
            if (!Objects.equals(d1[i], d2[i])) {
                ans.add(new Day[] { new Day(d1[i]), new Day(d2[i]) });
            }
        }
        return ans;
    }
}
