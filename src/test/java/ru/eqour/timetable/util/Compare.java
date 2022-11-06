package ru.eqour.timetable.util;

import org.junit.Assert;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Group;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;

public class Compare {

    public static void compareWeeks(Week a, Week b) {
        Assert.assertEquals(a.period, b.period);
        Assert.assertEquals(a.groups.length, b.groups.length);
        for (int i = 0; i < a.groups.length; i++) {
            compareGroups(a.groups[i], b.groups[i]);
        }
    }

    public static void compareGroups(Group a, Group b) {
        Assert.assertEquals(a.name, b.name);
        Assert.assertEquals(a.days.length, b.days.length);
        for (int i = 0; i < a.days.length; i++) {
            compareDays(a.days[i], b.days[i]);
        }
    }

    public static void compareDays(Day a, Day b) {
        Assert.assertEquals(a.date, b.date);
        Assert.assertEquals(a.lessons.length, b.lessons.length);
        for (int i = 0; i < a.lessons.length; i++) {
            compareLessons(a.lessons[i], b.lessons[i]);
        }
    }

    public static void compareLessons(Lesson a, Lesson b) {
        if (a != null || b != null) {
            if (a == null || b == null) {
                Assert.fail();
            }
            Assert.assertEquals(a.time, b.time);
            Assert.assertEquals(a.discipline, b.discipline);
            Assert.assertEquals(a.teacher, b.teacher);
            Assert.assertEquals(a.classroom, b.classroom);
        }
    }
}
