package ru.eqour.timetable.rest.service.code.payload;

import java.util.Objects;

public class UpdateChannelPayload {

    private final String channel;
    private final String recipient;

    public UpdateChannelPayload(String channel, String recipient) {
        this.channel = channel;
        this.recipient = recipient;
    }

    public String getChannel() {
        return channel;
    }

    public String getRecipient() {
        return recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateChannelPayload that = (UpdateChannelPayload) o;
        return Objects.equals(channel, that.channel) && Objects.equals(recipient, that.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, recipient);
    }
}
