package ru.eqour.timetable.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eqour.timetable.rest.exception.SendCodeException;
import ru.eqour.timetable.rest.model.auth.CodeRequest;
import ru.eqour.timetable.rest.model.auth.LoginRequest;
import ru.eqour.timetable.rest.model.auth.LoginResponse;
import ru.eqour.timetable.rest.service.auth.JwtService;
import ru.eqour.timetable.rest.service.code.CodeService;
import ru.eqour.timetable.rest.utils.MessageSenderFactory;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private CodeService<?> codeService;
    private JwtService jwtService;
    private MessageSenderFactory senderFactory;

    @Autowired
    public void setCodeService(CodeService<?> codeService) {
        this.codeService = codeService;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setSenderFactory(MessageSenderFactory senderFactory) {
        this.senderFactory = senderFactory;
    }

    @PostMapping("code")
    public ResponseEntity<?> code(@RequestBody CodeRequest request) {
        if (request == null || request.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        codeService.registerCode(request.getEmail(), null, request.getEmail(), senderFactory.getById("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getCode() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (codeService.verifyCode(request.getEmail(), request.getCode(), null)) {
            String token = jwtService.generateToken(request.getEmail());
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @ExceptionHandler(SendCodeException.class)
    private ResponseEntity<?> handleSendCodeException(SendCodeException exception) {
        return ResponseEntity.unprocessableEntity().build();
    }
}
