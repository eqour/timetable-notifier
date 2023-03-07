package ru.eqour.timetable.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eqour.timetable.rest.exception.SendCodeException;
import ru.eqour.timetable.rest.model.CodeRequest;
import ru.eqour.timetable.rest.model.LoginRequest;
import ru.eqour.timetable.rest.model.LoginResponse;
import ru.eqour.timetable.rest.service.CodeService;
import ru.eqour.timetable.rest.service.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CodeService codeService;
    private final JwtService jwtService;

    public AuthController(CodeService codeService, JwtService jwtService) {
        this.codeService = codeService;
        this.jwtService = jwtService;
    }

    @PostMapping("code")
    public ResponseEntity<?> code(@RequestBody CodeRequest request) {
        if (request == null || request.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        codeService.registerCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getCode() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (codeService.verifyCode(request.getEmail(), request.getCode())) {
            codeService.removeCode(request.getEmail());
            String token = jwtService.generateToken(request.getEmail());
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @ExceptionHandler(SendCodeException.class)
    private ResponseEntity<?> handleSendCodeException(SendCodeException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }
}
