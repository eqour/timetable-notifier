package ru.eqour.timetable.watch.converter;

import ru.eqour.timetable.watch.model.Day;
import ru.eqour.timetable.watch.model.Group;
import ru.eqour.timetable.watch.model.Lesson;
import ru.eqour.timetable.watch.model.Week;

import java.util.*;

public class WeekConverter {

    public List<Week> convertToTeacherWeeks(List<Week> studentWeeks) {
        if (studentWeeks == null) throw new IllegalArgumentException();
        List<Week> teacherWeeks = new ArrayList<>();
        for (Week studentWeek : studentWeeks) {
            teacherWeeks.add(convertToTeacherWeek(studentWeek));
        }
        return teacherWeeks;
    }

    public Week convertToTeacherWeek(Week studentWeek) {
        if (studentWeek == null) throw new IllegalArgumentException();
        Map<String, Day[]> teacherDayMap = createTeacherDayMap(studentWeek.groups);
        Week teacherWeek = new Week();
        teacherWeek.period = studentWeek.period;
        teacherWeek.groups = new Group[teacherDayMap.size()];
        int i = 0;
        for (Map.Entry<String, Day[]> entry : teacherDayMap.entrySet()) {
            Group group = new Group();
            group.name = entry.getKey();
            group.days = entry.getValue();
            teacherWeek.groups[i] = group;
            i++;
        }
        return teacherWeek;
    }

    private int[] getMaxSizes(Group[] groups) {
        int maxDaysSize = 0, maxLessonsSize = 0;
        for (Group group : groups) {
            maxDaysSize = Math.max(maxDaysSize, group.days.length);
            for (Day day : group.days) {
                maxLessonsSize = Math.max(maxLessonsSize, day.lessons.length);
            }
        }
        return new int[] {maxDaysSize, maxLessonsSize};
    }

    private Map<String, Day[]> createTeacherDayMap(Group[] studentGroups) {
        Map<String, Day[]> teacherDayMap = new TreeMap<>(Comparator.comparing(k -> k));
        int[] maxSizes = getMaxSizes(studentGroups);
        int maxDaysSize = maxSizes[0];
        int maxLessonsSize = maxSizes[1];
        for (Group group : studentGroups) {
            for (int i = 0; i < maxDaysSize && i < group.days.length; i++) {
                Day day = group.days[i];
                for (int j = 0; j < maxLessonsSize && j < day.lessons.length; j++) {
                    Lesson lesson = day.lessons[j];
                    if (lesson != null && lesson.teacher != null) {
                        if (!teacherDayMap.containsKey(lesson.teacher)) {
                            Day[] days = new Day[maxDaysSize];
                            for (int k = 0; k < maxDaysSize; k++) {
                                days[k] = new Day();
                                days[k].date = group.days[k].date;
                                days[k].lessons = new Lesson[maxLessonsSize];
                            }
                            teacherDayMap.put(lesson.teacher, days);
                        }
                        if (teacherDayMap.get(lesson.teacher)[i] == null) {
                            teacherDayMap.get(lesson.teacher)[i] = new Day();
                            teacherDayMap.get(lesson.teacher)[i].lessons = new Lesson[maxLessonsSize];
                        }
                        if (teacherDayMap.get(lesson.teacher)[i].lessons[j] == null) {
                            teacherDayMap.get(lesson.teacher)[i].lessons[j] = new Lesson(lesson);
                            teacherDayMap.get(lesson.teacher)[i].lessons[j].teacher = group.name;
                        }
                    }
                }
            }
        }
        return teacherDayMap;
    }
}
