package ru.eqour.timetable.mock;

import ru.eqour.timetable.exception.WeekValidationException;
import ru.eqour.timetable.model.Week;
import ru.eqour.timetable.validator.WeekValidator;

public class WeekValidatorMock implements WeekValidator {

    private final boolean isIncorrectWeek;

    public WeekValidatorMock(boolean isIncorrectWeek) {
        this.isIncorrectWeek = isIncorrectWeek;
    }

    @Override
    public void validate(Week week) throws WeekValidationException {
        if (isIncorrectWeek) {
            throw new WeekValidationException();
        }
    }
}
