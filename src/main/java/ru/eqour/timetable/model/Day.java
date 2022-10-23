package ru.eqour.timetable.model;

import java.util.Arrays;
import java.util.Objects;

public class Day {

    public String date;
    public Lesson[] lessons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return date.equals(day.date) && Arrays.equals(lessons, day.lessons);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(date);
        result = 31 * result + Arrays.hashCode(lessons);
        return result;
    }
}
