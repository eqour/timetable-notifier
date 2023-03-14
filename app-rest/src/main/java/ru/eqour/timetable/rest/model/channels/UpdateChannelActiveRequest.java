package ru.eqour.timetable.rest.model.channels;

public class UpdateChannelActiveRequest {

    private boolean active;

    public UpdateChannelActiveRequest() {
    }

    public UpdateChannelActiveRequest(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
