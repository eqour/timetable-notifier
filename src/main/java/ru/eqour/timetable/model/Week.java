package ru.eqour.timetable.model;

import java.util.Arrays;

public class Week {

    public Day[] days;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Week week = (Week) o;
        return Arrays.equals(days, week.days);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(days);
    }
}
