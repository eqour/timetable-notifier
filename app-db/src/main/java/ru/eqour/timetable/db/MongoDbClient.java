package ru.eqour.timetable.db;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import ru.eqour.timetable.db.exception.DbException;
import ru.eqour.timetable.db.model.CollectionObject;
import ru.eqour.timetable.model.account.SubscriptionType;
import ru.eqour.timetable.model.account.UserAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MongoDbClient {

    private static final String DATABASE = "timetable-notifier";
    private static final String COLLECTION_USER_ACCOUNTS = "user-accounts";
    private static final String COLLECTION_SUBSCRIPTIONS = "subscriptions";

    private final String connectionString;
    private final Gson gson;

    public MongoDbClient(String connectionString) {
        this.connectionString = connectionString;
        this.gson = new Gson();
    }

    public UserAccount findUserAccountByEmail(String email) {
        return useMongoDb(database -> {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_USER_ACCOUNTS);
            BasicDBObject query = new BasicDBObject("email", email);
            FindIterable<Document> iterable = collection.find(query);
            try (MongoCursor<Document> cursor = iterable.cursor()) {
                if (cursor.hasNext()) {
                    return gson.fromJson(cursor.next().toJson(), UserAccount.class);
                }
            }
            return null;
        });
    }

    public List<UserAccount> findAllUserAccountsByGroup(String group) {
        return findUserAccounts(SubscriptionType.GROUP, group);
    }

    public List<UserAccount> findAllUserAccountsByTeacherSubscription(String teacher) {
        return findUserAccounts(SubscriptionType.TEACHER, teacher);
    }

    private List<UserAccount> findUserAccounts(SubscriptionType subscriptionType, String name) {
        return useMongoDb(database -> {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_USER_ACCOUNTS);
            BasicDBObject query = new BasicDBObject("subscriptions." + subscriptionType.getValue() + ".name", name);
            FindIterable<Document> iterable = collection.find(query);
            List<UserAccount> accounts = new ArrayList<>();
            try (MongoCursor<Document> cursor = iterable.cursor()) {
                while (cursor.hasNext()) {
                    accounts.add(gson.fromJson(cursor.next().toJson(), UserAccount.class));
                }
            }
            return accounts;
        });
    }

    public void replaceUserAccountByEmail(String email, UserAccount userAccount) {
        useMongoDb(database -> {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_USER_ACCOUNTS);
            BasicDBObject query = new BasicDBObject("email", email);
            collection.findOneAndDelete(query);
            String json = gson.toJson(userAccount);
            collection.insertOne(Document.parse(json));
        });
    }

    public List<String> findAllGroupSubscriptions() {
        return useMongoDb(database -> {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_SUBSCRIPTIONS);
            BasicDBObject query = new BasicDBObject("type", SubscriptionType.GROUP.getValue());
            return getCollectionOrEmpty(collection.find(query));
        });
    }

    public List<String> findAllTeacherSubscriptions() {
        return useMongoDb(database -> {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_SUBSCRIPTIONS);
            BasicDBObject query = new BasicDBObject("type", SubscriptionType.TEACHER.getValue());
            return getCollectionOrEmpty(collection.find(query));
        });
    }

    private List<String> getCollectionOrEmpty(FindIterable<Document> iterable) {
        try (MongoCursor<Document> cursor = iterable.cursor()) {
            if (cursor.hasNext()) {
                return gson.fromJson(cursor.next().toJson(), CollectionObject.class).getCollection();
            }
        }
        return new ArrayList<>();
    }

    private void useMongoDb(MongoConsumer consumer) {
        try (MongoClient client = MongoClients.create(connectionString)) {
            consumer.accept(client.getDatabase(DATABASE));
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    private <T> T useMongoDb(MongoFunction<T> function) {
        try (MongoClient client = MongoClients.create(connectionString)) {
            return function.apply(client.getDatabase(DATABASE));
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    private interface MongoConsumer extends Consumer<MongoDatabase> {
        @Override
        void accept(MongoDatabase t) throws RuntimeException;
    }

    private interface MongoFunction<R> extends Function<MongoDatabase, R> {
        @Override
        R apply(MongoDatabase mongoDatabase) throws RuntimeException;
    }
}
