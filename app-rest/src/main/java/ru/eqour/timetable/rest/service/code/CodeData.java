package ru.eqour.timetable.rest.service.code;

import java.util.Objects;

public class CodeData {

    private final String code;
    private final Object payload;

    public CodeData(String code, Object payload) {
        this.code = code;
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeData codeData = (CodeData) o;
        return Objects.equals(code, codeData.code) && Objects.equals(payload, codeData.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, payload);
    }
}
