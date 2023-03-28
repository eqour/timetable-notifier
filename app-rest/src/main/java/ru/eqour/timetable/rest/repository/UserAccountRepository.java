package ru.eqour.timetable.rest.repository;

import ru.eqour.timetable.rest.model.user.UserAccount;

public interface UserAccountRepository {

    UserAccount findByEmail(String email);
    void replaceByEmail(String email, UserAccount account);
}
