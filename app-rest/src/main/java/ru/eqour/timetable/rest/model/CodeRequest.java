package ru.eqour.timetable.rest.model;

public class CodeRequest {

    private String email;

    public CodeRequest() {
    }

    public CodeRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
