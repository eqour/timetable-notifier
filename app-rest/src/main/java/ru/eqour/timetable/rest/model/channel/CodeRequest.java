package ru.eqour.timetable.rest.model.channel;

public class CodeRequest {

    private String recipient;

    public CodeRequest() {
    }

    public CodeRequest(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
