package ru.eqour.timetable.comparer;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Week;

import java.util.List;
import java.util.Map;

public interface WeekComparer {

    Map<String, List<Day[]>> findDifferences(Week w1, Week w2);
}
