package ru.eqour.timetable.watch.repository;

import ru.eqour.timetable.db.MongoDbClient;
import ru.eqour.timetable.model.account.*;
import ru.eqour.timetable.watch.model.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoSubscriberRepository implements SubscriberRepository {

    private final MongoDbClient client;

    public MongoSubscriberRepository(MongoDbClient client) {
        this.client = client;
    }

    @Override
    public List<Subscriber> getSubscribersByStudentGroup(String groupName) {
        List<UserAccount> accounts = client.findAllUserAccountsByGroup(groupName);
        List<Subscriber> subscribers = new ArrayList<>();
        for (UserAccount account : accounts) {
            List<String> groupSubscriptionChannels = account.getSubscriptions().get(SubscriptionType.GROUP.toString()).getChannels();
            subscribers.add(createSubscriber(account, groupSubscriptionChannels));
        }
        return subscribers;
    }

    @Override
    public List<Subscriber> getSubscribersByTeacher(String teacherName) {
        List<UserAccount> accounts = client.findAllUserAccountsByTeacherSubscription(teacherName);
        List<Subscriber> subscribers = new ArrayList<>();
        for (UserAccount account : accounts) {
            List<String> teacherSubscriptionChannels = account.getSubscriptions().get(SubscriptionType.TEACHER.toString()).getChannels();
            subscribers.add(createSubscriber(account, teacherSubscriptionChannels));
        }
        return subscribers;
    }

    private Subscriber createSubscriber(UserAccount account, List<String> subscriptionChannels) {
        Subscriber subscriber = new Subscriber();
        subscriber.vkId = getChannelId(account.getChannels(), subscriptionChannels, ChannelType.VK.getValue());
        subscriber.telegramId = getChannelId(account.getChannels(), subscriptionChannels, ChannelType.TELEGRAM.getValue());
        subscriber.email = getChannelId(account.getChannels(), subscriptionChannels, ChannelType.EMAIL.getValue());
        return subscriber;
    }

    private String getChannelId(Map<String, CommunicationChannel> channels, List<String> subscriptionChannels, String key) {
        return subscriptionChannels.contains(key) ? channels.get(key).getRecipient() : null;
    }
}
