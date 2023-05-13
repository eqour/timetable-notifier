package ru.eqour.timetable.watch.util;

import ru.eqour.timetable.sender.model.EmailSenderSettings;
import ru.eqour.timetable.watch.model.*;
import ru.eqour.timetable.watch.settings.Settings;

import java.util.*;

public class TestDataFactory {

    private static final String GROUP_KEY = "group-1";
    private static final String TEACHER_KEY = "teacher-1";

    public static Map<String, List<Subscriber>> createGroupSubscribersMap() {
        Map<String, List<Subscriber>> map = new HashMap<>();
        map.put(GROUP_KEY, createSubscribers());
        return map;
    }

    public static Map<String, List<Subscriber>> createTeacherSubscribersMap() {
        Map<String, List<Subscriber>> map = new HashMap<>();
        map.put(TEACHER_KEY, createSubscribers());
        return map;
    }

    private static List<Subscriber> createSubscribers() {
        Subscriber s1 = new Subscriber();
        s1.vkId = "vk-1";
        Subscriber s2 = new Subscriber();
        s2.telegramId = "tg-1";
        s2.email = "email-1";
        return Arrays.asList(s1, s2);
    }

    public static Map<String, List<Day[]>> createGroupDifferences() {
        Map<String, List<Day[]>> differences = new HashMap<>();
        differences.put(GROUP_KEY, createDifferences());
        return differences;
    }

    public static Map<String, List<Day[]>> createTeacherDifferences() {
        Map<String, List<Day[]>> differences = new HashMap<>();
        differences.put(TEACHER_KEY, createDifferences());
        return differences;
    }

    public static Map<String, List<Day[]>> createGroupAndTeacherDifferences() {
        Map<String, List<Day[]>> differences = new HashMap<>();
        differences.put(GROUP_KEY, createDifferences());
        differences.put(TEACHER_KEY, createDifferences());
        return differences;
    }

    private static List<Day[]> createDifferences() {
        Lesson lesson = new Lesson();
        lesson.time = "mock-time";
        lesson.classroom = "mock-classroom";
        lesson.discipline = "mock-discipline";
        lesson.teacher = "mock-teacher";
        Day day = new Day();
        day.date = "mock-date";
        day.lessons = new Lesson[] { lesson };
        return Collections.singletonList(new Day[] { day, day });
    }

    public static Week createEmptyWeek() {
        Week week = new Week();
        week.period = "";
        week.groups = new Group[0];
        return week;
    }

    public static Settings createSettings() {
        Settings settings = new Settings();
        settings.timetableFileId = "id";
        settings.telegramToken = "telegram";
        settings.vkToken = "vk";
        settings.dbConnection = "db-connection";
        settings.emailSenderSettings = new EmailSenderSettings("host", 123, "username", "password", "protocol", "false");
        return settings;
    }
}
