package ru.eqour.timetable.model.account;

public enum ChannelType {

    EMAIL("email"),
    VK("vk"),
    TELEGRAM("telegram");

    private final String value;

    ChannelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
