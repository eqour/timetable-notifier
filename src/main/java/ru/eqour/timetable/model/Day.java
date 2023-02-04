package ru.eqour.timetable.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * День расписания.
 */
public class Day {

    /**
     * Дата в формате дд.мм.гггг.
     */
    public String date;

    /**
     * Занятия за день.
     */
    public Lesson[] lessons;

    public Day() {
    }

    public Day(Day other) {
        date = other.date;
        lessons = new Lesson[other.lessons.length];
        for (int i = 0; i < other.lessons.length; i++) {
            if (other.lessons[i] == null) {
                lessons[i] = null;
            } else {
                lessons[i] = new Lesson(other.lessons[i]);
            }
        }
    }

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
