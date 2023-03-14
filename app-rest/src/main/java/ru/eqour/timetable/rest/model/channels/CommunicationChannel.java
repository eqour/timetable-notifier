package ru.eqour.timetable.rest.model.channels;

public class CommunicationChannel {

    private String recipient;
    private boolean active;

    public CommunicationChannel() {
    }

    public CommunicationChannel(String recipient, boolean active) {
        this.recipient = recipient;
        this.active = active;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
