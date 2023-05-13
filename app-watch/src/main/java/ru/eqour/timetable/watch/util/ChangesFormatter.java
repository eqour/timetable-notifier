package ru.eqour.timetable.watch.util;

import ru.eqour.timetable.watch.model.Day;
import ru.eqour.timetable.watch.model.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangesFormatter {

    public static String formatChangesStringForTeacher(List<Day[]> differences) {
        return formatChangesStringForGroup(removeSameLessons(differences));
    }

    private static List<Day[]> removeSameLessons(List<Day[]> differences) {
        List<Day[]> ans = new ArrayList<>();
        for (Day[] difference : differences) {
            Day prev = new Day(difference[0]);
            Day next = new Day(difference[1]);
            for (int i = 0; i < prev.lessons.length; i++) {
                if (Objects.equals(prev.lessons[i], next.lessons[i])) {
                    prev.lessons[i] = next.lessons[i] = null;
                }
            }
            ans.add(new Day[] {prev, next});
        }
        return ans;
    }

    public static String formatChangesStringForGroup(List<Day[]> differences) {
        StringBuilder builder = new StringBuilder();
        builder.append("Изменения в расписании:\n\n");
        for (Day[] pair : differences) {
            Day day = pair[1];
            builder.append(day.date).append("\n\n");
            boolean hasLessons = false;
            for (int i = 0; i < day.lessons.length; i++) {
                Lesson lesson = day.lessons[i];
                if (lesson != null) {
                    hasLessons = true;
                    builder.append(i + 1).append(" пара, ").append(lesson.time).append("\n");
                    builder.append(getStringOrEmptyString(lesson.discipline)).append("\n");
                    if (Objects.equals(lesson.teacher, lesson.classroom)) {
                        builder.append(getStringOrEmptyString(lesson.teacher));
                    } else {
                        builder.append(getStringOrEmptyString(lesson.teacher)).append(" ")
                                .append(getStringOrEmptyString(lesson.classroom));
                    }
                    if (i != day.lessons.length - 1) {
                        builder.append("\n\n");
                    }
                }
                if (i == day.lessons.length - 1) {
                    if (!hasLessons) {
                        builder.append("Занятий нет\n\n");
                    }
                }
            }
        }
        return builder.toString();
    }

    private static String getStringOrEmptyString(String value) {
        return value == null ? "" : value;
    }
}
