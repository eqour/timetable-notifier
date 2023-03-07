package ru.eqour.timetable.rest.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CodeCache {

    Map<String, String> codeMap = new HashMap<>();

    public void putCode(String email, String code) {
        codeMap.put(email, code);
    }

    public Optional<String> getCode(String email) {
        if (codeMap.containsKey(email)) {
            return Optional.of(codeMap.get(email));
        } else {
            return Optional.empty();
        }
    }

    public void removeCode(String email) {
        codeMap.remove(email);
    }
}
