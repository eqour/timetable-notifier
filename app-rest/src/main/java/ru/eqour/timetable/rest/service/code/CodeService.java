package ru.eqour.timetable.rest.service.code;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.exception.SendCodeException;
import ru.eqour.timetable.rest.service.CodeGenerationService;
import ru.eqour.timetable.sender.MessageSender;
import ru.eqour.timetable.sender.exception.SendMessageException;
import ru.eqour.timetable.sender.model.Message;

import java.util.Objects;

@Component
public class CodeService<T> {

    @Value("${application.debug}")
    private boolean isDebug;

    private Cache<String, CodeData> codeCache;
    private CodeGenerationService generationService;

    @Autowired
    public void setCodeCache(CacheManager cacheManager) {
        this.codeCache = cacheManager.getCache("code", String.class, CodeData.class);
    }

    @Autowired
    public void setGenerationService(CodeGenerationService generationService) {
        this.generationService = generationService;
    }

    public void registerCode(String key, T payload, String recipient, MessageSender sender) {
        String code = generationService.generateCode();
        CodeData data =  new CodeData(code, payload);
        sendCode(recipient, code, sender);
        codeCache.put(key, data);
    }

    private void sendCode(String recipient, String code, MessageSender sender) {
        if (isDebug) {
            System.out.println("code " + code + " sent to " + recipient);
        } else {
            try {
                sender.sendMessage(recipient, new Message("", "Ваш код: " + code));
            } catch (SendMessageException e) {
                throw new SendCodeException(e);
            }
        }
    }

    public boolean verifyCode(String key, String code, T payload) {
        CodeData data = new CodeData(code, payload);
        CodeData storedData = codeCache.get(key);
        if (Objects.equals(data, storedData)) {
            codeCache.remove(key);
            return true;
        }
        return false;
    }
}
