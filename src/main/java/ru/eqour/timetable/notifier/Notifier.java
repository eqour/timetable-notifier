package ru.eqour.timetable.notifier;

public interface Notifier {

    void sendMessage(String recipient, String message);
}
