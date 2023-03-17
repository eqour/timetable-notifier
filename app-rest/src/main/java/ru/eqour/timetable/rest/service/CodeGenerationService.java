package ru.eqour.timetable.rest.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeGenerationService {

    private final Random random;

    public CodeGenerationService() {
        this.random = new Random();
    }

    public String generateCode() {
        return String.format("%04d", random.nextInt(10000));
    }
}
