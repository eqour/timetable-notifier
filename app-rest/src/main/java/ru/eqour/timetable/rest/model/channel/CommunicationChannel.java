package ru.eqour.timetable.rest.model.channel;

public class CommunicationChannel {

    private String type;
    private String recipient;
    private boolean active;

    public CommunicationChannel() {
    }

    public CommunicationChannel(String type, String recipient, boolean active) {
        this.type = type;
        this.recipient = recipient;
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
