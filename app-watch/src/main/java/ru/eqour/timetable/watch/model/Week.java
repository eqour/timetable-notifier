package ru.eqour.timetable.watch.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Учебная неделя.
 */
public class Week {

    /**
     * Дата начала и конца недели в формате дд.мм.гггг-дд.мм.гггг.
     */
    public String period;

    /**
     * Группы студентов.
     */
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
