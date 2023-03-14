package ru.eqour.timetable.rest.model.channels;

public class UpdateChannelRecipientData {

    private String channel;
    private String recipient;
    private String code;

    public UpdateChannelRecipientData() {
    }

    public UpdateChannelRecipientData(String channel, String recipient, String code) {
        this.channel = channel;
        this.recipient = recipient;
        this.code = code;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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
