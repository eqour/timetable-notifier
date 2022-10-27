package ru.eqour.timetable.notifier.main;

import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.notifier.api.GoogleDriveAPIImpl;
import ru.eqour.timetable.TimetableParser;
import ru.eqour.timetable.notifier.api.GoogleDriveApi;

import java.io.*;
import java.util.Scanner;

public class Application {

    private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final GoogleDriveApi googleDriveApi = new GoogleDriveAPIImpl();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ID файла: ");
        String fileId = scanner.nextLine();
        String fileVersion = googleDriveApi.getFileMetadata(fileId).version.toString();
        System.out.println("Версия файла: " + fileVersion);
        try (ByteArrayOutputStream outputStream = googleDriveApi.exportFile(fileId, XLSX_MIME_TYPE)) {
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
