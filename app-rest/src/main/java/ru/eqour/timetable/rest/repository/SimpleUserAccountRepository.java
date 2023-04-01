package ru.eqour.timetable.rest.repository;

import org.springframework.stereotype.Component;
import ru.eqour.timetable.model.account.UserAccount;

import java.util.HashMap;
import java.util.Map;

@Component
public class SimpleUserAccountRepository implements UserAccountRepository {

    private final Map<String, UserAccount> userAccountMap = new HashMap<>();

    @Override
    public UserAccount findByEmail(String email) {
        return userAccountMap.getOrDefault(email, null);
    }

    @Override
    public void replaceByEmail(String email, UserAccount account) {
        userAccountMap.put(email, account);
    }
}
