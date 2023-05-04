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
    public List<Subscriber> getSubscribers(String groupName) {
        List<UserAccount> accounts = client.findAllUserAccountsByGroup(groupName);
        List<Subscriber> subscribers = new ArrayList<>();
        for (UserAccount account : accounts) {
            List<String> groupSubscriptionChannels = account.getSubscriptions().get(SubscriptionType.GROUP.toString()).getChannels();
            Subscriber subscriber = new Subscriber();
            subscriber.vkId = getChannelId(account.getChannels(), groupSubscriptionChannels, ChannelType.VK.getValue());
            subscriber.telegramId = getChannelId(account.getChannels(), groupSubscriptionChannels,  ChannelType.TELEGRAM.getValue());
            subscribers.add(subscriber);
        }
        return subscribers;
    }

    private String getChannelId(Map<String, CommunicationChannel> channels, List<String> subscriptionChannels, String key) {
        return subscriptionChannels.contains(key) ? channels.get(key).getRecipient() : null;
    }
}
