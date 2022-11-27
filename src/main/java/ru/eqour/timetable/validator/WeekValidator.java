package ru.eqour.timetable.validator;

import ru.eqour.timetable.exception.WeekValidationException;
import ru.eqour.timetable.model.Week;

public interface WeekValidator {
    void validate(Week week) throws WeekValidationException;
}
