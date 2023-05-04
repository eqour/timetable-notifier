package ru.eqour.timetable.rest.model.subscription;

import java.util.List;

public class UpdateSubscriptionChannelsRequest {

    private List<String> channels;

    public UpdateSubscriptionChannelsRequest() {
    }

    public UpdateSubscriptionChannelsRequest(List<String> channels) {
        this.channels = channels;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }
}
