package ru.eqour.timetable.model.account;

import java.util.List;

public class NotificationSubscription {

    private String name;
    private List<String> channels;

    public NotificationSubscription() {
    }

    public NotificationSubscription(String name, List<String> channels) {
        this.name = name;
        this.channels = channels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}
