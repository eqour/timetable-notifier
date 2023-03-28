package ru.eqour.timetable.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.eqour.timetable.rest.model.user.UserAccount;
import ru.eqour.timetable.rest.service.UserAccountService;

@RestController
@RequestMapping("/api/v1/account")
public class UserAccountController {

    private UserAccountService userAccountService;

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("")
    public ResponseEntity<UserAccount> getAccount(@CurrentSecurityContext SecurityContext context) {
        String email = context.getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok(userAccountService.findByEmailOrCreateEmpty(email));
    }
}
