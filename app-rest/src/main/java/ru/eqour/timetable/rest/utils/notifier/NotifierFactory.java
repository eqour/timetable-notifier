package ru.eqour.timetable.rest.utils.notifier;

import ru.eqour.timetable.rest.exception.NotifierNotFoundException;

public class NotifierFactory {

    public static Notifier createNotifierById(String id) {
        switch (id) {
            case "vk":
            case "telegram":
                return (recipient, message) -> System.out.println("send message");
            default:
                throw new NotifierNotFoundException();
        }
    }
}
