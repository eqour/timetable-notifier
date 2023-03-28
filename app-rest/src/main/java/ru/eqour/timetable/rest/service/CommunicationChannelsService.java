package ru.eqour.timetable.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.model.channel.CommunicationChannel;
import ru.eqour.timetable.rest.model.user.UserAccount;
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
        if (channel.getRecipient() == null) channel.setActive(true);
        channel.setRecipient(newRecipient);
        repository.replaceByEmail(email, account);
    }

    public void setActive(String email, String channelId, boolean isActive) {
        UserAccount account = service.findByEmailOrCreateEmpty(email);
        account.getChannels().get(channelId).setActive(isActive);
        repository.replaceByEmail(email, account);
    }

    public void deleteChannel(String email, String channelId) {
        UserAccount account = service.findByEmailOrCreateEmpty(email);
        CommunicationChannel channel = account.getChannels().get(channelId);
        channel.setActive(false);
        channel.setRecipient(null);
        repository.replaceByEmail(email, account);
    }
}
