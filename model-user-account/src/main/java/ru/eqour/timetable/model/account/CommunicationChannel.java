package ru.eqour.timetable.model.account;

public class CommunicationChannel {

    private String recipient;

    public CommunicationChannel() {
    }

    public CommunicationChannel(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
