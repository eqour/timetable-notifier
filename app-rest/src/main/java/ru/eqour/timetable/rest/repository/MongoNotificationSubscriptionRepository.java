package ru.eqour.timetable.rest.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.db.MongoDbClient;

import java.util.List;

@Primary
@Component
public class MongoNotificationSubscriptionRepository implements NotificationSubscriptionRepository {

    private MongoDbClient client;

    @Autowired
    public void setClient(MongoDbClient client) {
        this.client = client;
    }

    @Override
    public List<String> findAllGroupSubscriptions() {
        return client.findAllGroupSubscriptions();
    }

    @Override
    public List<String> findAllTeacherSubscriptions() {
        return client.findAllTeacherSubscriptions();
    }
}
