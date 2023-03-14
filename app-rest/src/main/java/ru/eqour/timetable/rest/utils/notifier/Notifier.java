package ru.eqour.timetable.rest.utils.notifier;

import ru.eqour.timetable.rest.exception.SendCodeException;

public interface Notifier {

    void sendMessage(String recipient, String message) throws SendCodeException;
}
