package ru.eqour.timetable.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.model.channels.CommunicationChannel;
import ru.eqour.timetable.rest.repository.CommunicationChannelRepository;

import java.util.Map;
import java.util.Optional;

@Component
public class CommunicationChannelsService {

    private CommunicationChannelRepository repository;

    @Autowired
    public void setRepository(CommunicationChannelRepository repository) {
        this.repository = repository;
    }

    public Map<String, CommunicationChannel> findAllChannelsByEmail(String email) {
        return repository.findByEmailOrCreateEmpty(email);
    }

    public void updateChannelRecipient(String email, String channelId, String newRecipient) {
        Map<String, CommunicationChannel> channels = repository.findByEmailOrCreateEmpty(email);
        CommunicationChannel channel = channels.get(email);
        if (channel == null) {
            channel = new CommunicationChannel(newRecipient, true);
            channels.put(channelId, channel);
        } else {
            channel.setRecipient(newRecipient);
        }
        repository.updateAllByEmail(email, channels);
    }

    public void setActive(String email, String channelId, boolean isActive) {
        Map<String, CommunicationChannel> channels = repository.findByEmailOrCreateEmpty(email);
        CommunicationChannel channel = channels.get(channelId);
        Optional.ofNullable(channel).ifPresent(c -> channels.get(channelId).setActive(isActive));
        repository.updateAllByEmail(email, channels);
    }

    public void deleteChannel(String email, String channelId) {
        Map<String, CommunicationChannel> channels = repository.findByEmailOrCreateEmpty(email);
        channels.remove(channelId);
        repository.updateAllByEmail(email, channels);
    }
}
