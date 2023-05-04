package ru.eqour.timetable.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import ru.eqour.timetable.rest.model.subscription.UpdateSubscriptionChannelsRequest;
import ru.eqour.timetable.rest.model.subscription.SubscribeRequest;
import ru.eqour.timetable.rest.service.NotificationSubscriptionService;
import ru.eqour.timetable.rest.service.UserAccountService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class NotificationSubscriptionController {

    private NotificationSubscriptionService subscriptionService;
    private UserAccountService userAccountService;

    @Autowired
    public void setSubscriptionService(NotificationSubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("{type}")
    public ResponseEntity<List<String>> getSubscriptions(@PathVariable String type) {
        if (type == null || userAccountService.subscriptionTypeIsInvalid(type)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(subscriptionService.findAllSubscriptions(type));
    }

    @PostMapping("{type}")
    public ResponseEntity<?> subscribeToNotification(@PathVariable String type,
                                                     @RequestBody SubscribeRequest request,
                                                     @CurrentSecurityContext SecurityContext context) {
        if (request == null || type == null
                || userAccountService.subscriptionTypeIsInvalid(type)) {
            return ResponseEntity.badRequest().build();
        }
        String email = context.getAuthentication().getPrincipal().toString();
        subscriptionService.subscribeToNotification(email, type, request.getName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("{type}/channels")
    public ResponseEntity<?> updateSubscriptionChannels(@PathVariable String type,
                                          @RequestBody UpdateSubscriptionChannelsRequest request,
                                          @CurrentSecurityContext SecurityContext context) {
        if (request == null || type == null || userAccountService.subscriptionTypeIsInvalid(type)
                || userAccountService.channelTypesIsInvalid(request.getChannels())) {
            return ResponseEntity.badRequest().build();
        }
        String email = context.getAuthentication().getPrincipal().toString();
        subscriptionService.updateSubscriptionChannels(email, type, request.getChannels());
        return ResponseEntity.ok().build();
    }
}
