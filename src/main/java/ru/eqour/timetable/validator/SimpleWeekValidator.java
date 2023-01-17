package ru.eqour.timetable.validator;

import ru.eqour.timetable.exception.WeekValidationException;
import ru.eqour.timetable.model.Day;
import ru.eqour.timetable.model.Group;
import ru.eqour.timetable.model.Lesson;
import ru.eqour.timetable.model.Week;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class SimpleWeekValidator implements WeekValidator {

    @Override
    public void validate(List<Week> weeks) throws WeekValidationException {
        for (Week week : weeks) {
            validate(week);
        }
    }

    @Override
    public void validate(Week week) throws WeekValidationException {
        if (week.period == null) {
            throw new WeekValidationException("week.period is null");
        } else {
            validatePeriod(week.period);
        }
        if (week.groups == null) {
            throw new WeekValidationException("week.groups is null");
        }
        for (Group group : week.groups) {
            validateGroup(group);
        }
    }

    private void validatePeriod(String periodString) throws WeekValidationException {
        String[] splitString = periodString.split("-");
        if (splitString.length != 2) {
            throw new WeekValidationException("incorrect period format: missing \"-\"");
        }
        validateDate(splitString[0]);
        validateDate(splitString[1]);
    }

    private void validateGroup(Group group) throws WeekValidationException {
        int DAYS_IN_WEEK = 6;
        if (group.name == null) {
            throw new WeekValidationException("group.name is null");
        }
        if (group.days == null) {
            throw new WeekValidationException("group.days is null");
        }
        if (group.days.length != DAYS_IN_WEEK) {
            throw new WeekValidationException("group.days has invalid size (" + group.days.length + ")");
        }
        for (Day day : group.days) {
            validateDay(day);
        }
    }

    private void validateDay(Day day) throws WeekValidationException {
        int LESSONS_IN_DAY = 7;
        if (day.date == null) {
            throw new WeekValidationException("day.date is null");
        } else {
            validateDate(day.date);
        }
        if (day.lessons == null) {
            throw new WeekValidationException("day.lessons is null");
        }
        if (day.lessons.length != LESSONS_IN_DAY) {
            throw new WeekValidationException("day.lessons has invalid size (" + day.lessons.length + ")");
        }
        for (Lesson lesson : day.lessons) {
            if (lesson != null) {
                validateLesson(lesson);
            }
        }
    }

    private void validateDate(String dateString) throws WeekValidationException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new WeekValidationException("incorrect date format", e);
        }
    }

    private void validateLesson(Lesson lesson) throws WeekValidationException {
        if (lesson.time == null) {
            throw new WeekValidationException("lesson.time is null");
        } else {
            validateTime(lesson.time);
        }
        if ((lesson.teacher == null || lesson.teacher.isEmpty())
                && (lesson.classroom == null || lesson.classroom.isEmpty())
                && (lesson.discipline == null || lesson.discipline.isEmpty())) {
            throw new WeekValidationException("lesson is empty");
        }
    }

    private void validateTime(String timeString) throws WeekValidationException {
        String[] splitString = timeString.split("-");
        if (splitString.length != 2) {
            throw new WeekValidationException("incorrect time format: missing \"-\"");
        }
        DateFormat dateFormat = new SimpleDateFormat("kk:mm");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(splitString[0]);
            dateFormat.parse(splitString[1]);
        } catch (ParseException e) {
            throw new WeekValidationException("incorrect time format", e);
        }
    }
}
