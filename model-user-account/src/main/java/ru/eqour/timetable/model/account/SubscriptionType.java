package ru.eqour.timetable.model.account;

public enum SubscriptionType {

    GROUP("group"),
    TEACHER("teacher");

    private final String value;

    SubscriptionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
