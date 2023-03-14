package ru.eqour.timetable.rest.service;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.eqour.timetable.rest.model.channels.CommunicationChannel;
import ru.eqour.timetable.rest.model.channels.UpdateChannelRecipientData;
import ru.eqour.timetable.rest.utils.notifier.NotifierFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CommunicationChannelsService {

    private CodeGenerationService codeGenerationService;
    private Cache<String, UpdateChannelRecipientData> recipientCache;

    @Autowired
    public void setCodeGenerationService(CodeGenerationService codeGenerationService) {
        this.codeGenerationService = codeGenerationService;
    }

    @Autowired
    public void setRecipientCache(CacheManager cacheManager) {
        recipientCache = cacheManager.getCache("recipient", String.class, UpdateChannelRecipientData.class);
    }

    private Map<String, CommunicationChannel> getDefaultChannels() {
        return new HashMap<String, CommunicationChannel>() {{
            put("vk", null);
            put("telegram", null);
        }};
    }

    public Map<String, CommunicationChannel> findAllChannelsByEmail(String email) {
        // TODO: 14.03.2023 get data from database
        if (email.equals("test@test.ru")) {
            return getDefaultChannels();
        } else {
            return Collections.emptyMap();
        }
    }

    public void registerCode(String email, String channelId, String recipient) {
        String code = codeGenerationService.generateCode();
        NotifierFactory.createNotifierById(channelId).sendMessage(recipient, code);
        recipientCache.put(email, new UpdateChannelRecipientData(channelId, recipient, code));
    }

    public void updateChannelRecipient(String channelId, String newRecipient, String code) {
        // TODO: 14.03.2023 update data in database
    }

    public boolean verifyCode(String email, String channelId, String recipient, String code) {
        return Optional.ofNullable(recipientCache.get(email))
                .map(r -> r.getCode().equals(code) && r.getRecipient().equals(recipient)
                        && r.getChannel().equals(channelId))
                .orElse(false);
    }

    public void removeUpdateChannelRecipientRequest(String email) {
        recipientCache.remove(email);
    }

    public void setActive(String channelId, boolean isActive) {
        // TODO: 14.03.2023 update data in database
    }

    public void deleteChannel(String channelId) {
        // TODO: 14.03.2023 update data in database
    }
}
