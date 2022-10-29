package ru.eqour.timetable.notifier.main;

import ru.eqour.timetable.TimetableParser;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.notifier.api.FileActualizer;
import ru.eqour.timetable.notifier.api.google.GoogleDriveApiImpl;
import ru.eqour.timetable.notifier.api.google.GoogleDriveExcelFileActualizer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ID файла: ");
        String fileId = scanner.nextLine();
        FileActualizer actualizer = new GoogleDriveExcelFileActualizer(fileId, new GoogleDriveApiImpl());
        if (actualizer.actualize()) {
            try (InputStream inputStream = new ByteArrayInputStream(actualizer.getActualFile())) {
                Week week = TimetableParser.parseTimetable(inputStream);
                System.out.println("Получена информация о расписании:");
                printTimetableWeek(week);
            }
        }
    }

    private static void printTimetableWeek(Week week) {
        for (Day day : week.days) {
            printTimetableDay(day);
        }
    }

    private static void printTimetableDay(Day day) {
        System.out.println(day.date);
        for (int i = 0; i < day.lessons.length; i++) {
            Lesson lesson = day.lessons[i];
            if (lesson == null) {
                System.out.println("\t" + (i + 1) + " -");
            } else {
                System.out.println("\t" + (i + 1));
                System.out.println("\t" + lesson.time);
                System.out.println("\t" + lesson.discipline);
                System.out.println("\t" + lesson.teacher);
                System.out.println("\t" + lesson.classroom);
            }
        }
    }
}
