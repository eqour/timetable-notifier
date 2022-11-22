package ru.eqour.timetable.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.eqour.timetable.exception.RepositoryException;
import ru.eqour.timetable.model.Subscriber;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class SimpleSubscriberRepository implements SubscriberRepository {

    private final Gson gson;
    private final String path;

    public SimpleSubscriberRepository(String path) {
        this.path = path;
        gson = new Gson().newBuilder().setPrettyPrinting().create();
    }

    public Map<String, List<Subscriber>> loadSubscriberMap() {
        try {
            String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            return gson.fromJson(json, new TypeToken<Map<String, List<Subscriber>>>(){}.getType());
        } catch (IOException | InvalidPathException e) {
            throw new RepositoryException();
        }
    }

    @Override
    public List<Subscriber> getSubscribers(String groupName) {
        return loadSubscriberMap().get(groupName);
    }
}
