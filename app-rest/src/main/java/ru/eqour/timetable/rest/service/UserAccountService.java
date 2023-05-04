package ru.eqour.timetable.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.model.account.*;
import ru.eqour.timetable.rest.repository.UserAccountRepository;

import java.util.*;

@Component
public class UserAccountService {

    private UserAccountRepository repository;

    @Autowired
    public void setRepository(UserAccountRepository repository) {
        this.repository = repository;
    }

    public boolean channelTypesIsInvalid(List<String> types) {
        return types.stream().anyMatch(this::channelTypeIsInvalid);
    }

    public boolean channelTypeIsInvalid(String type) {
        return Arrays.stream(ChannelType.values()).noneMatch(t -> t.getValue().equals(type));
    }

    public boolean subscriptionTypeIsInvalid(String type) {
        return Arrays.stream(SubscriptionType.values()).noneMatch(t -> t.getValue().equals(type));
    }

    public UserAccount findByEmailOrCreateEmpty(String email) {
        UserAccount account = repository.findByEmail(email);
        if (account == null) {
            account = createUserAccount(email);
            repository.replaceByEmail(email, account);
        }
        return account;
    }

    private UserAccount createUserAccount(String email) {
        Map<String, NotificationSubscription> subscriptions = new HashMap<>();
        subscriptions.put(SubscriptionType.GROUP.getValue(),
                new NotificationSubscription(null, new ArrayList<>()));
        subscriptions.put(SubscriptionType.TEACHER.getValue(),
                new NotificationSubscription(null, new ArrayList<>()));
        Map<String, CommunicationChannel> channels = new HashMap<>();
        channels.put(ChannelType.VK.getValue(),
                new CommunicationChannel(null));
        channels.put(ChannelType.TELEGRAM.getValue(),
                new CommunicationChannel(null));
        return new UserAccount(email, subscriptions, channels);
    }
}
