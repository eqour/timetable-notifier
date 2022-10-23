package ru.eqour.timetable.notifier.main;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.notifier.api.GoogleDriveAPI;
import ru.eqour.timetable.parser.TimetableParser;

import java.io.*;
import java.util.Scanner;

public class Application {

    private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ID файла: ");
        String fileId = scanner.nextLine();
        String fileVersion = GoogleDriveAPI.getFileMetadata(fileId).getVersion().toString();
        System.out.println("Версия файла: " + fileVersion);
        try (ByteArrayOutputStream outputStream = GoogleDriveAPI.exportFile(fileId, XLSX_MIME_TYPE)) {
            try (InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
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
