package ru.eqour.timetable.rest.repository;

import ru.eqour.timetable.rest.model.channels.CommunicationChannel;

import java.util.Map;

public interface CommunicationChannelRepository {

    Map<String, CommunicationChannel> findByEmailOrCreateEmpty(String email);
    void updateAllByEmail(String email, Map<String, CommunicationChannel> channels);
}
