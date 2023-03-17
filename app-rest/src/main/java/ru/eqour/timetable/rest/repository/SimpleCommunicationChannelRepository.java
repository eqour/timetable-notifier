package ru.eqour.timetable.rest.repository;

import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.model.channels.CommunicationChannel;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleCommunicationChannelRepository implements CommunicationChannelRepository {

    private final Map<String, Map<String, CommunicationChannel>> channelsData = new HashMap<>();

    private Map<String, CommunicationChannel> getChannelsInstance(String email) {
        if (!channelsData.containsKey(email)) {
            channelsData.put(email, createDefault());
        }
        return channelsData.get(email);
    }

    private Map<String, CommunicationChannel> createDefault() {
        return new HashMap<String, CommunicationChannel>() {{
            put("vk", null);
            put("telegram", null);
        }};
    }

    @Override
    public Map<String, CommunicationChannel> findByEmailOrCreateEmpty(String email) {
        return getChannelsInstance(email);
    }

    @Override
    public void updateAllByEmail(String email, Map<String, CommunicationChannel> channels) {
        channelsData.put(email, channels);
    }
}
