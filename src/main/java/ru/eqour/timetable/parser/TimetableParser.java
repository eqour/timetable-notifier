package ru.eqour.timetable.parser;

import ru.eqour.timetable.model.Week;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface TimetableParser {

    List<Week> parseTimetable(InputStream inputStream) throws IOException;
}
