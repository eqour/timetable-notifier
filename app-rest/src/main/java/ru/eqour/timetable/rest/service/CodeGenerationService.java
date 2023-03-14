package ru.eqour.timetable.rest.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CodeGenerationService {

    private final Random random;

    public CodeGenerationService() {
        this.random = new Random();
    }

    public String generateCode() {
        return String.format("%04d", random.nextInt(10000));
    }
}
