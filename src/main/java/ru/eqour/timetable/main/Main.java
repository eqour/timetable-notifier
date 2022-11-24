package ru.eqour.timetable.main;

import ru.eqour.timetable.actualizer.Application;
import ru.eqour.timetable.actualizer.SettingsManager;
import ru.eqour.timetable.repository.SimpleSubscriberRepository;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Path APP_PATH = Paths.get(System.getProperty("user.dir"));
    private static final Path SETTINGS_PATH = Paths.get(APP_PATH.toString(), "settings.json");
    private static final Path SUBSCRIBERS_PATH = Paths.get(APP_PATH.toString(), "subscribers.json");

    public static void main(String[] args) {
        new Application(
                new SettingsManager(SETTINGS_PATH.toString()),
                new SimpleSubscriberRepository(SUBSCRIBERS_PATH.toString())
        ).start();
    }
}
