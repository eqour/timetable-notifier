package ru.eqour.timetable;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Group;
import ru.eqour.timetable.model.Week;

import java.util.*;

public class WeekComparer {

    public static Map<String, List<Day[]>> findDifferences(Week w1, Week w2) {
        if (w1 == null || w2 == null || w1.groups == null || w2.groups == null) {
            throw new IllegalArgumentException();
        }
        Map<String, List<Day[]>> ans = new HashMap<>();
        Map<String, Group> weekMap1 = new HashMap<>();
        Map<String, Group> weekMap2 = new HashMap<>();
        for (Group g : w1.groups) {
            weekMap1.put(g.name, g);
        }
        for (Group g : w2.groups) {
            weekMap2.put(g.name, g);
        }
        for (String key : weekMap1.keySet()) {
            if (weekMap2.containsKey(key)) {
                List<Day[]> differences = findDifferences(weekMap1.get(key), weekMap2.get(key));
                if (!differences.isEmpty()) {
                    ans.put(key, differences);
                }
            }
        }
        return ans;
    }

    private static List<Day[]> findDifferences(Group g1, Group g2) {
        if (g1 == null || g2 == null) {
            throw new IllegalArgumentException();
        }
        return findDifferences(g1.days, g2.days);
    }

    private static List<Day[]> findDifferences(Day[] d1, Day[] d2) {
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
