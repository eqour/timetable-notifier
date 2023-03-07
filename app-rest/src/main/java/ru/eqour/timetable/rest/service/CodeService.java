package ru.eqour.timetable.rest.service;

import org.springframework.stereotype.Service;
import ru.eqour.timetable.rest.exception.SendCodeException;
import ru.eqour.timetable.rest.utils.CodeCache;

import java.util.Random;

@Service
public class CodeService {

    private final CodeCache codeCache;
    private final Random random;

    public CodeService(CodeCache codeCache) {
        this.codeCache = codeCache;
        random = new Random();
    }

    public void registerCode(String email) {
        String code = generateCode();
        sendCode(email, code);
        codeCache.putCode(email, code);
    }

    private String generateCode() {
        return String.format("%04d", random.nextInt(10000));
    }

    private void sendCode(String email, String code) throws SendCodeException {
        // todo implement sending the code to email
        if (!email.endsWith("@gmail.com")) throw new SendCodeException(email);
        System.out.println("Code " + code + " sent to email " + email);
    }

    public boolean verifyCode(String email, String code) {
        return codeCache.getCode(email)
                .map(s -> s.equals(code))
                .orElse(false);
    }

    public void removeCode(String email) {
        codeCache.removeCode(email);
    }
}
