package ru.eqour.timetable.model;

import java.util.Arrays;
import java.util.Objects;

public class Week {

    public String period;
    public Group[] groups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Week that = (Week) o;
        return period.equals(that.period) && Arrays.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(period);
        result = 31 * result + Arrays.hashCode(groups);
        return result;
    }
}
