package ru.eqour.timetable.mock;

import ru.eqour.timetable.comparer.WeekComparer;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Week;

import java.util.List;
import java.util.Map;

public class WeekComparerMock implements WeekComparer {

    private final Map<String, List<Day[]>> differences;
    private final boolean throwException;

    public WeekComparerMock(Map<String, List<Day[]>> differences, boolean throwException) {
        this.differences = differences;
        this.throwException = throwException;
    }

    @Override
    public Map<String, List<Day[]>> findDifferences(List<Week> w1, List<Week> w2) {
        return findDifferences();
    }

    @Override
    public Map<String, List<Day[]>> findDifferences(Week w1, Week w2) {
        return findDifferences();
    }

    private Map<String, List<Day[]>> findDifferences() {
        if (throwException) {
            throw new IllegalArgumentException();
        } else {
            return differences;
        }
    }
}
