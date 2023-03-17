package ru.eqour.timetable.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eqour.timetable.rest.exception.SendCodeException;
import ru.eqour.timetable.rest.model.auth.CodeRequest;
import ru.eqour.timetable.rest.model.auth.LoginRequest;
import ru.eqour.timetable.rest.model.auth.LoginResponse;
import ru.eqour.timetable.rest.service.auth.EmailService;
import ru.eqour.timetable.rest.service.auth.JwtService;
import ru.eqour.timetable.rest.service.code.CodeService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CodeService<?> codeService;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthController(CodeService<?> codeService, JwtService jwtService, EmailService emailService) {
        this.codeService = codeService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @PostMapping("code")
    public ResponseEntity<?> code(@RequestBody CodeRequest request) {
        if (request == null || request.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        codeService.registerCode(request.getEmail(), null, request.getEmail(), emailService);
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
