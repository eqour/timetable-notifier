package ru.eqour.timetable;

import com.google.gson.Gson;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.notifier.api.GoogleDriveAPI;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DemoApp {

    private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String FILE_ID = "19LhM5qwpqU42Wqlxfwf6Esdq2qejI7_Fnc1w9-4r1eM";
    private static final Path DATA_PATH = Paths.get(System.getProperty("user.dir"), "data.json");

    private final Gson GSON = new Gson().newBuilder().setPrettyPrinting().create();

    private AppData data;

    public static void main(String[] args) {
        DemoApp app = new DemoApp();
        app.run();
    }

    public DemoApp() {
    }

    public void run() {
        long timer = System.currentTimeMillis();
        //noinspection InfiniteLoopStatement
        while (true) {
            long current = System.currentTimeMillis();
            if (current > timer + 30000) {
                timer = current;
                actualize();
            } else {
                Thread.yield();
            }
        }
    }

    private void actualize() {
        System.out.println("Загрузка сохранённых данных");
        loadData();
        System.out.println("Получение версии файла");
        Long actualVersion = getFileVersion();
        if (actualVersion != data.version) {
            System.out.println("Получение файла с обновлённым расписанием");
            Week actualWeek = getActualWeek();
            if (data.savedWeek != null) {
                List<Day[]> differences = WeekComparator.findDifferences(data.savedWeek, actualWeek);
                if (!differences.isEmpty()) {
                    printDifferences(differences);
                    sendMessage(data.telegramToken, formatChangesString(differences));
                }
            }
            data.version = actualVersion;
            data.savedWeek = actualWeek;
            System.out.println("Сохранение данных");
            saveData();
        } else {
            System.out.println("Расписание в актуальном состоянии");
        }
    }

    private void sendMessage(String token, String message) {
        try {
            String text = URLEncoder.encode(message, "UTF-8");
            String url = "https://api.telegram.org/bot" + token + "/sendMessage?chat_id=" + data.telegramUserId + "&text=" + text;
            getHTML(url);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void getHTML(String urlToRead) {
        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();
        } catch (Exception e) {
            System.out.println("Не удалось выполнить запрос");
        }
    }

    private void printDifferences(List<Day[]> differences) {
        System.out.println("Изменения:");
        for (Day[] pairs : differences) {
            System.out.println("-----");
            System.out.println("до:");
            printTimetableDay(pairs[0]);
            System.out.println("после:");
            printTimetableDay(pairs[1]);
            System.out.println("-----");
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

    private String formatChangesString(List<Day[]> days) {
        StringBuilder builder = new StringBuilder();
        builder.append("Изменения в расписании:\n\n");
        for (Day[] pair : days) {
            Day day = pair[1];
            builder.append(day.date).append("\n\n");
            for (int i = 0; i < day.lessons.length; i++) {
                Lesson lesson = day.lessons[i];
                if (lesson != null) {
                    builder.append(i + 1).append(" пара, ").append(lesson.time).append("\n");
                    builder.append(lesson.discipline).append("\n");
                    builder.append(lesson.teacher).append(" ").append(lesson.classroom);
                    if (i != day.lessons.length - 1) {
                        builder.append("\n\n");
                    }
                }
            }
        }
        return builder.toString();
    }

    private Long getFileVersion() {
        try {
            return GoogleDriveAPI.getFileMetadata(FILE_ID).getVersion();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Week getActualWeek() {
        try {
            ByteArrayOutputStream outputStream = GoogleDriveAPI.exportFile(FILE_ID, XLSX_MIME_TYPE);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                return TimetableParser.parseTimetable(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() {
        try {
            String json = new String(Files.readAllBytes(DATA_PATH), StandardCharsets.UTF_8);
            data = GSON.fromJson(json, AppData.class);
        } catch (IOException e) {
            data = new AppData();
        }
    }

    private void saveData() {
        String json = GSON.toJson(data);
        try (PrintWriter writer = new PrintWriter(DATA_PATH.toString(), "UTF-8")) {
            writer.write(json);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static class AppData {
        public long version;
        public Week savedWeek;
        public String telegramToken;
        public String telegramUserId;
    }
}
