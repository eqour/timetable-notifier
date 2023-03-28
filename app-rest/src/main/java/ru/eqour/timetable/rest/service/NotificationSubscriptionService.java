package ru.eqour.timetable.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eqour.timetable.rest.model.user.UserAccount;
import ru.eqour.timetable.rest.repository.NotificationSubscriptionRepository;
import ru.eqour.timetable.rest.repository.UserAccountRepository;

import java.util.List;

@Component
public class NotificationSubscriptionService {

    private NotificationSubscriptionRepository subscriptionRepository;
    private UserAccountRepository userAccountRepository;
    private UserAccountService userAccountService;

    @Autowired
    public void setSubscriptionRepository(NotificationSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Autowired
    public void setUserAccountRepository(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Autowired
    public void setUserAccountService(UserAccountService service) {
        this.userAccountService = service;
    }

    public List<String> findAllSubscriptions(String type) {
        if (UserAccountService.SUB_GROUP_TYPE.equals(type))
            return subscriptionRepository.findAllGroupSubscriptions();
        if (UserAccountService.SUB_TEACHER_TYPE.equals(type))
            return subscriptionRepository.findAllTeacherSubscriptions();
        throw new RuntimeException("invalid subscription type");
    }

    public void subscribeToNotification(String email, String type, String name) {
        UserAccount account = userAccountService.findByEmailOrCreateEmpty(email);
        account.getSubscriptions().get(type).setName(name);
        userAccountRepository.replaceByEmail(email, account);
    }
}
