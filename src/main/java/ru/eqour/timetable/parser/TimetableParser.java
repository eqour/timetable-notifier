package ru.eqour.timetable.parser;

import ru.eqour.timetable.model.Week;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Выполняет парсинг расписания.
 */
public interface TimetableParser {

    /**
     * Выполняет парсинг файла расписания.
     *
     * @param inputStream входной поток источника файла расписания.
     * @return список недель {@code List<Week>}.
     * @throws IOException при возникновении ошибки чтения/записи.
     */
    List<Week> parseTimetable(InputStream inputStream) throws IOException;
}
