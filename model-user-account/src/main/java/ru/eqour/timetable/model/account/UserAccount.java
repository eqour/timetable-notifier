package ru.eqour.timetable.model.account;

import java.util.Map;

public class UserAccount {

    private String email;
    private Map<String, NotificationSubscription> subscriptions;
    private Map<String, CommunicationChannel> channels;

    public UserAccount() {
    }

    public UserAccount(String email, Map<String, NotificationSubscription> subscriptions,
                       Map<String, CommunicationChannel> channels) {
        this.email = email;
        this.subscriptions = subscriptions;
        this.channels = channels;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, NotificationSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Map<String, NotificationSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Map<String, CommunicationChannel> getChannels() {
        return channels;
    }

    public void setChannels(Map<String, CommunicationChannel> channels) {
        this.channels = channels;
    }
}
