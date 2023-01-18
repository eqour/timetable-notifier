package ru.eqour.timetable.parser;

import ru.eqour.timetable.model.Week;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;

public class UdsuVoTimetableParser implements TimetableParser {

    private OffsetDateTime currentDateTime;

    @Override
    public List<Week> parseTimetable(InputStream inputStream) throws IOException {
        return null;
    }

    public void setCurrentDateTime(OffsetDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}
