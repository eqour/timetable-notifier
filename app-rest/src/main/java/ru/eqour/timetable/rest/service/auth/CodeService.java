package ru.eqour.timetable.rest.service.auth;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import ru.eqour.timetable.rest.exception.SendCodeException;
import ru.eqour.timetable.rest.service.CodeGenerationService;

import java.util.Optional;

@Service
public class CodeService {

    @Value("${application.debug}")
    private boolean isDebug;

    private CodeGenerationService codeGenerationService;
    private EmailService emailService;
    private Cache<String, String> codeCache;

    @Autowired
    public void setCodeGenerationService(CodeGenerationService codeGenerationService) {
        this.codeGenerationService = codeGenerationService;
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
        String code = codeGenerationService.generateCode();
        sendCode(email, code);
        codeCache.put(email, code);
    }

    private void sendCode(String email, String code) throws SendCodeException {
        try {
            if (isDebug) {
                System.out.println("code " + code + " sent to the email " + email);
            } else {
                emailService.sendEmail(email, "Timetable Notifier: access code", "Use this code to login: " + code);
            }
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
