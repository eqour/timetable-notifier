package ru.eqour.timetable.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.model.channel.CommunicationChannel;
import ru.eqour.timetable.rest.model.subscription.NotificationSubscription;
import ru.eqour.timetable.rest.model.user.UserAccount;
import ru.eqour.timetable.rest.repository.UserAccountRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserAccountService {

    public static final String SUB_GROUP_TYPE = "group";
    public static final String SUB_TEACHER_TYPE = "teacher";
    private static final String CHANNEL_VK = "vk";
    private static final String CHANNEL_TELEGRAM = "telegram";

    private UserAccountRepository repository;

    @Autowired
    public void setRepository(UserAccountRepository repository) {
        this.repository = repository;
    }

    public boolean channelTypeIsInvalid(String type) {
        return !CHANNEL_VK.equals(type) && !CHANNEL_TELEGRAM.equals(type);
    }

    public boolean subscriptionTypeIsInvalid(String type) {
        return !SUB_GROUP_TYPE.equals(type) && !SUB_TEACHER_TYPE.equals(type);
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
        subscriptions.put(SUB_GROUP_TYPE, new NotificationSubscription(SUB_GROUP_TYPE, null));
        subscriptions.put(SUB_TEACHER_TYPE, new NotificationSubscription(SUB_TEACHER_TYPE, null));
        Map<String, CommunicationChannel> channels = new HashMap<>();
        channels.put(CHANNEL_VK, new CommunicationChannel(CHANNEL_VK, null, false));
        channels.put(CHANNEL_TELEGRAM, new CommunicationChannel(CHANNEL_TELEGRAM, null, false));
        return new UserAccount(email, subscriptions, channels);
    }
}
