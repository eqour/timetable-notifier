package ru.eqour.timetable.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.model.account.CommunicationChannel;
import ru.eqour.timetable.model.account.UserAccount;
import ru.eqour.timetable.rest.repository.UserAccountRepository;

@Component
public class CommunicationChannelsService {

    private UserAccountRepository repository;
    private UserAccountService service;

    @Autowired
    public void setRepository(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setUserAccountService(UserAccountService service) {
        this.service = service;
    }

    public void updateChannelRecipient(String email, String channelId, String newRecipient) {
        UserAccount account = service.findByEmailOrCreateEmpty(email);
        CommunicationChannel channel = account.getChannels().get(channelId);
        channel.setRecipient(newRecipient);
        repository.replaceByEmail(email, account);
    }

    public void deleteChannel(String email, String channelId) {
        UserAccount account = service.findByEmailOrCreateEmpty(email);
        CommunicationChannel channel = account.getChannels().get(channelId);
        channel.setRecipient(null);
        repository.replaceByEmail(email, account);
    }
}
