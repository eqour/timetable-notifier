package ru.eqour.timetable.validator;

import ru.eqour.timetable.exception.WeekValidationException;
import ru.eqour.timetable.model.Week;

import java.util.List;

public interface WeekValidator {
    void validate(List<Week> week) throws WeekValidationException;
    void validate(Week week) throws WeekValidationException;
}
