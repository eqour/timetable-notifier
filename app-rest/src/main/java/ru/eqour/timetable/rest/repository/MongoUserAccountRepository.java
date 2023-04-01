package ru.eqour.timetable.rest.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.db.MongoDbClient;
import ru.eqour.timetable.model.account.UserAccount;

@Primary
@Component
public class MongoUserAccountRepository implements UserAccountRepository {

    private MongoDbClient client;

    @Autowired
    public void setClient(MongoDbClient client) {
        this.client = client;
    }

    @Override
    public UserAccount findByEmail(String email) {
        return client.findUserAccountByEmail(email);
    }

    @Override
    public void replaceByEmail(String email, UserAccount account) {
        client.replaceUserAccountByEmail(email, account);
    }
}
