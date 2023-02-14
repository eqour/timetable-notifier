package ru.eqour.timetable.watch;

import org.junit.Assert;
import org.junit.Test;
import ru.eqour.timetable.watch.exception.WeekValidationException;
import ru.eqour.timetable.watch.model.Day;
import ru.eqour.timetable.watch.model.Group;
import ru.eqour.timetable.watch.model.Lesson;
import ru.eqour.timetable.watch.model.Week;
import ru.eqour.timetable.watch.util.JsonFileHelper;
import ru.eqour.timetable.watch.util.ResourceHelper;
import ru.eqour.timetable.watch.validator.SimpleWeekValidator;
import ru.eqour.timetable.watch.validator.WeekValidator;

import java.util.Arrays;
import java.util.function.Consumer;

public class WeekValidatorTests {

    private final WeekValidator weekValidator = new SimpleWeekValidator();

    @Test
    public void whenValidWeekThenNotThrowException() {
        executeTestWithChangedWeekAndNotExpectException(w -> {});
    }

    @Test
    public void whenWeekWithoutGroupsThenNotThrowException() {
        executeTestWithChangedWeekAndNotExpectException(w -> w.groups = new Group[0]);
    }

    @Test
    public void whenWeekWithEmptyLessonThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> {
            Lesson l = w.groups[0].days[2].lessons[2];
            l.discipline = l.classroom = l.teacher = "";
        });
    }

    @Test
    public void whenWeekWithLessonWithNullsThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> {
            Lesson l = w.groups[0].days[2].lessons[2];
            l.discipline = l.classroom = l.teacher = null;
        });
    }

    @Test
    public void whenWeekWithEmptyTimeInLessonThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].lessons[3].time = "");
    }

    @Test
    public void whenWeekWithIncorrectTimeInLessonThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].lessons[3].time = "89:56-10:74");
    }

    @Test
    public void whenWeekWithNullTimeInLessonThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].lessons[3].time = null);
    }

    @Test
    public void whenWeekWithNullDayDateThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].date = null);
    }

    @Test
    public void whenWeekWithEmptyDayDateThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].date = "");
    }

    @Test
    public void whenWeekWithIncorrectDayDateThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].date = "24.65.3456");
    }

    @Test
    public void whenWeekWithNullLessonsInDayThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].lessons = null);
    }

    @Test
    public void whenWeekWith8LessonsInDayThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].lessons = new Lesson[8]);
    }

    @Test
    public void whenWeekWith6LessonsInDayThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days[2].lessons = new Lesson[6]);
    }

    @Test
    public void whenWeekWithNullGroupNameThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].name = null);
    }

    @Test
    public void whenWeekWithNullGroupDaysThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days = null);
    }

    @Test
    public void whenWeekWith5DaysInGroupThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days = new Day[5]);
    }

    @Test
    public void whenWeekWith7DaysInGroupThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups[0].days = new Day[7]);
    }

    @Test
    public void whenWeekWithNullGroupsThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.groups = null);
    }

    @Test
    public void whenWeekWithNullPeriodThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.period = null);
    }

    @Test
    public void whenWeekWithEmptyPeriodThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.period = "");
    }

    @Test
    public void whenWeekWithIncorrectPeriodThenThrowException() {
        executeTestWithChangedWeekAndExpectException(w -> w.period = "45.34.7655-34.65.5645");
    }

    @Test
    public void whenValidWeeksThenNotThrowException() {
        try {
            weekValidator.validate(Arrays.asList(getValidWeek(), getValidWeek()));
        } catch (WeekValidationException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void whenOneWeekInvalidThenThrowException() {
        Assert.assertThrows(WeekValidationException.class, () -> {
            Week week = getValidWeek();
            week.groups[0].days[2].lessons[2].time = null;
            weekValidator.validate(Arrays.asList(getValidWeek(), week, getValidWeek()));
        });
    }

    private void executeTestWithChangedWeekAndExpectException(Consumer<Week> weekChanger) {
        Assert.assertThrows(WeekValidationException.class, () -> {
            Week week = getValidWeek();
            weekChanger.accept(week);
            weekValidator.validate(week);
        });
    }

    private void executeTestWithChangedWeekAndNotExpectException(Consumer<Week> weekChanger) {
        try {
            Week week = getValidWeek();
            weekChanger.accept(week);
            weekValidator.validate(week);
        } catch (WeekValidationException e) {
            Assert.fail(e.getMessage());
        }
    }

    private Week getValidWeek() {
        return JsonFileHelper.loadFromFile(
                ResourceHelper.getFullPathToResource("/week-validator/valid-week.json").toString(), Week.class);
    }
}
