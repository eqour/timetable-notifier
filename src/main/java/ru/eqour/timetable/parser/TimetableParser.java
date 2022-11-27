package ru.eqour.timetable.parser;

import ru.eqour.timetable.model.Week;

import java.io.IOException;
import java.io.InputStream;

public interface TimetableParser {

    Week parseTimetable(InputStream inputStream) throws IOException;
}
