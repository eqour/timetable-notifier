package ru.eqour.timetable.watch.validator;

import ru.eqour.timetable.watch.exception.WeekValidationException;
import ru.eqour.timetable.watch.model.Week;

import java.util.List;

/**
 * Описывает интерфейс для валидации недель.
 */
public interface WeekValidator {

    /**
     * Выполняет валидацию списка недель.
     *
     * @param week список недель.
     * @throws WeekValidationException если хотя бы одна неделя из списка не прошла валидацию.
     */
    void validate(List<Week> week) throws WeekValidationException;

    /**
     * Выполняет валидацию недели.
     *
     * @param week неделя.
     * @throws WeekValidationException если неделя не прошла валидацию.
     */
    void validate(Week week) throws WeekValidationException;
}
