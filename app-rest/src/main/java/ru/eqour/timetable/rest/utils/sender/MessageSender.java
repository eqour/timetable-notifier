package ru.eqour.timetable.rest.utils.sender;

import ru.eqour.timetable.rest.exception.SendMessageException;

public interface MessageSender {

    void sendMessage(String recipient, String title, String message) throws SendMessageException;
}
