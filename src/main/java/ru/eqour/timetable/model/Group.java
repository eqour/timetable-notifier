package ru.eqour.timetable.model;

import java.util.Arrays;
import java.util.Objects;

public class Group {

    public String name;
    public Day[] days;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return name.equals(group.name) && Arrays.equals(days, group.days);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(days);
        return result;
    }
}