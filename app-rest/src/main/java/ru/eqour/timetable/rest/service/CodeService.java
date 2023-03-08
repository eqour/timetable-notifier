package ru.eqour.timetable.rest.service;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import ru.eqour.timetable.rest.exception.SendCodeException;

import java.util.Optional;
import java.util.Random;

@Service
public class CodeService {

    private EmailService emailService;
    private Cache<String, String> codeCache;
    private final Random random;

    public CodeService() {
        random = new Random();
    }

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setCodeCache(CacheManager cacheManager) {
        codeCache = cacheManager.getCache("code", String.class, String.class);
    }

    public void registerCode(String email) {
        String code = generateCode();
        sendCode(email, code);
        codeCache.put(email, code);
    }

    private String generateCode() {
        return String.format("%04d", random.nextInt(10000));
    }

    private void sendCode(String email, String code) throws SendCodeException {
        try {
            emailService.sendEmail(email, "Timetable Notifier: access code", "Use this code to login: " + code);
        } catch (MailException e) {
            throw new SendCodeException(e);
        }
    }

    public boolean verifyCode(String email, String code) {
        return Optional.ofNullable(codeCache.get(email))
                .map(s -> s.equals(code))
                .orElse(false);
    }

    public void removeCode(String email) {
        codeCache.remove(email);
    }
}
