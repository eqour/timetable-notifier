package ru.eqour.timetable.watch.mock;

import ru.eqour.timetable.watch.exception.WeekValidationException;
import ru.eqour.timetable.watch.model.Week;
import ru.eqour.timetable.watch.validator.WeekValidator;

import java.util.List;

public class WeekValidatorMock implements WeekValidator {

    private final boolean isIncorrectWeek;

    public WeekValidatorMock(boolean isIncorrectWeek) {
        this.isIncorrectWeek = isIncorrectWeek;
    }

    @Override
    public void validate(List<Week> week) throws WeekValidationException {
        validate();
    }

    @Override
    public void validate(Week week) throws WeekValidationException {
        validate();
    }

    private void validate() throws WeekValidationException {
        if (isIncorrectWeek) {
            throw new WeekValidationException();
        }
    }
}
