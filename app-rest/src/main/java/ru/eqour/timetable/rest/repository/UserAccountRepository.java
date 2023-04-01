package ru.eqour.timetable.rest.repository;

import ru.eqour.timetable.model.account.UserAccount;

public interface UserAccountRepository {

    UserAccount findByEmail(String email);
    void replaceByEmail(String email, UserAccount account);
}
