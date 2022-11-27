package ru.eqour.timetable.mock;

import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.parser.TimetableParser;

import java.io.IOException;
import java.io.InputStream;

public class TimetableParserMock implements TimetableParser {

    private final boolean isCorrectParsing;

    public TimetableParserMock(boolean isCorrectParsing) {
        this.isCorrectParsing = isCorrectParsing;
    }

    @Override
    public Week parseTimetable(InputStream inputStream) throws IOException {
        if (isCorrectParsing) {
            return new Week();
        } else {
            throw new IOException();
        }
    }
}
