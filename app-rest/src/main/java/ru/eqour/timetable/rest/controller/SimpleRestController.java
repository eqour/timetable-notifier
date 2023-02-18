package ru.eqour.timetable.rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SimpleRestController {

    @RequestMapping("/hello-world")
    public String[] getResponse() {
        return new String[] {
                "Hello",
                "world"
        };
    }
}
