package ru.eqour.timetable.rest.model.subscription;

public class SubscribeRequest {

    private String name;

    public SubscribeRequest() {
    }

    public SubscribeRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
