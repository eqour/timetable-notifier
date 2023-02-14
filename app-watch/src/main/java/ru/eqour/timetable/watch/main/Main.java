package ru.eqour.timetable.watch.main;

import ru.eqour.timetable.watch.repository.SimpleSubscriberRepository;
import ru.eqour.timetable.watch.util.AppConstants;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Path SUBSCRIBERS_PATH = Paths.get(AppConstants.APP_DATA_PATH.toString(), "subscribers.json");

    public static void main(String[] args) throws InterruptedException {
        new UdSUApplication(new SimpleSubscriberRepository(SUBSCRIBERS_PATH.toString())).start();
    }
}
