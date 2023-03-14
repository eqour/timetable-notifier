package ru.eqour.timetable.rest.model.channels;

public class UpdateChannelRecipientRequest {

    private String recipient;
    private String code;

    public UpdateChannelRecipientRequest() {
    }

    public UpdateChannelRecipientRequest(String recipient, String code) {
        this.recipient = recipient;
        this.code = code;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
